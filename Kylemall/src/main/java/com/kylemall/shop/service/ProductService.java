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

	private static final int PAGE_SIZE = 8;
	private static final int PAGE_GROUP = 10;
	
	@Autowired
	private ProductMapper productMapper;
	
	public List<Product> categoryList(int category) {
		
		List<Product> pList = productMapper.productList(0, 4, "null", "null", category);
		
		return pList;
		
	}
	
	public Map<String, Object> newMainList() {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		List<Product> mList = productMapper.productList(0, 4, "null", "null", 0);
		
		resultMap.put("mList", mList);
		
		return resultMap;
		
	}
	
	public Map<String, Object> productList(int pageNum, String type, String keyword, int category) {
		
		int currentPage = pageNum;
		
		int startRow = (currentPage - 1) * PAGE_SIZE;
		boolean searchOption = (type.equals("null") || keyword.equals("null")) ? false : true;
		
		int listCount = productMapper.productCount(category, type, keyword);
		
		List<Product> productList = productMapper.productList(startRow, PAGE_SIZE, type, keyword, category);
		
		int pageCount = listCount / PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1);
		
		int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1 - (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
		int endPage = startPage + PAGE_GROUP - 1;
		
		if(endPage > pageCount) {
			endPage = pageCount;
			}
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("pList", productList);
		resultMap.put("pageCount", pageCount);
		resultMap.put("startPage", startPage);
		resultMap.put("endPage", endPage);
		resultMap.put("currentPage", currentPage);
		resultMap.put("listCount", listCount);
		resultMap.put("pageGroup", PAGE_GROUP);
		resultMap.put("searchOption", searchOption);
		resultMap.put("category", category);
		
		if(searchOption) {
			resultMap.put("type", type);
			resultMap.put("keyword", keyword);
		}
		
		return resultMap;
	}
	
	public Product getProduct(int no) {
		
		return productMapper.getProduct(no);
		
	}
	
}
