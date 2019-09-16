package com.amx.jax.branchremittance.manager;
/**
 * @author rabil 
 * @date 21/01/2019
 */

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.util.JaxValidationUtil;
import com.amx.jax.branchremittance.dao.BranchRemittanceDao;
import com.amx.jax.branchremittance.service.BranchRemittanceExchangeRateService;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.ExchangeRateProcedureDao;
import com.amx.jax.dao.BranchRemittancePaymentDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.partner.RemitApplSrvProv;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.AmiecAndBankMapping;
import com.amx.jax.dbmodel.remittance.BankBranch;
import com.amx.jax.dbmodel.remittance.DeliveryMode;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceModeMaster;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.CActivityEvent.Type;
import com.amx.jax.logger.events.RemitInfo;
import com.amx.jax.manager.RemittanceApplicationAdditionalDataManager;
import com.amx.jax.manager.RemittanceApplicationManager;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.manager.remittance.CorporateDiscountManager;
import com.amx.jax.manager.remittance.RemittanceAdditionalFieldManager;
import com.amx.jax.manager.remittance.RemittanceApplicationParamManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.AmlCheckResponseDto;
import com.amx.jax.model.response.remittance.BeneAdditionalDto;
import com.amx.jax.model.response.remittance.BranchRemittanceApplResponseDto;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.model.response.remittance.ServiceProviderDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.pricer.var.PricerServiceConstants;
import com.amx.jax.repository.BankMasterRepository;
import com.amx.jax.repository.DeviceStateRepository;
import com.amx.jax.repository.IAmiecAndBankMappingRepository;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.IDeviceRepository;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.BeneficiaryValidationService;
import com.amx.jax.services.LoyalityPointService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.AmxDBConstants;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.RemittanceTransactionRequestValidator;
import com.amx.utils.JsonUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceApplManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MetaData metaData;

	@Autowired
	BranchRemittanceManager branchRemitManager;

	@Autowired
	IDocumentDao documentDao;

	@Autowired
	IApplicationCountryRepository applCountryRepos;

	@Autowired
	CompanyService companyService;

	@Autowired
	BankMetaService bankMetaService;

	@Autowired
	FinancialService finanacialService;

	@Autowired
	ICurrencyDao currDao;

	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;

	@Autowired
	RemittanceApplicationManager remitApplManager;

	@Autowired
	EmployeeDetailsRepository employeeDetailsRepository;

	@Autowired
	BeneficiaryService beneficiaryService;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	BankService bankService;

	@Resource
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	BranchRemittanceDao brRemittanceDao;

	@Autowired
	BranchRemittancePaymentManager branchRemittancePaymentManager; 

	@Autowired
	LoyalityPointService loyalityPointService;

	@Autowired
	RemittanceApplicationDao remittApplDao;

	@Autowired
	RemittanceApplicationAdditionalDataManager remittanceAppAddlDataManager;

	@Autowired
	BranchRoutingManager branchRoutingManager;

	@Autowired
	BranchRemittanceExchangeRateService branchExchRateService;

	@Autowired
	RemittanceTransactionRequestValidator remittanceTransactionRequestValidator;

	@Autowired
	RemittanceAdditionalFieldManager remittanceAdditionalFieldManager;

	@Autowired
	DeviceStateRepository deviceStateRepository;

	@Autowired
	CorporateDiscountManager corporateDiscountManager;

	@Autowired
	IDeviceRepository deviceRepository;

	@Autowired
	RemittanceApplicationRepository appRepository;

	@Autowired
	AuditService auditService;

	@Autowired
	RemittanceTransactionManager remitTrnxManager;

	@Autowired
	private ExchangeRateProcedureDao exchangeRateProcedureDao;

	@Autowired
	private BizcomponentDao bizcomponentDao;

	@Autowired
	BranchRemittanceExchangeRateManager branchRemittanceExchangeRateManager;

	@Autowired
	BeneficiaryValidationService beneValidationService;

	@Autowired
	RemittanceApplicationParamManager remittanceApplicationParamManager;
	
	@Autowired
	BranchRemittancePaymentDao branchRemittancePaymentDao;
	
	@Autowired
	BankMasterRepository bankMasterRepo;
	
	@Autowired
	IAmiecAndBankMappingRepository amiecAndBankMappingRepository;
	
	@Autowired
	PartnerTransactionManager partnerTransactionManager;
	
	
	public BranchRemittanceApplResponseDto saveBranchRemittanceApplication(BranchRemittanceApplRequestModel requestApplModel) {
		Map<String,Object> hashMap = new HashMap<>();

		validateSaveApplRequest(requestApplModel);

		// validation for Home Send SP
		checkServiceProviderValidation(requestApplModel);

		/*To fetch customer details **/
		Customer customer = custDao.getCustById(metaData.getCustomerId());
		/** To fetch bene details **/
		BenificiaryListView beneficaryDetails =getBeneDetails(requestApplModel);

		/** to vlidate BSB  account though api **/
		/*	String errMsg = remitTrnxManager.beneAccountValidationThroughApi(requestApplModel.getServiceMasterId(),requestApplModel.getRoutingBankId(),beneficaryDetails);
			if(!StringUtils.isBlank(errMsg)) {
				throw new GlobalException(JaxError.BSB_ACCOUNT_VALIATION,"Invalid account number "+errMsg);
			}*/
		/** end here */

		/*checkingStaffIdNumberWithCustomer **/
		branchRemitManager.checkingStaffIdNumberWithCustomer();
		/*checking bene account exception  **/
		branchRemitManager.beneAccountException(beneficaryDetails);
		/*checking bene account type **/
		branchRemitManager.checkBeneAccountType(beneficaryDetails);
		/*checking bene additional info missing detail check**/
		branchRemitManager.beneAddCheck(beneficaryDetails);
		/*checking banned bank details **/
		// String warningMsg = branchRemitManager.bannedBankCheck(requestApplModel.getBeneId());
		/* validate blck list bene **/
		branchRemitManager.validateBlackListedBene(beneficaryDetails);

		if(JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getRoutingBankId())) {
			requestApplModel.setRoutingBankId(requestApplModel.getRoutingBankId());
		}

		ExchangeRateBreakup rateBreakUp = requestApplModel.getDynamicRroutingPricingBreakup().getExRateBreakup();

		ExchangeRateBreakup exchangeRateBreakup = null;
		BigDecimal commission = null;
		String bankIndicator = requestApplModel.getDynamicRroutingPricingBreakup().getTrnxRoutingPaths().getBankIndicator();
		if(bankIndicator != null && bankIndicator.equalsIgnoreCase(ConstantDocument.BANK_INDICATOR_SERVICE_PROVIDER_BANK)) {
			// no need to call probot
			exchangeRateBreakup = rateBreakUp;
			commission = requestApplModel.getDynamicRroutingPricingBreakup().getTxnFee();
			logger.debug("branchExchangeRate :"+exchangeRateBreakup+" commission : "+commission);
			BenificiaryListView beneficiaryView = beneValidationService.validateBeneficiary(beneficaryDetails.getBeneficiaryRelationShipSeqId());
			remittanceApplicationParamManager.populateRemittanceApplicationParamMap(requestApplModel, beneficiaryView,rateBreakUp);
		}else {
			//Dynamic Routing and Priccing API
			BranchRemittanceGetExchangeRateResponse exchangeRateResposne =branchRemittanceExchangeRateManager.getDynamicRoutingAndPricingExchangeRateResponseCompare(requestApplModel);
			remittanceTransactionRequestValidator.validateExchangeRate(requestApplModel, exchangeRateResposne);
			logger.debug("branchExchangeRate :"+exchangeRateResposne);

			exchangeRateBreakup = exchangeRateResposne.getExRateBreakup();
			commission = exchangeRateResposne.getTxnFee();
			logger.debug("branchExchangeRate :"+exchangeRateBreakup+" commission : "+commission);
		}

		remittanceTransactionRequestValidator.validateFlexFields(requestApplModel, remitApplParametersMap);
		remittanceAdditionalFieldManager.validateAdditionalFields(requestApplModel, remitApplParametersMap);
		remittanceAdditionalFieldManager.processAdditionalFields(requestApplModel); 

		//logger.debug("branchExchangeRate :"+exchangeRateResposne);
		/* get aml cehck   details **/
		List<AmlCheckResponseDto> amlList= branchRemitManager.amlTranxAmountCheckForRemittance(requestApplModel.getBeneId(),rateBreakUp.getConvertedLCAmount());
		logger.info("amlList :"+amlList.toString());
		/* additional check **/ 
		branchRemitManager.validateAdditionalCheck(customer,beneficaryDetails,rateBreakUp.getNetAmount(),requestApplModel);

		/** bene additional check **/
		// Map<String, Object> addBeneDetails =branchRemitManager.validateAdditionalBeneDetails(beneficaryDetails,requestApplModel);

		BeneAdditionalDto beneAddlDto  =branchRemitManager.getAdditionalBeneDetailJax(beneficaryDetails,requestApplModel);

		/** validate trnx limit check **/
		branchRemitManager.validateTrnxLimitCheck(exchangeRateBreakup,commission,beneficaryDetails);

		//hashMap.put("EXCH_RATE_MAP", exchangeRateResposne);
		hashMap.put("APPL_REQ_MODEL", requestApplModel);
		hashMap.put("BENEFICIARY_DETAILS", beneficaryDetails);
		//hashMap.put("ADD_BENE_DETAILS", addBeneDetails);
		hashMap.put("ADD_BENE_DETAILS", beneAddlDto);
		hashMap.put("CUSTOMER", customer);
		hashMap.put("AML_CHECK", amlList);
		branchRemitManager.validateAdditionalErrorMessages(hashMap);

		/* create applciation */
		RemittanceApplication remittanceApplication = this.createRemittanceApplication(hashMap);
		RemittanceAppBenificiary remittanceAppBeneficairy = this.createRemittanceAppBeneficiary(remittanceApplication,hashMap);
		List<AdditionalInstructionData>  additioalInstructionData = remittanceAppAddlDataManager.createAdditionalInstnDataForBranch(remittanceApplication,hashMap);

		//RemittanceTransactionRequestModel
		List<RemitApplAmlModel> amlData = this.saveRemittanceAppAML(remittanceApplication,hashMap);

		// Remittance srv prov details
		RemitApplSrvProv remitApplSrvProv = createRemitApplSrvProv(requestApplModel.getDynamicRroutingPricingBreakup(),remittanceApplication.getCreatedBy());
		if(remitApplSrvProv != null) {
			for (AdditionalInstructionData applAddlData : additioalInstructionData) {
				if(applAddlData.getFlexField() != null && applAddlData.getFlexField().equalsIgnoreCase(AmxDBConstants.INDIC1)) {
					if(applAddlData.getAmiecCode() != null) {
						AmiecAndBankMapping amicAndBankMapping = amiecAndBankMappingRepository.fetchAmiecBankData(remittanceApplication.getFsCountryMasterByBankCountryId().getCountryId(), remitApplSrvProv.getBankId(), applAddlData.getFlexField(), applAddlData.getAmiecCode(), AmxDBConstants.Yes);
						if(amicAndBankMapping != null) {
							remittanceApplication.setWuPurposeOfTransaction(amicAndBankMapping.getBankCode());
							break;
						}
					}
				}
			}
		}

		/* Saving application deatils */
		HashMap<String, Object> mapAllDetailApplSave = new HashMap<String, Object>();
		mapAllDetailApplSave.put("EX_APPL_TRNX", remittanceApplication);
		mapAllDetailApplSave.put("EX_APPL_BENE", remittanceAppBeneficairy);
		mapAllDetailApplSave.put("EX_APPL_ADDL", additioalInstructionData);
		mapAllDetailApplSave.put("EX_APPL_AML", amlData);
		mapAllDetailApplSave.put("EX_APPL_SRV_PROV", remitApplSrvProv);
		validateApplDetails(mapAllDetailApplSave);
		brRemittanceDao.saveAllApplications(mapAllDetailApplSave);
		auditService.log(new CActivityEvent(Type.APPLICATION_CREATED,String.format("%s/%s", remittanceApplication.getDocumentFinancialyear(),remittanceApplication.getDocumentNo()))
				.field("STATUS").to(JaxTransactionStatus.APPLICATION_CREATED)
				.set(new RemitInfo(remittanceApplication.getRemittanceApplicationId(), remittanceApplication.getLocalTranxAmount()))
				.result(Result.DONE));
		/*checking banned bank details **/
		String warningMsg = branchRemitManager.bannedBankCheck(requestApplModel.getBeneId());
		BranchRemittanceApplResponseDto applResponseDto = branchRemittancePaymentManager.fetchCustomerShoppingCart(customer.getCustomerId(),metaData.getDefaultCurrencyId());
		applResponseDto.setWarnigMsg(warningMsg);
		return applResponseDto;
	}

	@SuppressWarnings("unchecked")
	private RemittanceApplication createRemittanceApplication(Map<String,Object> hashMap){

		RemittanceApplication remittanceApplication = new RemittanceApplication();
		try {

			String signature =null;
			BranchRemittanceApplRequestModel applRequestModel = (BranchRemittanceApplRequestModel)hashMap.get("APPL_REQ_MODEL");
			BenificiaryListView beneDetails  =(BenificiaryListView) hashMap.get("BENEFICIARY_DETAILS");

			if(!StringUtils.isBlank(applRequestModel.getSignature())) {
				signature =applRequestModel.getSignature();
			}else {
				//signature =getCustomerSignature();
				throw new GlobalException(JaxError.CUSTOMER__SIGNATURE_UNAVAILABLE,"Customer signature required");
			}

			if(!StringUtils.isBlank(signature)) {
				try {
					remittanceApplication.setCustomerSignatureClob(stringToClob(signature));
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else {
				throw new GlobalException(JaxError.CUSTOMER_SIGNATURE_UNAVAILABLE,"Customer signature required");
			}

			DynamicRoutingPricingDto dynamicRoutingPricingResponse =applRequestModel.getDynamicRroutingPricingBreakup();
			ExchangeRateBreakup rateBreakUp = dynamicRoutingPricingResponse.getExRateBreakup();

			//remitTrnxManager.reCalculateComission();

			BigDecimal routingCountryId = applRequestModel.getRoutingCountryId();
			Customer customer = (Customer) hashMap.get("CUSTOMER");
			BigDecimal routingBankId = applRequestModel.getRoutingBankId();
			BigDecimal routingBankBranchId =applRequestModel.getRoutingBankBranchId();
			BigDecimal foreignCurrencyId = beneDetails.getCurrencyId();
			BigDecimal deliveryId =applRequestModel.getDeliveryModeId();
			BigDecimal remittanceId = applRequestModel.getRemittanceModeId();

			BigDecimal selectedCurrencyId = branchRemitManager.getSelectedCurrency(foreignCurrencyId, applRequestModel);

			Document document = documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION).get(0);

			remittanceApplication.setExDocument(document);
			remittanceApplication.setDocumentCode(document.getDocumentCode());
			CountryMaster appCountryId = new CountryMaster();

			ApplicationSetup applSetup = getApplicationSetup();
			CurrencyMasterModel LocalCurrencyModel = getCurrencyByCountryId(applSetup.getApplicationCountryId());


			appCountryId.setCountryId(applSetup.getApplicationCountryId());
			remittanceApplication.setFsCountryMasterByApplicationCountryId(appCountryId);
			CurrencyMasterModel localCurrency = new CurrencyMasterModel();
			CurrencyMasterModel foreignCurrency = new CurrencyMasterModel();
			foreignCurrency.setCurrencyId(foreignCurrencyId);
			localCurrency.setCurrencyId(LocalCurrencyModel.getCurrencyId());
			remittanceApplication.setSourceofincome(applRequestModel.getSourceOfFund());
			remittanceApplication.setExCurrencyMasterByForeignCurrencyId(foreignCurrency);
			// commission currency
			remittanceApplication.setExCurrencyMasterByLocalCommisionCurrencyId(localCurrency);
			// local currency
			remittanceApplication.setExCurrencyMasterByLocalTranxCurrencyId(localCurrency);
			// local charge currency
			remittanceApplication.setExCurrencyMasterByLocalChargeCurrencyId(localCurrency);
			// net amt currency
			remittanceApplication.setExCurrencyMasterByLocalNetCurrencyId(localCurrency);
			remittanceApplication.setSpotRateInd(ConstantDocument.No);

			// company Id and code
			CompanyMaster companymaster = new CompanyMaster();
			companymaster.setCompanyId(applSetup.getCompanyId());
			remittanceApplication.setFsCompanyMaster(companymaster);
			ViewCompanyDetails companyDetails = companyService.getCompanyDetailsById(applSetup.getCompanyId());
			remittanceApplication.setCompanyCode(companyDetails.getCompanyCode());
			// branch id
			CountryBranch countryBranch = bankMetaService.getCountryBranchById(getEmployeeDetails().getCountryBranchId()); //user branch not customer branch
			remittanceApplication.setLoccod(countryBranch.getBranchId());
			remittanceApplication.setExCountryBranch(countryBranch);
			// fin year
			UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
			if(userFinancialYear!=null) {
				remittanceApplication.setExUserFinancialYearByDocumentFinanceYear(userFinancialYear);
				remittanceApplication.setTransactionFinancialyear(userFinancialYear.getFinancialYear());
				remittanceApplication.setDocumentFinancialyear(userFinancialYear.getFinancialYear());
			}else {
				throw new GlobalException(JaxError.NULL_FINANCIAL_YEAR,"Financial error is not defined");
			}

			// routing Country
			CountryMaster bencountrymaster = new CountryMaster();
			bencountrymaster.setCountryId(routingCountryId);
			remittanceApplication.setFsCountryMasterByBankCountryId(bencountrymaster);

			// Delivery Mode from service
			DeliveryMode deliverymode = new DeliveryMode();

			deliverymode.setDeliveryModeId(deliveryId);
			remittanceApplication.setExDeliveryMode(deliverymode);

			// RemittanceModeMaster to get Remittance
			RemittanceModeMaster remittancemode = new RemittanceModeMaster();
			remittancemode.setRemittanceModeId(remittanceId);
			remittanceApplication.setExRemittanceMode(remittancemode);

			// Customer id
			remittanceApplication.setFsCustomer(customer);
			remittanceApplication.setCustomerRef(customer.getCustomerReference());
			remittanceApplication.setCustomerName(getCustomerFullName(customer));

			// Routing Bank
			BankMasterModel bankmaster = new BankMasterModel();
			bankmaster.setBankId(routingBankId);
			remittanceApplication.setExBankMaster(bankmaster);

			// Routing Bank Branch
			BankBranch bankbranch = new BankBranch();
			bankbranch.setBankBranchId(routingBankBranchId);
			remittanceApplication.setExBankBranch(bankbranch);
			// document date
			remittanceApplication.setDocumentDate(new Date());
			remittanceApplication.setDebitAccountNo(beneDetails.getBankAccountNumber());

			// rates
			if(JaxUtil.isNullZeroBigDecimalCheck(rateBreakUp.getConvertedFCAmount())) {
				remittanceApplication.setForeignTranxAmount(rateBreakUp.getConvertedFCAmount());
			}else {
				throw new GlobalException(JaxError.INVALID_FC_AMOUNT,"Invalid foreign Amount");
			}
			if(JaxUtil.isNullZeroBigDecimalCheck(rateBreakUp.getConvertedLCAmount())) {
				remittanceApplication.setLocalTranxAmount(rateBreakUp.getConvertedLCAmount());
			}else {
				throw new GlobalException(JaxError.INVALID_FC_AMOUNT,"Invalid local Amount");
			}
			if(JaxUtil.isNullZeroBigDecimalCheck(rateBreakUp.getNetAmountWithoutLoyality())) {
				remittanceApplication.setLocalNetTranxAmount(rateBreakUp.getNetAmountWithoutLoyality());
			}else {
				throw new GlobalException(JaxError.INVALID_FC_AMOUNT,"Invalid local Amount");
			}

			if(JaxUtil.isNullZeroBigDecimalCheck(rateBreakUp.getInverseRate())) {
				remittanceApplication.setExchangeRateApplied(rateBreakUp.getInverseRate());
			}else {
				throw new GlobalException(JaxError.EXCHANGE_RATE_ERROR,"Invalid exchange rate");
			}


			remittanceApplication.setLocalCommisionAmount(dynamicRoutingPricingResponse.getTxnFee());
			remittanceApplication.setLocalChargeAmount(BigDecimal.ZERO);
			remittanceApplication.setLocalDeliveryAmount(BigDecimal.ZERO);
			remittanceApplication.setLoyaltyPointsEncashed(BigDecimal.ZERO);

			BigDecimal loyalityPointsEncashed = BigDecimal.ZERO;
			if(applRequestModel.isAvailLoyalityPoints() && JaxUtil.isNullZeroBigDecimalCheck(customer.getLoyaltyPoints()) && customer.getLoyaltyPoints().compareTo(new BigDecimal(1000))>=0) {
				remittanceApplication.setLoyaltyPointInd(ConstantDocument.Yes);
				loyalityPointsEncashed = getloyaltyAmountEncashed(dynamicRoutingPricingResponse.getTxnFee());
			}else {
				remittanceApplication.setLoyaltyPointInd(ConstantDocument.No);
			}

			remittanceApplication.setLoyaltyPointsEncashed(loyalityPointsEncashed); 


			remittanceApplication.setDocumentFinancialyear(userFinancialYear.getFinancialYear());
			remittanceApplication.setSelectedCurrencyId(selectedCurrencyId);

			try {
				remittanceApplication.setAccountMmyyyy(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
			} catch (ParseException e) {
				logger.error("Error in saving application", e);
			}

			remittanceApplication.setCreatedBy(getEmployeeDetails().getUserName());
			remittanceApplication.setEmployeeId(getEmployeeDetails().getEmployeeId());
			remittanceApplication.setCreatedDate(new Date());
			remittanceApplication.setIsactive(ConstantDocument.Yes);
			remittanceApplication.setSourceofincome(applRequestModel.getSourceOfFund());
			remittanceApplication.setApplInd(ConstantDocument.COUNTER);
			remittanceApplication.setWuIpAddress(metaData.getDeviceIp());
			remittanceApplication.setInstruction("URGENT");
			if(JaxUtil.isNullZeroBigDecimalCheck(remittanceApplication.getLocalCommisionAmount())) {
				remittanceApplication.setDiscountOnCommission(corporateDiscountManager.corporateDiscount());
			}

			if(dynamicRoutingPricingResponse.getCostRateLimitReached()!=null) {
				remittanceApplication.setReachedCostRateLimit(dynamicRoutingPricingResponse.getCostRateLimitReached()==false?"N":"Y");
			}
			
			if(JaxUtil.isNullZeroBigDecimalCheck(rateBreakUp.getBaseRate())) {
				remittanceApplication.setOriginalExchangeRate(rateBreakUp.getBaseRate());
			}
			
			if(dynamicRoutingPricingResponse.getServiceProviderDto() != null && dynamicRoutingPricingResponse.getServiceProviderDto().getIntialAmountInSettlCurr() != null) {
				remittanceApplication.setUsdAmt(dynamicRoutingPricingResponse.getServiceProviderDto().getIntialAmountInSettlCurr());
			}

			remittanceApplication.setBeneDeductFlag(dynamicRoutingPricingResponse.getBeneDeductFlag());

			remitApplManager.setCustomerDiscountColumns(remittanceApplication, dynamicRoutingPricingResponse);
			remitApplManager.setVatDetails(remittanceApplication, dynamicRoutingPricingResponse);

			BigDecimal documentNo = branchRemitManager.generateDocumentNumber(applSetup.getApplicationCountryId(), applSetup.getCompanyId(), ConstantDocument.DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION, userFinancialYear.getFinancialYear(), ConstantDocument.A, countryBranch.getBranchId());
			if(JaxUtil.isNullZeroBigDecimalCheck(documentNo)) {
				remittanceApplication.setDocumentNo(documentNo);
			}else {
				throw new GlobalException(JaxError.INVALID_APPLICATION_DOCUMENT_NO,"Application document number shouldnot be null or blank");
			}			



			return remittanceApplication;

		}catch(GlobalException e){
			logger.error("create application", e.getErrorMessage() + "" +e.getErrorKey());
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}


	}

	@SuppressWarnings("unchecked")
	private RemittanceAppBenificiary createRemittanceAppBeneficiary(RemittanceApplication remittanceApplication,Map<String, Object> hashMap) {

		logger.info(" Enter into saveRemittanceAppBenificary :");
		RemittanceAppBenificiary remittanceAppBenificary = new RemittanceAppBenificiary();
		BenificiaryListView beneficiaryDT  =(BenificiaryListView) hashMap.get("BENEFICIARY_DETAILS");
		String telNumber = beneficiaryService.getBeneficiaryContactNumber(beneficiaryDT.getBeneficaryMasterSeqId());
		//Map<String,Object> beneAddDeatisl = (HashMap)hashMap.get("ADD_BENE_DETAILS");
		BeneAdditionalDto beneAddDeatisl = (BeneAdditionalDto)hashMap.get("ADD_BENE_DETAILS");

		remittanceAppBenificary = new RemittanceAppBenificiary();

		// Document Id
		Document documentid = new Document();
		documentid.setDocumentID(remittanceApplication.getExDocument().getDocumentID());
		remittanceAppBenificary.setExDocument(documentid);

		// company Id
		CompanyMaster companymaster = new CompanyMaster();
		companymaster.setCompanyId(remittanceApplication.getFsCompanyMaster().getCompanyId());
		remittanceAppBenificary.setFsCompanyMaster(companymaster);

		// company code
		remittanceAppBenificary.setCompanyCode(remittanceApplication.getCompanyCode());

		// User Financial Year for Transaction
		UserFinancialYear userfinancialyear = new UserFinancialYear();
		userfinancialyear.setFinancialYearID(remittanceApplication.getExUserFinancialYearByDocumentFinanceYear().getFinancialYearID());
		remittanceAppBenificary.setExUserFinancialYear(userfinancialyear);

		// RemittanceApplication Id
		remittanceAppBenificary.setExRemittanceAppfromBenfi(remittanceApplication);
		remittanceAppBenificary.setDocumentCode(remittanceApplication.getDocumentCode());
		remittanceAppBenificary.setDocumentNo(remittanceApplication.getDocumentNo());
		remittanceAppBenificary.setBeneficiaryId(beneficiaryDT.getBeneficaryMasterSeqId());


		if (beneficiaryDT.getBankAccountNumber() != null) {
			remittanceAppBenificary.setBeneficiaryAccountNo(getAccountNumber(beneficiaryDT));
		}

		remittanceAppBenificary.setBeneficiaryBank(beneficiaryDT.getBankName());

		/*	remittanceAppBenificary.setBeneficiaryBranch(beneAddDeatisl.get("P_BENE_BRANCH_NAME")==null?beneficiaryDT.getBankBranchName():(String) beneAddDeatisl.get("P_BENE_BRANCH_NAME"));
		remittanceAppBenificary.setBeneficiaryName(beneAddDeatisl.get("P_BENEFICIARY_NAME")==null? beneficiaryDT.getBenificaryName():(String) beneAddDeatisl.get("P_BENEFICIARY_NAME"));
		remittanceAppBenificary.setBeneficiaryFirstName(beneAddDeatisl.get("P_BENEFICIARY_FIRST_NAME")==null?beneficiaryDT.getFirstName():(String) beneAddDeatisl.get("P_BENEFICIARY_FIRST_NAME"));
		remittanceAppBenificary.setBeneficiarySecondName(beneAddDeatisl.get("P_BENEFICIARY_SECOND_NAME")==null?beneficiaryDT.getSecondName():(String) beneAddDeatisl.get("P_BENEFICIARY_SECOND_NAME"));
		remittanceAppBenificary.setBeneficiaryThirdName(beneAddDeatisl.get("P_BENEFICIARY_THIRD_NAME")==null?beneficiaryDT.getThirdName():(String) beneAddDeatisl.get("P_BENEFICIARY_THIRD_NAME"));
		remittanceAppBenificary.setBeneficiaryFourthName(beneAddDeatisl.get("P_BENEFICIARY_FOURTH_NAME")==null?beneficiaryDT.getFourthName():(String) beneAddDeatisl.get("P_BENEFICIARY_FOURTH_NAME"));
		remittanceAppBenificary.setBeneficiaryFifthName(beneAddDeatisl.get("P_BENEFICIARY_FIFTH_NAME")==null?beneficiaryDT.getFiftheName():(String) beneAddDeatisl.get("P_BENEFICIARY_FIFTH_NAME"));

		remittanceAppBenificary.setBeneficiaryBranchStateId(beneAddDeatisl.get("P_BENEFICIARY_STATE_ID")!=null?(BigDecimal)beneAddDeatisl.get("P_BENEFICIARY_STATE_ID"):null);
		remittanceAppBenificary.setBeneficiaryBranchDistrictId(beneAddDeatisl.get("P_BENEFICIARY_DISTRICT_ID")!=null?(BigDecimal)beneAddDeatisl.get("P_BENEFICIARY_DISTRICT_ID"):null);
		remittanceAppBenificary.setBeneficiaryBranchCityId(beneAddDeatisl.get("P_BENEFICIARY_CITY_ID")!=null?(BigDecimal)beneAddDeatisl.get("P_BENEFICIARY_CITY_ID"):null);
		 */

		remittanceAppBenificary.setBeneficiaryBranch(beneAddDeatisl.getBeneBranchName()==null?beneficiaryDT.getBankBranchName():beneAddDeatisl.getBeneBranchName());
		remittanceAppBenificary.setBeneficiaryName(beneAddDeatisl.getBeneName()==null? beneficiaryDT.getBenificaryName():beneAddDeatisl.getBeneName());
		remittanceAppBenificary.setBeneficiaryFirstName(beneAddDeatisl.getBeneFirstName()==null?beneficiaryDT.getFirstName():beneAddDeatisl.getBeneFirstName());
		remittanceAppBenificary.setBeneficiarySecondName(beneAddDeatisl.getBeneSecondName()==null?beneficiaryDT.getSecondName():beneAddDeatisl.getBeneSecondName());
		remittanceAppBenificary.setBeneficiaryThirdName(beneAddDeatisl.getBeneThirdName()==null?beneficiaryDT.getThirdName():beneAddDeatisl.getBeneThirdName());
		remittanceAppBenificary.setBeneficiaryFourthName(beneAddDeatisl.getBeneFourthName()==null?beneficiaryDT.getFourthName():beneAddDeatisl.getBeneFourthName());
		remittanceAppBenificary.setBeneficiaryFifthName(beneAddDeatisl.getBeneFifthName()==null?beneficiaryDT.getFiftheName():beneAddDeatisl.getBeneFifthName());

		remittanceAppBenificary.setBeneficiaryBranchStateId(beneAddDeatisl.getStateId());
		remittanceAppBenificary.setBeneficiaryBranchDistrictId(beneAddDeatisl.getDistrictId());
		remittanceAppBenificary.setBeneficiaryBranchCityId(beneAddDeatisl.getCityId());



		if(beneficiaryDT.getSwiftBic()!=null) {
			remittanceAppBenificary.setBeneficiaryBankSwift(beneficiaryDT.getSwiftBic());
		}else {
			remittanceAppBenificary.setBeneficiaryBankSwift(bankService.getBranchSwiftCode(beneficiaryDT.getBankId(),beneficiaryDT.getBranchId()));
		}


		remittanceAppBenificary.setCreatedBy(remittanceApplication.getCreatedBy());
		remittanceAppBenificary.setCreatedDate(new Date());
		remittanceAppBenificary.setIsactive(ConstantDocument.Yes);


		remittanceAppBenificary.setBeneficiaryBankCountryId(beneficiaryDT.getBenificaryCountry());
		remittanceAppBenificary.setBeneficiaryBankId(beneficiaryDT.getBankId());
		remittanceAppBenificary.setBeneficiaryBankBranchId(beneficiaryDT.getBranchId());
		remittanceAppBenificary.setBeneficiaryAccountSeqId(beneficiaryDT.getBeneficiaryAccountSeqId());
		remittanceAppBenificary.setBeneficiaryRelationShipSeqId(beneficiaryDT.getBeneficiaryRelationShipSeqId());
		remittanceAppBenificary.setBeneficiaryTelephoneNumber(telNumber);

		logger.info(" Exit from saveRemittanceAppBenificary ");

		return remittanceAppBenificary;
	}


	// checking Indic1,Indic2,Indic3,Indic4,Indic5
	private AdditionalInstructionData createAdditionalIndicatorsData(RemittanceApplication remittanceApplication,BigDecimal applicationCountryId, String indicatorCode, String amiecCode, String flexFieldValue,BigDecimal additionalBankRuleId) {

		logger.info(" Enter into fetchIndicatorsData :" + flexFieldValue);

		AdditionalInstructionData additionalInsData = new AdditionalInstructionData();

		// document Id
		Document document = new Document();
		document.setDocumentID(remittanceApplication.getExDocument().getDocumentID());
		additionalInsData.setExDocument(document);

		// company Id
		CompanyMaster companymaster = new CompanyMaster();
		companymaster.setCompanyId(remittanceApplication.getFsCompanyMaster().getCompanyId());
		additionalInsData.setFsCompanyMaster(companymaster);

		// Application Country
		CountryMaster countrymaster = new CountryMaster();
		countrymaster.setCountryId(applicationCountryId);
		additionalInsData.setFsCountryMaster(countrymaster);

		if (additionalBankRuleId != null) {
			AdditionalBankRuleMap additionalBank = new AdditionalBankRuleMap();
			additionalBank.setAdditionalBankRuleId(additionalBankRuleId);
			additionalInsData.setAdditionalBankFieldsId(additionalBank);
		}
		additionalInsData.setFlexField(indicatorCode);
		additionalInsData.setFlexFieldValue(flexFieldValue);
		if (amiecCode != null) {
			additionalInsData.setAmiecCode(amiecCode);
		} else {
			additionalInsData.setAmiecCode(ConstantDocument.AMIEC_CODE);
		}

		additionalInsData.setExRemittanceApplication(remittanceApplication);
		additionalInsData.setExUserFinancialYear(remittanceApplication.getExUserFinancialYearByDocumentFinanceYear());
		additionalInsData.setDocumentFinanceYear(remittanceApplication.getDocumentFinancialyear());

		additionalInsData.setCreatedBy(remittanceApplication.getCreatedBy());
		additionalInsData.setCreatedDate(new Date());
		additionalInsData.setIsactive(ConstantDocument.Yes);
		additionalInsData.setDocumentNo(remittanceApplication.getDocumentNo());

		logger.info(" Exit from fetchIndicatorsData ");

		return additionalInsData;
	}


	/* saving data to Remittance Application AML */
	@SuppressWarnings("unchecked")
	public List<RemitApplAmlModel> saveRemittanceAppAML(RemittanceApplication remittanceApplication,Map hashMap){
		List<AmlCheckResponseDto> amlList =(List<AmlCheckResponseDto>)hashMap.get("AML_CHECK");
		BranchRemittanceApplRequestModel requestApplModel = (BranchRemittanceApplRequestModel)hashMap.get("APPL_REQ_MODEL");
		List<RemitApplAmlModel> remitApplAmlList = new ArrayList<RemitApplAmlModel>();

		try {
			for(AmlCheckResponseDto amlDto :amlList) {
				RemitApplAmlModel amlModel = new RemitApplAmlModel();

				RemitApplAmlModel remitApplAml = new RemitApplAmlModel();
				remitApplAml.setCompanyId(remittanceApplication.getFsCompanyMaster().getCompanyId());
				remitApplAml.setExRemittanceAppfromAml(remittanceApplication);
				remitApplAml.setCountryId(remittanceApplication.getFsCountryMasterByApplicationCountryId().getCountryId());
				remitApplAml.setCreatedBy(remittanceApplication.getCreatedBy());
				remitApplAml.setCreatedDate(new Date());
				remitApplAml.setIsactive(ConstantDocument.Yes);
				remitApplAml.setAuthorizedBy(requestApplModel.getStaffUserName());
				remitApplAml.setAuthType(null);
				remitApplAml.setBlackListReason(amlDto.getMessageDescription());
				remitApplAml.setBlackListRemarks(requestApplModel.getAmlRemarks());
				remitApplAmlList.add(remitApplAml);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return remitApplAmlList;
	}




	public ApplicationSetup getApplicationSetup() {

		ApplicationSetup appl = applCountryRepos.findByCountryIdAndCompanyIdDeatils(metaData.getCompanyId(),metaData.getCountryId());
		if(appl==null) {
			appl = applCountryRepos.getApplicationSetupDetails();
		}		
		if(appl==null) {
			throw new GlobalException(JaxError.INVALID_APPLICATION_COUNTRY_ID,"Application country setup not defined");
		}
		return appl;
	}

	public CurrencyMasterModel getCurrencyByCountryId(BigDecimal applCountryId) {

		List<CurrencyMasterModel> currencyMaster = currDao.getCurrencyListByCountryId(applCountryId);
		if(currencyMaster!=null && !currencyMaster.isEmpty()) {
			return currencyMaster.get(0);
		}else {
			throw new GlobalException(JaxError.INVALID_CURRENCY_ID,"Invalid currency");
		}

	}

	public BenificiaryListView getBeneDetails(BranchRemittanceApplRequestModel requestApplModel) { 
		BenificiaryListView beneficaryDetails =beneficiaryRepository.findByCustomerIdAndBeneficiaryRelationShipSeqIdAndIsActive(metaData.getCustomerId(),requestApplModel.getBeneId(),ConstantDocument.Yes);
		if(beneficaryDetails==null) {
			throw new GlobalException(JaxError.BENEFICIARY_LIST_NOT_FOUND,"Beneficairy not found "+metaData.getCustomerId()+"/"+requestApplModel.getBeneId());
		}
		return beneficaryDetails;
	}


	public EmployeeDetailsView getEmployeeDetails() {
		EmployeeDetailsView empDetails = employeeDetailsRepository.findByEmployeeId(metaData.getEmployeeId());
		if(empDetails==null) {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"Employee detais not found"+metaData.getEmployeeId());
		}else if(empDetails!=null && !empDetails.getIsActive().equalsIgnoreCase(ConstantDocument.Yes)) {
			throw new GlobalException(JaxError.INACTIVE_EMPLOYEE,"Employee is not active "+metaData.getEmployeeId());
		}else if(empDetails!=null && StringUtils.isBlank(empDetails.getUserName())) {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"User name should not be  blank "+metaData.getEmployeeId());
		}
		return empDetails;
	}





	public Boolean loyalityPointsAvailed(RemittanceTransactionRequestModel requestModel,RemittanceTransactionResponsetModel responseModel) {
		if (requestModel.isAvailLoyalityPoints() && responseModel.getCanRedeemLoyalityPoints()) {
			return true;
		}
		return false;
	}


	public String getCustomerFullName(Customer customer){
		String customerName =null;

		if(customer !=null){
			if(customer.getFirstName() !=null){
				customerName = customer.getFirstName(); 
			}
			if(!StringUtils.isEmpty(customer.getMiddleName())){
				customerName = customerName +" "+customer.getMiddleName();
			}
			if(!StringUtils.isEmpty(customer.getLastName())){
				customerName = customerName+ " "+ customer.getLastName();
			}
		}
		return customerName;
	}



	public java.sql.Clob stringToClob(String source) throws Exception {
		try {
			return new javax.sql.rowset.serial.SerialClob(source.toCharArray());
		} catch (Exception e) {
			return null;
		}
	}

	public BranchRemittanceApplResponseDto deleteFromShoppingCart(BigDecimal remittanceAppliId) {
		//brRemittanceDao.deleteFromCart(remittanceAppliId,ConstantDocument.Deleted);
		brRemittanceDao.deleteFromCartUsingJdbcTemplate(remittanceAppliId,ConstantDocument.Deleted);
		BranchRemittanceApplResponseDto applResponseDto = branchRemittancePaymentManager.fetchCustomerShoppingCart(metaData.getCustomerId(),metaData.getDefaultCurrencyId());
		return applResponseDto;
	}


	public String getCustomerSignature() {
		String signature = null;
		String ipaddress = metaData.getDeviceIp();
		BigDecimal terminalId = metaData.getTerminalId();

		logger.debug("ipaddress :"+ipaddress+"\t CustomerId :"+metaData.getCustomerId()+"\t terminalId :"+terminalId);

		if(JaxUtil.isNullZeroBigDecimalCheck(terminalId)) {
			Device deviceClient = deviceRepository.findByDeviceTypeAndBranchSystemInventoryIdAndStatus(ClientType.SIGNATURE_PAD, terminalId,ConstantDocument.Yes);
			if(deviceClient!=null && JaxUtil.isNullZeroBigDecimalCheck(deviceClient.getRegistrationId())) {
				DeviceStateInfo deviceStateInfo =  deviceStateRepository.findOne(deviceClient.getRegistrationId());
				if(deviceStateInfo!=null && deviceStateInfo.getSignature()!=null) {
					signature = deviceStateInfo.getSignature();
				}
			}
		}
		return signature;
	}


	private String getAccountNumber(BenificiaryListView beneficiaryDT) {
		String iBanFlag = bankService.getBankById(beneficiaryDT.getBankId()).getIbanFlag();
		String accountNumber = beneficiaryDT.getBankAccountNumber();
		String ibanNumber = beneficiaryService.getBeneAccountByAccountSeqId(beneficiaryDT.getBeneficiaryAccountSeqId()).getIbanNumber();
		logger.debug("iBanFlag: {} , iBANNum: {}", iBanFlag, ibanNumber);
		if (ConstantDocument.Yes.equalsIgnoreCase(iBanFlag) && StringUtils.isNotBlank(ibanNumber)) {
			accountNumber = ibanNumber;
		}
		return accountNumber;
	}


	public BigDecimal getloyaltyAmountEncashed(BigDecimal commission) {
		BigDecimal discount = corporateDiscountManager.corporateDiscount();
		BigDecimal loyalityPoints = loyalityPointService.getVwLoyalityEncash().getLoyalityPoint();
		BigDecimal loyalityPointsEncashed = loyalityPointService.getVwLoyalityEncash().getEquivalentAmount();
		if(JaxUtil.isNullZeroBigDecimalCheck(commission) && JaxUtil.isNullZeroBigDecimalCheck(loyalityPoints) && loyalityPointsEncashed.compareTo(discount)>0) {
			if(commission.compareTo(loyalityPointsEncashed)>=0) {
				loyalityPointsEncashed =loyalityPointsEncashed.subtract(BigDecimal.ZERO);
			}else {
				loyalityPointsEncashed =loyalityPointsEncashed.subtract(discount);
			}
		}
		return loyalityPointsEncashed;
	}

	public void validateApplDetails(HashMap<String, Object> mapAllDetailApplSave) {
		RemittanceApplication saveApplTrnx = (RemittanceApplication) mapAllDetailApplSave.get("EX_APPL_TRNX");
		RemittanceAppBenificiary saveApplBene = (RemittanceAppBenificiary) mapAllDetailApplSave.get("EX_APPL_BENE");
		List<AdditionalInstructionData> saveApplAddlData = (List<AdditionalInstructionData>)mapAllDetailApplSave.get("EX_APPL_ADDL");

		if(saveApplTrnx==null) {
			throw new GlobalException(JaxError.APPL_CREATION_ERROR,"Application details not found");
		}
		if(saveApplBene==null) {
			throw new GlobalException(JaxError.APPL_BENE_CREATION_ERROR,"Application bene details not found");
		}

		if(saveApplAddlData==null || saveApplAddlData.isEmpty()) {
			throw new GlobalException(JaxError.APPL_ADD_INSTRUCTION_ERROR,"Application additional details is missing ");
		}

	}


	private void validateSaveApplRequest(BranchRemittanceApplRequestModel request) {
		if (request.getForeignAmount() == null && request.getLocalAmount() == null) {
			throw new GlobalException(JaxError.INVALID_AMOUNT, "Either local or foreign amount must be present");
		}
		JaxValidationUtil.validatePositiveNumber(request.getForeignAmount(), "Foreign Amount should be positive",JaxError.INVALID_AMOUNT);
		JaxValidationUtil.validatePositiveNumber(request.getLocalAmount(), "Local Amount should be positive", JaxError.INVALID_AMOUNT);
		JaxValidationUtil.validatePositiveNumber(request.getRoutingBankId(), "Routing bank must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getRoutingBankBranchId(), "Routing bank branch must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getBeneficiaryRelationshipSeqIdBD(), "bene seq id bank must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getServiceMasterId(), "service indic id bank must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getRemittanceModeId(),"Remittance mode id must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getDeliveryModeId(),"Delivery mode id must be positive number");
	}

	public RemitApplSrvProv createRemitApplSrvProv(DynamicRoutingPricingDto dynamicRoutingPricingDto,String createdBy) {

		ServiceProviderDto serviceProviderDto = dynamicRoutingPricingDto.getServiceProviderDto();

		RemitApplSrvProv remitApplSrvProv = null;

		if (serviceProviderDto != null) {

			remitApplSrvProv = new RemitApplSrvProv();

			remitApplSrvProv.setAmgSessionId(serviceProviderDto.getAmgSessionId());
			remitApplSrvProv.setBankId(dynamicRoutingPricingDto.getTrnxRoutingPaths().getRoutingBankId());
			remitApplSrvProv.setFixedCommInSettlCurr(serviceProviderDto.getFixedCommInSettlCurr());
			remitApplSrvProv.setIntialAmountInSettlCurr(serviceProviderDto.getIntialAmountInSettlCurr());
			remitApplSrvProv.setPartnerReferenceNo(serviceProviderDto.getPartnerReferenceNo());
			remitApplSrvProv.setPartnerSessionId(serviceProviderDto.getPartnerSessionId());
			remitApplSrvProv.setSettlementCurrency(serviceProviderDto.getSettlementCurrency());
			remitApplSrvProv.setTransactionMargin(serviceProviderDto.getTransactionMargin());
			remitApplSrvProv.setVariableCommInSettlCurr(serviceProviderDto.getVariableCommInSettlCurr());
			remitApplSrvProv.setCreatedBy(createdBy);
			remitApplSrvProv.setCreatedDate(new Date());
			if(serviceProviderDto.getOfferExpirationDate() != null) {
				remitApplSrvProv.setOfferExpirationDate(serviceProviderDto.getOfferExpirationDate().getTime());
			}
			if(serviceProviderDto.getOfferStartingDate() != null) {
				remitApplSrvProv.setOfferStartingDate(serviceProviderDto.getOfferStartingDate().getTime());
			}

			logger.warn("RemitApplSrvProv : " + JsonUtil.toJson(remitApplSrvProv));
		}

		return remitApplSrvProv;
	}

	public void checkServiceProviderValidation(BranchRemittanceApplRequestModel requestApplModel) {
		boolean errorStatus = Boolean.FALSE;
		Boolean multipleTrnx = Boolean.FALSE;
		int trnxCount = 0;
		
		if(requestApplModel != null) {
			// fetch any shopping records available
			List<ShoppingCartDetails> lstCustomerShopping = branchRemittancePaymentDao.fetchCustomerShoppingCart(metaData.getCustomerId());
			if(lstCustomerShopping != null && lstCustomerShopping.size() != 0) {
				// checking home send transaction
				BankMasterModel bankMaster = bankMasterRepo.findByBankCodeAndRecordStatus(PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE.HOME.name(), PricerServiceConstants.Yes);
				if(bankMaster == null) {
					//throw new GlobalException(JaxError.NO_RECORD_FOUND,"Record not found for bank code :"+PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE.HOME.name());
					// not required to send error
				}else {
					for (ShoppingCartDetails shoppingCartDetails : lstCustomerShopping) {
						if(shoppingCartDetails.getApplicationType() != null && !shoppingCartDetails.getApplicationType().equalsIgnoreCase("FS")) {
							trnxCount++;
							if(shoppingCartDetails.getRoutingBankId().compareTo(bankMaster.getBankId()) == 0) {
								errorStatus = Boolean.TRUE;
								break;
							}
						}
					}
				}
				if(trnxCount > 1) {
					multipleTrnx = Boolean.TRUE;
				}

				if(errorStatus) {
					if(multipleTrnx) {
						throw new GlobalException(JaxError.SINGLE_TRANSACTION_SERVICE_PROVIDER,"You cannot create the next application as HomeSend application is created as the last application.");
					}else {
						throw new GlobalException(JaxError.SINGLE_TRANSACTION_SERVICE_PROVIDER,"You cannot create the next application as HomeSend application is created.");
					}
				}
			}
			
			if(requestApplModel.getDynamicRroutingPricingBreakup() != null && requestApplModel.getDynamicRroutingPricingBreakup().getServiceProviderDto() != null) {
				BankMasterModel bankMaster = bankMasterRepo.findByBankCodeAndRecordStatus(PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE.HOME.name(), PricerServiceConstants.Yes);
				// home send related validation check
				if(bankMaster != null && requestApplModel.getRoutingBankId().compareTo(bankMaster.getBankId()) == 0) {
					partnerTransactionManager.validateServiceProvider(requestApplModel.getAdditionalFields(),requestApplModel.getBeneId());
				}
			}
		}
	}


}
