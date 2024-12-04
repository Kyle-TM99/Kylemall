package com.kylemall.shop.DTO;

import lombok.Data;

@Data
public class KakaoPayReadyResponse {
    private String tid; // 거래 ID
    private String next_redirect_pc_url; // PC 결제 페이지 URL

}