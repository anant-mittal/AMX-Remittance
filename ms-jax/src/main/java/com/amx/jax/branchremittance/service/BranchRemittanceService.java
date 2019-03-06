package com.amx.jax.branchremittance.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.branchremittance.manager.BranchRemittanceApplManager;
import com.amx.jax.branchremittance.manager.BranchRemittanceManager;
import com.amx.jax.branchremittance.manager.BranchRemittancePaymentManager;
import com.amx.jax.branchremittance.manager.BranchRemittanceSaveManager;
import com.amx.jax.branchremittance.manager.BranchRoutingManager;
import com.amx.jax.manager.FcSaleBranchOrderManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.BranchRemittanceRequestModel;
import com.amx.jax.model.request.remittance.CustomerBankRequest;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.CustomerBankDetailsDto;
import com.amx.jax.model.response.remittance.LocalBankDetailsDto;
import com.amx.jax.model.response.remittance.PaymentModeOfPaymentDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.services.AbstractService;
import com.amx.jax.validation.FxOrderValidation;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class BranchRemittanceService extends AbstractService{
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	FxOrderValidation validation;
	
	@Autowired
	BranchRemittanceManager branchRemitManager;
	
	@Autowired
	BranchRemittanceApplManager branchRemitApplManager;
	
	@Autowired
	FcSaleBranchOrderManager fcSaleBranchOrderManager;
	
	@Autowired
	BranchRemittancePaymentManager branchRemittancePaymentManager;
	
	@Autowired
	BranchRoutingManager branchRoutingManager;
	
	@Autowired
	BranchRemittanceSaveManager branchRemittanceSaveManager;

	
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> saveBranchRemittanceApplication(BranchRemittanceApplRequestModel requestApplModel){
		BranchRemittanceApplResponseDto applResponseDto = branchRemitApplManager.saveBranchRemittanceApplication(requestApplModel);
		return AmxApiResponse.build(applResponseDto);
	}
	
	
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> fetchCustomerShoppingCart(){
		validation.validateHeaderInfo();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BranchRemittanceApplResponseDto custShpCart = branchRemittancePaymentManager.fetchCustomerShoppingCart(customerId,localCurrencyId);
		
		return AmxApiResponse.build(custShpCart);
	}
	
	public AmxApiResponse<PaymentModeOfPaymentDto, Object> fetchModeOfPayment(){
		validation.validateHeaderInfo();
		BigDecimal languageId = metaData.getLanguageId();
		List<PaymentModeOfPaymentDto> lstPaymentMode = branchRemittancePaymentManager.fetchModeOfPayment(languageId);
		return AmxApiResponse.buildList(lstPaymentMode);
	}
	
	public AmxApiResponse<LocalBankDetailsDto, Object> fetchLocalBanks(){
		validation.validateHeaderInfo();
		BigDecimal appcountryId = metaData.getCountryId();
		List<LocalBankDetailsDto> lstLocalBanks = branchRemittancePaymentManager.fetchLocalBanks(appcountryId);
		return AmxApiResponse.buildList(lstLocalBanks);
	}
	
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerLocalBanks(){
		validation.validateHeaderInfo();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal appcountryId = metaData.getCountryId();
		List<CustomerBankDetailsDto> lstLocalBanks = branchRemittancePaymentManager.fetchCustomerLocalBanks(customerId,appcountryId);
		return AmxApiResponse.buildList(lstLocalBanks);
	}
	
	public AmxApiResponse<String, Object> fetchCustomerBankNames(BigDecimal bankId){
		validation.validateHeaderInfo();
		BigDecimal customerId = metaData.getCustomerId();
		List<String> lstCustomerNames = branchRemittancePaymentManager.fetchCustomerNames(customerId,bankId);
		return AmxApiResponse.buildList(lstCustomerNames);
	}
	
	public AmxApiResponse<ResourceDTO, Object> fetchPosBanks(){
		List<ResourceDTO> lstPosBanks = branchRemittancePaymentManager.fetchPosBanks();
		return AmxApiResponse.buildList(lstPosBanks);
	}
	
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyDenomination(){
		validation.validateHeaderInfo();
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		List<UserStockDto> lstLocalCurrencyDeno = branchRemittancePaymentManager.fetchLocalCurrencyDenomination(localCurrencyId);
		return AmxApiResponse.buildList(lstLocalCurrencyDeno);
	}
	
	public AmxApiResponse<UserStockDto, Object> fetchLocalCurrencyRefundDenomination(){
		validation.validateHeaderInfo();
		BigDecimal appcountryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		List<UserStockDto> lstLocalCurrencyDeno = fcSaleBranchOrderManager.fetchUserStockViewByCurrency(appcountryId, employeeId, localCurrencyId);
		return AmxApiResponse.buildList(lstLocalCurrencyDeno);
	}
	
	public BoolRespModel saveCustomerBankDetails(List<CustomerBankRequest> customerBank) {
		validation.validateHeaderInfo();
		BigDecimal appcountryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal companyId = metaData.getCompanyId();
		Boolean saveStatus = branchRemittancePaymentManager.saveCustomerBankDetails(customerBank,appcountryId,employeeId,customerId,companyId);
		return new BoolRespModel(saveStatus);
	}
	

	
	public BoolRespModel validationStaffCredentials(String staffUserName,String staffPassword) {
		BigDecimal countryBranchCode = null;
		Boolean validateStatus = branchRemittancePaymentManager.validationStaffCredentials(staffUserName,staffPassword,countryBranchCode);
		return new BoolRespModel(validateStatus);
	}
	
	
	public AmxApiResponse<RoutingResponseDto,Object> getRoutingDetails(BigDecimal beneRelaId) {
		RoutingResponseDto respondeDto = branchRoutingManager.getRoutingSetupDeatils(beneRelaId);
		return AmxApiResponse.build(respondeDto);
	}
	
	
	
	public AmxApiResponse<RoutingResponseDto,Object> getRoutingDetailsByServiceId(BigDecimal beneRelaId,BigDecimal serviceMasterId) {
		RoutingResponseDto respondeDto = branchRoutingManager.getRoutingDetailsByServiceId(beneRelaId,serviceMasterId);
		return AmxApiResponse.build(respondeDto);
	}

	public AmxApiResponse<AdditionalExchAmiecDto, Object> getPurposeOfTrnx(BigDecimal beneRelaId){
		List<AdditionalExchAmiecDto> dto =  branchRemitManager.getPurposeOfTrnx(beneRelaId);
		return AmxApiResponse.buildList(dto);
	}
	
	public AmxApiResponse<RemittanceResponseDto, Object> saveRemittanceTransaction(BranchRemittanceRequestModel remittanceRequestModel){
		 RemittanceResponseDto dto = branchRemittanceSaveManager.saveRemittance(remittanceRequestModel);
		 return AmxApiResponse.build(dto);
	 }
	
}
