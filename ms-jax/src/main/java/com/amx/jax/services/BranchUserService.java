package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.manager.BranchUserManager;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class BranchUserService extends AbstractService{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	BranchUserManager branchUserManager;
	
	
	public AmxApiResponse<UserwiseTransactionDto, Object> getTotalCount(String transactiondate){
		UserwiseTransactionDto userDto  = branchUserManager.getTotalTrnxUserWise(transactiondate);
		return AmxApiResponse.build(userDto);
	}
	
	
}
