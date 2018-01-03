package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.META_API_ENDPOINT;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constant.ConstantDocument;
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
//import com.amx.jax.service.RemittanceTransactionService;
import com.amx.jax.service.TermsAndConditionService;
//import com.amx.jax.service.TransactionHistroyService;
import com.amx.jax.service.ViewDistrictService;
import com.amx.jax.service.ViewStateService;
import com.amx.jax.service.WhyDoAskService;

/**
 * 
 * @author Rabil
 * @param <T>
 *
 */
@RestController
@RequestMapping(META_API_ENDPOINT)
@SuppressWarnings("rawtypes")
public class MetaController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9206662296859918461L;

	final static Logger logger = Logger.getLogger(MetaController.class);

	//@Autowired
	//ConstantDocum
	
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

	
/*	@Autowired
	TransactionHistroyService transactionHistroyService;
	
	
	@Autowired
	RemittanceTransactionService remittanceTransactionService;*/
	
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
	

	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public ApiResponse getCountryListResponse() {
		ApiResponse response = countryService.getCountryListResponse();
		return response;
	}
	
	
	@RequestMapping(value = "/country/{languageId}", method = RequestMethod.GET)
	public ApiResponse getCountryByLanguageIdResponse(@PathVariable("languageId") BigDecimal languageId) {
		ApiResponse response = countryService.getCountryListByLanguageIdResponse(languageId);
		return response;
	}
	
	
	@RequestMapping(value = "/country/{languageId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getCountryByLanguageIdAndCountryIdResponse(@PathVariable("languageId") BigDecimal languageId, @PathVariable("countryId") BigDecimal countryId) {
		ApiResponse response = countryService.getCountryByLanguageIdAndCountryIdResponse(languageId, countryId);
		return response;
	}
	
	
	@RequestMapping(value = "/country/bc/{languageId}", method = RequestMethod.GET)
	public ApiResponse getBusinessCountryResponse(@PathVariable("languageId") BigDecimal languageId) {
		ApiResponse response = countryService.getBusinessCountryResponse(languageId);
		return response;
	}
	
	
	@RequestMapping(value = "/applcountry", method = RequestMethod.GET)
	public ApiResponse getApplicationCountryResponse() {
		ApiResponse response = applicationCountryService.getApplicationCountryListResponse();
		return response;
	}
	
	
	@RequestMapping(value = "/applcountry/{companyId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getApplicationCountryResponse(@PathVariable("companyId") BigDecimal companyId,@PathVariable("countryId") BigDecimal countryId) {
		ApiResponse response = applicationCountryService.getApplicationCountryResponse(companyId, countryId);
		return response;
	}
	
	
	@RequestMapping(value = "/quest/{languageId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getAllQuestionResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		ApiResponse response = questionAnswerService.findAllQuestion(languageId, countryId);
		return response;
	}
	
	
	
	@RequestMapping(value = "/quest/{languageId}/{countryId}/{questId}", method = RequestMethod.GET)
	public ApiResponse getAllQuestionResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("questId") BigDecimal questId){
		ApiResponse response = questionAnswerService.getQuestionDescription(languageId, countryId,questId);
		return response;
	}
	
	
	@RequestMapping(value = "/terms/{languageId}", method = RequestMethod.GET)
	public ApiResponse getTermsAndConditionResponse(@PathVariable("languageId") BigDecimal languageId){
		ApiResponse response = termsAndConditionService.getTermsAndCondition(languageId);
		return response;
	}
	
	
	@RequestMapping(value = "/terms/{languageId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getTermsAndConditionAsPerCountryResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		ApiResponse response = termsAndConditionService.getTermsAndConditionAsPerCountry(languageId, countryId);
		return response;
	}
	
	
	@RequestMapping(value = "/why/{languageId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getWhyAskInformationResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		ApiResponse response = whyDoAskService.getWhyDoAskInformation(languageId,countryId);
		return response;
	}
	
	@RequestMapping(value = "/emailcheck/{languageId}/{countryId}/{emailId}", method = RequestMethod.GET)
	public ApiResponse emailCheckResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("emailId") String emailId){
		ApiResponse response = emailMobileCheckService.checkEmail(languageId, countryId, emailId);
		return response;
	}
	
	
	
	@RequestMapping(value = "/mobilecheck/{languageId}/{countryId}/{mobile}", method = RequestMethod.GET)
	public ApiResponse mobileCheckResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("mobile") String mobile){
		ApiResponse response = emailMobileCheckService.checkMobile(languageId, countryId, mobile);
		return response;
	}
	
	@RequestMapping(value = "/fyear", method = RequestMethod.GET)
	public ApiResponse getFinancialYearResponse(){
		ApiResponse response = financialService.getFinancialYear();
		return response;
	}
	
	
	@RequestMapping(value = "/contacttime", method = RequestMethod.GET)
	public ApiResponse getContactTimeResponse(){
		ApiResponse response = parameterService.getContactUsTime();
		return response;
	}
	
	@RequestMapping(value = "/contactnumber", method = RequestMethod.GET)
	public ApiResponse getContactNumberResponse(){
		ApiResponse response = parameterService.getContactPhoneNo();
		return response;
	}
	
	@RequestMapping(value = "/company/{languageId}", method = RequestMethod.GET)
	public ApiResponse getCompanyDetailResponse(@PathVariable("languageId") BigDecimal languageId){
		ApiResponse response = companyService.getCompanyDetails(languageId);
		return response;
	}
	
	
/*	@RequestMapping(value = "/trnxHist/{cutomerReference}/{docfyr}/{docNumber}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public ApiResponse getTrnxHistroyDetailResponse(@PathVariable("customerId") BigDecimal customerId,
			@PathVariable("docfyr") BigDecimal docfyr,
			@PathVariable("docNumber") String docNumber,
			@PathVariable("fromDate")  String fromDate,
			@PathVariable("toDate") String toDate) {
			
		logger.info("customerId :"+customerId+"\t docfyr :"+docfyr+"\t docNumber :"+docNumber+"\t fromDate :"+fromDate+"\t toDate :"+toDate);
		ApiResponse response = null;
		if(docNumber!=null && !docNumber.equals("null")) {
			 response = transactionHistroyService.getTransactionHistroyByDocumentNumber(customerId, docfyr, new BigDecimal(docNumber));
		}else if((fromDate!= null && !fromDate.equals("null")) || (toDate!= null && !toDate.equals("null"))) {
			response = transactionHistroyService.getTransactionHistroyDateWise(customerId, docfyr,fromDate,toDate); 
		}
		else {
			response = transactionHistroyService.getTransactionHistroy(customerId, docfyr); //, fromDate, toDate
		}
		return response;
	}
	*/
	
	
	
	
	
/*	@RequestMapping(value = "/remitPrint/{documnetNo}/{docFyr}", method = RequestMethod.GET)
	public ApiResponse getRrmittanceDetailForPrintResponse(@PathVariable("documnetNo") BigDecimal documnetNo,@PathVariable("docFyr") BigDecimal docFyr){
		//BigDecimal collectionDocumentCode =new BigDecimal(2);
		ApiResponse response = remittanceTransactionService.getRemittanceTransactionDetails(documnetNo,docFyr,ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
		return response;
	}
	*/
	

	@RequestMapping(value = "/currency/{currencyId}", method = RequestMethod.GET)
	public ApiResponse getCurrencyMasterResponse(@PathVariable("currencyId") BigDecimal currencyId){
		ApiResponse response = currencyMasterService.getCurrencyDetails(currencyId);
		return response;
	}
	
	@RequestMapping(value = "/currency/online/", method = RequestMethod.GET)
	public ApiResponse getAllOnlineCurrencyDetails(){
		ApiResponse response = currencyMasterService.getAllOnlineCurrencyDetails();
		return response;
	}
	
	
	@RequestMapping(value = "/currency/bycountry/{countryId}", method = RequestMethod.GET)
	public ApiResponse getCurrencyDetailsByCountryId(@PathVariable("countryId") BigDecimal countryId){
		ApiResponse response = currencyMasterService.getCurrencyByCountryId(countryId);
		return response;
	}
	

	
	
	@RequestMapping(value = "/purpose/{documentNumber}/{documentFinancialYear}", method = RequestMethod.GET)
	public ApiResponse getPurposeOfRemittanceResponse(@PathVariable("documentNumber") BigDecimal documentNumber,@PathVariable("documentFinancialYear") BigDecimal documentFinancialYear){
		ApiResponse response =purposeOfRemittanceService.getPurposeOfRemittance(documentNumber, documentFinancialYear);
		return response;
	}
	
	
	@RequestMapping(value = "/colldetview/{companyId}/{documentNo}/{documentFinancialYear}/{documentCode}", method = RequestMethod.GET)
	public ApiResponse getCollectionDetailFromView(
			@PathVariable("companyId") BigDecimal companyId,
			@PathVariable("documentNo") BigDecimal documentNo,
			@PathVariable("documentFinancialYear") BigDecimal documentFinancialYear) {		
		ApiResponse response =collectionDetailViewService.getCollectionDetailFromView(companyId, documentNo, documentFinancialYear, ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
		return response;
	}
	
	
	

	@RequestMapping(value = "/collpaydetview/{companyId}/{documentNo}/{documentFinancialYear}/{documentCode}", method = RequestMethod.GET)
	public ApiResponse getCollectPaymentDetailsFromView(
			@PathVariable("companyId") BigDecimal companyId,
			@PathVariable("documentNo") BigDecimal documentNo,
			@PathVariable("documentFinancialYear") BigDecimal documentFinancialYear){			
		System.out.println("Document :"+ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
		ApiResponse response =collectionPaymentDetailsViewService.getCollectionPaymentDetailsFromView(companyId, documentNo, documentFinancialYear, 
				ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
		return response;
	}
	
	@RequestMapping(value = "/multicountry/", method = RequestMethod.GET)
	public ApiResponse getMultiCountry() {
		ApiResponse response = multiCountryService.getMultiCountryList();
		return response;
		
	}

	@RequestMapping(value = "/bank/{country-id}", method = RequestMethod.GET)
	public ApiResponse getAllCurrencyDetails(@PathVariable("country-id") BigDecimal countryId){
		ApiResponse response = bankMasterService.getBanksApiResponseByCountryId(countryId);
		return response;
	}
	
	
	
	@RequestMapping(value = "/districtdesc/{languageId}/{stateId}/{districtId}", method = RequestMethod.GET)
	public ApiResponse getDistrictNameResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("stateId") BigDecimal stateId,@PathVariable("districtId") BigDecimal districtId){
		ApiResponse response = districtService.getDistrict(stateId, districtId, languageId);
		return response;
	}
	
	@RequestMapping(value = "/districtlist/{languageId}/{stateId}", method = RequestMethod.GET)
	public ApiResponse getDistrictNameResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("stateId") BigDecimal stateId){
		ApiResponse response = districtService.getAllDistrict(stateId, languageId);
		return response;
	}
	
	@RequestMapping(value = "/statedesc/{languageId}/{stateId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getStateNameResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("stateId") BigDecimal stateId,@PathVariable("countryId") BigDecimal countryId){
		ApiResponse response = stateService.getState(countryId, stateId, languageId);
		return response;
	}
	
	
	@RequestMapping(value = "/statelist/{languageId}/{countryId}", method = RequestMethod.GET)
	public ApiResponse getStateNameListResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		ApiResponse response = stateService.getStateAll(countryId, languageId);
		return response;
	}
	
	
	@RequestMapping(value = "/citylist/{languageId}/{districtId}", method = RequestMethod.GET)
	public ApiResponse getCityListResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("districtId") BigDecimal districtId){
		ApiResponse response = metaService.getDistrictCity(districtId, languageId);
		return response;
	}
	
	
	@RequestMapping(value = "/citydesc/{districtid}/{languageId}/{cityid}", method = RequestMethod.GET)
	public ApiResponse getCityNameResponse(@PathVariable("districtid") BigDecimal districtid,
			@PathVariable("languageId") BigDecimal languageId,
			@PathVariable("cityid") BigDecimal cityid){
		ApiResponse response = metaService.getCityDescription(districtid, languageId, cityid);
		return response;
	}
	
	
	
}
