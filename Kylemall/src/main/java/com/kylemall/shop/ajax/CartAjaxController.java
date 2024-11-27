package com.kylemall.shop.ajax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kylemall.shop.domain.ShoppingCart;
import com.kylemall.shop.service.ShoppingCartService;

@RestController
public class CartAjaxController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@PostMapping("/deleteCart.ajax")
	public List<ShoppingCart> deleteCart{
		
		List<ShoppingCart> cart = shoppingCartService.getCartItemsByMemberId(member.getId());
	}
	
	@PostMapping("/addCart.ajax")
	public Map<String, Integer> cartCountCheck(
			@RequestParam("count") int count,
			@RequestParam("memberId") String id,
			@RequestParam("productNo") int No){
		
		shoppingCartService.insertCart(count, id, No);
		
		int cartCnt = shoppingCartService.getCartCount(id);
		Map<String, Integer> map = new HashMap<>();
		map.put("count", cartCnt);
		
		return map;
	}
	
}
