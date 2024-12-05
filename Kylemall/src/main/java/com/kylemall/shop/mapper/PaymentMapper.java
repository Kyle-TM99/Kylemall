package com.kylemall.shop.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kylemall.shop.domain.Payment;
import com.kylemall.shop.domain.Shipping;

@Mapper
public interface PaymentMapper {
	
	int orderCountCheck(String impUid);
	
	void insertOrder(String merchantUid, String memberId, int totalAmount);
	
	void insertOrderDetail(int detailQuantity, String merchantUid, int productNo);
	
	void insertPayment(Payment payment);
	
	void insertShipping(Shipping shipping);
	
}
