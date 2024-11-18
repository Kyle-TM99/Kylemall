package com.kylemall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kylemall.shop.domain.Product;

@Mapper
public interface ProductMapper {

	// 상품 리스트를 가져오는 메서드
	List<Product> productList(@Param("startRow") int startRow, @Param("num") int num,
			@Param("type") String type, @Param("keyword") String keyword, @Param("category") int category);
	
	// 상퓸 리스트의 수를 가져오는 메서드
	int productCount(@Param("category") int category, @Param("type") String type,
			@Param("keyword") String keyword);
	
	// 하나의 상품 정보를 가져오는 메서드
	Product getProduct(int no);
	
	
}
