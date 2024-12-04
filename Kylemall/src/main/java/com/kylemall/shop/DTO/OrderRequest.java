package com.kylemall.shop.DTO;

import lombok.Data;

@Data
public class OrderRequest {
    private String deliveryName;   // 수령인 이름
    private String payPhone;       // 연락처
    private String emergencyPhone; // 비상 연락처
    private String payZipcode;     // 우편번호
    private String payAddress1;    // 기본 주소
    private String payAddress2;    // 상세 주소
    private String orderMessage;    // 주문 메시지
    private String deliveryMessage;  // 배송 메시지
    private String totalPrice;      // 결제 총액
    private String paymentMethod;   // 결제 방법 (C: 신용카드, K: 카카오페이)
    private String orderProductName;
}