package com.kylemall.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.domain.ShoppingCart;
import com.kylemall.shop.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@GetMapping("/shoppingCart")
	public String getCartItems(HttpSession session, Model model) {
	    Member member = (Member) session.getAttribute("member");
	    if (member == null) {
	        // 로그인되지 않은 사용자 처리
	        return "redirect:/login";
	    }
	    List<ShoppingCart> cart = shoppingCartService.getCartItemsByMemberId(member.getId());
	    model.addAttribute("sCart", cart);
	    return "views/productCart";
	}
}