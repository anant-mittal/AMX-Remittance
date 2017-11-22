package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.ViewOnlineCustomerCheck;
import com.amx.jax.repository.IViewOnlineCustomerCheck;

@Service
public class CustomerOnlineServiceFromView {
	
	@Autowired
	IViewOnlineCustomerCheck viewOnlineCustomerCheck;
	
	
	
	public List<ViewOnlineCustomerCheck> civilIdCheckForOnlineUser(BigDecimal companyId,BigDecimal countryId,String civilId){
		return viewOnlineCustomerCheck.civilIdCheckForOnlineUser(companyId, countryId, civilId);
	}

}
