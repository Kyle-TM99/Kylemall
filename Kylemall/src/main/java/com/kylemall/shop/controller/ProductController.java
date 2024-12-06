package com.kylemall.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kylemall.shop.domain.Order;
import com.kylemall.shop.domain.Product;
import com.kylemall.shop.service.PaymentService;
import com.kylemall.shop.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/orderComplete")
	public String orderComplete(Model model,
			@RequestParam(value = "merchantUid", required = true) String merchantUid) {
		
		model.addAttribute("payment", paymentService.getPaymentList(merchantUid));
		model.addAttribute("order", paymentService.getOrderList(merchantUid));
		model.addAttribute("shipping", paymentService.getShippingList(merchantUid));
		
		return "views/orderComplete";
	}
	
	@GetMapping("/productList")
	public String productList(Model model,
		@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
		@RequestParam(value = "type", required = false, defaultValue = "null") String type,
		@RequestParam(value = "keyword", required = false, defaultValue = "null") String keyword,
		@RequestParam(value = "category", required = false, defaultValue = "0") int category) {
		
		Map<String, Object> modelMap = productService.productList(pageNum, type, keyword, category);
		model.addAllAttributes(modelMap);
		
		return "views/productList";
	}
	
	@GetMapping({"/", "/mainList"})
	public String mainList(Model model) {
		Map<String, Object> modelMap = productService.mainList();
		model.addAllAttributes(modelMap);
		return "views/mainList";
	}
	
	@GetMapping("/productDetail")
	public String getProduct(Model model,
			@RequestParam("no") int no,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "type", defaultValue = "null") String type,
			@RequestParam(value = "keyword", defaultValue = "null") String keyword,
			@RequestParam(value = "category", defaultValue = "0") int category) {
		
		boolean searchOption = (type.equals("null") || keyword.equals("null")) ? false : true;
		Product product = productService.getProduct(no);
		List<Product> pList = productService.categoryList(category);
		
		model.addAttribute("product", product);
		model.addAttribute("pList", pList);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("searchOption", searchOption);
		model.addAttribute("category", category);
		
		if (searchOption) {
			model.addAttribute("type", type);
			model.addAttribute("keyword", keyword);
		}
		return "views/productDetail";
	}
	
}
