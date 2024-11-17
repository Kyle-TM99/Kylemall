package com.kylemall.shop.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.Map;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoController {

    private final String CLIENT_ID = "785bf78e1c03819b773c1d0211d3d3a9"; // REST API 키
    private final String REDIRECT_URI = "http://localhost:8080/oauth/kakao/callback";

    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam String code, Model model) {
        // 1. 토큰 요청
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        HttpEntity<String> request = new HttpEntity<>(
                "grant_type=authorization_code" +
                        "&client_id=" + CLIENT_ID +
                        "&redirect_uri=" + REDIRECT_URI +
                        "&code=" + code,
                headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                request,
                Map.class
        );

        String accessToken = (String) response.getBody().get("access_token");

        // 2. 사용자 정보 요청
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<Map> userResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                userRequest,
                Map.class
        );

        Map<String, Object> userData = userResponse.getBody();
        model.addAttribute("userData", userData);

        // 3. 사용자 정보를 세션 또는 데이터베이스에 저장
        // 필요 시, userData를 기반으로 추가 처리

        return "joinSuccess"; // 회원가입 성공 페이지로 이동
    }
}
