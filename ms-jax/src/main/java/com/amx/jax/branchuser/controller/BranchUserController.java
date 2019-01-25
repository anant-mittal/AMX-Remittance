package com.amx.jax.branchuser.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchuser.service.BranchUserService;
import com.amx.jax.client.branch.IBranchService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;

@RestController
public class BranchUserController implements IBranchService{
	
private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	BranchUserService branchUserService;
	
	@RequestMapping(value = Path.BR_REMITTANCE_USER_WISE_COUNT, method = RequestMethod.GET)
	public AmxApiResponse<UserwiseTransactionDto, Object> getTotalCount(@RequestParam(value =Params.TRNX_DATE,required=false) String transactiondate){
		logger.info("user wise total getTotalCount " + transactiondate);
		return branchUserService.getTotalCount(transactiondate);
	}

	}

