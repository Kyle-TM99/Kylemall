package com.kylemall.shop.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoController {

    @Value("${kakao.client.id}")
    private String CLIENT_ID;

    @Value("${kakao.redirect.uri}")
    private String REDIRECT_URI;

    @Autowired
    private MemberService memberService;

    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam String code, HttpSession session, Model model) {
        try {
            String accessToken = requestAccessToken(code);
            Map<String, Object> userData = requestUserInfo(accessToken);

            String kakaoId = userData.get("id").toString();
            Map<String, Object> properties = (Map<String, Object>) userData.get("properties");
            String nickname = (String) properties.get("nickname");
            String profileImage = (String) properties.get("profile_image");

            Member member = processKakaoUser(kakaoId, nickname, profileImage, session);
            if (member != null) {
                return "redirect:/home";
            } else {
                model.addAttribute("kakaoId", kakaoId);
                model.addAttribute("nickname", nickname);
                return "additionalInfo";
            }
        } catch (Exception e) {
            return "errorPage"; // 오류 페이지로 이동
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam("kakaoId") String kakaoId,
                           @RequestParam("emailId") String emailId,
                           @RequestParam("emailDomain") String emailDomain,
                           @RequestParam("mobile1") String mobile1,
                           @RequestParam("mobile2") String mobile2,
                           @RequestParam("mobile3") String mobile3,
                           @RequestParam(value = "emailGet", required = false, defaultValue = "false") boolean emailGet,
                           HttpSession session) {
        if (memberService.getMember(kakaoId) != null) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }

        Member newMember = new Member();
        newMember.setEmail(emailId + "@" + emailDomain);
        newMember.setMobile(mobile1 + "-" + mobile2 + "-" + mobile3);
        newMember.setEmailGet(emailGet);
        newMember.setId(kakaoId);
        newMember.setSocial(true);
        newMember.setSocialType("kakao");

        memberService.addMember(newMember);
        session.setAttribute("user", newMember);
        return "redirect:/mainList";
    }

    private String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", CLIENT_ID);
        parameters.add("redirect_uri", REDIRECT_URI);
        parameters.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    private Map<String, Object> requestUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                Map.class
        );

        return response.getBody();
    }

    private Member processKakaoUser(String kakaoId, String nickname, String profileImage, HttpSession session) {
        Member existingUser = memberService.getMember(kakaoId);
        if (existingUser != null) {
            session.setAttribute("user", existingUser);
            return existingUser;
        }
        return null;
    }
}
