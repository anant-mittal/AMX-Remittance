package com.amx.jax.branch.controller;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.compliance.ComplianceBlockedTrnxType;
import com.amx.jax.client.compliance.ComplianceClient;
import com.amx.jax.client.compliance.HighValueTrnxDto;
import com.amx.jax.model.customer.ComplianceTrnxDocumentInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Compliance  APIs")
public class BranchComplianceController {

	@Autowired
	private ComplianceClient complianceClient;

	@RequestMapping(value = "/api/compliance/trnx/list", method = { RequestMethod.GET })
	public AmxApiResponse<HighValueTrnxDto, Object> getTransactions(ComplianceBlockedTrnxType trnxType) {
		return complianceClient.listHighValueTransaction(trnxType);
	}


	@RequestMapping(value = "/api/compliance/trnx/view", method = { RequestMethod.GET })
	public AmxApiResponse<ComplianceTrnxDocumentInfo, Object> clearCustomerDetails(Long trnxId) {
		return complianceClient.getTransactionDocuments(trnxId);
	}


}

