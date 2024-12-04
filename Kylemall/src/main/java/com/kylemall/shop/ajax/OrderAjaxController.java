package com.kylemall.shop.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderAjaxController {

	@Autowired
	private OrderService orderService;

	// 카카오페이 결제 요청 처리
	@PostMapping("/kakaoPay")
	public ResponseEntity<String> processKakaoPay(@RequestBody OrderDTO orderDTO) {
		try {
			orderService.processKakaoPay(orderDTO);
			return ResponseEntity.ok("카카오페이 결제가 성공적으로 처리되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("카카오페이 결제 실패: " + e.getMessage());
		}
	}

	// 주문 데이터 저장 요청
	@PostMapping("/saveOrder")
	public ResponseEntity<String> saveOrder(@RequestBody OrderDTO orderDTO) {
		try {
			orderService.saveOrder(orderDTO);
			return ResponseEntity.ok("주문이 성공적으로 저장되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("주문 저장 실패: " + e.getMessage());
		}
	}
}
