package com.kylemall.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kylemall.shop.domain.ShoppingCart;
import com.kylemall.shop.service.ShoppingCartService;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@GetMapping("/{memberId}")
	public List<ShoppingCart> getCartItems(@PathVariable String memberId) {
	    return shoppingCartService.getCartItemsByMemberId(memberId);
	}

}
