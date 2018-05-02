package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.META_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.manager.JaxNotificationManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.service.ApplicationCountryService;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CollectionDetailViewService;
import com.amx.jax.service.CollectionPaymentDetailsViewService;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.service.EmailMobileCheckService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.service.MetaService;
import com.amx.jax.service.MultiCountryService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.service.PurposeOfRemittanceService;
import com.amx.jax.service.QuestionAnswerService;
import com.amx.jax.service.TermsAndConditionService;
import com.amx.jax.service.ViewDistrictService;
import com.amx.jax.service.ViewStateService;
import com.amx.jax.service.WhyDoAskService;
import com.amx.jax.validation.BankBranchSearchRequestlValidator;

/**
 * 
 * @author Rabil
 * @param <T>
 *
 */
@RestController
@RequestMapping(META_API_ENDPOINT)
@SuppressWarnings("rawtypes")
public class MetaController {


	private static final Logger LOGGER = Logger.getLogger(MetaController.class);
	
	@Autowired
	CountryService countryService;

	@Autowired
	ApplicationCountryService applicationCountryService;
	
	@Autowired
	QuestionAnswerService questionAnswerService;
	
	@Autowired
	TermsAndConditionService termsAndConditionService;
	
	@Autowired
	WhyDoAskService whyDoAskService;
	
	@Autowired
	EmailMobileCheckService emailMobileCheckService;
	

	@Autowired
	FinancialService financialService;
	@Autowired
	ParameterService parameterService;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	MultiCountryService multiCountryService;
	
	@Autowired
	ViewDistrictService districtService;
	
	@Autowired
	ViewStateService stateService;
	
	@Autowired
	MetaService metaService;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	CurrencyMasterService currencyMasterService;
	
	@Autowired
	PurposeOfRemittanceService purposeOfRemittanceService;
	
	@Autowired
	CollectionDetailViewService collectionDetailViewService;
	
	
	@Autowired
	CollectionPaymentDetailsViewService collectionPaymentDetailsViewService;
	
	@Autowired
	BankMetaService bankMasterService;
	
	@Autowired
	JaxNotificationManager jaxNotificationManager;
	
	@Autowired
	BankBranchSearchRequestlValidator bankBranchSearchRequestlValidator;
	

	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public ApiResponse getCountryListResponse() {
		return countryService.getCountryListResponse();
	}	
	
	@RequestMapping(value = "/country/{languageId}", method = RequestMethod.GET)
	public ApiResponse getCountryByLanguageIdResponse(@PathVariable("languageId") BigDecimal languageId) {
		return countryService.getCountryListByLanguageIdResponse(languageId);
	}	
	
	@RequestMapping(value = "/country/{languageId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getCountryByLanguageIdAndCountryIdResponse(@PathVariable("languageId") BigDecimal languageId, @PathVariable("countryId") BigDecimal countryId) {
		return countryService.getCountryByLanguageIdAndCountryIdResponse(languageId, countryId);
	}
	
	
	@RequestMapping(value = "/country/bc/{languageId}", method = RequestMethod.GET)
	public ApiResponse getBusinessCountryResponse(@PathVariable("languageId") BigDecimal languageId) {
		return countryService.getBusinessCountryResponse(languageId);
	}	
	
	@RequestMapping(value = "/applcountry", method = RequestMethod.GET)
	public ApiResponse getApplicationCountryResponse() {
		return applicationCountryService.getApplicationCountryListResponse();
	}
	
	
	@RequestMapping(value = "/applcountry/{companyId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getApplicationCountryResponse(@PathVariable("companyId") BigDecimal companyId,@PathVariable("countryId") BigDecimal countryId) {
		return applicationCountryService.getApplicationCountryResponse(companyId, countryId);
	}	
	
	@RequestMapping(value = "/quest/{languageId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getAllQuestionResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		return questionAnswerService.findAllQuestion(languageId, countryId);
	}
		
	@RequestMapping(value = "/quest/{languageId}/{countryId}/{questId}", method = RequestMethod.GET)
	public ApiResponse getAllQuestionResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("questId") BigDecimal questId){
		return questionAnswerService.getQuestionDescription(languageId, countryId,questId);
	}	
	
	@RequestMapping(value = "/terms/{languageId}", method = RequestMethod.GET)
	public ApiResponse getTermsAndConditionResponse(@PathVariable("languageId") BigDecimal languageId){
		return termsAndConditionService.getTermsAndCondition(languageId);
	}	
	
	@RequestMapping(value = "/terms/{languageId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getTermsAndConditionAsPerCountryResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		return termsAndConditionService.getTermsAndConditionAsPerCountry(languageId, countryId);
	}
	
	
	@RequestMapping(value = "/why/{languageId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getWhyAskInformationResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		return whyDoAskService.getWhyDoAskInformation(languageId,countryId);
	}
	
	@RequestMapping(value = "/emailcheck/{languageId}/{countryId}/{emailId}", method = RequestMethod.GET)
	public ApiResponse emailCheckResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("emailId") String emailId){
		return emailMobileCheckService.checkEmail(languageId, countryId, emailId);
	}
		
	@RequestMapping(value = "/mobilecheck/{languageId}/{countryId}/{mobile}", method = RequestMethod.GET)
	public ApiResponse mobileCheckResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("mobile") String mobile){
		return emailMobileCheckService.checkMobile(languageId, countryId, mobile);
	}
	
	@RequestMapping(value = "/fyear", method = RequestMethod.GET)
	public ApiResponse getFinancialYearResponse(){
		return financialService.getFinancialYear();
	}
	
	
	@RequestMapping(value = "/contacttime", method = RequestMethod.GET)
	public ApiResponse getContactTimeResponse(){
		return parameterService.getContactUsTime();
	}
	
	@RequestMapping(value = "/contactnumber", method = RequestMethod.GET)
	public ApiResponse getContactNumberResponse(){
		return parameterService.getContactPhoneNo();
	}
	
	@RequestMapping(value = "/company/{languageId}", method = RequestMethod.GET)
	public ApiResponse getCompanyDetailResponse(@PathVariable("languageId") BigDecimal languageId){
		return companyService.getCompanyDetails(languageId);
	}

	@RequestMapping(value = "/currency/{currencyId}", method = RequestMethod.GET)
	public ApiResponse getCurrencyMasterResponse(@PathVariable("currencyId") BigDecimal currencyId){
		return currencyMasterService.getCurrencyDetails(currencyId);
	}
	
	@RequestMapping(value = "/currency/online/", method = RequestMethod.GET)
	public ApiResponse getAllOnlineCurrencyDetails(){
		return currencyMasterService.getAllOnlineCurrencyDetails();
	}
	
	// added by chetan 30/04/2018 list the country for currency.
	@RequestMapping(value = "/exchange-rate-currency/list/", method = RequestMethod.GET)
	public ApiResponse getAllExchangeRateCurrencyDetails() {
		return currencyMasterService.getAllExchangeRateCurrencyList();
	}
	
	
	@RequestMapping(value = "/currency/bycountry/{countryId}", method = RequestMethod.GET)
	public ApiResponse getCurrencyDetailsByCountryId(@PathVariable("countryId") BigDecimal countryId){
		return currencyMasterService.getCurrencyByCountryId(countryId);
	}
	
	@RequestMapping(value = "/purpose/{documentNumber}/{documentFinancialYear}", method = RequestMethod.GET)
	public ApiResponse getPurposeOfRemittanceResponse(@PathVariable("documentNumber") BigDecimal documentNumber,@PathVariable("documentFinancialYear") BigDecimal documentFinancialYear){
		return purposeOfRemittanceService.getPurposeOfRemittance(documentNumber, documentFinancialYear);
	}
	
	
	@RequestMapping(value = "/colldetview/{companyId}/{documentNo}/{documentFinancialYear}/{documentCode}", method = RequestMethod.GET)
	public ApiResponse getCollectionDetailFromView(
			@PathVariable("companyId") BigDecimal companyId,
			@PathVariable("documentNo") BigDecimal documentNo,
			@PathVariable("documentFinancialYear") BigDecimal documentFinancialYear) {		
		return collectionDetailViewService.getCollectionDetailFromView(companyId, documentNo, documentFinancialYear, ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
	}

	@RequestMapping(value = "/collpaydetview/{companyId}/{documentNo}/{documentFinancialYear}/{documentCode}", method = RequestMethod.GET)
	public ApiResponse getCollectPaymentDetailsFromView(
			@PathVariable("companyId") BigDecimal companyId,
			@PathVariable("documentNo") BigDecimal documentNo,
			@PathVariable("documentFinancialYear") BigDecimal documentFinancialYear){			
		System.out.println("Document :"+ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
		return collectionPaymentDetailsViewService.getCollectionPaymentDetailsFromView(companyId, documentNo, documentFinancialYear, 
				ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
	}
	
	@RequestMapping(value = "/multicountry/", method = RequestMethod.GET)
	public ApiResponse getMultiCountry() {
		return multiCountryService.getMultiCountryList();
	}

	@RequestMapping(value = "/bank/{country-id}", method = RequestMethod.GET)
	public ApiResponse getAllCurrencyDetails(@PathVariable("country-id") BigDecimal countryId){
		return bankMasterService.getBanksApiResponseByCountryId(countryId);
	}
		
	@RequestMapping(value = "/districtdesc/{languageId}/{stateId}/{districtId}", method = RequestMethod.GET)
	public ApiResponse getDistrictNameResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("stateId") BigDecimal stateId,@PathVariable("districtId") BigDecimal districtId){
		return districtService.getDistrict(stateId, districtId, languageId);
	}
	
	@RequestMapping(value = "/districtlist/{languageId}/{stateId}/", method = RequestMethod.GET)
	public ApiResponse getDistrictNameResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("stateId") BigDecimal stateId){
		return districtService.getAllDistrict(stateId, languageId);
	}
	
	@RequestMapping(value = "/statedesc/{languageId}/{stateId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getStateNameResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("stateId") BigDecimal stateId,@PathVariable("countryId") BigDecimal countryId){
		return stateService.getState(countryId, stateId, languageId);
	}
	
	@RequestMapping(value = "/statelist/{languageId}/{countryId}/", method = RequestMethod.GET)
	public ApiResponse getStateNameListResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		return stateService.getStateAll(countryId, languageId);
	}
	
	@RequestMapping(value = "/citylist/{languageId}/{districtId}", method = RequestMethod.GET)
	public ApiResponse getCityListResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("districtId") BigDecimal districtId){
		return metaService.getDistrictCity(districtId, languageId);
	}
		
	@RequestMapping(value = "/citydesc/{districtid}/{languageId}/{cityid}", method = RequestMethod.GET)
	public ApiResponse getCityNameResponse(@PathVariable("districtid") BigDecimal districtid,
			@PathVariable("languageId") BigDecimal languageId,
			@PathVariable("cityid") BigDecimal cityid){
		return metaService.getCityDescription(districtid, languageId, cityid);
	}
	
	@RequestMapping(value = "/onlineconfig/{applInd}/", method = RequestMethod.GET)
	public ApiResponse getOnlineConfig(@PathVariable("applInd") String applInd){
		return metaService.getOnlineConfig(applInd);
	}
		
	@RequestMapping(value = "/bankbranch/get/", method = RequestMethod.POST)
	public ApiResponse getBankBranches(@RequestBody GetBankBranchRequest request,BindingResult bindingResult){
		LOGGER.info("in getbankBranches" + request.toString());
		bankBranchSearchRequestlValidator.validate(request, bindingResult);
		ApiResponse<BankBranchDto> apiResponse = bankMasterService.getBankBranches(request);
		jaxNotificationManager.sendBranchSearchNotificationToSOA(apiResponse, request);
		return apiResponse;
	}
	
	@RequestMapping(value = "/service-group/", method = RequestMethod.GET)
	public ApiResponse getServiceGroup(){
		return metaService.getServiceGroups();
	}
	
	@RequestMapping(value = "/currency/beneservice/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryCurrencyList(@RequestParam(value = "beneficiaryCountryId", required = true) BigDecimal beneficiaryCountryId){
		return currencyMasterService.getBeneficiaryCurrencyList(beneficiaryCountryId);
	}
	
	@RequestMapping(value = "/meta-parameter/", method = RequestMethod.GET)
	public ApiResponse getAuthParameter(){
		return parameterService.getJaxMetaParameter();
	}
}
