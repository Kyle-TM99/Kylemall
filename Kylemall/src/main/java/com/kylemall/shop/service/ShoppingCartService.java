package com.kylemall.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kylemall.shop.domain.ShoppingCart;
import com.kylemall.shop.mapper.ShoppingCartMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShoppingCartService {
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	
	public int getCartCount(String memberId) {
		return shoppingCartMapper.getCartCount(memberId);
	}
	
	public List<ShoppingCart> getCartItemsByMemberId(String memberId) {
	    return shoppingCartMapper.getCartByMemberId(memberId);
	}

}
