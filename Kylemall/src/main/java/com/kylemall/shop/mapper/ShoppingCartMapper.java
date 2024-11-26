package com.kylemall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kylemall.shop.domain.ShoppingCart;

@Mapper
public interface ShoppingCartMapper {

	int getCartCount(String memberId);
	
	List<ShoppingCart> getCartByMemberId(String memberId);
	
}
