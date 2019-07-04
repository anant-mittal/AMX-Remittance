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
import com.amx.jax.branchremittance.manager.ReportManager;
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
import com.amx.jax.model.response.remittance.PaymentModeDto;
import com.amx.jax.model.response.remittance.RemittanceDeclarationReportDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.services.AbstractService;
import com.amx.jax.validation.FxOrderValidation;
import com.amx.utils.JsonUtil;

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
	
	@Autowired
	ReportManager reportManager;
	

	
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> saveBranchRemittanceApplication(BranchRemittanceApplRequestModel requestApplModel){
		logger.info("saveBranchRemittanceApplication : " + JsonUtil.toJson(requestApplModel));
		BranchRemittanceApplResponseDto applResponseDto = branchRemitApplManager.saveBranchRemittanceApplication(requestApplModel);
		AmxApiResponse resopnse = AmxApiResponse.build(applResponseDto);
		return resopnse;
	}
	
	
	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> fetchCustomerShoppingCart(){
		validation.validateHeaderInfo();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BranchRemittanceApplResponseDto custShpCart = branchRemittancePaymentManager.fetchCustomerShoppingCart(customerId,localCurrencyId);
		
		return AmxApiResponse.build(custShpCart);
	}
	
	public AmxApiResponse<PaymentModeDto, Object> fetchModeOfPayment(){
		validation.validateHeaderInfo();
		BigDecimal languageId = metaData.getLanguageId();
		PaymentModeDto  lstPaymentMode= branchRemittancePaymentManager.fetchModeOfPayment(languageId);
		return AmxApiResponse.build(lstPaymentMode);
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
	

	
	public AmxApiResponse<CustomerBankDetailsDto, Object> fetchCustomerBankNames(BigDecimal bankId){
	validation.validateHeaderInfo();
	BigDecimal customerId = metaData.getCustomerId();
	CustomerBankDetailsDto customerBankDetailDto = branchRemittancePaymentManager.fetchCustomerNames(customerId,bankId);
	return AmxApiResponse.build(customerBankDetailDto);
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
	
	public BoolRespModel saveCustomerBankDetails(CustomerBankRequest customerBank) {
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
		AmxApiResponse resopnse = AmxApiResponse.build(respondeDto);
		resopnse.setWarningKey(respondeDto.getWarnigMsg());
		return resopnse;
	}
	
	
	
	public AmxApiResponse<RoutingResponseDto,Object> getRoutingDetailsByServiceId(BigDecimal beneRelaId,BigDecimal serviceMasterId) {
		RoutingResponseDto respondeDto = branchRoutingManager.getRoutingDetailsByServiceId(beneRelaId,serviceMasterId);
		return AmxApiResponse.build(respondeDto);
	}

	public AmxApiResponse<AdditionalExchAmiecDto, Object> getPurposeOfTrnx(BigDecimal beneRelaId,BigDecimal routingCountryId){
		List<AdditionalExchAmiecDto> dto =  branchRemitManager.getPurposeOfTrnx(beneRelaId,routingCountryId);
		return AmxApiResponse.buildList(dto);
	}
	
	public AmxApiResponse<RemittanceResponseDto, Object> saveRemittanceTransaction(BranchRemittanceRequestModel remittanceRequestModel){
		 logger.info("saveBranchRemittanceApplication : " + JsonUtil.toJson(remittanceRequestModel));
		 RemittanceResponseDto dto = branchRemittanceSaveManager.saveRemittanceTrnx(remittanceRequestModel);
		 return AmxApiResponse.build(dto);
	 }
	

	public AmxApiResponse<BranchRemittanceApplResponseDto, Object> deleteFromShoppingCart(BigDecimal remittanceApplicationId){
		BranchRemittanceApplResponseDto applResponseDto = branchRemitApplManager.deleteFromShoppingCart(remittanceApplicationId);
		return AmxApiResponse.build(applResponseDto);
	}
	
	
	public AmxApiResponse<RemittanceDeclarationReportDto,Object> fetchCustomerDeclarationReport(BigDecimal collectionDocNo, BigDecimal collectionDocYear,BigDecimal collectionDocCode){
		RemittanceDeclarationReportDto applResponseDto = reportManager.fetchCustomerDeclarationReport(collectionDocNo,collectionDocYear,collectionDocCode);
		return AmxApiResponse.build(applResponseDto);
	}

	public BoolRespModel sendReceiptOnEmail(BigDecimal collectionDocNo,BigDecimal collectionDocYear ,BigDecimal collectionDocCode){
		Boolean result = branchRemittanceSaveManager.sendReceiptOnEmail(collectionDocNo,collectionDocYear,collectionDocCode);
		return new BoolRespModel(result);
	}
	
	

}
