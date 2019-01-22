package com.amx.jax.branchremittance.controller;
/**
 * @author rabil
 * @date  17/01/2019 
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.service.BranchRemittanceService;
import com.amx.jax.client.remittance.IRemittanceService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;

@RestController
public class BranchRemittanceController implements IRemittanceService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	BranchRemittanceService branchRemitService;
	

}
