package com.kylemall.shop.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.service.PaymentService;
import com.kylemall.shop.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;

@RestController
public class MemberAjaxController {
	
	@Autowired
	private PaymentService paymentService;
	
	
	
}
