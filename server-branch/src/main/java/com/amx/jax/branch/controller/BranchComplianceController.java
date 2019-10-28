package com.amx.jax.branch.controller;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.compliance.ApproveDocRequest;
import com.amx.jax.client.compliance.ComplianceBlockedTrnxType;
import com.amx.jax.client.compliance.ComplianceClient;
import com.amx.jax.client.compliance.HighValueTrnxDto;
import com.amx.jax.client.compliance.RejectDocRequest;
import com.amx.jax.client.customer.CustomerManagementClient;
import com.amx.jax.model.customer.ComplianceTrnxDocumentInfo;
import com.amx.jax.model.customer.document.CustomerDocCatTypeDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Compliance  APIs")
public class BranchComplianceController {

	@Autowired
	private ComplianceClient complianceClient;

	@Autowired
	CustomerManagementClient customerManagementClient;

	@RequestMapping(value = "/api/compliance/trnx/list", method = { RequestMethod.GET })
	public AmxApiResponse<HighValueTrnxDto, Object> getTransactions(ComplianceBlockedTrnxType trnxType) {
		return complianceClient.listHighValueTransaction(trnxType);
	}


	@RequestMapping(value = "/api/compliance/trnx/view", method = { RequestMethod.GET })
	public AmxApiResponse<ComplianceTrnxDocumentInfo, Object> clearCustomerDetails(Long trnxId) {
		return complianceClient.getTransactionDocuments(trnxId);
	}

	@RequestMapping(value = "/api/compliance/doc/types", method = { RequestMethod.GET })
	public AmxApiResponse<CustomerDocCatTypeDto, Object> getDocTypes() {
		return customerManagementClient.listDocCatType();
	}

	@RequestMapping(value = "/api/compliance/doc/approve", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> approveTrnxDoc(@RequestBody ApproveDocRequest request) {
		return complianceClient.approveTrnxDoc(request);
	}

	@RequestMapping(value = "/api/compliance/doc/reject", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> rejectTrnxDoc(@RequestBody RejectDocRequest request) {
		return complianceClient.rejectTrnxDoc(request);
	}



}

