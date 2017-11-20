package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.ViewOnlineEmailMobileCheck;
import com.amx.jax.repository.ICustomerEmailMobileCheck;

@Service
public class EmailMobileCheckService {

	@Autowired
	ICustomerEmailMobileCheck customerEmailMobileCheck;
	
	public List<ViewOnlineEmailMobileCheck> checkEmail(BigDecimal languageId,BigDecimal countryId,String email){
		return  customerEmailMobileCheck.getEmailCheck(languageId, countryId, email);
	}

	public List<ViewOnlineEmailMobileCheck> checkMobile(BigDecimal languageId,BigDecimal countryId,String mobile){
		return  customerEmailMobileCheck.getMobileCheck(languageId, countryId, mobile);
	}

	
	
	
}
