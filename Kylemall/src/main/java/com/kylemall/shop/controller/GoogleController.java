package com.kylemall.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.kylemall.shop.service.MemberService;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import com.kylemall.shop.domain.Member;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequestMapping("/login/oauth2/code")
@Controller
@RequiredArgsConstructor
@Slf4j
public class GoogleController {

    private final MemberService memberService;

    private PasswordEncoder passwordEncoder;

    @GetMapping("/google")
    public String googleCallback(@AuthenticationPrincipal OAuth2User oauth2User, HttpSession session, Model model) {
        try {
            if (oauth2User == null) {
                log.error("OAuth2User is null");
                return "redirect:/loginForm?error=auth";
            }

            log.info("Google OAuth2 callback received - User: {}", oauth2User);
            
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String picture = oauth2User.getAttribute("picture");

            log.info("Extracted user info - email: {}, name: {}", email, name);

            // DB에서 해당 이메일로 회원 조회
            Member member = memberService.getMember(email);
            log.info("DB lookup result - member: {}", member);
            
            if (member != null) {
                // 5a. 기존 회원이면 로그인 처리
                log.info("Existing member found. Logging in...");
                session.setAttribute("member", member);
                session.setAttribute("isLogin", true);
                return "redirect:/mainList";
            } else {
                // 5b. 신규 회원이면 추가 정보 입력 페이지로
                log.info("New member. Redirecting to additional info page...");
                model.addAttribute("googleId", email);
                model.addAttribute("nickname", name);
                model.addAttribute("profileImage", picture);
                return "member/googleAdditionalInfo";
            }

        } catch (Exception e) {
            log.error("Error in Google callback: ", e);
            return "redirect:/loginForm?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }

     @PostMapping("/register")
    public String register(
        @RequestParam(name = "id") String googleId,
        @RequestParam(name = "name") String name,
        @RequestParam(name = "nickname") String nickname,
        @RequestParam(name = "emailId") String emailId,
        @RequestParam(name = "emailDomain") String emailDomain,
        @RequestParam(name = "mobile1") String mobile1,
        @RequestParam(name = "mobile2") String mobile2,
        @RequestParam(name = "mobile3") String mobile3,
        @RequestParam(name = "zipcode") String zipcode,
        @RequestParam(name = "address1") String address1,
        @RequestParam(name = "address2") String address2,
        @RequestParam(name = "emailGet", defaultValue = "false") boolean emailGet,
        @RequestParam(name = "profileImage", required = false) String profileImage,
        HttpSession session) {
        
        try {
            log.info("Registering new member with kakao ID: {}", googleId);
            
            // 1. 비밀번호 암호화
            String temporaryPassword = googleId + UUID.randomUUID().toString().substring(0, 8);
            String encodedPassword = passwordEncoder.encode(temporaryPassword);
            
            // 2. Member 객체 생성 및 설정
            Member newMember = new Member();
            newMember.setId(googleId);
            newMember.setPass(encodedPassword);
            newMember.setName(name);
            newMember.setNickname(nickname);
            newMember.setEmail(emailId + "@" + emailDomain);
            newMember.setMobile(mobile1 + mobile2 + mobile3);
            newMember.setZipcode(zipcode);
            newMember.setAddress1(address1);
            newMember.setAddress2(address2);
            newMember.setEmailGet(emailGet);
            newMember.setProfileImage(profileImage);
            newMember.setSocial(true);
            newMember.setSocialType("google");
            
            log.info("Registering social member: {}", newMember);
            memberService.addMember(newMember);
            
            // 4. 세션에 회원 정보 저장
            session.setAttribute("member", newMember);
            session.setAttribute("isLogin", true);
            
            return "redirect:/mainList";
            
        } catch (Exception e) {
            log.error("Error during kakao member registration: {}", e.getMessage(), e);
            return "redirect:/loginForm?error=register";
        }
    }
} 