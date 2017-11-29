package com.amx.jax.controller;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.service.ApplicationCountryService;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.EmailMobileCheckService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.service.QuestionAnswerService;
import com.amx.jax.service.TermsAndConditionService;
import com.amx.jax.service.TransactionHistroyService;
import com.amx.jax.service.ViewDistrictService;
import com.amx.jax.service.WhyDoAskService;

/**
 * 
 * @author Rabil
 * @param <T>
 *
 */
@RestController
@RequestMapping("/meta")
public class MetaController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9206662296859918461L;

	final static Logger logger = Logger.getLogger(MetaController.class);

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
	ViewDistrictService districtServcie;
	
	
	@Autowired
	ParameterService parameterService;
	
	@Autowired
	CompanyService companyService;
	

	
	@Autowired
	TransactionHistroyService transactionHistroyService;
	
	
	/*@Autowired
	MetaService<T> metaService;*/
	
	

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
	
	
	@RequestMapping(value = "/trnxHist/{cutomerReference}/{docfyr}/{docNumber}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public ApiResponse getTrnxHistroyDetailResponse(@PathVariable("cutomerReference") BigDecimal cutomerReference,
			@PathVariable("docfyr") BigDecimal docfyr,
			@PathVariable("docNumber") String docNumber,
			@PathVariable("fromDate")  String fromDate,
			@PathVariable("toDate") String toDate) {
			
		logger.info("cutomerReference :"+cutomerReference+"\t docfyr :"+docfyr+"\t docNumber :"+docNumber+"\t fromDate :"+fromDate+"\t toDate :"+toDate);
		ApiResponse response = null;
		if(docNumber!=null && !docNumber.equals("null")) {
			 response = transactionHistroyService.getTransactionHistroyByDocumentNumber(cutomerReference, docfyr, new BigDecimal(docNumber));
		}else if((fromDate!= null && !fromDate.equals("null")) || (toDate!= null && !toDate.equals("null"))) {
			response = transactionHistroyService.getTransactionHistroyDateWise(cutomerReference, docfyr,fromDate,toDate); 
		}
		else {
			response = transactionHistroyService.getTransactionHistroy(cutomerReference, docfyr); //, fromDate, toDate
		}
		return response;
	}
	
	
/*	@RequestMapping(value = "/district/{languageId}/{stateId}/{districtId}", method = RequestMethod.GET)
	public ApiResponse getDistrictResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("stateId") BigDecimal stateId,@PathVariable("districtId") BigDecimal districtId){
		ApiResponse response = metaService.getDistrict(stateId, districtId, languageId);
		return response;
	}*/
	
	
	
}
