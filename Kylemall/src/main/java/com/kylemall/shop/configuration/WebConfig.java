package com.kylemall.shop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kylemall.shop.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/productPayment").setViewName("views/productPayment");
		registry.addViewController("/mainList").setViewName("views/mainList");
		registry.addViewController("/loginForm").setViewName("member/loginForm");
		registry.addViewController("/joinForm").setViewName("member/memberJoinForm");
		registry.addViewController("/productList").setViewName("views/productList");
		registry.addViewController("/joinChoice").setViewName("member/joinChoice");
		registry.addViewController("/overlapIdCheck").setViewName("member/overlapIdCheck");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		/*
		 * 기존에 정적 리소스 핸들러의 설정은 그대로 유지하며 새로운 리소스 핸들러 추가 /resources/** 로 요청되는 리소스 요청 설정
		 **/
		registry.addResourceHandler("/resources/files/**")
				// file: 프로토콜을 사용하면 업로드한 이미지가 바로 보인다.
				.addResourceLocations("file:./src/main/resources/static/files/").setCachePeriod(1); // 캐쉬 지속시간(초)
	}

	  @Override 
	  public void addInterceptors(InterceptorRegistry registry) {
		  registry.addInterceptor(new LoginCheckInterceptor())
	  		   	  .addPathPatterns("/**") // 인터셉터를 적용할 경로
	              .excludePathPatterns( // 로그인 없이 접근 가능한 경로
	                      "/",              // 메인 페이지
	                      "/mainList",		// 메인 페이지
	                      "/productList",   // 상품 리스트
	                      "/login",
	                      "/loginForm",     // 로그인 페이지
	                      "/joinChoice",    // 회원가입 선택   
	                      "/joinForm", // 회원가입 페이지
	                      "/overlapIdCheck", // 아이디 중복 확인 페이지
	                      "/css/**",        // 정적 자원 (CSS)
	                      "/js/**",         // 정적 자원 (JS)
	                      "/images/**",      // 정적 자원 (이미지)
	                      "/kylemallproducts/**",      // 정적 자원 (이미지)
	                      "/assets/**",
	                      "/bootstrap/**"
	              );
	  }

}
