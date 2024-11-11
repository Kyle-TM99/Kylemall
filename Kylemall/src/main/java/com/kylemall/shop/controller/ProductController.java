package com.kylemall.shop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kylemall.shop.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/")
	public String productList(Model model) {
		
		Map<String, Object> modelMap = productService.productList();
		model.addAllAttributes(modelMap);
		
		return "views/index";
		
	}
	
}
