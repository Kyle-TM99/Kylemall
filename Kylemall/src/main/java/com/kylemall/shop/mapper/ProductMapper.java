package com.kylemall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kylemall.shop.domain.Product;

@Mapper
public interface ProductMapper {

	// 상품 리스트를 가져오는 메서드
	List<Product> productList();
	
	// 하나의 상품 정보를 가져오는 메서드
	Product getProduct(int no);
	
}
