package com.kylemall.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String merchantUid; // 주문 번호 (Primary Key)
    private String memberId;    // 회원 ID (Foreign Key)
    private Integer totalAmount;    // 총 결제 금액
    private String orderStatus; // 주문 상태
    private String productTitle; // 상품명
    private String orderMsg;      // 주문 메세지
    private Timestamp createdAt; // 생성 일자
    private Timestamp updatedAt; // 수정 일자
}
