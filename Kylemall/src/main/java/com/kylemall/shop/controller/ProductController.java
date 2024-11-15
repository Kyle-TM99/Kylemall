package com.kylemall.shop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kylemall.shop.domain.Product;
import com.kylemall.shop.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping({"/", "/mainList"})
	public String productList(Model model) {
		
		Map<String, Object> modelMap = productService.productList();
		model.addAllAttributes(modelMap);
		return "views/mainList";
		
	}
	
	@GetMapping("/productDetail")
	public String getProduct(Model model,
			@RequestParam("no") int no) {
		
		Product product = productService.getProduct(no);
		
		model.addAttribute("product", product);
		
		return "views/productDetail";
		
	}
	
}
