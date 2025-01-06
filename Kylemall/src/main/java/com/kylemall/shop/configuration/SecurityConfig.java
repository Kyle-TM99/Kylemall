package com.kylemall.shop.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean  // 스프링 컨테이너에 이 메서드가 반환하는 객체를 빈으로 등록
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// CSRF 보호 기능을 비활성화 (REST API 서버의 경우 일반적으로 비활성화)
			.csrf(csrf -> csrf.disable())
			
			// HTTP 요청에 대한 접근 권한 설정
			.authorizeHttpRequests(auth -> auth
				// OAuth2 콜백 URL에 대한 접근을 모든 사용자에게 허용
				.requestMatchers("/login/oauth2/code/**").permitAll()
				// Google OAuth2 관련 경로에 대한 접근을 모든 사용자에게 허용
				.requestMatchers("/oauth2/google/**").permitAll()
				// 로그인, OAuth2, 그리고 모든 경로("/**")에 대한 접근을 모든 사용자에게 허용
				.requestMatchers("/login/**", "/oauth2/**", "/loginForm/**", "/**").permitAll()
				// 위에서 설정한 경로 외의 모든 요청은 인증된 사용자만 접근 가능
				.anyRequest().authenticated()
			)
			
			// OAuth2 로그인 설정
			.oauth2Login(oauth2 -> oauth2
				// 커스텀 로그인 페이지 경로 설정
				.loginPage("/loginForm")
				// 로그인 성공 시 실행될 핸들러 설정
				.successHandler((request, response, authentication) -> {
					OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
					request.getSession().setAttribute("oauth2User", oauth2User);
					
					// 직접 forward 수행
					request.getRequestDispatcher("/login/oauth2/code/google")
						.forward(request, response);
				})
			);
	
		return http.build();  // 설정이 완료된 SecurityFilterChain 객체 반환
	}
	
	// OAuth2 사용자 서비스 설정을 위한 빈 등록
	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
		// 기본 OAuth2 사용자 서비스 객체 생성
		DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
		
		// OAuth2 사용자 정보를 로드하는 로직
		return (userRequest) -> {
			// 기본 서비스를 통해 OAuth2 사용자 정보를 가져옴
			OAuth2User oauth2User = delegate.loadUser(userRequest);
			return oauth2User;
		};
	}
}