package com.kylemall.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import lombok.extern.slf4j.Slf4j;
import com.kylemall.shop.service.MemberService;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import com.kylemall.shop.domain.Member;
import java.util.UUID;

@Controller
@RequestMapping("/login/oauth2/code")
@Slf4j
public class GoogleController {

    @Autowired
    private MemberService memberService;
    
    @Autowired
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
            
            // 이메일을 ID로 사용
            String googleId = email;
            
            // 기존 회원인지 확인
            Member existingMember = memberService.getMember(googleId);
            
            if (existingMember != null) {
                // 기존 회원이면 로그인 처리
                session.setAttribute("member", existingMember);
                session.setAttribute("isLogin", true);
                return "redirect:/mainList";
            } else {
                // 신규 회원이면 추가 정보 입력 페이지로 이동
                model.addAttribute("googleId", googleId);
                model.addAttribute("nickname", name);
                model.addAttribute("profileImage", picture);
                return "member/googleAdditionalInfo";
            }

        } catch (Exception e) {
            log.error("Error during Google authentication: {}", e.getMessage(), e);
            return "redirect:/loginForm?error=auth";
        }
    }

    @PostMapping("/register")
    public String registerGoogleMember(
        @RequestParam("id") String googleId,
        @RequestParam("name") String name,
        @RequestParam("nickname") String nickname,
        @RequestParam("mobile1") String mobile1,
        @RequestParam("mobile2") String mobile2,
        @RequestParam("mobile3") String mobile3,
        @RequestParam("zipcode") String zipcode,
        @RequestParam("address1") String address1,
        @RequestParam("address2") String address2,
        @RequestParam(value = "emailGet", defaultValue = "false") boolean emailGet,
        @RequestParam(value = "profileImage", required = false) String profileImage,
        HttpSession session) {
        
        try {
            log.info("Registering new member with Google ID: {}", googleId);
            
            // 임시 비밀번호 생성 및 암호화
            String temporaryPassword = UUID.randomUUID().toString().substring(0, 8);
            String encodedPassword = passwordEncoder.encode(temporaryPassword);
            
            Member newMember = new Member();
            newMember.setId(googleId);
            newMember.setPass(encodedPassword);
            newMember.setName(name);
            newMember.setNickname(nickname);
            newMember.setEmail(googleId); // Google ID가 이메일이므로 그대로 사용
            newMember.setMobile(mobile1 + "-" + mobile2 + "-" + mobile3);
            newMember.setZipcode(zipcode);
            newMember.setAddress1(address1);
            newMember.setAddress2(address2);
            newMember.setEmailGet(emailGet);
            newMember.setProfileImage(profileImage);
            newMember.setSocial(true);
            newMember.setSocialType("google");
            
            log.info("Registering social member: {}", newMember);
            memberService.addMember(newMember);
            
            session.setAttribute("member", newMember);
            session.setAttribute("isLogin", true);
            
            return "redirect:/mainList";
            
        } catch (Exception e) {
            log.error("Error during Google member registration: {}", e.getMessage(), e);
            return "redirect:/loginForm?error=register";
        }
    }
} 