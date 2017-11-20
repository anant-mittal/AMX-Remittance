package com.amx.jax.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.model.ApplicationSetup;
import com.amx.jax.model.CountryMasterView;
import com.amx.jax.model.OnlineQuestModel;
import com.amx.jax.model.TermsAndCondition;
import com.amx.jax.model.UserFinancialYear;
import com.amx.jax.model.ViewOnlineEmailMobileCheck;
import com.amx.jax.model.WhyDoAskInformation;
import com.amx.jax.service.ApplicationCountryService;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.EmailMobileCheckService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.service.QuestionAnswerService;
import com.amx.jax.service.TermsAndConditionService;
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

	
	

	@RequestMapping(value = "/country", method = RequestMethod.GET)
	public ResponseEntity<List<CountryMasterView>> getCountryList() {
		List<CountryMasterView> countryList = countryService.getCountryList();
		if (countryList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<CountryMasterView>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + countryList.size() + " Employees 1");
		logger.debug(countryList);
		logger.debug(Arrays.toString(countryList.toArray()));
		return new ResponseEntity<List<CountryMasterView>>(countryList, HttpStatus.OK);
	}

	@RequestMapping(value = "/country/{languageId}", method = RequestMethod.GET)
	public ResponseEntity<List<CountryMasterView>> getCountryByLanguageId(@PathVariable("languageId") BigDecimal languageId) {
		List<CountryMasterView> countryList = countryService.getCountryByLanguageId(languageId);
		if (countryList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<CountryMasterView>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + countryList.size() + " Employees 1");
		logger.debug(countryList);
		logger.debug(Arrays.toString(countryList.toArray()));
		return new ResponseEntity<List<CountryMasterView>>(countryList, HttpStatus.OK);
	}
	
	
	
	
	@RequestMapping(value = "/country/bc/{languageId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<CountryMasterView>> getBusinessCountry(@PathVariable("languageId") BigDecimal languageId) {
		List<CountryMasterView> countryList = countryService.getBusinessCountry(languageId);
		if (countryList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<CountryMasterView>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + countryList.size() + " Employees 1");
		logger.debug(countryList);
		logger.debug(Arrays.toString(countryList.toArray()));
		return new ResponseEntity<List<CountryMasterView>>(countryList, HttpStatus.OK);
	}

	

	@RequestMapping(value = "/country/{languageId}/{countryId}", method = RequestMethod.GET)
	public ResponseEntity<List<CountryMasterView>> getCountryByLanguageIdAndCountryId(
			@PathVariable("languageId") BigDecimal languageId, @PathVariable("countryId") BigDecimal countryId) {
		List<CountryMasterView> countryList = countryService.getCountryByLanguageIdAndCountryId(languageId, countryId);
		if (countryList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<CountryMasterView>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + countryList.size() + " Employees 1");
		logger.debug(countryList);
		logger.debug(Arrays.toString(countryList.toArray()));
		return new ResponseEntity<List<CountryMasterView>>(countryList, HttpStatus.OK);
	}

	@RequestMapping(value = "/applcountry")
	public ResponseEntity<List<ApplicationSetup>> getApplicationCountry() {
		List<ApplicationSetup> appCountryList = applicationCountryService.getApplicationCountryList();
		if (appCountryList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<ApplicationSetup>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + appCountryList.size() + " Employees 1");
		logger.debug(appCountryList);
		logger.debug(Arrays.toString(appCountryList.toArray()));
		return new ResponseEntity<List<ApplicationSetup>>(appCountryList, HttpStatus.OK);

	}

	@RequestMapping(value = "/applcountry/{companyId}/{countryId}")
	public ResponseEntity<List<ApplicationSetup>> getApplicationCountry(@PathVariable("companyId") BigDecimal companyId,
			@PathVariable("countryId") BigDecimal countryId) {
		List<ApplicationSetup> appCountryList = applicationCountryService.getApplicationCountry(companyId, countryId);
		if (appCountryList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<ApplicationSetup>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + appCountryList.size() + " Employees 1");
		logger.debug(appCountryList);
		logger.debug(Arrays.toString(appCountryList.toArray()));
		return new ResponseEntity<List<ApplicationSetup>>(appCountryList, HttpStatus.OK);

	}
	
	@RequestMapping(value="/quest/{languageId}/{countryId}")
	public ResponseEntity<List<OnlineQuestModel>> getAllQuestion(@PathVariable("languageId") BigDecimal languageId,
			@PathVariable("countryId") BigDecimal countryId,@PathVariable("companyId") BigDecimal companyId){
		List<OnlineQuestModel> questList = questionAnswerService.findAllQuestion(languageId, countryId);
		if (questList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<OnlineQuestModel>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + questList.size() );
		logger.debug(questList);
		logger.debug(Arrays.toString(questList.toArray()));
		
		return new ResponseEntity<List<OnlineQuestModel>>(questList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/quest/{languageId}/{countryId}/{questId}")
	public ResponseEntity<List<OnlineQuestModel>> getQuestionDescription(@PathVariable("languageId") BigDecimal languageId,
			@PathVariable("countryId") BigDecimal countryId,@PathVariable("questId") BigDecimal questId){
		List<OnlineQuestModel> questList = questionAnswerService.getQuestionDescription(languageId, countryId,questId);
		if (questList.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<OnlineQuestModel>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + questList.size());
		logger.debug(questList);
		logger.debug(Arrays.toString(questList.toArray()));
		
		return new ResponseEntity<List<OnlineQuestModel>>(questList, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/terms/{languageId}")
	public ResponseEntity<List<TermsAndCondition>> getTermsAndCondition(@PathVariable("languageId") BigDecimal languageId){
		List<TermsAndCondition> termsAndConditionList = termsAndConditionService.getTermsAndCondition(languageId);
		if (termsAndConditionList.isEmpty()) {
			logger.debug("Terms and Condition  does not exists");
			return new ResponseEntity<List<TermsAndCondition>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + termsAndConditionList.size());
		logger.debug(termsAndConditionList);
		logger.debug(Arrays.toString(termsAndConditionList.toArray()));
		
		return new ResponseEntity<List<TermsAndCondition>>(termsAndConditionList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/why/{languageId}/{countryId}")
	public ResponseEntity<List<WhyDoAskInformation>> getWhyAskInformation(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		List<WhyDoAskInformation> whyDoAskInformationList = whyDoAskService.getWhyDoAskInformation(languageId,countryId);
		if (whyDoAskInformationList.isEmpty()) {
			logger.debug("Terms and Condition  does not exists");
			return new ResponseEntity<List<WhyDoAskInformation>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + whyDoAskInformationList.size());
		logger.debug(whyDoAskInformationList);
		logger.debug(Arrays.toString(whyDoAskInformationList.toArray()));
		
		return new ResponseEntity<List<WhyDoAskInformation>>(whyDoAskInformationList, HttpStatus.OK);
	}

	
	@RequestMapping(value="/emailcheck/{languageId}/{countryId}/{emailId}")
	public ResponseEntity<List<ViewOnlineEmailMobileCheck>> emailCheck(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("emailId") String emailId){
		List<ViewOnlineEmailMobileCheck> viewOnlineEmailMobileChecList = emailMobileCheckService.checkEmail(languageId, countryId, emailId);
		if (viewOnlineEmailMobileChecList.isEmpty()) {
			logger.debug("Email doesnot exist");
			return new ResponseEntity<List<ViewOnlineEmailMobileCheck>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + viewOnlineEmailMobileChecList.size());
		logger.debug(viewOnlineEmailMobileChecList);
		logger.debug(Arrays.toString(viewOnlineEmailMobileChecList.toArray()));
		
		return new ResponseEntity<List<ViewOnlineEmailMobileCheck>>(viewOnlineEmailMobileChecList, HttpStatus.OK);
	}
	
	

	@RequestMapping(value="/mobilecheck/{languageId}/{countryId}/{mobile}",headers = "Accept=application/json")
	public ResponseEntity<List<ViewOnlineEmailMobileCheck>> mobileCheck(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId,@PathVariable("mobile") String mobile){
		List<ViewOnlineEmailMobileCheck> viewOnlineEmailMobileChecList = emailMobileCheckService.checkMobile(languageId, countryId, mobile);
		if (viewOnlineEmailMobileChecList.isEmpty()) {
			logger.debug("Mobile does not exists");
			return new ResponseEntity<List<ViewOnlineEmailMobileCheck>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + viewOnlineEmailMobileChecList.size());
		logger.debug(viewOnlineEmailMobileChecList);
		logger.debug(Arrays.toString(viewOnlineEmailMobileChecList.toArray()));
		
		return new ResponseEntity<List<ViewOnlineEmailMobileCheck>>(viewOnlineEmailMobileChecList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/fyear",headers = "Accept=application/json")
	public ResponseEntity<List<UserFinancialYear>> getFinancialYear(){
		//Date currentDate = new 
		List<UserFinancialYear> userFinancialYearList = financialService.getFinancialYear();
		if (userFinancialYearList.isEmpty()) {
			logger.debug("Mobile does not exists");
			return new ResponseEntity<List<UserFinancialYear>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + userFinancialYearList.size());
		logger.debug(userFinancialYearList);
		logger.debug(Arrays.toString(userFinancialYearList.toArray()));
		
		return new ResponseEntity<List<UserFinancialYear>>(userFinancialYearList, HttpStatus.OK);
	}
	
	
}
