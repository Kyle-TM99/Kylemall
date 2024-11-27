package com.kylemall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kylemall.shop.domain.ShoppingCart;

@Mapper
public interface ShoppingCartMapper {

	void insertCart(int cnt, String memberId, int productNo);
	
	int getCartCount(String memberId);
	
	List<ShoppingCart> getCartByMemberId(String memberId);
	
}
