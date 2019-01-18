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
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.amx.amxlib.constant.AuthType;
import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.constant.LoyalityPointState;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.PromotionDto;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.auditlog.JaxTransactionEvent;
import com.amx.jax.config.JaxProperties;
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
import com.amx.jax.dbmodel.BankCharges;
import com.amx.jax.dbmodel.BankServiceRule;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.dbmodel.TransactionLimitCheckView;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dbmodel.remittance.ViewTransfer;
import com.amx.jax.error.JaxError;
import com.amx.jax.exrateservice.dao.ExchangeRateDao;
import com.amx.jax.exrateservice.dao.PipsMasterDao;
import com.amx.jax.exrateservice.service.NewExchangeRateService;
import com.amx.jax.logger.AuditEvent;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.CActivityEvent.Type;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.VTransferRepository;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.service.LoyalityPointService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.services.BeneficiaryCheckService;
import com.amx.jax.services.JaxConfigService;
import com.amx.jax.services.RemittanceApplicationService;
import com.amx.jax.services.RoutingService;
import com.amx.jax.services.TransactionHistroyService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.jax.validation.RemittanceTransactionRequestValidator;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceTransactionManager {

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
	private UserService userService;

	protected Map<String, Object> validatedObjects = new HashMap<>();

	private boolean isSaveRemittanceFlow;

	@Autowired
	private JaxUtil jaxUtil;
	@Autowired
	RoutingService routingService;
	@Autowired
	JaxProperties jaxProperties;
	@Autowired
	NewExchangeRateService newExchangeRateService;
	@Autowired
	PromotionManager promotionManager;
	@Autowired
	CountryService countryService;
	@Autowired
	JaxConfigService jaxConfigService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String IOS = "IOS";
	private static final String ANDROID = "ANDROID";
	private static final String WEB = "WEB";

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
		// validateRiskyBene(beneficiary, customer); //it is not required at the time of
		// trnx ,the procedure will take care for existing bene with different
		// nationality
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
		if (!jaxProperties.getExrateBestRateLogicEnable() && (exchangeRates == null || exchangeRates.isEmpty())) {
			throw new GlobalException(REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL,
					"No exchange rate found for bank- " + routingBankId);
		}
		validateNumberOfTransactionLimits();
		validateBeneficiaryTransactionLimit(beneficiary);
		setLoyalityPointIndicaters(responseModel);
		List<BankServiceRule> rules = bankServiceRuleDao.getBankServiceRule(routingBankId, rountingCountryId,
				currencyId, remittanceMode, deliveryMode);
		BankServiceRule appliedRule = rules.get(0);
		List<BankCharges> charges = appliedRule.getBankCharges();
		BankCharges bankCharge = getApplicableCharge(charges);
		BigDecimal commission = bankCharge.getChargeAmount();
		if (newCommission != null) {
			commission = newCommission;
		}
		ExchangeRateBreakup breakup = getExchangeRateBreakup(exchangeRates, model, responseModel, commission);
		remitApplParametersMap.put("P_CALCULATED_FC_AMOUNT", breakup.getConvertedFCAmount());
		remitApplParametersMap.put("P_CALCULATED_LC_AMOUNT", breakup.getConvertedLCAmount());

		if (model.isAvailLoyalityPoints()) {
			validateLoyalityPointsBalance(customer.getLoyaltyPoints());
		}

		logger.info("rountingCountryId: " + rountingCountryId + " serviceMasterId: " + serviceMasterId);

		applyRoudingLogic(breakup);
		breakup = getExchangeRateBreakup(exchangeRates, model, responseModel, commission);
		validateTransactionAmount(breakup, newCommission, currencyId);
		// commission
		responseModel.setTxnFee(commission);
		// exrate
		responseModel.setExRateBreakup(breakup);

		addExchangeRateParameters(responseModel);
		applyRoudingLogic(responseModel.getExRateBreakup());
		return responseModel;

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

	private void setLoyalityPointFlags(Customer customer, RemittanceTransactionResponsetModel responseModel) {
		if (customer.getLoyaltyPoints() != null && customer.getLoyaltyPoints().compareTo(BigDecimal.ZERO) > 0) {
			responseModel.setTotalLoyalityPoints(customer.getLoyaltyPoints());
		} else {
			responseModel.setTotalLoyalityPoints(BigDecimal.ZERO);
		}
		responseModel
				.setMaxLoyalityPointsAvailableForTxn(loyalityPointService.getVwLoyalityEncash().getLoyalityPoint());
	}

	private void applyRoudingLogic(ExchangeRateBreakup exRatebreakUp) {
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
		exRatebreakUp.setNetAmountWithoutLoyality(RoundUtil.roundBigDecimal(exRatebreakUp.getNetAmountWithoutLoyality(),
				exRatebreakUp.getLcDecimalNumber().intValue()));
		exRatebreakUp.setInverseRate((RoundUtil.roundBigDecimal(exRatebreakUp.getInverseRate(), 6)));
	}

	private BigDecimal reCalculateComission() {
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

	private Map<String, Object> getCommissionRange(ExchangeRateBreakup breakup) {

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
				throw new GlobalException(
						TOO_MANY_COMISSION_NOT_DEFINED_FOR_ROUTING_BANK,
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

	private void setLoyalityPointIndicaters(RemittanceTransactionResponsetModel responseModel) {
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

	private void validateLoyalityPointsBalance(BigDecimal availableLoyaltyPoints) {

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

	private void addBeneficiaryParameters(BenificiaryListView beneficiary) {

		remitApplParametersMap.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneficiary.getBeneficiaryAccountSeqId());
		remitApplParametersMap.put("P_BENEFICIARY_ACCOUNT_NO", beneficiary.getBankAccountNumber());
		remitApplParametersMap.put("P_SERVICE_PROVIDER", beneficiary.getServiceProvider());
		remitApplParametersMap.put("P_FOREIGN_CURRENCY_ID", beneficiary.getCurrencyId());
		remitApplParametersMap.put("P_BENEFICARY_RELATIONSHIP_ID", beneficiary.getBeneficiaryRelationShipSeqId());

	}

	private void validateTransactionAmount(ExchangeRateBreakup breakup, BigDecimal newCommission,
			BigDecimal currencyId) {
		if (!isSaveRemittanceFlow) {
			return;
		}
		String appCurrencyQuote = currencyMasterService.getApplicationCountryCurrencyQuote();
		BigDecimal netAmount = breakup.getNetAmount();
		AuthenticationLimitCheckView onlineTxnLimit = parameterService.getOnlineTxnLimit();
		if (netAmount.compareTo(onlineTxnLimit.getAuthLimit()) > 0) {
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

	private ExchangeRateBreakup getExchangeRateBreakup(List<ExchangeRateApprovalDetModel> exchangeRates,
			RemittanceTransactionRequestModel model, RemittanceTransactionResponsetModel responseModel,
			BigDecimal comission) {
		BigDecimal fcAmount = model.getForeignAmount();
		BigDecimal lcAmount = model.getLocalAmount();
		ExchangeRateBreakup exchangeRateBreakup;
		if (jaxProperties.getExrateBestRateLogicEnable()) {
			BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
			BigDecimal fCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
			exchangeRateBreakup = newExchangeRateService.getExchangeRateBreakUp(fCurrencyId, lcAmount, fcAmount,
					routingBankId);
		} else {
			exchangeRateBreakup = createExchangeRateBreakUp(exchangeRates, model, responseModel);
		}

		BigDecimal netAmount = exchangeRateBreakup.getConvertedLCAmount().add(comission);
		exchangeRateBreakup.setNetAmountWithoutLoyality(netAmount);

		if (comission == null || comission.intValue() == 0) {
			responseModel.setCanRedeemLoyalityPoints(false);
			responseModel.setLoyalityPointState(LoyalityPointState.CAN_NOT_AVAIL);
		}
		if (remitAppManager.loyalityPointsAvailed(model, responseModel)) {
			exchangeRateBreakup
					.setNetAmount(netAmount.subtract(loyalityPointService.getVwLoyalityEncash().getEquivalentAmount()));
		} else {
			exchangeRateBreakup.setNetAmount(netAmount);
		}
		return exchangeRateBreakup;

	}

	private ExchangeRateBreakup createExchangeRateBreakUp(List<ExchangeRateApprovalDetModel> exchangeRates,
			RemittanceTransactionRequestModel model, RemittanceTransactionResponsetModel responseModel) {
		BigDecimal fcAmount = model.getForeignAmount();
		BigDecimal lcAmount = model.getLocalAmount();
		ExchangeRateBreakup breakup = new ExchangeRateBreakup();
		ExchangeRateApprovalDetModel exchangeRate = exchangeRates.get(0);
		BigDecimal inverseExchangeRate = exchangeRate.getSellRateMax();
		breakup.setInverseRate(inverseExchangeRate);

		breakup.setRate(new BigDecimal(1).divide(inverseExchangeRate, 10, RoundingMode.HALF_UP));

		if (fcAmount != null && fcAmount.compareTo(BigDecimal.ZERO) > 0) {
			breakup.setConvertedLCAmount(breakup.getInverseRate().multiply(fcAmount));
			breakup.setConvertedFCAmount(fcAmount);
		}
		if (lcAmount != null && lcAmount.compareTo(BigDecimal.ZERO) > 0) {
			breakup.setConvertedFCAmount(breakup.getRate().multiply(lcAmount));
			breakup.setConvertedLCAmount(lcAmount);
		}
		List<PipsMaster> pips = null;

		if (fcAmount != null && fcAmount.compareTo(BigDecimal.ZERO) > 0) {
			pips = pipsDao.getPipsMasterForBranch(exchangeRate, fcAmount);
		} else {
			pips = pipsDao.getPipsMasterForBranch(exchangeRate, breakup.getConvertedFCAmount());
		}
		// apply discounts
		if (pips != null && !pips.isEmpty()) {
			PipsMaster pip = pips.get(0);
			inverseExchangeRate = inverseExchangeRate.subtract(pip.getPipsNo());
			breakup.setInverseRate(inverseExchangeRate);
			breakup.setRate(new BigDecimal(1).divide(inverseExchangeRate, 10, RoundingMode.HALF_UP));
		}

		if (fcAmount != null && fcAmount.compareTo(BigDecimal.ZERO) > 0) {
			breakup.setConvertedLCAmount(breakup.getInverseRate().multiply(fcAmount));
			breakup.setConvertedFCAmount(fcAmount);
		}
		if (lcAmount != null && lcAmount.compareTo(BigDecimal.ZERO) > 0) {
			breakup.setConvertedFCAmount(breakup.getRate().multiply(lcAmount));
			breakup.setConvertedLCAmount(lcAmount);
		}
		return breakup;
	}

	private BankCharges getApplicableCharge(List<BankCharges> charges) {

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

	private void validateBlackListedBene(BenificiaryListView beneficiary) {
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
		if (jaxProperties.getFlexFieldEnabled()) {
			remittanceTransactionRequestValidator.validateExchangeRate(model, validationResults);
			remittanceTransactionRequestValidator.validateFlexFields(model, remitApplParametersMap);
		}
		// validate routing bank requirements
		ExchangeRateBreakup breakup = validationResults.getExRateBreakup();
		BigDecimal netAmountPayable = breakup.getNetAmount();
		RemittanceApplicationResponseModel remiteAppModel = new RemittanceApplicationResponseModel();
		deactivatePreviousApplications();
		validateAdditionalCheck();
		validateAdditionalBeneDetails(model);
		RemittanceApplication remittanceApplication = remitAppManager.createRemittanceApplication(model,
				validatedObjects, validationResults, remitApplParametersMap);
		RemittanceAppBenificiary remittanceAppBeneficairy = remitAppBeneManager
				.createRemittanceAppBeneficiary(remittanceApplication);
		List<AdditionalInstructionData> additionalInstrumentData;
		if (jaxProperties.getFlexFieldEnabled()) {
			additionalInstrumentData = remittanceAppAddlDataManager.createAdditionalInstnData(remittanceApplication,
					model);
		} else {
			additionalInstrumentData = oldRemittanceApplicationAdditionalDataManager
					.createAdditionalInstnData(remittanceApplication);
		}
		remitAppDao.saveAllApplicationData(remittanceApplication, remittanceAppBeneficairy, additionalInstrumentData);
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

		auditService.log(
				new CActivityEvent(Type.APPLICATION_CREATED,
						String.format("{}/{}", remiteAppModel.getDocumentFinancialYear(),
								remiteAppModel.getDocumentIdForPayment())).field("STATUS")
										.to(JaxTransactionStatus.APPLICATION_CREATED).result(Result.DONE));
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
				throw new GlobalException(
						"The selected beneficiary is deactivated. Please activate the beneficiary to proceed with the transaction.");
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
			BigDecimal cutomerReference = remittanceTransaction.getCustomerId();
			BigDecimal remittancedocfyr = remittanceTransaction.getDocumentFinancialyear();
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
		model.setErrorCategory(application.getErrorCategory());
		model.setErrorMessage(application.getErrorMessage());
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

			List<CommunicationChannel> channel = new ArrayList<>();
			channel.add(CommunicationChannel.EMAIL_AS_MOBILE);
			channel.add(CommunicationChannel.MOBILE);
			otpMmodel = (CivilIdOtpModel) userService.sendOtpForCivilId(null, channel, null, null).getData().getValues()
					.get(0);
		}
		return otpMmodel;
	}
}
