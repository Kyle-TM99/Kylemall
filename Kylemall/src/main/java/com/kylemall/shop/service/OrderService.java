package com.kylemall.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void processKakaoPay(OrderDTO orderDTO) {
        // TODO: 카카오페이 API 호출 로직 추가 (생략)
        // 성공적으로 결제가 완료되었다고 가정하고 주문 저장 처리
        saveOrder(orderDTO);
    }

    @Transactional
    public void saveOrder(OrderDTO orderDTO) {
        orderMapper.insertOrder(orderDTO);
    }
}