package com.kylemall.shop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kylemall.shop.domain.Product;
import com.kylemall.shop.mapper.ProductMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

	@Autowired
	private ProductMapper productMapper;
	
	public Map<String, Object> productList() {
		
		List<Product> productList = productMapper.productList();
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pList", productList);
		
		return resultMap;
	}
	
	public Product getProduct(int no) {
		
		return productMapper.getProduct(no);
		
	}
	
}
