package com.kylemall.shop.service;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kylemall.shop.DTO.OrderRequest;

import DTO.ReadyResponse;

@Service
public class KakaoService {

    static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
    static final String admin_Key = "{YOUR_ADMIN_KEY}"; // 본인 애플리케이션의 어드민 키를 넣어주세요

    public Map<String, String> kakaoPayReady(OrderRequest orderRequest) {
        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", "orderId_" + UUID.randomUUID());
        parameters.add("partner_user_id", orderRequest.getDeliveryName());
        parameters.add("item_name", orderRequest.getOrderProductName());
        parameters.add("quantity", "1"); // 수량은 1로 가정
        parameters.add("total_amount", orderRequest.getTotalPrice());
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", "http://localhost:8080/payment/success");
        parameters.add("cancel_url", "http://localhost:8080/payment/cancel");
        parameters.add("fail_url", "http://localhost:8080/payment/fail");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, getHeaders());

        // 카카오페이 준비 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ReadyResponse> response = restTemplate.exchange(
                "https://kapi.kakao.com/v1/payment/ready",
                HttpMethod.POST,
                requestEntity,
                ReadyResponse.class
        );

        Map<String, String> result = new HashMap<>();
        if (response.getBody() != null) {
            result.put("redirectUrl", response.getBody().getNextRedirectPcUrl());
        } else {
            throw new RuntimeException("Failed to process payment");
        }
        return result;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auto = "KakaoAK " + admin_Key;
        httpHeaders.set("Authorization", auto);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return httpHeaders;
    }
}
