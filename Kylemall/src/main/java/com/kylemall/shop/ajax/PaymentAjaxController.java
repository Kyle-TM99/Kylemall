package com.kylemall.shop.ajax;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.kylemall.shop.DTO.KakaoPayReadyResponse;
import com.kylemall.shop.DTO.OrderRequest;

@RestController
@RequestMapping("/payment")
public class PaymentAjaxController {

    @PostMapping("/kakaopay")
    public ResponseEntity<Map<String, String>> kakaopay(@RequestBody OrderRequest orderRequest) {
        try {
            // 1. 카카오페이 준비 API 호출
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "KakaoAK {YOUR_ADMIN_KEY}");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("cid", "TC0ONETIME");
            params.add("partner_order_id", "orderId_" + UUID.randomUUID());
            params.add("partner_user_id", orderRequest.getDeliveryName());
            params.add("item_name", orderRequest.getOrderProductName());
            params.add("quantity", "1");
            params.add("total_amount", orderRequest.getTotalPrice());
            params.add("tax_free_amount", "0");
            params.add("approval_url", "http://localhost:8080/payment/success");
            params.add("cancel_url", "http://localhost:8080/payment/cancel");
            params.add("fail_url", "http://localhost:8080/payment/fail");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(params, headers)<>;
            ResponseEntity<KakaoPayReadyResponse> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v1/payment/ready",
                    HttpMethod.POST,
                    request,
                    KakaoPayReadyResponse.class
            );

            if (response.getBody() != null) {
                Map<String, String> result = new HashMap<>();
                result.put("redirectUrl", response.getBody().getNextRedirectPcUrl());
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to process payment"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An error occurred during payment processing"));
        }
    }
}
