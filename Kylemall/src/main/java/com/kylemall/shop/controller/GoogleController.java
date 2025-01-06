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
@Slf4j
public class GoogleController {

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login/oauth2/code/google")
    public String googleCallback(HttpSession session, Model model) {
        try {
            OAuth2User oauth2User = (OAuth2User) session.getAttribute("oauth2User");
            if (oauth2User == null) {
                log.error("OAuth2User is null");
                return "redirect:/loginForm?error=auth";
            }

            log.debug("OAuth2User attributes: {}", oauth2User.getAttributes());
            
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String picture = oauth2User.getAttribute("picture");
            
            // 이메일로 기존 회원 확인
            Member existingMember = memberService.getMember(email);
            
            if (existingMember != null) {
                // 기존 회원이면 세션에 저장하고 메인으로
                session.setAttribute("member", existingMember);
                session.setAttribute("isLogin", true);
                return "redirect:/mainList";
            } else {
                // 신규 회원이면 추가 정보 입력 페이지로
                model.addAttribute("googleId", email);
                model.addAttribute("nickname", name);
                model.addAttribute("profileImage", picture);
                model.addAttribute("email", email);
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
            log.info("Registering new Google member: {}", googleId);
            
            Member newMember = new Member();
            newMember.setId(googleId);
            newMember.setPass(passwordEncoder.encode(googleId)); // 이메일을 임시 비밀번호로 사용
            newMember.setName(name);
            newMember.setNickname(nickname);
            newMember.setEmail(googleId); // 구글 이메일 사용
            newMember.setMobile(mobile1 + mobile2 + mobile3);
            newMember.setZipcode(zipcode);
            newMember.setAddress1(address1);
            newMember.setAddress2(address2);
            newMember.setEmailGet(emailGet);
            newMember.setProfileImage(profileImage);
            newMember.setSocial(true);
            newMember.setSocialType("google");
            
            memberService.addMember(newMember);
            
            session.setAttribute("member", newMember);
            session.setAttribute("isLogin", true);
            
            return "redirect:/mainList";
            
        } catch (Exception e) {
            log.error("Google 회원가입 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/loginForm?error=register";
        }
    }
} 