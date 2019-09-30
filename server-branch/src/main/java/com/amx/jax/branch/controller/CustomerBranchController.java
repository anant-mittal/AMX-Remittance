package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.branch.beans.BranchSession;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.client.branch.BranchUserClient;
import com.amx.jax.client.customer.CustomerManagementClient;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.DuplicateCustomerDto;
import com.amx.jax.model.customer.document.CustomerDocumentCategoryDto;
import com.amx.jax.model.customer.document.CustomerDocumentTypeDto;
import com.amx.jax.model.customer.document.UploadCustomerDocumentRequest;
import com.amx.jax.model.customer.document.UploadCustomerDocumentResponse;
import com.amx.jax.model.customer.document.UploadCustomerKycRequest;
import com.amx.jax.model.customer.document.UploadCustomerKycResponse;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.VerifyCustomerContactRequest;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.terminal.TerminalService;
import com.amx.libjax.model.jaxfield.JaxConditionalFieldDto;
import com.amx.libjax.model.jaxfield.JaxFieldDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Customer APIs")
public class CustomerBranchController {

	@Autowired
	OffsiteCustRegClient offsiteCustRegClient;

	@Autowired
	CustomerManagementClient customerManagementClient;

	@Autowired
	private SSOUser ssoUser;

	@Autowired
	TerminalService terminalService;

	@Autowired
	BranchSession branchSession;

	@Autowired
	private BranchUserClient branchUserClient;

	@RequestMapping(value = "/api/customer/details", method = { RequestMethod.POST })

	public AmxApiResponse<OffsiteCustomerDataDTO, Object> setCustomerDetails(
			@RequestParam(value = "identity", required = false) String identity,
			@RequestParam(value = "identityType", required = false) BigDecimal identityType,
			@RequestParam(value = "customerId", required = false) BigDecimal customerId,
			@RequestParam boolean session) {
		branchSession.getCustomerContext(session).refresh();
		AmxApiResponse<OffsiteCustomerDataDTO, Object> customer = offsiteCustRegClient
				.getOffsiteCustomerDetails(identity, identityType, customerId);
		return customer;
	}

	@RequestMapping(value = "/api/customer/details", method = { RequestMethod.GET })
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getCustomerDetails() {
		return AmxApiResponse.build(branchSession.getCustomerData());
	}

	@RequestMapping(value = "/api/customer/details", method = { RequestMethod.DELETE })
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> clearCustomerDetails() {
		branchSession.setCustomer(null);
		return AmxApiResponse.build(branchSession.getCustomerData());
	}

	@RequestMapping(value = "/api/customer/trnxdata", method = { RequestMethod.GET })
	public AmxApiResponse<UserwiseTransactionDto, Object> fetchCustomerTrnxInfo(
			@RequestParam(required = false) String transactiondate) {
		return branchUserClient.getTotalCount(transactiondate);
	}

	@RequestMapping(value = "/api/customer/emptype/list", method = { RequestMethod.GET })
	public AmxApiResponse<ComponentDataDto, Object> fetchEmpTypeList() {
		return offsiteCustRegClient.sendEmploymentTypeList();
	}

	@RequestMapping(value = "/api/customer/article/list", method = { RequestMethod.GET })
	public AmxApiResponse<ArticleMasterDescDto, Object> fetchArticleList() {
		return offsiteCustRegClient.getArticleListResponse();
	}

	@RequestMapping(value = "/api/customer/designation/list", method = { RequestMethod.POST })
	public AmxApiResponse<ArticleDetailsDescDto, Object> fetchDesignationList(
			@RequestBody EmploymentDetailsRequest model) {
		return offsiteCustRegClient.getDesignationListResponse(model);
	}

	@RequestMapping(value = "/api/customer/incomerange/list", method = { RequestMethod.POST })
	public AmxApiResponse<IncomeRangeDto, Object> fetchIncomeRangeList(@RequestBody EmploymentDetailsRequest model) {
		return offsiteCustRegClient.getIncomeRangeResponse(model);
	}

	@RequestMapping(value = "/api/customer/profession/list", method = { RequestMethod.GET })
	public AmxApiResponse<ComponentDataDto, Object> fetchProfessionList() {
		return offsiteCustRegClient.sendProfessionList();
	}

	@RequestMapping(value = "/api/customer/create", method = { RequestMethod.POST })
	public AmxApiResponse<CustomerInfo, Object> createCustomer(
			@RequestBody CreateCustomerInfoRequest createCustomerRequest) {
		return customerManagementClient.createCustomer(createCustomerRequest);
	}

	@RequestMapping(value = "/api/customer/update", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateCustomer(
			@RequestBody UpdateCustomerInfoRequest updateCustomerRequest) {
		branchSession.getCustomerContext().refresh();
		return customerManagementClient.updateCustomer(updateCustomerRequest);
	}

	@RequestMapping(value = "/api/customer/kyc/upload", method = { RequestMethod.POST })
	public AmxApiResponse<UploadCustomerKycResponse, Object> uploadCustomerKyc(
			@RequestBody UploadCustomerKycRequest uploadCustomerKycRequest) {
		return customerManagementClient.uploadCustomerKyc(uploadCustomerKycRequest);
	}

	@RequestMapping(value = "/api/customer/doc/categories", method = { RequestMethod.GET })
	public AmxApiResponse<CustomerDocumentCategoryDto, Object> getDocCategories() {
		return customerManagementClient.getDocumentCategory();
	}

	@RequestMapping(value = "/api/customer/doc/types", method = { RequestMethod.POST })
	public AmxApiResponse<CustomerDocumentTypeDto, Object> getDocTypes(
			@RequestParam(required = true) String documentCategory) {
		return customerManagementClient.getDocumentType(documentCategory);
	}

	@RequestMapping(value = "/api/customer/doc/fields", method = { RequestMethod.GET })
	public AmxApiResponse<JaxConditionalFieldDto, Object> getDocFields(
			@RequestParam(required = true) String documentCategory,
			@RequestParam(required = true) String documentType) {
		return customerManagementClient.getDocumentFields(documentCategory, documentType);
	}

	@RequestMapping(value = "/api/customer/doc/upload", method = { RequestMethod.POST })
	public AmxApiResponse<UploadCustomerDocumentResponse, Object> uploadCustomerDoc(
			@RequestBody UploadCustomerDocumentRequest uploadCustomerDocumentRequest) {
		return customerManagementClient.uploadCustomerDocument(uploadCustomerDocumentRequest);
	}

	@RequestMapping(value = "/api/customer/custreg/status", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateCustRegStatus(
			@RequestBody SignaturePadCustomerRegStateMetaInfo signaturePadCustRegInfo) {
		return terminalService.updateCustomerRegStateData(ssoUser.getUserClient().getTerminalId().intValue(),
				ssoUser.getUserDetails().getEmployeeId(), signaturePadCustRegInfo);
	}

	@RequestMapping(value = "/api/customer/custupdate/status", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateCustUpdateStatus(
			@RequestBody SignaturePadCustomerRegStateMetaInfo signaturePadCustRegInfo) {
		return terminalService.updateCustomerProfileStateData(ssoUser.getUserClient().getTerminalId().intValue(),
				ssoUser.getUserDetails().getEmployeeId(), signaturePadCustRegInfo);
	}

	@RequestMapping(value = "/api/customer/contact/verify", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> uploadCustomerKyc(@RequestBody VerifyCustomerContactRequest request) {
		return customerManagementClient.verifyContact(request);
	}

	@RequestMapping(value = "/api/customer/duplicate/check", method = { RequestMethod.POST })
	public AmxApiResponse<DuplicateCustomerDto, Object> checkForDuplicateCustomer(
			@RequestBody CustomerPersonalDetail customerPersonalDetail) {
		return customerManagementClient.checkForDuplicateCustomer(customerPersonalDetail);
	}

}
