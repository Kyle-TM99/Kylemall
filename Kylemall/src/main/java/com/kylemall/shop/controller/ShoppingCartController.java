package com.kylemall.shop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.domain.ShoppingCart;
import com.kylemall.shop.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@GetMapping("/shoppingCart")
	public String getCartItems(HttpSession session, Model model) {
	    Member member = (Member) session.getAttribute("member");
	    if (member == null) {
	        log.warn("Unauthenticated access to shopping cart");
	        return "redirect:/loginForm";
	    }

	    List<ShoppingCart> cart = shoppingCartService.getCartItemsByMemberId(member.getId());
	    log.info("Retrieved cart items: {}", cart);
	    
	    if (cart == null) {
	        cart = new ArrayList<>();
	    }
	    model.addAttribute("sCart", cart);
	    return "views/productCart";
	}
	
}