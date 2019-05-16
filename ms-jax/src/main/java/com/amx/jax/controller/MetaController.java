package com.amx.jax.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.ApiEndpoint.MetaApi;
import com.amx.amxlib.meta.model.ApplicationSetupDTO;
import com.amx.amxlib.meta.model.AuthenticationLimitCheckDTO;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.DeclarationDTO;
import com.amx.amxlib.meta.model.JaxMetaParameter;
import com.amx.amxlib.meta.model.MultiCountryDTO;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.TermsAndConditionDTO;
import com.amx.amxlib.meta.model.ViewAreaDto;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewCompanyDetailDTO;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewGovernateAreaDto;
import com.amx.amxlib.meta.model.ViewGovernateDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.meta.model.ViewStatusDto;
import com.amx.amxlib.meta.model.WhyDoAskInformationDTO;
import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.dbmodel.BranchDetailModel;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.CollectionDetailViewModel;
import com.amx.jax.dbmodel.CollectionPaymentDetailsViewModel;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.PrefixModel;
import com.amx.jax.dbmodel.PurposeOfRemittanceViewModel;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.ViewOnlineEmailMobileCheck;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.manager.JaxNotificationManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.model.response.BranchSystemDetailDto;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.service.ApplicationCountryService;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.BranchDetailService;
import com.amx.jax.service.CollectionDetailViewService;
import com.amx.jax.service.CollectionPaymentDetailsViewService;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.CountryBranchService;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.service.EmailMobileCheckService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.service.MetaService;
import com.amx.jax.service.MultiCountryService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.service.PrefixService;
import com.amx.jax.service.PurposeOfRemittanceService;
import com.amx.jax.service.QuestionAnswerService;
import com.amx.jax.service.TermsAndConditionService;
import com.amx.jax.service.ViewDistrictService;
import com.amx.jax.service.ViewStateService;
import com.amx.jax.service.WhyDoAskService;
import com.amx.jax.util.JaxContextUtil;
import com.amx.jax.validation.BankBranchSearchRequestlValidator;

/**
 * 
 * @author Rabil
 * @param <T>
 *
 */
/**
 * @author Chetan Pawar
 *
 */
@RestController
@RequestMapping(MetaApi.PREFIX)
public class MetaController {

	private static final Logger LOGGER = LoggerService.getLogger(MetaController.class);

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

	@Autowired
	PrefixService prefixService;

	@Autowired
	BranchDetailService branchDetailService;
	
	@Autowired
	CountryBranchService countryBranchService;
	

	@RequestMapping(value = MetaApi.API_COUNTRY, method = RequestMethod.GET)
	public AmxApiResponse<CountryMasterView, Object> getCountryListResponse() {
		return countryService.getCountryListResponse();
	}

	@RequestMapping(value = MetaApi.API_COUNTRY_BY_lANG_ID, method = RequestMethod.GET)
	public AmxApiResponse<CountryMasterView, Object> getCountryByLanguageIdResponse() {
		return countryService.getCountryListByLanguageIdResponse(metaData.getLanguageId());
	}

	@RequestMapping(value = MetaApi.API_COUNTRY_BY_lANG_COUNTRY_ID, method = RequestMethod.GET)
	public AmxApiResponse<CountryMasterView, Object> getCountryByLanguageIdAndCountryIdResponse() {
		return countryService.getCountryByLanguageIdAndCountryIdResponse(metaData.getLanguageId(),
				metaData.getCountryId());
	}

	@RequestMapping(value = MetaApi.API_COUNTRY_BC, method = RequestMethod.GET)
	public AmxApiResponse<CountryMasterView, Object> getBusinessCountryResponse() {
		return countryService.getBusinessCountryResponse(metaData.getLanguageId());
	}

	@RequestMapping(value = MetaApi.APPL_COUNTRY, method = RequestMethod.GET)
	public AmxApiResponse<ApplicationSetupDTO, Object> getApplicationCountryResponse() {
		return applicationCountryService.getApplicationCountryListResponse();
	}

	@RequestMapping(value = MetaApi.APPL_COUNTRY_COMP, method = RequestMethod.GET)
	public AmxApiResponse<ApplicationSetupDTO, Object> getApplicationCountrycompanyResponse() {
		return applicationCountryService.getApplicationCountryResponse(metaData.getCompanyId(),
				metaData.getCountryId());
	}

	@RequestMapping(value = MetaApi.SEQ_QUEST_LIST, method = RequestMethod.GET)
	public AmxApiResponse<QuestModelDTO, Object> getAllQuestionListResponse() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{} {} {}", MetaApi.SEQ_QUEST_LIST, metaData.getLanguageId(), metaData.getCountryId());
		}
		return questionAnswerService.findAllQuestion(metaData.getLanguageId(), metaData.getCountryId());
	}

	@RequestMapping(value = MetaApi.SEQ_QUEST_BY_ID, method = RequestMethod.GET)
	public AmxApiResponse<QuestModelDTO, Object> getAllQuestionResponse(
			@PathVariable(MetaApi.PARAM_QUEST_ID) BigDecimal questId) {
		return questionAnswerService.getQuestionDescription(metaData.getLanguageId(), metaData.getCountryId(), questId);
	}

	@RequestMapping(value = MetaApi.API_TERMS_BY_lANG_ID, method = RequestMethod.GET)
	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndConditionResponse() {
		return termsAndConditionService.getTermsAndCondition(metaData.getLanguageId());
	}

	@RequestMapping(value = MetaApi.API_TERMS_BY_lANG_COUNTRY_ID, method = RequestMethod.GET)
	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndConditionAsPerCountryResponse() {
		return termsAndConditionService.getTermsAndConditionAsPerCountry(metaData.getLanguageId(),
				metaData.getCountryId());
	}

	@RequestMapping(value = MetaApi.API_WHY, method = RequestMethod.GET)
	public AmxApiResponse<WhyDoAskInformationDTO, Object> getWhyAskInformationResponse() {
		return whyDoAskService.getWhyDoAskInformation(metaData.getLanguageId(), metaData.getCountryId());
	}

	@RequestMapping(value = "/emailcheck/{languageId}/{countryId}/{emailId}", method = RequestMethod.GET)
	public AmxApiResponse<ViewOnlineEmailMobileCheck, Object> emailCheckResponse(
			@PathVariable("languageId") BigDecimal languageId, @PathVariable("countryId") BigDecimal countryId,
			@PathVariable("emailId") String emailId) {
		return emailMobileCheckService.checkEmail(languageId, countryId, emailId);
	}

	@RequestMapping(value = "/mobilecheck/{languageId}/{countryId}/{mobile}", method = RequestMethod.GET)
	public AmxApiResponse<ViewOnlineEmailMobileCheck, Object> mobileCheckResponse(
			@PathVariable("languageId") BigDecimal languageId, @PathVariable("countryId") BigDecimal countryId,
			@PathVariable("mobile") String mobile) {
		return emailMobileCheckService.checkMobile(languageId, countryId, mobile);
	}

	@RequestMapping(value = MetaApi.API_FYEAR, method = RequestMethod.GET)
	public AmxApiResponse<UserFinancialYear, Object> getFinancialYearResponse() {
		return financialService.getFinancialYear();
	}

	@RequestMapping(value = MetaApi.HELP_DESK_TIME, method = RequestMethod.GET)
	public AmxApiResponse<AuthenticationLimitCheckDTO, Object> getContactTimeResponse() {
		return parameterService.getContactUsTime();
	}

	@RequestMapping(value = MetaApi.API_HELP_NO, method = RequestMethod.GET)
	public AmxApiResponse<AuthenticationLimitCheckDTO, Object> getContactNumberResponse() {
		return parameterService.getContactPhoneNo();
	}

	@RequestMapping(value = "/company/{languageId}", method = RequestMethod.GET)
	public AmxApiResponse<ViewCompanyDetailDTO, Object> getCompanyDetailResponse(
			@PathVariable("languageId") BigDecimal languageId) {
		return companyService.getCompanyDetails(languageId);
	}

	@RequestMapping(value = "/currency/{currencyId}", method = RequestMethod.GET)
	public AmxApiResponse<CurrencyMasterModel, Object> getCurrencyMasterResponse(
			@PathVariable("currencyId") BigDecimal currencyId) {
		return currencyMasterService.getCurrencyDetails(currencyId);
	}

	@RequestMapping(value = MetaApi.CURRENCY_ONLINE, method = RequestMethod.GET)
	public AmxApiResponse<CurrencyMasterDTO, Object> getAllOnlineCurrencyDetails() {
		return currencyMasterService.getAllOnlineCurrencyDetails();
	}

	// added by chetan 30/04/2018 list the country for currency.
	@RequestMapping(value = MetaApi.EXCHANGE_RATE_CURRENCY_LIST, method = RequestMethod.GET)
	public AmxApiResponse<CurrencyMasterDTO, Object> getAllExchangeRateCurrencyDetails() {
		return currencyMasterService.getAllExchangeRateCurrencyList();
	}

	@RequestMapping(value = "/currency/bycountry/{countryId}", method = RequestMethod.GET)
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyDetailsByCountryId(
			@PathVariable("countryId") BigDecimal countryId) {
		return currencyMasterService.getCurrencyByCountryId(countryId);
	}

	@RequestMapping(value = "/purpose/{documentNumber}/{documentFinancialYear}", method = RequestMethod.GET)
	public AmxApiResponse<PurposeOfRemittanceViewModel, Object> getPurposeOfRemittanceResponse(
			@PathVariable("documentNumber") BigDecimal documentNumber,
			@PathVariable("documentFinancialYear") BigDecimal documentFinancialYear) {
		return purposeOfRemittanceService.getPurposeOfRemittance(documentNumber, documentFinancialYear);
	}

	@RequestMapping(value = "/colldetview/{companyId}/{documentNo}/{documentFinancialYear}/{documentCode}",
			method = RequestMethod.GET)
	public AmxApiResponse<CollectionDetailViewModel, Object> getCollectionDetailFromView(
			@PathVariable("companyId") BigDecimal companyId, @PathVariable("documentNo") BigDecimal documentNo,
			@PathVariable("documentFinancialYear") BigDecimal documentFinancialYear) {
		return collectionDetailViewService.getCollectionDetailFromView(companyId, documentNo, documentFinancialYear,
				ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
	}

	@RequestMapping(value = "/collpaydetview/{companyId}/{documentNo}/{documentFinancialYear}/{documentCode}",
			method = RequestMethod.GET)
	public AmxApiResponse<CollectionPaymentDetailsViewModel, Object> getCollectPaymentDetailsFromView(
			@PathVariable("companyId") BigDecimal companyId, @PathVariable("documentNo") BigDecimal documentNo,
			@PathVariable("documentFinancialYear") BigDecimal documentFinancialYear) {
		LOGGER.info("Document :" + ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
		return collectionPaymentDetailsViewService.getCollectionPaymentDetailsFromView(companyId, documentNo,
				documentFinancialYear, ConstantDocument.DOCUMENT_CODE_FOR_COLLECT_TRANSACTION);
	}

	@RequestMapping(value = MetaApi.MULTI_COUNTRY, method = RequestMethod.GET)
	public AmxApiResponse<MultiCountryDTO, Object> getMultiCountry() {
		return multiCountryService.getMultiCountryList();
	}

	@RequestMapping(value = "/bank/{country-id}", method = RequestMethod.GET)
	public AmxApiResponse<BankMasterDTO, Object> getAllCurrencyDetails(
			@PathVariable("country-id") BigDecimal countryId) {
		return bankMasterService.getBanksApiResponseByCountryId(countryId);
	}

	@RequestMapping(value = MetaApi.API_DISTRICTDESC, method = RequestMethod.GET)
	public AmxApiResponse<ViewDistrictDto, Object> getDistrictNameResponse(
			@PathVariable(MetaApi.PARAM_STATE_ID) BigDecimal stateId,
			@PathVariable(MetaApi.PARAM_DISTRICT_ID) BigDecimal districtId) {
		return districtService.getDistrict(stateId, districtId, metaData.getLanguageId());
	}

	@RequestMapping(value = MetaApi.API_DISTRICTLIST, method = RequestMethod.GET)
	public AmxApiResponse<ViewDistrictDto, Object> getDistrictNameResponse(
			@PathVariable(MetaApi.PARAM_STATE_ID) BigDecimal stateId) {
		return districtService.getAllDistrict(stateId, metaData.getLanguageId());
	}

	@RequestMapping(value = MetaApi.API_STATE_DESC, method = RequestMethod.GET)
	public AmxApiResponse<ViewStateDto, Object> getStateNameResponse(
			@PathVariable(MetaApi.PARAM_COUNTRY_ID) BigDecimal countryId,
			@PathVariable(MetaApi.PARAM_STATE_ID) BigDecimal stateId) {
		return stateService.getState(countryId, stateId, metaData.getLanguageId());
	}

	@RequestMapping(value = MetaApi.API_STATE_LIST, method = RequestMethod.GET)
	public AmxApiResponse<ViewStateDto, Object> getStateNameListResponse(
			@PathVariable(MetaApi.PARAM_COUNTRY_ID) BigDecimal countryId) {
		return stateService.getStateAll(countryId, metaData.getLanguageId());
	}

	@RequestMapping(value = MetaApi.API_CITY_LIST, method = RequestMethod.GET)
	public AmxApiResponse<ViewCityDto, Object> getCityListResponse(
			@PathVariable(MetaApi.PARAM_DISTRICT_ID) BigDecimal districtId) {
		return metaService.getDistrictCity(districtId, metaData.getLanguageId());
	}

	@RequestMapping(value = MetaApi.API_CITY_DESC, method = RequestMethod.GET)
	public AmxApiResponse<ViewCityDto, Object> getCityNameResponse(
			@PathVariable(MetaApi.PARAM_DISTRICT_ID) BigDecimal districtid,
			@PathVariable(MetaApi.PARAM_CITY_ID) BigDecimal cityid) {
		return metaService.getCityDescription(districtid, metaData.getLanguageId(), cityid);
	}

	@RequestMapping(value = MetaApi.API_ONLINE_CONFIG, method = RequestMethod.GET)
	public AmxApiResponse<OnlineConfigurationDto, Object> getOnlineConfig(
			@PathVariable(MetaApi.PARAM_IND) String applInd) {
		return metaService.getOnlineConfig(applInd);
	}

	@RequestMapping(value = "/bankbranch/get/", method = RequestMethod.POST)
	public AmxApiResponse<BankBranchDto, Object> getBankBranches(@RequestBody GetBankBranchRequest request,
			BindingResult bindingResult) {
		LOGGER.info("in getbankBranches" + request.toString());
		JaxContextUtil.setJaxEvent(JaxEvent.BANK_BRANCH_SEARCH);
		JaxContextUtil.setRequestModel(request);
		bankBranchSearchRequestlValidator.validate(request, bindingResult);
		AmxApiResponse<BankBranchDto, Object> apiResponse = bankMasterService.getBankBranches(request);
		return apiResponse;
	}

	@RequestMapping(value = MetaApi.SERVICE_GROUP, method = RequestMethod.GET)
	public AmxApiResponse<ServiceGroupMasterDescDto, Object> getServiceGroup() {
		return metaService.getServiceGroups();
	}

	/**
	 * @param beneficiaryCountryId
	 * @param serviceGroupId
	 * @param routingBankId
	 * @return CurrencyMasterDTO
	 */
	@RequestMapping(value = "/currency/beneservice/", method = RequestMethod.GET)
	public AmxApiResponse<CurrencyMasterDTO, Object> getBeneficiaryCurrencyList(
			@RequestParam(value = "beneficiaryCountryId", required = true) BigDecimal beneficiaryCountryId,
			@RequestParam(value = "serviceGroupId", required = false) BigDecimal serviceGroupId,
			@RequestParam(value = "routingBankId", required = false) BigDecimal routingBankId) {
		return currencyMasterService.getBeneficiaryCurrencyList(beneficiaryCountryId, serviceGroupId, routingBankId);
	}

	@RequestMapping(value = MetaApi.META_PARAMETER, method = RequestMethod.GET)
	public AmxApiResponse<JaxMetaParameter, Object> getAuthParameter() {
		return parameterService.getJaxMetaParameter();
	}

	@RequestMapping(value = MetaApi.API_PREFIX, method = RequestMethod.GET)
	public AmxApiResponse<PrefixModel, Object> getPrefixList() {
		return prefixService.getPrefixListResponse();
	}

	@RequestMapping(value = MetaApi.API_BRANCH_DETAIL, method = RequestMethod.GET)
	public AmxApiResponse<BranchDetailModel, Object> getBranchDetail() {
		return branchDetailService.getBracnchDetailResponse();
	}

	@RequestMapping(value = "/branch/{countryBranchId}/systeminfo", method = RequestMethod.GET)
	public AmxApiResponse<BranchSystemDetail, Object> getBranchSystemList(
			@PathVariable("countryBranchId") BigDecimal countryBranchId) {
		return branchDetailService.getBranchSystemDetailResponse(countryBranchId);
	}

	@RequestMapping(value = MetaApi.API_BRANCH_SYSTEM_INV_LIST, method = RequestMethod.GET)
	public AmxApiResponse<BranchSystemDetailDto, Object> listBranchSystemInventory() {
		return branchDetailService.listBranchSystemInventory();
	}

	/**
	 * @Paurpose : Terms and condtion for FX Order
	 **/
	@RequestMapping(value = MetaApi.API_TERMS_BY_lANG_COUNTRY_ID_FOR_FX, method = RequestMethod.GET)
	public AmxApiResponse<TermsAndConditionDTO, Object> getTermsAndConditionAsPerCountryResponseforFxOrder() {
		return termsAndConditionService.getTermsAndConditionAsPerCountryForFxOrder(metaData.getLanguageId(),
				metaData.getCountryId());
	}

	@RequestMapping(value = MetaApi.API_AREA_LIST, method = RequestMethod.GET)
	public AmxApiResponse<ViewAreaDto, Object> getAreaList() {
		return metaService.getAreaList();
	}
	
	@RequestMapping(value = MetaApi.API_GOVERNATE_LIST, method = RequestMethod.GET)
	public AmxApiResponse<ViewGovernateDto, Object> getGovernateList() {
		return metaService.getGovernateList(metaData.getCountryId());
	}
	
	@RequestMapping(value = MetaApi.API_GOVERNATE_AREA_LIST, method = RequestMethod.GET)
	public AmxApiResponse<ViewGovernateAreaDto, Object> getGovernateAreaList(@RequestParam(value = "governateId", required = true) BigDecimal governateId) {
		return metaService.getGovernateAreaList(governateId);
	}
	
	@RequestMapping(value = MetaApi.API_STATUS_LIST, method = RequestMethod.GET)
	public AmxApiResponse<ViewStatusDto, Object> getStatusList() {
		return metaService.getStatusList();
	}
	
	@RequestMapping(value = MetaApi.API_COUNTRY_BRANCH_LIST, method = RequestMethod.GET)
	public AmxApiResponse<CountryBranchDTO, Object> getCountryBranchList() {
		return countryBranchService.getCountryBranchList();
	}
	
	
	
	@RequestMapping(value = MetaApi.API_DECLARATION, method = RequestMethod.POST)
	public AmxApiResponse<DeclarationDTO, Object> getDeclaration() {
		return metaService.getDeclaration(metaData.getLanguageId());
	}
}
