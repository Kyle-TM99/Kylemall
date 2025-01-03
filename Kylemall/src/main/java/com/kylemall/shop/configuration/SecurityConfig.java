package com.kylemall.shop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {		
		
		http.authorizeHttpRequests(
				authorizeHttpRequests -> 
					authorizeHttpRequests
						.requestMatchers(
							new AntPathRequestMatcher("/oauth/kakao/**"),
							new AntPathRequestMatcher("/login/**"),
							new AntPathRequestMatcher("/loginForm/**"),
							new AntPathRequestMatcher("/**")
						).permitAll()
			)
			.csrf(csrf -> csrf
				.ignoringRequestMatchers(
					new AntPathRequestMatcher("/h2-console/**"),
					new AntPathRequestMatcher("/oauth/kakao/**")
				)
			)
			.csrf(csrf -> csrf.disable());
		
		return http.build();
	}
}