package com.kylemall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kylemall.shop.domain.CustomerSupport;

@Mapper
public interface CustomerSupportMapper {

	public List<CustomerSupport> viewFaq ();
	
	public void deleteFaq(Integer faqId);
	
}

