package com.kylemall.shop.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

// 접속자가 로그인 상태인지 체크하는 인터셉터
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
	

	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception {
		log.info("LoginCheckInterceptor - preHandle()");
		
		// 현재 요청 URI 확인
		String requestURI = request.getRequestURI();
		log.info("Request URI: {}", requestURI);
		
		// 카카오 인증 관련 경로는 인터셉터 체크 제외
		if (requestURI.startsWith("/oauth/kakao")) {
			return true;
		}
		
		if (requestURI.startsWith("/google/")) {
			return true;
		}
		
		HttpSession session = request.getSession();
		session.removeAttribute("loginMsg");
				
		// 세션에 isLogin이 없으면 로그인 상태가 아님
		if(session.getAttribute("isLogin") == null) {
			response.sendRedirect("/loginForm");
			session.setAttribute("loginMsg", "로그인이 필요한 서비스");
			return false;
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("##########LoginCheckInterceptor - postHandle()##########");		

		response.setHeader("Cache-Control", "no-cache");
	}
	

	@Override
	public void afterCompletion(HttpServletRequest request, 
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("##########LoginCheckInterceptor - afterCompletion()##########");
	}	
}
