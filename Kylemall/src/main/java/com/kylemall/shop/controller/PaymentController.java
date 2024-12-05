package com.kylemall.shop.controller;

import org.springframework.web.bind.annotation.*;

import com.kylemall.shop.DTO.PaymentVerificationRequest;
import com.kylemall.shop.domain.Member;
import com.kylemall.shop.domain.Payment;
import com.kylemall.shop.domain.Shipping;
import com.kylemall.shop.domain.ShoppingCart;
import com.kylemall.shop.service.PaymentService;
import com.kylemall.shop.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private ShoppingCartService shoppingCartService; 

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentVerificationRequest request,
    		HttpSession session) {
    	log.info("/verify : " + request.getImpUid());
        boolean isVerified = paymentService.verifyPayment(request.getImpUid(), request.getMerchantUid(), request.getTotalPrice());
        
        Member member = (Member) session.getAttribute("member");
        
        if (isVerified) {
            
            // 최종 주문 처리 - DB 작업
            // 주문 정보 DB 저장
            paymentService.insertOrder(request.getMerchantUid(), member.getId(), request.getTotalPrice());
            
            // 주문 상세 정보 DB 저장
            List<ShoppingCart> cart = shoppingCartService.getCartItemsByMemberId(member.getId());
            
            for (ShoppingCart od : cart) {
            	paymentService.insertOrderDetail(od.getQuantity(), request.getMerchantUid(), od.getProductNo());
            }
            
            // 결제 정보 DB 저장
            Payment payment = new Payment();
            
            payment.setImpUid(request.getImpUid());
            payment.setMerchantUid(request.getMerchantUid());
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setAmount(request.getTotalPrice());
            payment.setPaidAt(Timestamp.valueOf(LocalDateTime.now()));
            
            paymentService.insertPayment(payment);
            
            // 배송 정보 DB 저장
            Shipping shipping = new Shipping();
            
            shipping.setMerchantUid(request.getMerchantUid());
            shipping.setRecipientName(request.getRecipientName());
            shipping.setAddress(request.getAddress());
            shipping.setPhoneNumber(request.getPhoneNumber());
            
            paymentService.insertShipping(shipping);
            
            // 장바구니 내역 삭제
            shoppingCartService.clearCart(member.getId());
            
            return ResponseEntity.ok("결제 검증 성공");
                      
        }
        return ResponseEntity.status(400).body("결제 검증 실패");
    }
    
    
}