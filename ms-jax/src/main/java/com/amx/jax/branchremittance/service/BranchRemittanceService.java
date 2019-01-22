package com.amx.jax.branchremittance.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.manager.BranchRemittanceApplManager;
import com.amx.jax.branchremittance.manager.BranchRemittanceManager;
import com.amx.jax.client.remittance.IRemittanceService.Params;
import com.amx.jax.dbmodel.PurposeOfTransaction;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;
import com.amx.jax.services.AbstractService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class BranchRemittanceService extends AbstractService{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	
	@Autowired
	BranchRemittanceManager branchRemitManager;
	
	@Autowired
	BranchRemittanceApplManager branchRemitApplManager;
	
	
	
	public AmxApiResponse<Object, Object> saveBranchApplicationManager(BranchRemittanceApplRequestModel applRequestModel){
		branchRemitApplManager.saveBranchApplication(applRequestModel);
		return null;
	}
	
}
