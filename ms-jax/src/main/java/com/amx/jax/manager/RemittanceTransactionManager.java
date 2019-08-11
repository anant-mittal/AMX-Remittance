package com.amx.jax.manager;

import static com.amx.jax.error.JaxError.BLACK_LISTED_CUSTOMER;
import static com.amx.jax.error.JaxError.COMISSION_NOT_DEFINED_FOR_ROUTING_BANK;
import static com.amx.jax.error.JaxError.NO_OF_TRANSACTION_LIMIT_EXCEEDED;
import static com.amx.jax.error.JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL;
import static com.amx.jax.error.JaxError.TOO_MANY_COMISSION_NOT_DEFINED_FOR_ROUTING_BANK;
import static com.amx.jax.error.JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED;
import static com.amx.jax.error.JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_NEW_BENE;
import static com.amx.jax.error.JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_PER_BENE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.AuthType;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.PromotionDto;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.api.ResponseCodeDetailDTO;
import com.amx.jax.branchremittance.manager.BranchRemittanceApplManager;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxDbConfig;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.constants.JaxTransactionStatus;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.ExchangeRateProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.BankDao;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.AuthenticationView;
import com.amx.jax.dbmodel.BankCharges;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BankServiceRule;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.dbmodel.TransactionLimitCheckView;
import com.amx.jax.dbmodel.partner.RemitApplSrvProv;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.OWSScheduleModel;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dbmodel.remittance.ServiceProviderCredentialsModel;
import com.amx.jax.dbmodel.remittance.ViewTransfer;
import com.amx.jax.dbmodel.remittance.ViewVatDetails;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.PayGRespCodeJSONConverter;
import com.amx.jax.dict.UserClient;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.error.JaxError;
import com.amx.jax.exrateservice.dao.ExchangeRateDao;
import com.amx.jax.exrateservice.dao.PipsMasterDao;
import com.amx.jax.exrateservice.service.NewExchangeRateService;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.CActivityEvent.Type;
import com.amx.jax.logger.events.RemitInfo;
import com.amx.jax.manager.remittance.CorporateDiscountManager;
import com.amx.jax.manager.remittance.RemittanceAdditionalFieldManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.remittance.AbstractRemittanceApplicationRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionDrRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.BsbApiResponse;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.LoyalityPointState;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.model.response.remittance.VatDetailsDto;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.pricer.dto.TrnxRoutingDetails;
import com.amx.jax.remittance.manager.RemittanceParameterMapManager;
import com.amx.jax.repository.AuthenticationViewRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.VTransferRepository;
import com.amx.jax.repository.remittance.IOWSScheduleModelRepository;
import com.amx.jax.repository.remittance.IServiceProviderCredentailsRepository;
import com.amx.jax.repository.remittance.IViewVatDetailsRespository;
import com.amx.jax.rest.RestService;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryCheckService;
import com.amx.jax.services.JaxConfigService;
import com.amx.jax.services.LoyalityPointService;
import com.amx.jax.services.RemittanceApplicationService;
import com.amx.jax.services.RoutingService;
import com.amx.jax.services.TransactionHistroyService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.jax.validation.RemittanceTransactionRequestValidator;
import com.amx.utils.JsonUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceTransactionManager {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	private BlackListDao blistDao;

	@Autowired
	private MetaData meta;

	@Autowired
	private BankDao bankServiceRuleDao;

	@Autowired
	private ExchangeRateDao exchangeRateDao;

	@Autowired
	private PipsMasterDao pipsDao;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private VTransferRepository transferRepo;

	@Autowired
	private ParameterService parameterService;

	@Autowired
	private RemittanceApplicationManager remitAppManager;

	@Autowired
	private ApplicationProcedureDao applicationProcedureDao;

	@Autowired
	private AuthenticationViewRepository authViewRepo;

	@Resource
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	private RemittanceAppBeneficiaryManager remitAppBeneManager;

	@Autowired
	private RemittanceApplicationAdditionalDataManager remittanceAppAddlDataManager;

	@Autowired
	private OldRemittanceApplicationAdditionalDataManager oldRemittanceApplicationAdditionalDataManager;

	@Autowired
	private RemittanceApplicationDao remitAppDao;

	@Autowired
	private RemittanceApplicationService remittanceApplicationService;

	@Autowired
	private LoyalityPointService loyalityPointService;

	@Autowired
	private TransactionHistroyService transactionHistroyService;

	@Autowired
	private ExchangeRateProcedureDao exchangeRateProcedureDao;

	@Autowired
	private BizcomponentDao bizcomponentDao;

	@Autowired
	private CurrencyMasterService currencyMasterService;

	@Autowired
	private BeneficiaryCheckService beneCheckService;

	@Autowired
	private RemittanceTransactionRequestValidator remittanceTransactionRequestValidator;
	
	@Autowired
	RemittanceAdditionalFieldManager remittanceAdditionalFieldManager;

	@Autowired
	private UserService userService;
	
	@Autowired
	DailyPromotionManager dailyPromotionManager;
	
	@Autowired
	PartnerTransactionManager partnerTransactionManager;

	protected Map<String, Object> validatedObjects = new HashMap<>();

	private boolean isSaveRemittanceFlow;

	@Autowired
	private JaxUtil jaxUtil;
	@Autowired
	RoutingService routingService;
	@Autowired
	JaxTenantProperties jaxTenantProperties;
	@Autowired
	NewExchangeRateService newExchangeRateService;
	@Autowired
	PromotionManager promotionManager;
	@Autowired
	CountryService countryService;
	@Autowired
	JaxConfigService jaxConfigService;
	@Autowired
	RemittanceParameterMapManager remittanceParameterMapManager;
	@Autowired
	CorporateDiscountManager corporateDiscountManager;
	
	@Autowired
	IViewVatDetailsRespository vatDetailsRepository;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	ICurrencyDao currencyDao;
	
	@Autowired
	IOWSScheduleModelRepository iOWSScheduleModelRepository ;
	
	@Autowired
	IServiceProviderCredentailsRepository serviceProviderCredentailsRepository;
	
	@Autowired
	private RestService restService;
	@Autowired
	BankService bankService;

	@Autowired
	BranchRemittanceApplManager branchRemittanceApplManager;


	private static final String IOS = "IOS";
	private static final String ANDROID = "ANDROID";
	private static final String WEB = "WEB";

	/** New Dynamic routing and pricing Api **/ 
	public RemittanceTransactionResponsetModel validateTransactionDataV2(RemittanceTransactionDrRequestModel model) {

	
		addRequestParametersV2(model);
		Customer customer = custDao.getCustById(meta.getCustomerId());
		validatedObjects.put("CUSTOMER", customer);
		RemittanceTransactionResponsetModel responseModel = new RemittanceTransactionResponsetModel();
		setLoyalityPointFlags(customer, responseModel);
		BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(model.getBeneId());
		remitApplParametersMap.put("P_BENEFICIARY_MASTER_ID", beneficiary.getBeneficaryMasterSeqId());
		addBeneficiaryParameters(beneficiary);
		validateBlackListedBene(beneficiary);
		//validateRiskyBene(beneficiary, customer);  //it is not required at the time of trnx ,the procedure will take care for existing bene with different nationality
		validatedObjects.put("BENEFICIARY", beneficiary);
		HashMap<String, Object> beneBankDetails = getBeneBankDetails(beneficiary);
		remitApplParametersMap.putAll(beneBankDetails);
		
	
		DynamicRoutingPricingDto  dynamicRoutingPricing = model.getDynamicRroutingPricingBreakup();
		
		if(dynamicRoutingPricing==null) {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"Routing path is missing");
		}
		
		TrnxRoutingDetails trnxRoutingDetails =  dynamicRoutingPricing.getTrnxRoutingPaths();
		ExchangeRateBreakup breakup = dynamicRoutingPricing.getExRateBreakup();
		Map<String, Object>  routingDetails =setupRoutingDetails(trnxRoutingDetails);
	
		
		remitApplParametersMap.putAll(routingDetails);
		//remitApplParametersMap.put("P_BENEFICIARY_SWIFT_BANK1", routingDetails.get("P_SWIFT"));
		remitApplParametersMap.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneficiary.getBeneficiaryAccountSeqId());
		/** Added by Rabil on 03 May 2018 **/
		remitApplParametersMap.put("P_BENE_RELATION_SEQ_ID", beneficiary.getBeneficiaryRelationShipSeqId());
		
		/** End here **/
		validatedObjects.put("ROUTINGDETAILS", routingDetails);
		remitApplParametersMap.put("BENEFICIARY", beneficiary);
	
		BigDecimal commission = dynamicRoutingPricing.getTxnFee();

		BigDecimal serviceMasterId = new BigDecimal(remitApplParametersMap.get("P_SERVICE_MASTER_ID").toString());
		BigDecimal routingBankId = new BigDecimal(remitApplParametersMap.get("P_ROUTING_BANK_ID").toString());
		BigDecimal rountingCountryId = new BigDecimal(remitApplParametersMap.get("P_ROUTING_COUNTRY_ID").toString());
		BigDecimal remittanceMode = new BigDecimal(remitApplParametersMap.get("P_REMITTANCE_MODE_ID").toString());
		BigDecimal deliveryMode = new BigDecimal(remitApplParametersMap.get("P_DELIVERY_MODE_ID").toString());
		BigDecimal currencyId = beneficiary.getCurrencyId();
		BigDecimal applicationCountryId = meta.getCountryId();
		logger.info("currencyId :" + currencyId + "\t rountingCountryId :" + rountingCountryId + "\t routingBankId :"+ routingBankId + "\t serviceMasterId :" + serviceMasterId);
		
		VatDetailsDto vatDetails = getVatAmount(commission);
		if(vatDetails!=null && !StringUtils.isBlank(vatDetails.getVatApplicable()) && vatDetails.getVatApplicable().equalsIgnoreCase(ConstantDocument.Yes)) {
			responseModel.setVatAmount(vatDetails.getVatAmount()==null?BigDecimal.ZERO:vatDetails.getVatAmount());
			responseModel.setVatPercentage(vatDetails.getVatPercentage()==null?BigDecimal.ZERO:vatDetails.getVatPercentage());
			responseModel.setVatType(vatDetails.getVatType()==null?"":vatDetails.getVatType());
			if(JaxUtil.isNullZeroBigDecimalCheck(vatDetails.getCommission())) {
			commission =vatDetails.getCommission();
			logger.info("VatAmount: " +vatDetails.getVatAmount());
			logger.info("VatPercentage: "  +vatDetails.getVatPercentage());
			}
		}
		
		
		 /** to vlidate BSB  account though api by rabil**/
		/*String errMsg = beneAccountValidationThroughApi(serviceMasterId,routingBankId,beneficiary);
		if(!StringUtils.isBlank(errMsg)) {
			throw new GlobalException(JaxError.BSB_ACCOUNT_VALIATION,"Invalid account number "+errMsg);
		}		*/
		/** end here**/
		
		validateNumberOfTransactionLimits();
		validateBeneficiaryTransactionLimit(beneficiary);
		setLoyalityPointIndicaters(responseModel);
		setNetAmountAndLoyalityState(breakup, model, responseModel, commission,vatDetails.getVatApplicableAmount());
		remitApplParametersMap.put("P_CALCULATED_FC_AMOUNT", dynamicRoutingPricing.getExRateBreakup().getConvertedFCAmount());
		remitApplParametersMap.put("P_CALCULATED_LC_AMOUNT", dynamicRoutingPricing.getExRateBreakup().getConvertedLCAmount());

		if (model.isAvailLoyalityPoints()) {
			validateLoyalityPointsBalance(customer.getLoyaltyPoints());
		}

		logger.info("rountingCountryId: " + rountingCountryId + " serviceMasterId: " + serviceMasterId);

		applyCurrencyRoudingLogic(breakup);
		validateTransactionAmount(breakup, commission, currencyId);
		// commission
		responseModel.setTxnFee(commission);
		// exrate
		responseModel.setExRateBreakup(breakup);
		addExchangeRateParameters(responseModel); 
		applyCurrencyRoudingLogic(responseModel.getExRateBreakup());
		setCustomerDiscountColumnsV2(responseModel, dynamicRoutingPricing);
		return responseModel;

	}
	
	
	
	
	public RemittanceTransactionResponsetModel validateTransactionData(RemittanceTransactionRequestModel model) {

		addRequestParameters(model);
		Customer customer = custDao.getCustById(meta.getCustomerId());
		validatedObjects.put("CUSTOMER", customer);
		RemittanceTransactionResponsetModel responseModel = new RemittanceTransactionResponsetModel();
		setLoyalityPointFlags(customer, responseModel);
		BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(model.getBeneId());
		remitApplParametersMap.put("P_BENEFICIARY_MASTER_ID", beneficiary.getBeneficaryMasterSeqId());
		addBeneficiaryParameters(beneficiary);
		validateBlackListedBene(beneficiary);
		//validateRiskyBene(beneficiary, customer);  //it is not required at the time of trnx ,the procedure will take care for existing bene with different nationality
		validatedObjects.put("BENEFICIARY", beneficiary);
		HashMap<String, Object> beneBankDetails = getBeneBankDetails(beneficiary);
		remitApplParametersMap.putAll(beneBankDetails);
		Map<String, Object> routingDetails = routingService.getRoutingDetails(remitApplParametersMap);

		remitApplParametersMap.putAll(routingDetails);
		remitApplParametersMap.put("P_BENEFICIARY_SWIFT_BANK1", routingDetails.get("P_SWIFT"));
		remitApplParametersMap.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneficiary.getBeneficiaryAccountSeqId());
		/** Added by Rabil on 03 May 2018 **/
		remitApplParametersMap.put("P_BENE_RELATION_SEQ_ID", beneficiary.getBeneficiaryRelationShipSeqId());
		/** End here **/
		validatedObjects.put("ROUTINGDETAILS", routingDetails);
		remitApplParametersMap.put("BENEFICIARY", beneficiary);
		remitApplParametersMap.put("P_CALCULATED_FC_AMOUNT",
				newExchangeRateService.getForeignAmount(remitApplParametersMap));
		BigDecimal newCommission = reCalculateComission();

		logger.info("newCommission: " + newCommission);
		if (new BigDecimal(94).equals(remitApplParametersMap.get("P_ROUTING_COUNTRY_ID"))
				&& new BigDecimal(102).equals(remitApplParametersMap.get("P_SERVICE_MASTER_ID"))
				&& newCommission == null) {
			logger.info("recalculating del mode for TT and routing countyr india");
			recalculateDeliveryAndRemittanceModeId();
		}
		routingService.recalculateRemittanceAndDeliveryMode(remitApplParametersMap);
		BigDecimal serviceMasterId = new BigDecimal(remitApplParametersMap.get("P_SERVICE_MASTER_ID").toString());
		BigDecimal routingBankId = new BigDecimal(remitApplParametersMap.get("P_ROUTING_BANK_ID").toString());
		BigDecimal rountingCountryId = new BigDecimal(remitApplParametersMap.get("P_ROUTING_COUNTRY_ID").toString());
		BigDecimal remittanceMode = new BigDecimal(remitApplParametersMap.get("P_REMITTANCE_MODE_ID").toString());
		BigDecimal deliveryMode = new BigDecimal(remitApplParametersMap.get("P_DELIVERY_MODE_ID").toString());
		BigDecimal currencyId = beneficiary.getCurrencyId();
		BigDecimal applicationCountryId = meta.getCountryId();

		logger.info("currencyId :" + currencyId + "\t rountingCountryId :" + rountingCountryId + "\t routingBankId :"
				+ routingBankId + "\t serviceMasterId :" + serviceMasterId);
		List<ExchangeRateApprovalDetModel> exchangeRates = exchangeRateDao.getExchangeRatesForRoutingBank(currencyId,
				meta.getCountryBranchId(), rountingCountryId, applicationCountryId, routingBankId, serviceMasterId);
		if (!jaxTenantProperties.getExrateBestRateLogicEnable() && !jaxTenantProperties.getIsDynamicPricingEnabled()
				&& (exchangeRates == null || exchangeRates.isEmpty())) {
			throw new GlobalException(REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL,
					"No exchange rate found for bank- " + routingBankId);
		}
		validateNumberOfTransactionLimits();
		validateBeneficiaryTransactionLimit(beneficiary);
		setLoyalityPointIndicaters(responseModel);
		BigDecimal commission = getCommissionAmount(routingBankId, rountingCountryId, currencyId, remittanceMode,
				deliveryMode);
		
		logger.info("commission: " +commission);
		
		if (newCommission != null) {
			commission = newCommission;
		}
		if (commission.longValue() > 0) {
			commission = commission.subtract(corporateDiscountManager.corporateDiscount());
			logger.info("commissioncorporate: " +commission);
			
		}
		ExchangeRateBreakup breakup = getExchangeRateBreakup(exchangeRates, model, responseModel, commission);
		remitApplParametersMap.put("P_CALCULATED_FC_AMOUNT", breakup.getConvertedFCAmount());
		remitApplParametersMap.put("P_CALCULATED_LC_AMOUNT", breakup.getConvertedLCAmount());

		if (model.isAvailLoyalityPoints()) {
			validateLoyalityPointsBalance(customer.getLoyaltyPoints());
		}

		logger.info("rountingCountryId: " + rountingCountryId + " serviceMasterId: " + serviceMasterId);

		applyCurrencyRoudingLogic(breakup);
		breakup = getExchangeRateBreakup(exchangeRates, model, responseModel, commission);
		
		validateTransactionAmount(breakup, newCommission, currencyId);	
		
		logger.debug("newCommissioncompare :" +newCommission);
		logger.info("commissioncompare: " +commission);
			
				VatDetailsDto vatDetails = getVatAmount(commission);
				if(vatDetails!=null && !StringUtils.isBlank(vatDetails.getVatApplicable()) && vatDetails.getVatApplicable().equalsIgnoreCase(ConstantDocument.Yes)) {
					responseModel.setVatAmount(vatDetails.getVatAmount()==null?BigDecimal.ZERO:vatDetails.getVatAmount());
					responseModel.setVatPercentage(vatDetails.getVatPercentage()==null?BigDecimal.ZERO:vatDetails.getVatPercentage());
					responseModel.setVatType(vatDetails.getVatType()==null?"":vatDetails.getVatType());
					if(JaxUtil.isNullZeroBigDecimalCheck(vatDetails.getCommission())) {
					commission =vatDetails.getCommission();
					logger.info("VatAmount: " +vatDetails.getVatAmount());
					logger.info("VatPercentage: "  +vatDetails.getVatPercentage());
					
					}
				}
		
		// commission
		responseModel.setTxnFee(commission);
		// exrate
		responseModel.setExRateBreakup(breakup);

		addExchangeRateParameters(responseModel);
		applyCurrencyRoudingLogic(responseModel.getExRateBreakup());
		return responseModel;

	}
	
	
	
	

	public BigDecimal getCommissionAmount(BigDecimal routingBankId, BigDecimal rountingCountryId, BigDecimal currencyId,
			BigDecimal remittanceMode, BigDecimal deliveryMode) {
		List<BankServiceRule> rules = bankServiceRuleDao.getBankServiceRule(routingBankId, rountingCountryId,
				currencyId, remittanceMode, deliveryMode);
		BankServiceRule appliedRule = rules.get(0);
		List<BankCharges> charges = appliedRule.getBankCharges();
		BankCharges bankCharge = getApplicableCharge(charges);
		BigDecimal commission = bankCharge.getChargeAmount();
		return commission;
	}

	/** added by Rabil **/
	public VatDetailsDto getVatAmount(BigDecimal commission) {
		VatDetailsDto vatDetails = new VatDetailsDto();
		List<ViewVatDetails> vatList = vatDetailsRepository.getVatDetails(metaData.getCountryId(),ConstantDocument.VAT_CATEGORY,ConstantDocument.VAT_ACCOUNT_TYPE_COMM);
		String vatAppliable = null;
		if(vatList.isEmpty()) {
			vatAppliable ="N";
		}else if(vatList!=null && !vatList.isEmpty() && vatList.size()>1) {
			vatAppliable ="N";
			throw new GlobalException(JaxError.MUTIPLE_RECORD_FOUND, "More than one record available for VAT on Commission");
		}else if(vatList!=null && !vatList.isEmpty() && vatList.size()==1) {
			vatAppliable ="Y";
			vatDetails.setVatPercentage(vatList.get(0).getVatPercentage());
			vatDetails.setVatType(vatList.get(0).getVatType());
			vatDetails.setCalculatuonType(vatList.get(0).getCalculationType());
			vatDetails.setRoudingOff(vatList.get(0).getRoundOff()==null?BigDecimal.ZERO:vatList.get(0).getRoundOff());
		}
		if(JaxUtil.isNullZeroBigDecimalCheck(commission) && commission.compareTo(BigDecimal.ZERO)>0) {
		if(!StringUtils.isBlank(vatAppliable) && vatAppliable.equalsIgnoreCase(ConstantDocument.Yes) ) {
			vatDetails.setVatApplicable(vatAppliable);
			
			if(JaxUtil.isNullZeroBigDecimalCheck(vatDetails.getVatPercentage()) && vatDetails.getVatPercentage().compareTo(BigDecimal.ZERO)>0) {
				BigDecimal BIG_HUNDRED = new BigDecimal(100);
				BigDecimal vatAmount =BigDecimal.ZERO;
				if(!StringUtils.isBlank(vatDetails.getCalculatuonType()) && vatDetails.getCalculatuonType().equalsIgnoreCase(ConstantDocument.VAT_CALCULATION_TYPE_INCLUDE)) {
					vatAmount = RoundUtil.roundBigDecimal(((new BigDecimal(commission.doubleValue()/((vatDetails.getVatPercentage().add(BIG_HUNDRED)).doubleValue())).multiply(BIG_HUNDRED))), vatDetails.getRoudingOff().intValue());
					vatDetails.setVatAmount(commission.subtract(vatAmount));
					vatDetails.setVatApplicableAmount(vatDetails.getVatAmount());
					vatDetails.setCommission(commission.subtract(vatDetails.getVatAmount()==null?BigDecimal.ZERO:vatDetails.getVatAmount()));
					
				}else if(!StringUtils.isBlank(vatDetails.getCalculatuonType()) && vatDetails.getCalculatuonType().equalsIgnoreCase(ConstantDocument.VAT_CALCULATION_TYPE_EXCLUDE)) {
					vatAmount = commission.multiply(RoundUtil.roundBigDecimal(vatDetails.getVatPercentage().divide(BIG_HUNDRED),vatDetails.getRoudingOff().intValue()));
					vatDetails.setVatAmount(vatAmount);
					vatDetails.setCommission(commission);
					vatDetails.setVatApplicableAmount(vatAmount);
				}
			}
		}
	}else {
		vatDetails.setVatApplicable(vatAppliable);
	}
		return  vatDetails;
	}

	private void validateRiskyBene(BenificiaryListView beneficiary, Customer customer) {
		if (jaxConfigService.getBooleanConfigValue(JaxDbConfig.BLOCK_BENE_RISK_TRANSACTION, true)) {
			if (beneficiary.getCountryId().intValue() != customer.getNationalityId().intValue()) {
				int beneCountryRisk = countryService.getCountryMaster(beneficiary.getCountryId()).getBeneCountryRisk();
				if (beneCountryRisk == 1) {
					throw new GlobalException(JaxError.BENE_COUNTRY_RISK, "Bene country risk");
				}
			}
		}
	}

	public void setLoyalityPointFlags(Customer customer, RemittanceTransactionResponsetModel responseModel) {
		if (customer.getLoyaltyPoints() != null && customer.getLoyaltyPoints().compareTo(BigDecimal.ZERO) > 0) {
			responseModel.setTotalLoyalityPoints(customer.getLoyaltyPoints());
		} else {
			responseModel.setTotalLoyalityPoints(BigDecimal.ZERO);
		}
		responseModel.setMaxLoyalityPointsAvailableForTxn(loyalityPointService.getVwLoyalityEncash().getLoyalityPoint());
	}

	public void applyCurrencyRoudingLogic(ExchangeRateBreakup exRatebreakUp) {
		BigDecimal fcurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal localCurrencyId = meta.getDefaultCurrencyId();
		exRatebreakUp.setFcDecimalNumber(currencyMasterService.getCurrencyMasterById(fcurrencyId).getDecinalNumber());
		exRatebreakUp
				.setLcDecimalNumber(currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber());

		if (exRatebreakUp.getConvertedFCAmount() != null) {
			logger.debug("getConvertedFCAmount :"
					+ RoundUtil.roundToZeroDecimalPlaces(exRatebreakUp.getConvertedFCAmount()));
			exRatebreakUp
					.setConvertedFCAmount(RoundUtil.roundToZeroDecimalPlaces(exRatebreakUp.getConvertedFCAmount()));
			exRatebreakUp.setConvertedFCAmount(RoundUtil.roundBigDecimal(exRatebreakUp.getConvertedFCAmount(),
					exRatebreakUp.getFcDecimalNumber().intValue()));
		}

		if (exRatebreakUp.getConvertedLCAmount() != null) {
			exRatebreakUp.setConvertedLCAmount(RoundUtil.roundBigDecimal(exRatebreakUp.getConvertedLCAmount(),
					exRatebreakUp.getLcDecimalNumber().intValue()));
		}

		exRatebreakUp.setNetAmount(
				RoundUtil.roundBigDecimal(exRatebreakUp.getNetAmount(), exRatebreakUp.getLcDecimalNumber().intValue()));
		
		logger.info("amount in ex:" +exRatebreakUp.getNetAmount());
		exRatebreakUp.setNetAmountWithoutLoyality(RoundUtil.roundBigDecimal(exRatebreakUp.getNetAmountWithoutLoyality(),
				exRatebreakUp.getLcDecimalNumber().intValue()));
		exRatebreakUp.setInverseRate((RoundUtil.roundBigDecimal(exRatebreakUp.getInverseRate(), 6)));
	}

	public ExchangeRateBreakup applyChannelAmountRouding(ExchangeRateBreakup exchangeRateBreakup,
			UserClient.Channel channel, boolean isRoundUp) {

		if (channel == null || exchangeRateBreakup == null || exchangeRateBreakup.getConvertedLCAmount() == null) {
			return exchangeRateBreakup;
		}

		BigDecimal rounder = null;
		boolean applyRound = false;

		if (Channel.BRANCH.equals(channel)) {
			AuthenticationView authView = authViewRepo.getOne(new BigDecimal(8));
			rounder = authView.getAuthLimit();
			applyRound = true;
		} else if (Channel.KIOSK.equals(channel)) {
			rounder = new BigDecimal(0.250);
			applyRound = true;
		}

		if (rounder != null && applyRound) {

			MathContext context = new MathContext(3, RoundingMode.HALF_EVEN);

			rounder = rounder.setScale(3, RoundingMode.HALF_EVEN);

			BigDecimal decimalAmt = exchangeRateBreakup.getConvertedLCAmount().remainder(new BigDecimal(1))
					.round(context).setScale(3, RoundingMode.HALF_EVEN);

			BigDecimal diffVal = decimalAmt.remainder(rounder);

			if (diffVal.doubleValue() > 0) {

				BigDecimal bumpLcVal;

				if (isRoundUp) {
					bumpLcVal = rounder.subtract(diffVal);
				} else {
					bumpLcVal = diffVal.negate();
				}

				BigDecimal newDecimalAmt = decimalAmt.add(bumpLcVal);

				BigDecimal oldLcAmount = exchangeRateBreakup.getConvertedLCAmount();

				BigDecimal newLcAmount = new BigDecimal(exchangeRateBreakup.getConvertedLCAmount().longValue())
						.add(newDecimalAmt);

				BigDecimal bumpedFcVal = (newLcAmount.subtract(oldLcAmount)).multiply(exchangeRateBreakup.getRate())
						.setScale(10, RoundingMode.HALF_EVEN);

				BigDecimal newFcAmount = exchangeRateBreakup.getConvertedFCAmount().add(bumpedFcVal);

				exchangeRateBreakup.setConvertedLCAmount(newLcAmount);
				exchangeRateBreakup.setConvertedFCAmount(newFcAmount);

			}
		}

		return exchangeRateBreakup;

	}

	public BigDecimal reCalculateComission() {
		logger.debug("recalculating comission ");
		BigDecimal custtype = bizcomponentDao.findCustomerTypeId("I");
		remitApplParametersMap.put("P_CUSTYPE_ID", custtype);
		BigDecimal comission = exchangeRateProcedureDao.getCommission(remitApplParametersMap);
		logger.debug("newCommission 95: " + comission);
		if (comission == null) {
			remitApplParametersMap.put("P_CUSTYPE_ID", new BigDecimal(777));
			comission = exchangeRateProcedureDao.getCommission(remitApplParametersMap);
		}
		logger.debug("newCommission: " + comission);
		return comission;
	}

	public Map<String, Object> getCommissionRange(ExchangeRateBreakup breakup) {

		remitApplParametersMap.put("P_CALCULATED_FC_AMOUNT", breakup.getConvertedFCAmount());
		BigDecimal custtype = bizcomponentDao.findCustomerTypeId("I");
		remitApplParametersMap.put("P_CUSTYPE_ID", custtype);
		Map<String, Object> comissionRangeMap = exchangeRateProcedureDao.getCommissionRange(remitApplParametersMap);
		if (comissionRangeMap.get("FROM_AMOUNT") == null || comissionRangeMap.get("TO_AMOUNT") == null) {
			remitApplParametersMap.put("P_CUSTYPE_ID", new BigDecimal(777));
			comissionRangeMap = exchangeRateProcedureDao.getCommissionRange(remitApplParametersMap);
		}
		logger.info("comissionRangeMap: " + comissionRangeMap.toString());
		return comissionRangeMap;

	}

	private void recalculateDeliveryAndRemittanceModeId() {

		if (remitApplParametersMap.get("P_CALCULATED_FC_AMOUNT") != null) {

			BigDecimal custtype = bizcomponentDao.findCustomerTypeId("I");
			remitApplParametersMap.put("P_CUSTYPE_ID", custtype);
			Map<String, Object> outputMap = exchangeRateProcedureDao
					.findRemittanceAndDevlieryModeId(remitApplParametersMap);
			if (outputMap.size() == 0) {
				remitApplParametersMap.put("P_CUSTYPE_ID", new BigDecimal(777));
				outputMap = exchangeRateProcedureDao.findRemittanceAndDevlieryModeId(remitApplParametersMap);
			}
			if (outputMap.size() > 2) {
				throw new GlobalException(TOO_MANY_COMISSION_NOT_DEFINED_FOR_ROUTING_BANK,
						"TOO MANY COMMISSION DEFINED for rounting bankid: "
								+ remitApplParametersMap.get("P_ROUTING_BANK_ID"));
			}

			if (outputMap.get("P_DELIVERY_MODE_ID") == null) {
				throw new GlobalException(COMISSION_NOT_DEFINED_FOR_ROUTING_BANK,
						"COMMISSION NOT DEFINED BankId: " + remitApplParametersMap.get("P_ROUTING_BANK_ID"));
			}
			remitApplParametersMap.putAll(outputMap);
		}
	}

	public void setLoyalityPointIndicaters(RemittanceTransactionResponsetModel responseModel) {
		if (responseModel.getCanRedeemLoyalityPoints() == null) {
			BigDecimal maxLoyalityPointRedeem = responseModel.getMaxLoyalityPointsAvailableForTxn();
			BigDecimal loyalityPointsAvailable = responseModel.getTotalLoyalityPoints();
			if (loyalityPointsAvailable == null
					|| (loyalityPointsAvailable.longValue() < maxLoyalityPointRedeem.longValue())) {
				responseModel.setCanRedeemLoyalityPoints(false);
				responseModel.setLoyalityPointState(LoyalityPointState.CAN_NOT_AVAIL);
			} else {
				responseModel.setCanRedeemLoyalityPoints(true);
			}
		}
	}

	private void validateBeneficiaryTransactionLimit(BenificiaryListView beneficiary) {
		AuthenticationLimitCheckView beneficiaryPerDayLimit = parameterService.getPerCustomerPerBeneTrnxLimit();

		Customer customer = custDao.getCustById(meta.getCustomerId());
		logger.debug("customer Id :" + customer.getCustomerReference() + "\t  beneficiary.getBankCode() :"
				+ beneficiary.getBankCode() + "\t Acc No :" + beneficiary.getBankAccountNumber() + "\t Bene Name :"
				+ beneficiary.getBenificaryName());

		List<ViewTransfer> transfers = transferRepo.todayTransactionCheck(customer.getCustomerReference(),
				beneficiary.getBankCode(),
				beneficiary.getBankAccountNumber() == null ? "" : beneficiary.getBankAccountNumber(),
				beneficiary.getBenificaryName(), new BigDecimal(90));
		logger.debug("in validateBeneficiaryTransactionLimit today bene with BeneficiaryRelationShipSeqId: "
				+ beneficiary.getBeneficiaryRelationShipSeqId() + " and todays tnx are: " + transfers.size());
		if (beneficiaryPerDayLimit != null && transfers != null
				&& transfers.size() >= beneficiaryPerDayLimit.getAuthLimit().intValue()) {
			throw new GlobalException(TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_PER_BENE,
					beneficiaryPerDayLimit.getAuthMessage());
		}
		validateNewBeneficiaryTransactionLimit(beneficiary);
	}

	private void validateNewBeneficiaryTransactionLimit(BenificiaryListView beneficiary) {

		Boolean canTransact = beneCheckService.canTransact(beneficiary.getCreatedDate());
		if (!canTransact) {
			throw new GlobalException(JaxError.NEW_BENEFICIARY_TRANSACTION_TIME_LIMIT,
					"Newly added beneficiary cannot transact until certain time");
		}
	}

	public void validateLoyalityPointsBalance(BigDecimal availableLoyaltyPoints) {

		BigDecimal maxLoyalityPoints = loyalityPointService.getVwLoyalityEncash().getLoyalityPoint();
		BigDecimal todaysLoyalityPointsEncashed = loyalityPointService.getTodaysLoyalityPointsEncashed();
		int todaysLoyalityPointsEncashedInt = todaysLoyalityPointsEncashed == null ? 0
				: todaysLoyalityPointsEncashed.intValue();
		logger.debug("Available loyalitypoint= " + availableLoyaltyPoints + " maxLoyalityPoints=" + maxLoyalityPoints
				+ " todaysLoyalityPointsEncashed=" + todaysLoyalityPointsEncashed);
		if (availableLoyaltyPoints.intValue() < maxLoyalityPoints.intValue()) {
			throw new GlobalException(REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL,
					"Insufficient loyality points. Available points- : " + availableLoyaltyPoints);
		}
		if (availableLoyaltyPoints.intValue() - todaysLoyalityPointsEncashedInt < 0) {
			throw new GlobalException(REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL,
					"Insufficient loyality points. Available points- : " + availableLoyaltyPoints);
		}
	}

	private void addExchangeRateParameters(RemittanceTransactionResponsetModel responseModel) {
		ExchangeRateBreakup breakup = responseModel.getExRateBreakup();
		remitApplParametersMap.put("P_EXCHANGE_RATE_APPLIED", breakup.getRate());
		remitApplParametersMap.put("P_LOCAL_COMMISION_CURRENCY_ID", meta.getDefaultCurrencyId());
		remitApplParametersMap.put("P_LOCAL_COMMISION_AMOUNT", responseModel.getTxnFee());
		remitApplParametersMap.put("P_LOCAL_CHARGE_CURRENCY_ID", meta.getDefaultCurrencyId());
		remitApplParametersMap.put("P_LOCAL_CHARGE_AMOUNT", breakup.getConvertedLCAmount());
		remitApplParametersMap.put("P_FOREIGN_TRANX_AMOUNT", breakup.getConvertedFCAmount());
		remitApplParametersMap.put("P_LOCAL_NET_CURRENCY_ID", meta.getDefaultCurrencyId());
		remitApplParametersMap.put("P_LOCAL_NET_TRANX_AMOUNT", breakup.getNetAmount());
		remitApplParametersMap.put("P_FOREIGN_AMOUNT", breakup.getConvertedFCAmount());

	}

	private void addRequestParameters(RemittanceTransactionRequestModel model) {
		BigDecimal beneId = model.getBeneId();
		remitApplParametersMap.put("P_BENEFICIARY_RELASHIONSHIP_ID", beneId);
		remitApplParametersMap.put("P_BRANCH_ID", meta.getCountryBranchId());
		remitApplParametersMap.put("P_SOURCE_OF_INCOME_ID", model.getSourceOfFund());
		remitApplParametersMap.put("P_LOCAL_AMT", model.getLocalAmount());
		remitApplParametersMap.put("P_FOREIGN_AMT", model.getForeignAmount());
	}

	
	private void addRequestParametersV2(RemittanceTransactionDrRequestModel model) {
		BigDecimal beneId = model.getBeneId();
		remitApplParametersMap.put("P_BENEFICIARY_RELASHIONSHIP_ID", beneId);
		remitApplParametersMap.put("P_BRANCH_ID", meta.getCountryBranchId());
		remitApplParametersMap.put("P_SOURCE_OF_INCOME_ID", model.getSourceOfFund());
		remitApplParametersMap.put("P_LOCAL_AMT", model.getLocalAmount());
		remitApplParametersMap.put("P_FOREIGN_AMT", model.getForeignAmount());
	}

	
	
	private void addBeneficiaryParameters(BenificiaryListView beneficiary) {

		remitApplParametersMap.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneficiary.getBeneficiaryAccountSeqId());
		remitApplParametersMap.put("P_BENEFICIARY_ACCOUNT_NO", beneficiary.getBankAccountNumber());
		remitApplParametersMap.put("P_SERVICE_PROVIDER", beneficiary.getServiceProvider());
		remitApplParametersMap.put("P_FOREIGN_CURRENCY_ID", beneficiary.getCurrencyId());
		remitApplParametersMap.put("P_BENEFICARY_RELATIONSHIP_ID", beneficiary.getBeneficiaryRelationShipSeqId());

	}
	
	
	private Map<String, Object> setupRoutingDetails(TrnxRoutingDetails trnxRoutingDetails) {
		Map<String, Object> routingDetails  =new HashMap<>();
		routingDetails.put("P_ROUTING_COUNTRY_ID", trnxRoutingDetails.getRoutingCountryId());
		routingDetails.put("P_SERVICE_MASTER_ID", trnxRoutingDetails.getServiceMasterId());
		routingDetails.put("P_ROUTING_BANK_ID", trnxRoutingDetails.getRoutingBankId());
		routingDetails.put("P_ROUTING_BANK_BRANCH_ID", trnxRoutingDetails.getBankBranchId());
		routingDetails.put("P_REMITTANCE_MODE_ID", trnxRoutingDetails.getRemittanceModeId());
		routingDetails.put("P_DELIVERY_MODE_ID", trnxRoutingDetails.getDeliveryModeId());
		return routingDetails;
	}
	

	private void validateTransactionAmount(ExchangeRateBreakup breakup, BigDecimal newCommission,
			BigDecimal currencyId) {
		if (!isSaveRemittanceFlow) {
			return;
		}
		String appCurrencyQuote = currencyMasterService.getApplicationCountryCurrencyQuote();
		BigDecimal netAmount = breakup.getNetAmount();
		String inclusiveExclusiveComm = null;
		AuthenticationLimitCheckView onlineTxnLimit = parameterService.getOnlineTxnLimit();
		
		// online sp limit check
		onlineTxnLimit = partnerTransactionManager.onlineServiceProviderLimit(onlineTxnLimit);
		
		if(onlineTxnLimit!=null ) {
			inclusiveExclusiveComm = onlineTxnLimit.getCharField2();
			if(!StringUtils.isBlank(inclusiveExclusiveComm)  && inclusiveExclusiveComm.equalsIgnoreCase(ConstantDocument.COMM_EXCLUDE)) {
			netAmount =netAmount.subtract(newCommission==null?BigDecimal.ZERO:newCommission);
			}else if(!StringUtils.isBlank(inclusiveExclusiveComm)  && inclusiveExclusiveComm.equalsIgnoreCase(ConstantDocument.COMM_INCLUDE)) {
				netAmount =breakup.getNetAmount();
				
		}else {
				netAmount =netAmount.subtract(newCommission==null?BigDecimal.ZERO:newCommission);
			}
			
		}
		
		if (onlineTxnLimit!=null && onlineTxnLimit.getAuthLimit() !=null && netAmount.compareTo(onlineTxnLimit.getAuthLimit()) > 0) {
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("Online Transaction Amount should not exceed - ").append(appCurrencyQuote);
			errorMessage.append(" ").append(onlineTxnLimit.getAuthLimit());
			throw new GlobalException(TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED, errorMessage.toString());
		}
		CurrencyMasterModel beneCurrencyMaster = currencyMasterService.getCurrencyMasterById(currencyId);
		BigDecimal decimalCurrencyValue = beneCurrencyMaster.getDecinalNumber();
		String currencyQuoteName = beneCurrencyMaster.getQuoteName();
		if (newCommission == null) {
			Map<String, Object> commissionRangeMap = getCommissionRange(breakup);
			String msg = "";
			BigDecimal fromAmount = BigDecimal.ZERO;
			BigDecimal toAmount = BigDecimal.ZERO;
			BigDecimal fcAmount = RoundUtil.roundBigDecimal(breakup.getConvertedFCAmount(),
					decimalCurrencyValue.intValue());
			if (commissionRangeMap.get("FROM_AMOUNT") != null || commissionRangeMap.get("TO_AMOUNT") != null) {
				fromAmount = (BigDecimal) commissionRangeMap.get("FROM_AMOUNT");
				toAmount = (BigDecimal) commissionRangeMap.get("TO_AMOUNT");
				if (fcAmount.compareTo(fromAmount) < 0) {
					msg = "Amount to be remitted, cannot be lesser than " + currencyQuoteName + " " + fromAmount
							+ ".Please increase the amount to be remitted.";
				} else if (fcAmount.compareTo(toAmount) > 0) {
					msg = "Amount to be remitted, exceeds the permissible limit .Please decrease the amount to be remitted to less than "
							+ currencyQuoteName + " " + toAmount + ".";
				}
			}

			if (!StringUtils.isBlank(msg)) {
				throw new GlobalException(REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL, msg);
			}

		}
		validateNewBeneTransactionAmount(breakup);
	}
	private void validateNewBeneTransactionAmount(ExchangeRateBreakup breakup) {
		AuthenticationLimitCheckView authLimit = parameterService
				.getAuthenticationViewRepository(AuthType.NEW_BENE_TRANSACT_AMOUNT_LIMIT.getAuthType());
		BigDecimal netAmount = breakup.getNetAmount();
		BenificiaryListView benificiary = (BenificiaryListView) validatedObjects.get("BENEFICIARY");
		boolean isNewBene = DateUtil.isToday(benificiary.getCreatedDate());
		if (isNewBene && authLimit != null && netAmount.doubleValue() > authLimit.getAuthLimit().doubleValue()) {
			String errorExpr = jaxUtil.buildErrorExpression(TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_NEW_BENE.toString(),
					authLimit.getAuthLimit());
			throw new GlobalException(errorExpr, "New beneficiary max allowed limit exceeds");
		}
	}

	private void validateNumberOfTransactionLimits() {
		Map<String, Integer> customerTxnAmounts = getCustomerTransactionCounts();
		List<AuthenticationLimitCheckView> txnLimits = parameterService.getAllNumberOfTxnLimits();

		for (AuthenticationLimitCheckView limitView : txnLimits) {

			logger.debug(" limitView.getAuthorizationType() :" + limitView.getAuthorizationType() + "\t Auth Limit :"
					+ limitView.getAuthLimit());
			Integer txnCount = customerTxnAmounts.get(limitView.getAuthorizationType());
			logger.debug("Trnx Count for Limit Check :" + txnCount);
			if (txnCount >= limitView.getAuthLimit().intValue()) {
				throw new GlobalException(NO_OF_TRANSACTION_LIMIT_EXCEEDED, limitView.getAuthMessage());
			}
		}

	}

	
	public void setNetAmountAndLoyalityState(ExchangeRateBreakup exchangeRateBreakup,AbstractRemittanceApplicationRequestModel model, RemittanceTransactionResponsetModel responseModel,BigDecimal comission,BigDecimal vatamount) {
		BigDecimal netAmount = exchangeRateBreakup.getConvertedLCAmount().add(comission==null?BigDecimal.ZERO:comission).add(vatamount==null?BigDecimal.ZERO:vatamount);
		exchangeRateBreakup.setNetAmountWithoutLoyality(netAmount);
		responseModel.setLoyalityAmountAvailableForTxn(loyalityPointService.getloyaltyAmountEncashed(comission==null?BigDecimal.ZERO:comission));
		if (!JaxUtil.isNullZeroBigDecimalCheck(comission)) {
			responseModel.setCanRedeemLoyalityPoints(false);
			responseModel.setLoyalityPointState(LoyalityPointState.CAN_NOT_AVAIL);
			responseModel.setDiscountOnComission(BigDecimal.ZERO);
		}else {
			responseModel.setDiscountOnComission(corporateDiscountManager.corporateDiscount());
		}
		if (remitAppManager.loyalityPointsAvailed(model, responseModel)) {
			/** old logic **/
			// exchangeRateBreakup.setNetAmount(netAmount.subtract(loyalityPointService.getVwLoyalityEncash().getEquivalentAmount()));
			/** Modified by Rabil for corporate employee discount on 24 Mar 2018 **/
			BigDecimal loyaltyAmount = loyalityPointService.getVwLoyalityEncash().getEquivalentAmount();
			if (JaxUtil.isNullZeroBigDecimalCheck(comission) && comission.compareTo(loyaltyAmount) > 0) {
				exchangeRateBreakup.setNetAmount(netAmount.subtract(loyalityPointService.getVwLoyalityEncash().getEquivalentAmount()));
				
				logger.info("net maount in ex1:"+exchangeRateBreakup.getNetAmount());
			} else {
				exchangeRateBreakup.setNetAmount(netAmount.subtract(comission));
				logger.info("net maount in ex2:"+exchangeRateBreakup.getNetAmount());
			}
		} else {
			exchangeRateBreakup.setNetAmount(netAmount);
			logger.info("net maount in ex3:"+exchangeRateBreakup.getNetAmount());
		}
	}

	public BankCharges getApplicableCharge(List<BankCharges> charges) {

		BankCharges output = null;
		if (charges != null && !charges.isEmpty()) {
			output = charges.get(0);
			for (BankCharges charge : charges) {
				BizComponentData chargesFor = charge.getChargeFor();
				if (chargesFor != null) {
					// individual charges
					if ("Y".equals(chargesFor.getActive()) && "I".equals(chargesFor.getComponentCode())) {
						output = charge;
					}
				}
			}
		}
		return output;
	}

	private HashMap<String, Object> getBeneBankDetails(BenificiaryListView beneficiary) {

		HashMap<String, Object> beneBankDetails = new HashMap<>();
		beneBankDetails.put("P_APPLICATION_COUNTRY_ID", meta.getCountryId());
		beneBankDetails.put("P_USER_TYPE", "ONLINE");
		beneBankDetails.put("P_BENEFICIARY_COUNTRY_ID", beneficiary.getBenificaryCountry());
		beneBankDetails.put("P_BENEFICIARY_BANK_ID", beneficiary.getBankId());
		beneBankDetails.put("P_BENEFICIARY_BRANCH_ID", beneficiary.getBranchId());
		beneBankDetails.put("P_BENEFICIARY_BANK_ACCOUNT", beneficiary.getBankAccountNumber());
		beneBankDetails.put("P_CUSTOMER_ID", meta.getCustomerId());
		beneBankDetails.put("P_SERVICE_GROUP_CODE", beneficiary.getServiceGroupCode());
		beneBankDetails.put("P_CURRENCY_ID", beneficiary.getCurrencyId());
		beneBankDetails.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneficiary.getBeneficiaryAccountSeqId());

		return beneBankDetails;
	}
	
	
	
	
	
	

	public void validateBlackListedBene(BenificiaryListView beneficiary) {
		List<BlackListModel> blist = blistDao.getBlackByName(beneficiary.getBenificaryName());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException(BLACK_LISTED_CUSTOMER.getStatusKey(),
					"The beneficiary you have selected has been black-listed by CBK ");
		}
		if (beneficiary.getArbenificaryName() != null) {
			blist = blistDao.getBlackByName(beneficiary.getArbenificaryName());
			if (blist != null && !blist.isEmpty()) {
				throw new GlobalException(BLACK_LISTED_CUSTOMER.getStatusKey(),
						"Beneficiary local name found matching with black list ");
			}
		}
	}

	/**
	 * Returns customer's transaction amounts in 3 forms 1. daily 2. weekly 3.
	 * monthly
	 */
	public Map<String, Integer> getCustomerTransactionCounts() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Integer> output = new HashMap<>();
		Customer customer = custDao.getCustById(meta.getCustomerId());
		List<ViewTransfer> monthlyTxns = transferRepo
				.getMonthlyTransactionByCustomerReference(customer.getCustomerReference());
		int monthlyCount = monthlyTxns.size();
		int weeklyCount = 0;
		int dailyCount = 0;
		Calendar today = Calendar.getInstance();
		String todayStr = sdf.format(today.getTime());
		for (ViewTransfer txn : monthlyTxns) {
			Calendar txnDate = Calendar.getInstance();
			txnDate.setTime(txn.getDocDate());
			if (todayStr.equals(sdf.format(txn.getDocDate()))) {
				dailyCount++;
			}
			int thisWeek = today.get(Calendar.WEEK_OF_MONTH);
			int txnWeek = txnDate.get(Calendar.WEEK_OF_MONTH);
			if (thisWeek == txnWeek) {
				weeklyCount++;
			}
		}
		logger.debug("getCustomerTransactionCounts CustomerId" + meta.getCustomerId() + "\t dailyCount" + dailyCount
				+ "\t monthlyCount :" + monthlyCount + "\t weeklyCount" + weeklyCount);
		output.put("10", dailyCount);
		output.put("12", monthlyCount);
		output.put("11", weeklyCount);
		return output;
	}

	@Autowired
	AuditService auditService;

	public RemittanceApplicationResponseModel saveApplication(RemittanceTransactionRequestModel model) {
		this.isSaveRemittanceFlow = true;
		
		RemittanceTransactionResponsetModel validationResults = this.validateTransactionData(model);
		
		if (validationResults !=null && jaxTenantProperties.getFlexFieldEnabled()) {
			remittanceTransactionRequestValidator.validateExchangeRate(model, validationResults);
			remittanceTransactionRequestValidator.validateFlexFields(model, remitApplParametersMap);
		}else {
			throw new GlobalException(JaxError.VALIDATION_NOT_NULL, "Validation is missing");
		}
		remittanceAdditionalFieldManager.validateAdditionalFields(model, remitApplParametersMap);
		// validate routing bank requirements
		ExchangeRateBreakup breakup = validationResults.getExRateBreakup();
		
		logger.info("amount in exchnagerate break up"+breakup.getNetAmount());
		BigDecimal netAmountPayable = breakup.getNetAmount();
		RemittanceApplicationResponseModel remiteAppModel = new RemittanceApplicationResponseModel();
		deactivatePreviousApplications();
		validateAdditionalCheck();
		validateAdditionalBeneDetails(model);
		remittanceAdditionalFieldManager.processAdditionalFields(model);
		RemittanceApplication remittanceApplication = remitAppManager.createRemittanceApplication(model,validatedObjects, validationResults, remitApplParametersMap);
		RemittanceAppBenificiary remittanceAppBeneficairy = remitAppBeneManager.createRemittanceAppBeneficiary(remittanceApplication);
		List<AdditionalInstructionData> additionalInstrumentData;
		if (jaxTenantProperties.getFlexFieldEnabled()) {
			additionalInstrumentData = remittanceAppAddlDataManager.createAdditionalInstnData(remittanceApplication,model);
		} else {
			additionalInstrumentData = oldRemittanceApplicationAdditionalDataManager.createAdditionalInstnData(remittanceApplication);
		}
	
		// save service provider
		RemitApplSrvProv remitApplSrvProv = null;
		remitAppDao.saveAllApplicationData(remittanceApplication, remittanceAppBeneficairy, additionalInstrumentData,remitApplSrvProv);
		remitAppDao.updatePlaceOrder(model, remittanceApplication);
		remiteAppModel.setRemittanceAppId(remittanceApplication.getRemittanceApplicationId());
		remiteAppModel.setNetPayableAmount(netAmountPayable);
		remiteAppModel.setDocumentIdForPayment(remittanceApplication.getPaymentId());
		remiteAppModel.setDocumentFinancialYear(remittanceApplication.getDocumentFinancialyear());
		remiteAppModel.setMerchantTrackId(meta.getCustomerId());
		remiteAppModel.setDocumentIdForPayment(remittanceApplication.getDocumentNo().toString());

		CivilIdOtpModel civilIdOtpModel = null;
		if (model.getmOtp() == null) {
			// this flow is for send OTP
			civilIdOtpModel = addOtpOnRemittance(model);
		} else {
			// this flow is for validate OTP
			userService.validateOtp(null, model.getmOtp(), null);
		}
		remiteAppModel.setCivilIdOtpModel(civilIdOtpModel);

		logger.info("Application saved successfully, response: " + remiteAppModel.toString());

		auditService.log(new CActivityEvent(Type.APPLICATION_CREATED,
				String.format("%s/%s", remiteAppModel.getDocumentFinancialYear(),
						remiteAppModel.getDocumentIdForPayment()))
				.field("STATUS").to(JaxTransactionStatus.APPLICATION_CREATED)
				.set(new RemitInfo(remittanceApplication.getRemittanceApplicationId(), remittanceApplication.getLocalTranxAmount()))
				.result(Result.DONE));
		
		return remiteAppModel;

	}

	
	
	public RemittanceApplicationResponseModel saveApplicationV2(RemittanceTransactionDrRequestModel model) {
		this.isSaveRemittanceFlow = true;
		
		RemittanceTransactionResponsetModel validationResults = this.validateTransactionDataV2(model);
		
		if (validationResults !=null && jaxTenantProperties.getFlexFieldEnabled()) {
			remittanceTransactionRequestValidator.validateExchangeRate(model, validationResults);
			remittanceTransactionRequestValidator.validateFlexFields(model, remitApplParametersMap);
		}else {
			throw new GlobalException(JaxError.VALIDATION_NOT_NULL, "Validation is missing");
		}
		remittanceAdditionalFieldManager.validateAdditionalFields(model, remitApplParametersMap);
		// validate routing bank requirements
		ExchangeRateBreakup breakup = validationResults.getExRateBreakup();
		
		logger.info("amount in exchnagerate break up"+breakup.getNetAmount());
		BigDecimal netAmountPayable = breakup.getNetAmount();
		RemittanceApplicationResponseModel remiteAppModel = new RemittanceApplicationResponseModel();
		deactivatePreviousApplications();
		validateAdditionalCheck();
		validateAdditionalBeneDetailsV2(model);
		remittanceAdditionalFieldManager.processAdditionalFields(model);
		RemittanceApplication remittanceApplication = remitAppManager.createRemittanceApplicationV2(model,validatedObjects, validationResults, remitApplParametersMap);
		RemittanceAppBenificiary remittanceAppBeneficairy = remitAppBeneManager.createRemittanceAppBeneficiary(remittanceApplication);
		List<AdditionalInstructionData> additionalInstrumentData;
		if (jaxTenantProperties.getFlexFieldEnabled()) {
			additionalInstrumentData = remittanceAppAddlDataManager.createAdditionalInstnDataV2(remittanceApplication,model);
		} else {
			additionalInstrumentData = oldRemittanceApplicationAdditionalDataManager.createAdditionalInstnData(remittanceApplication);
		}
		RemitApplSrvProv remitApplSrvProv = null;
		if(model.getDynamicRroutingPricingBreakup() != null) {
			remitApplSrvProv = branchRemittanceApplManager.createRemitApplSrvProv(model.getDynamicRroutingPricingBreakup(),remittanceApplication.getCreatedBy());
		}
		remitAppDao.saveAllApplicationData(remittanceApplication, remittanceAppBeneficairy, additionalInstrumentData,remitApplSrvProv);
		remitAppDao.updatePlaceOrderV2(model, remittanceApplication);
		remiteAppModel.setRemittanceAppId(remittanceApplication.getRemittanceApplicationId());
		remiteAppModel.setNetPayableAmount(netAmountPayable);
		remiteAppModel.setDocumentIdForPayment(remittanceApplication.getPaymentId());
		remiteAppModel.setDocumentFinancialYear(remittanceApplication.getDocumentFinancialyear());
		remiteAppModel.setMerchantTrackId(meta.getCustomerId());
		remiteAppModel.setDocumentIdForPayment(remittanceApplication.getDocumentNo().toString());

		CivilIdOtpModel civilIdOtpModel = null;
		if (model.getmOtp() == null) {
			// this flow is for send OTP
			civilIdOtpModel = addOtpOnRemittanceV2(model);
		} else {
			// this flow is for validate OTP
			userService.validateOtp(null, model.getmOtp(), null);
		}
		remiteAppModel.setCivilIdOtpModel(civilIdOtpModel);

		logger.info("Application saved successfully, response: " + remiteAppModel.toString());

		auditService.log(new CActivityEvent(Type.APPLICATION_CREATED,
				String.format("%s/%s", remiteAppModel.getDocumentFinancialYear(),
						remiteAppModel.getDocumentIdForPayment()))
				.field("STATUS").to(JaxTransactionStatus.APPLICATION_CREATED)
				.set(new RemitInfo(remittanceApplication.getRemittanceApplicationId(), remittanceApplication.getLocalTranxAmount()))
				.result(Result.DONE));
		
		return remiteAppModel;

	}

	
	
	private void deactivatePreviousApplications() {
		BigDecimal customerId = meta.getCustomerId();
		remittanceApplicationService.deActivateApplication(customerId);
	}

	private void validateAdditionalBeneDetails(RemittanceTransactionRequestModel model) {
		Map<String, Object> output = applicationProcedureDao
				.toFetchDetilaFromAddtionalBenficiaryDetails(remitApplParametersMap);
		remitApplParametersMap.putAll(output);
		if (isSaveRemittanceFlow) {
			BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(model.getBeneId());
			if (!ConstantDocument.Yes.equals(beneficiary.getIsActive())) {
				throw new GlobalException("The selected beneficiary is deactivated. Please activate the beneficiary to proceed with the transaction.");
			}

			// Beneficiary not allow to remit if any data missing
			BeneficiaryListDTO beneDtoCheck = beneCheckService
					.beneCheck(transactionHistroyService.convertBeneModelToDto(beneficiary));

			if (CollectionUtils.isNotEmpty(beneDtoCheck.getBeneficiaryErrorStatus())) {
				throw new GlobalException(beneDtoCheck.getBeneficiaryErrorStatus().get(0).getErrorDesc());
			}
		}
	}

	
	private void validateAdditionalBeneDetailsV2(RemittanceTransactionDrRequestModel model) {
		Map<String, Object> output = applicationProcedureDao.toFetchDetilaFromAddtionalBenficiaryDetails(remitApplParametersMap);
		remitApplParametersMap.putAll(output);
		if (isSaveRemittanceFlow) {
			BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(model.getBeneId());
			if (!ConstantDocument.Yes.equals(beneficiary.getIsActive())) {
				throw new GlobalException("The selected beneficiary is deactivated. Please activate the beneficiary to proceed with the transaction.");
			}

			// Beneficiary not allow to remit if any data missing
			BeneficiaryListDTO beneDtoCheck = beneCheckService
					.beneCheck(transactionHistroyService.convertBeneModelToDto(beneficiary));

			if (CollectionUtils.isNotEmpty(beneDtoCheck.getBeneficiaryErrorStatus())) {
				throw new GlobalException(beneDtoCheck.getBeneficiaryErrorStatus().get(0).getErrorDesc());
			}
		}
	}
	
	
	private void validateAdditionalCheck() {
		applicationProcedureDao.getAdditionalCheckProcedure(remitApplParametersMap);
	}

	public RemittanceTransactionStatusResponseModel getTransactionStatus(
			RemittanceTransactionStatusRequestModel request) {
		RemittanceTransactionStatusResponseModel model = new RemittanceTransactionStatusResponseModel();
		RemittanceTransaction remittanceTransaction = remitAppDao
				.getRemittanceTransaction(request.getApplicationDocumentNumber(), request.getDocumentFinancialYear());
		RemittanceApplication application = remitAppDao.getApplication(request.getApplicationDocumentNumber(),
				request.getDocumentFinancialYear());
		remittanceApplicationService.checkForSuspiciousPaymentAttempts(application.getRemittanceApplicationId());
		if (remittanceTransaction != null) {
			BigDecimal cutomerReference = remittanceTransaction.getCustomerId().getCustomerId();
			BigDecimal remittancedocfyr = remittanceTransaction.getDocumentFinanceYear();
			BigDecimal remittancedocNumber = remittanceTransaction.getDocumentNo();
			TransactionHistroyDTO transactionHistoryDto = transactionHistroyService
					.getTransactionHistoryDto(cutomerReference, remittancedocfyr, remittancedocNumber);
			model.setTransactionHistroyDTO(transactionHistoryDto);
			if (Boolean.TRUE.equals(request.getPromotion())) {
				PromotionDto promoDto = promotionManager.getPromotionDto(remittancedocNumber, remittancedocfyr);
				if (promoDto != null && !promoDto.isChichenVoucher()) {
					model.setPromotionDto(promotionManager.getPromotionDto(remittancedocNumber, remittancedocfyr));
				}
			}
		}
		model.setTransactionReference(getTransactionReference(application));
		if ("Y".equals(application.getLoyaltyPointInd())) {
			model.setNetAmount(application.getLocalTranxAmount());
		} else {
			model.setNetAmount(application.getLocalNetTranxAmount());
		}
		JaxTransactionStatus status = getJaxTransactionStatus(application);
		model.setStatus(status);
		
		if (remittanceTransaction != null) {
			PromotionDto obj = dailyPromotionManager.getWanitBuyitMsg(remittanceTransaction);
			if(obj != null) {
				model.setPromotionDto(obj);
			}
		}
		
		model.setErrorCategory(application.getErrorCategory());
		model.setErrorMessage(application.getErrorMessage());
	if(application.getErrorCategory() != null) {
			ResponseCodeDetailDTO responseCodeDetail = PayGRespCodeJSONConverter.getResponseCodeDetail(application.getErrorCategory());
			
			responseCodeDetail.setPgPaymentId(application.getPaymentId());
			responseCodeDetail.setPgReferenceId(application.getPgReferenceId());
			responseCodeDetail.setPgTransId(application.getPgTransactionId());
			responseCodeDetail.setPgAuth(application.getPgAuthCode());
			
			model.setResponseCodeDetail(responseCodeDetail);
		}
		
		return model;
	}

	private String getTransactionReference(RemittanceApplication application) {
		try {
			return application.getDocumentNo().toString() + application.getDocumentFinancialyear().toString();
		} catch (Exception e) {
			return null;
		}
	}

	private JaxTransactionStatus getJaxTransactionStatus(RemittanceApplication remittanceApplication) {
		JaxTransactionStatus status = JaxTransactionStatus.APPLICATION_CREATED;
		String applicationStatus = remittanceApplication.getApplicaitonStatus();
		if (StringUtils.isBlank(applicationStatus) && remittanceApplication.getPaymentId() != null) {
			status = JaxTransactionStatus.PAYMENT_IN_PROCESS;
		}
		String resultCode = remittanceApplication.getResultCode();
		if ("CAPTURED".equalsIgnoreCase(resultCode)) {
			if ("S".equals(applicationStatus) || "T".equals(applicationStatus)) {
				status = JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_SUCCESS;
			} else {
				status = JaxTransactionStatus.PAYMENT_SUCCESS_APPLICATION_FAIL;
			}
		}
		if ("NOT CAPTURED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_FAIL;
		}
		if ("CANCELED".equalsIgnoreCase(resultCode) || "CANCELLED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_CANCELED_BY_USER;
		}

		return status;
	}

	private CivilIdOtpModel addOtpOnRemittance(RemittanceTransactionRequestModel model) {

		List<TransactionLimitCheckView> trnxLimitList = parameterService.getAllTxnLimits();

		BigDecimal onlineLimit = BigDecimal.ZERO;
		BigDecimal androidLimit = BigDecimal.ZERO;
		BigDecimal iosLimit = BigDecimal.ZERO;

		for (TransactionLimitCheckView view : trnxLimitList) {
			if (JaxChannel.ONLINE.toString().equals(view.getChannel())) {
				onlineLimit = view.getComplianceChkLimit();
			}
			if (ANDROID.equals(view.getChannel())) {
				androidLimit = view.getComplianceChkLimit();
			}
			if (IOS.equals(view.getChannel())) {
				iosLimit = view.getComplianceChkLimit();
			}
		}

		CivilIdOtpModel otpMmodel = null;
		BigDecimal localAmount = (BigDecimal) remitApplParametersMap.get("P_CALCULATED_LC_AMOUNT");
		if (((meta.getChannel().equals(JaxChannel.ONLINE)) && (WEB.equals(meta.getAppType()))
				&& (localAmount.compareTo(onlineLimit) >= 0)) ||

				(IOS.equals(meta.getAppType()) && localAmount.compareTo(iosLimit) >= 0) ||

				(ANDROID.equals(meta.getAppType()) && localAmount.compareTo(androidLimit) >= 0)) {

			List<ContactType> channel = new ArrayList<>();
			channel.add(ContactType.SMS_EMAIL);
			otpMmodel = (CivilIdOtpModel) userService.sendOtpForCivilId(null, channel, null, null).getData().getValues()
					.get(0);
		}
		return otpMmodel;
	}
	
	
	private CivilIdOtpModel addOtpOnRemittanceV2(RemittanceTransactionDrRequestModel model) {

		List<TransactionLimitCheckView> trnxLimitList = parameterService.getAllTxnLimits();

		BigDecimal onlineLimit = BigDecimal.ZERO;
		BigDecimal androidLimit = BigDecimal.ZERO;
		BigDecimal iosLimit = BigDecimal.ZERO;

		for (TransactionLimitCheckView view : trnxLimitList) {
			if (JaxChannel.ONLINE.toString().equals(view.getChannel())) {
				onlineLimit = view.getComplianceChkLimit();
			}
			if (ANDROID.equals(view.getChannel())) {
				androidLimit = view.getComplianceChkLimit();
			}
			if (IOS.equals(view.getChannel())) {
				iosLimit = view.getComplianceChkLimit();
			}
		}

		CivilIdOtpModel otpMmodel = null;
		BigDecimal localAmount = (BigDecimal) remitApplParametersMap.get("P_CALCULATED_LC_AMOUNT");
		if (((meta.getChannel().equals(JaxChannel.ONLINE)) && (WEB.equals(meta.getAppType()))
				&& (localAmount.compareTo(onlineLimit) >= 0)) ||

				(IOS.equals(meta.getAppType()) && localAmount.compareTo(iosLimit) >= 0) ||

				(ANDROID.equals(meta.getAppType()) && localAmount.compareTo(androidLimit) >= 0)) {

			List<ContactType> channel = new ArrayList<>();
			channel.add(ContactType.SMS_EMAIL);
			otpMmodel = (CivilIdOtpModel) userService.sendOtpForCivilId(null, channel, null, null).getData().getValues()
					.get(0);
		}
		return otpMmodel;
	}
	
	
	/** added by Rabil on 27 May 2019 **/
	public String beneAccountValidationThroughApi(BigDecimal serviceId,BigDecimal routingBankId ,BenificiaryListView beneficiary) {
		String accountNo = null;
		String errorMsg = null;

		if(beneficiary != null && beneficiary.getBankId() != null) {
			BankMasterModel bankMaster = bankService.getBankById(beneficiary.getBankId());
			OWSScheduleModel oWSScheduleModel = iOWSScheduleModelRepository.findByCorBank(bankMaster.getBankCode());
			if(oWSScheduleModel!=null && oWSScheduleModel.getBeneAccountCheckInd()!=null && !StringUtils.isBlank(oWSScheduleModel.getBeneAccountCheckInd()) && oWSScheduleModel.getBeneAccountCheckInd().equalsIgnoreCase("1")) {
				Boolean ibankCheck = checkIbanNumber(bankMaster);
				if(ibankCheck) {
					accountNo = beneficiary.getIbanNumber();
				}else {
					accountNo = beneficiary.getBankAccountNumber();
				}
				if(!StringUtils.isBlank(accountNo)) {
					errorMsg = accountValidationApi(bankMaster.getBankCode(),accountNo);
				}
			}
		}
		
		if(routingBankId != null) {
			BankMasterModel routingBankMaster = bankService.getBankById(routingBankId);
			OWSScheduleModel oWSScheduleModelTT = iOWSScheduleModelRepository.findByCorBank(routingBankMaster.getBankCode());
			
			if(oWSScheduleModelTT!=null && oWSScheduleModelTT.getTtbeneAccountCheckInd()!=null && !StringUtils.isBlank(oWSScheduleModelTT.getTtbeneAccountCheckInd()) && oWSScheduleModelTT.getTtbeneAccountCheckInd().equals("1")) {
				Boolean ibankCheck = checkIbanNumber(routingBankMaster);
				if(ibankCheck) {
					accountNo = beneficiary.getIbanNumber();
				}else {
					accountNo = beneficiary.getBankAccountNumber();
				}
				if(!StringUtils.isBlank(accountNo)) {
					errorMsg = accountValidationApi(routingBankMaster.getBankCode(),accountNo);
				}
			}
		}
		


		return errorMsg;
	}
	
	/** added by Rabil on 28 May 2019 **/
	private Boolean checkIbanNumber(BankMasterModel bankMaster) {
		Boolean isIban =false;
		if(bankMaster!=null && !StringUtils.isBlank(bankMaster.getIbanFlag()) && bankMaster.getIbanFlag().equals(ConstantDocument.Yes)) {
			isIban =true;
		}
		return isIban;
	}
	
	/** added by Rabil on 28 May 2019 **/
	private String accountValidationApi(String bankCode,String beneBankaccount) {
		String errorMessage =null;
		String accountValidation="Y";
		
		ServiceProviderCredentialsModel crdeModel = serviceProviderCredentailsRepository.findByLoginCredential1(ConstantDocument.BENE_ACCT_VALID);
		String bankUrl =null;
		if(crdeModel!=null && !StringUtils.isBlank(crdeModel.getLoginCredential2())) {
			bankUrl = crdeModel.getLoginCredential2();
		}
		if(!StringUtils.isBlank(bankUrl)) {
			String url = bankUrl+"?bank_code="+bankCode+"&bene_bank_account="+beneBankaccount;
			try {
			String response = restService.ajax(url).post().asString();
			logger.info("response :"+response);
			if(!StringUtils.isBlank(response)) {
				BsbApiResponse bsbApi=null;
					
					bsbApi = JsonUtil.getMapper().readValue(response, BsbApiResponse.class);

					if(bsbApi!=null &&  bsbApi.getTechError() == false && bsbApi.getIs_valid_account() == false) {
						accountValidation ="N";
						errorMessage =bsbApi.getResponseDesc();
					}
			}else {
				logger.info("BSB API Resonse :"+response);
			}
		} catch(Exception ee){
			accountValidation ="Y";
			ee.printStackTrace();
		}
		}	
		return accountValidation;	
	}
	
	private ExchangeRateBreakup getExchangeRateBreakup(List<ExchangeRateApprovalDetModel> exchangeRates,RemittanceTransactionRequestModel model, RemittanceTransactionResponsetModel responseModel,BigDecimal comission) {
		BigDecimal fcAmount = model.getForeignAmount();
		BigDecimal lcAmount = model.getLocalAmount();
		ExchangeRateBreakup exchangeRateBreakup;
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		BigDecimal fCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal beneCountryId = (BigDecimal) remitApplParametersMap.get("P_BENEFICIARY_COUNTRY_ID");

		if (jaxTenantProperties.getIsDynamicPricingEnabled() && !remittanceParameterMapManager.isCashChannel()) {
			ExchangeRateResponseModel exchangeRateResponseModel = newExchangeRateService.getExchangeRateResponseModelUsingDynamicPricing(fCurrencyId, lcAmount, fcAmount, beneCountryId,routingBankId);
			responseModel.setDiscountAvailed(exchangeRateResponseModel.getDiscountAvailed());
			responseModel.setCustomerDiscountDetails(exchangeRateResponseModel.getCustomerDiscountDetails());
			responseModel.setCostRateLimitReached(exchangeRateResponseModel.getCostRateLimitReached());
			exchangeRateBreakup = exchangeRateResponseModel.getExRateBreakup();
		} else if (jaxTenantProperties.getExrateBestRateLogicEnable()) {
			exchangeRateBreakup = newExchangeRateService.getExchangeRateBreakUpUsingBestRate(fCurrencyId, lcAmount,fcAmount, routingBankId);
		} else {
			exchangeRateBreakup = newExchangeRateService.createExchangeRateBreakUp(exchangeRates,model.getLocalAmount(), model.getForeignAmount());
		}

		setNetAmountAndLoyalityState(exchangeRateBreakup, model, responseModel, comission,BigDecimal.ZERO);
		return exchangeRateBreakup;
	}
/** Added by Rabil on 12 Jul 2019**/
	public void setCustomerDiscountColumnsV2(RemittanceTransactionResponsetModel validationResults,DynamicRoutingPricingDto model) {
		validationResults.setDiscountAvailed(model.getDiscountAvailed());
		validationResults.setDiscountOnComission(model.getDiscountOnComission());
		validationResults.setCustomerDiscountDetails(model.getCustomerDiscountDetails());
		validationResults.setCostRateLimitReached(model.getCostRateLimitReached());
		
	}
	
	public void applyCurrencyRoudingLogicSP(ExchangeRateBreakup exRatebreakUp) {
		BigDecimal fcurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal localCurrencyId = meta.getDefaultCurrencyId();
		exRatebreakUp.setFcDecimalNumber(currencyMasterService.getCurrencyMasterById(fcurrencyId).getDecinalNumber());
		exRatebreakUp.setLcDecimalNumber(currencyMasterService.getCurrencyMasterById(localCurrencyId).getDecinalNumber());

		if (exRatebreakUp.getConvertedFCAmount() != null) {
			logger.debug("getConvertedFCAmount :"+ RoundUtil.roundToZeroDecimalPlaces(exRatebreakUp.getConvertedFCAmount()));
			exRatebreakUp.setConvertedFCAmount(RoundUtil.roundBigDecimal(exRatebreakUp.getConvertedFCAmount(),exRatebreakUp.getFcDecimalNumber().intValue()));
		}

		if (exRatebreakUp.getConvertedLCAmount() != null) {
			exRatebreakUp.setConvertedLCAmount(RoundUtil.roundBigDecimal(exRatebreakUp.getConvertedLCAmount(),exRatebreakUp.getLcDecimalNumber().intValue()));
		}

		exRatebreakUp.setNetAmount(RoundUtil.roundBigDecimal(exRatebreakUp.getNetAmount(), exRatebreakUp.getLcDecimalNumber().intValue()));
		
		logger.info("amount in ex:" +exRatebreakUp.getNetAmount());
		exRatebreakUp.setNetAmountWithoutLoyality(RoundUtil.roundBigDecimal(exRatebreakUp.getNetAmountWithoutLoyality(),exRatebreakUp.getLcDecimalNumber().intValue()));
		exRatebreakUp.setInverseRate((RoundUtil.roundBigDecimal(exRatebreakUp.getInverseRate(), 6)));
	}
}
