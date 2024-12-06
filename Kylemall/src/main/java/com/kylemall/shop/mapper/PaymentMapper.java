package com.kylemall.shop.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kylemall.shop.domain.Order;
import com.kylemall.shop.domain.Payment;
import com.kylemall.shop.domain.Shipping;

@Mapper
public interface PaymentMapper {
	
	Payment getPaymentList(String merchantUid);
	
	Shipping getShippingList(String merchantUid);
	
	Order getOrderList(String merchantUid);
	
	int orderCountCheck(String impUid);
	
	void insertOrder(Order order);
	
	void insertOrderDetail(@Param("detailQuantity") int detailQuantity, @Param("merchantUid") String merchantUid, 
			@Param("productNo") int productNo);
	
	void insertPayment(Payment payment);
	
	void insertShipping(Shipping shipping);
	
}
