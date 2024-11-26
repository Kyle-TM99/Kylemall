package com.kylemall.shop.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kylemall.shop.service.ShoppingCartService;

@RestController
public class CartAjaxController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@GetMapping("/cartCount.ajax")
	public Map<String, Integer> cartCountCheck(
			@RequestParam("memberId") String id){
		
		int cnt = shoppingCartService.getCartCount(id);
		Map<String, Integer> map = new HashMap<>();
		map.put("cnt", cnt);
		
		return map;
	}
	
}
