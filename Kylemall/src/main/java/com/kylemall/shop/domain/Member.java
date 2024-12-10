package com.kylemall.shop.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private String id;
    private String name;
    private String pass;
    private String email;
    private String mobile;
    private String zipcode;
    private String address1;
    private String address2;
    private boolean emailGet;
    private Timestamp regDate;
    private String nickname;
    private String isAdmin;
	
}