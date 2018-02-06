package com.amx.jax.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.JaxTransactionStatus;
import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.ExchangeRateProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.BankDao;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.AuthenticationView;
import com.amx.jax.dbmodel.BankCharges;
import com.amx.jax.dbmodel.BankServiceRule;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dbmodel.remittance.ViewTransfer;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.exrateservice.dao.ExchangeRateDao;
import com.amx.jax.exrateservice.dao.PipsMasterDao;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.VTransferRepository;
import com.amx.jax.service.LoyalityPointService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.services.RemittanceApplicationService;
import com.amx.jax.services.TransactionHistroyService;
import com.amx.jax.userservice.dao.CustomerDao;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)

public class RemittanceTransactionManager {

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	private BlackListDao blistDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

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

	protected Map<String, Object> validatedObjects = new HashMap<>();

	private Logger logger = LoggerFactory.getLogger(getClass());

	public RemittanceTransactionResponsetModel validateTransactionData(RemittanceTransactionRequestModel model) {

		addRequestParameters(model);
		Customer customer = custDao.getCustById(meta.getCustomerId());
		validatedObjects.put("CUSTOMER", customer);
		RemittanceTransactionResponsetModel responseModel = new RemittanceTransactionResponsetModel();
		BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(model.getBeneId());
		remitApplParametersMap.put("P_BENEFICIARY_MASTER_ID", beneficiary.getBeneficaryMasterSeqId());
		addBeneficiaryParameters(beneficiary);
		validateBlackListedBene(beneficiary);
		validatedObjects.put("BENEFICIARY", beneficiary);
		HashMap<String, Object> beneBankDetails = getBeneBankDetails(beneficiary);
		Map<String, Object> routingDetails = applicationProcedureDao.getRoutingDetails(beneBankDetails);
		remitApplParametersMap.putAll(beneBankDetails);
		remitApplParametersMap.putAll(routingDetails);
		remitApplParametersMap.put("P_BENEFICIARY_SWIFT_BANK1", routingDetails.get("P_SWIFT"));
		remitApplParametersMap.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneficiary.getBeneficiaryAccountSeqId());
		validatedObjects.put("ROUTINGDETAILS", routingDetails);
		remitApplParametersMap.put("BENEFICIARY", beneficiary);
		BigDecimal serviceMasterId = new BigDecimal(routingDetails.get("P_SERVICE_MASTER_ID").toString());
		BigDecimal routingBankId = new BigDecimal(routingDetails.get("P_ROUTING_BANK_ID").toString());
		BigDecimal rountingCountryId = new BigDecimal(routingDetails.get("P_ROUTING_COUNTRY_ID").toString());
		BigDecimal remittanceMode = new BigDecimal(routingDetails.get("P_REMITTANCE_MODE_ID").toString());
		BigDecimal deliveryMode = new BigDecimal(routingDetails.get("P_DELIVERY_MODE_ID").toString());
		BigDecimal currencyId = beneficiary.getCurrencyId();
		BigDecimal countryId = beneficiary.getCountryId();
		BigDecimal applicationCountryId = meta.getCountryId();

		List<ExchangeRateApprovalDetModel> exchangeRates = exchangeRateDao.getExchangeRatesForRoutingBank(currencyId,
				meta.getCountryBranchId(), rountingCountryId, applicationCountryId, routingBankId, serviceMasterId);
		if (exchangeRates == null || exchangeRates.isEmpty()) {
			throw new GlobalException("No exchange rate found for bank- " + routingBankId,
					JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
		}
		validateNumberOfTransactionLimits();
		validateBeneficiaryTransactionLimit(beneficiary);
		List<BankServiceRule> rules = bankServiceRuleDao.getBankServiceRule(routingBankId, rountingCountryId,
				currencyId, remittanceMode, deliveryMode);
		BankServiceRule appliedRule = rules.get(0);
		List<BankCharges> charges = appliedRule.getBankCharges();
		BankCharges bankCharge = getApplicableCharge(charges);
		BigDecimal commission = bankCharge.getChargeAmount();
		ExchangeRateBreakup breakup = getExchangeRateBreakup(exchangeRates, model, commission);
		validateTransactionAmount(breakup.getConvertedLCAmount());

		if (model.isAvailLoyalityPoints()) {
			validateLoyalityPointsBalance(customer.getLoyaltyPoints());
		}

		if (new BigDecimal(94).equals(rountingCountryId) && new BigDecimal(102).equals(serviceMasterId)) {
			logger.info("recalculating comission for TT and routing countyr india");
			commission = reCalculateComission(routingDetails, breakup);
			logger.info("commission: " + commission);
			recalculateDeliveryAndRemittanceModeId(routingDetails, breakup);
		}
		if (commission == null) {
			throw new GlobalException("COMMISSION NOT DEFINED FOR Routing Bank Id:- " + routingBankId,
					JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
		} else {
			breakup = getExchangeRateBreakup(exchangeRates, model, commission);
		}
		// commission
		responseModel.setTxnFee(commission);
		// exrate
		responseModel.setExRateBreakup(breakup);
		responseModel.setTotalLoyalityPoints(customer.getLoyaltyPoints());
		responseModel
				.setMaxLoyalityPointsAvailableForTxn(loyalityPointService.getVwLoyalityEncash().getLoyalityPoint());
		addExchangeRateParameters(responseModel);
		setLoyalityPointIndicaters(responseModel);
		return responseModel;

	}

	private BigDecimal reCalculateComission(Map<String, Object> routingDetails, ExchangeRateBreakup breakup) {
		remitApplParametersMap.put("P_CALCULATED_FC_AMOUNT", breakup.getConvertedFCAmount());
		BigDecimal custtype = bizcomponentDao.findCustomerTypeId("I");
		remitApplParametersMap.put("P_CUSTYPE_ID", custtype);
		BigDecimal comission = exchangeRateProcedureDao.getCommission(remitApplParametersMap);
		if (comission == null) {
			remitApplParametersMap.put("P_CUSTYPE_ID", new BigDecimal(777));
			comission = exchangeRateProcedureDao.getCommission(remitApplParametersMap);
		}
		return comission;
	}

	private void recalculateDeliveryAndRemittanceModeId(Map<String, Object> routingDetails,
			ExchangeRateBreakup breakup) {
		if (breakup.getConvertedFCAmount() != null) {
			remitApplParametersMap.put("P_CALCULATED_FC_AMOUNT", breakup.getConvertedFCAmount());
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
						"TOO MANY COMMISSION DEFINED for rounting bankid: "
								+ remitApplParametersMap.get("P_ROUTING_BANK_ID"),
						JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
			}

			if (outputMap.get("P_DELIVERY_MODE_ID") == null) {
				throw new GlobalException("COMMISSION NOT DEFINED BankId: " + routingDetails.get("P_ROUTING_BANK_ID"),
						JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
			}
			routingDetails.putAll(outputMap);
			remitApplParametersMap.putAll(outputMap);
		}
	}

	private void setLoyalityPointIndicaters(RemittanceTransactionResponsetModel responseModel) {

		BigDecimal maxLoyalityPointRedeem = responseModel.getMaxLoyalityPointsAvailableForTxn();
		BigDecimal loyalityPointsAvailable = responseModel.getTotalLoyalityPoints();
		if (loyalityPointsAvailable.longValue() < maxLoyalityPointRedeem.longValue()) {
			responseModel.setCanRedeemLoyalityPoints(false);
		} else {
			responseModel.setCanRedeemLoyalityPoints(true);
		}
	}

	private void validateBeneficiaryTransactionLimit(BenificiaryListView beneficiary) {
		BigDecimal beneficiaryPerDayLimit = parameterService.getAuthenticationViewRepository(new BigDecimal(13))
				.getAuthLimit();
		List<ViewTransfer> transfers = transferRepo.todayTransactionCheck(beneficiary.getCustomerId(),
				beneficiary.getBankCode(), beneficiary.getBankAccountNumber(), beneficiary.getBenificaryName(),
				new BigDecimal(90));
		logger.info("in validateBeneficiaryTransactionLimit today bene with BeneficiaryRelationShipSeqId: "
				+ beneficiary.getBeneficiaryRelationShipSeqId() + " and todays tnx are: " + transfers.size());
		if (transfers != null && transfers.size() >= beneficiaryPerDayLimit.intValue()) {
			throw new GlobalException(
					"Dear Customer, you have already done 1 transaction to this beneficiary within the last "
							+ "24 hours. In the interest of safety, we do not allow a customer to repeat the same "
							+ "transaction to the same beneficiary more than once in 24 hours.",
					JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED_PER_BENE);
		}
	}

	private void validateLoyalityPointsBalance(BigDecimal availableLoyaltyPoints) {

		BigDecimal maxLoyalityPoints = loyalityPointService.getVwLoyalityEncash().getLoyalityPoint();
		BigDecimal todaysLoyalityPointsEncashed = loyalityPointService.getTodaysLoyalityPointsEncashed();
		int todaysLoyalityPointsEncashedInt = todaysLoyalityPointsEncashed == null ? 0
				: todaysLoyalityPointsEncashed.intValue();
		logger.info("Available loyalitypoint= " + availableLoyaltyPoints + " maxLoyalityPoints=" + maxLoyalityPoints
				+ " todaysLoyalityPointsEncashed=" + todaysLoyalityPointsEncashed);
		if (availableLoyaltyPoints.intValue() < maxLoyalityPoints.intValue()) {
			throw new GlobalException("Insufficient loyality points. Available points- : " + availableLoyaltyPoints,
					JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
		}
		if (availableLoyaltyPoints.intValue() - todaysLoyalityPointsEncashedInt < 0) {
			throw new GlobalException("Insufficient loyality points. Available points- : " + availableLoyaltyPoints,
					JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
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

	}

	private void addBeneficiaryParameters(BenificiaryListView beneficiary) {

		remitApplParametersMap.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneficiary.getBeneficiaryAccountSeqId());
		remitApplParametersMap.put("P_BENEFICIARY_ACCOUNT_NO", beneficiary.getBankAccountNumber());
		remitApplParametersMap.put("P_SERVICE_PROVIDER", beneficiary.getServiceProvider());
		remitApplParametersMap.put("P_FOREIGN_CURRENCY_ID", beneficiary.getCurrencyId());
		remitApplParametersMap.put("P_BENEFICARY_RELATIONSHIP_ID", beneficiary.getBeneficiaryRelationShipSeqId());

	}

	private void validateTransactionAmount(BigDecimal equivalentLCAmount) {
		AuthenticationLimitCheckView onlineTxnLimit = parameterService.getOnlineTxnLimit();
		if (equivalentLCAmount.compareTo(onlineTxnLimit.getAuthLimit()) > 0) {
			throw new GlobalException(
					"Online Transaction Amount should not exceed - KD " + onlineTxnLimit.getAuthLimit(),
					JaxError.TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED);
		}
	}

	private void validateNumberOfTransactionLimits() {
		Map<String, Integer> customerTxnAmounts = getCustomerTransactionCounts();
		List<AuthenticationLimitCheckView> txnLimits = parameterService.getAllNumberOfTxnLimits();
		for (AuthenticationLimitCheckView limitView : txnLimits) {
			Integer txnCount = customerTxnAmounts.get(limitView.getAuthorizationType());
			if (txnCount > limitView.getAuthLimit().intValue()) {
				throw new GlobalException(limitView.getAuthMessage(), JaxError.NO_OF_TRANSACTION_LIMIT_EXCEEDED);
			}
		}

	}

	private ExchangeRateBreakup getExchangeRateBreakup(List<ExchangeRateApprovalDetModel> exchangeRates,
			RemittanceTransactionRequestModel model, BigDecimal comission) {
		BigDecimal fcAmount = model.getForeignAmount();
		BigDecimal lcAmount = model.getLocalAmount();
		ExchangeRateBreakup breakup = new ExchangeRateBreakup();
		ExchangeRateApprovalDetModel exchangeRate = exchangeRates.get(0);
		BigDecimal inverseExchangeRate = exchangeRate.getSellRateMax();
		breakup.setInverseRate(inverseExchangeRate);
		breakup.setRate(new BigDecimal(1).divide(inverseExchangeRate, 10, RoundingMode.HALF_UP));

		if (fcAmount != null) {
			breakup.setConvertedLCAmount(breakup.getInverseRate().multiply(fcAmount));
			breakup.setConvertedFCAmount(fcAmount);
		}
		if (lcAmount != null) {
			breakup.setConvertedFCAmount(breakup.getRate().multiply(lcAmount));
			breakup.setConvertedLCAmount(lcAmount);
		}
		List<PipsMaster> pips = pipsDao.getPipsMasterForBranch(exchangeRate, breakup.getConvertedFCAmount());
		// apply discounts
		if (pips != null && !pips.isEmpty()) {
			PipsMaster pip = pips.get(0);
			inverseExchangeRate = inverseExchangeRate.subtract(pip.getPipsNo());
			breakup.setInverseRate(inverseExchangeRate);
			breakup.setRate(new BigDecimal(1).divide(inverseExchangeRate, 10, RoundingMode.HALF_UP));
		}
		if (fcAmount != null) {
			breakup.setConvertedLCAmount(breakup.getInverseRate().multiply(fcAmount));
			breakup.setConvertedFCAmount(fcAmount);
		}
		if (lcAmount != null) {
			breakup.setConvertedFCAmount(breakup.getRate().multiply(lcAmount));
			breakup.setConvertedLCAmount(lcAmount);
		}
		BigDecimal netAmount = breakup.getConvertedLCAmount().add(comission);
		breakup.setNetAmountWithoutLoyality(netAmount);
		if (model.isAvailLoyalityPoints()) {
			breakup.setNetAmount(netAmount.subtract(loyalityPointService.getVwLoyalityEncash().getEquivalentAmount()));
		} else {
			breakup.setNetAmount(netAmount);
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
		beneBankDetails.put("P_USER_TYPE", "I");
		beneBankDetails.put("P_BENEFICIARY_COUNTRY_ID", beneficiary.getBenificaryCountry());
		beneBankDetails.put("P_BENEFICIARY_BANK_ID", beneficiary.getBankId());
		beneBankDetails.put("P_BENEFICIARY_BRANCH_ID", beneficiary.getBranchId());
		beneBankDetails.put("P_BENEFICIARY_BANK_ACCOUNT", beneficiary.getBankAccountNumber());
		beneBankDetails.put("P_CUSTOMER_ID", meta.getCustomerId());
		beneBankDetails.put("P_SERVICE_GROUP_CODE", beneficiary.getServiceGroupCode());
		beneBankDetails.put("P_CURRENCY_ID", beneficiary.getCurrencyId());
		return beneBankDetails;
	}

	private void validateBlackListedBene(BenificiaryListView beneficiary) {
		List<BlackListModel> blist = blistDao.getBlackByName(beneficiary.getBenificaryName());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException("Beneficiary name found matching with black list ",
					JaxError.BLACK_LISTED_CUSTOMER.getCode());
		}
		if (beneficiary.getArbenificaryName() != null) {
			blist = blistDao.getBlackByName(beneficiary.getArbenificaryName());
			if (blist != null && !blist.isEmpty()) {
				throw new GlobalException("Beneficiary local name found matching with black list ",
						JaxError.BLACK_LISTED_CUSTOMER.getCode());
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
		output.put("13", dailyCount);
		output.put("12", monthlyCount);
		output.put("11", weeklyCount);
		return output;
	}

	public RemittanceApplicationResponseModel saveApplication(RemittanceTransactionRequestModel model) {
		RemittanceTransactionResponsetModel validationResults = this.validateTransactionData(model);
		ExchangeRateBreakup breakup = validationResults.getExRateBreakup();
		BigDecimal netAmountPayable = breakup.getNetAmount();
		RemittanceApplicationResponseModel remiteAppModel = new RemittanceApplicationResponseModel();
		deactivatePreviousApplications();
		validateAdditionalCheck();
		validateAdditionalBeneDetails();
		RemittanceApplication remittanceApplication = remitAppManager.createRemittanceApplication(model,
				validatedObjects, validationResults, remitApplParametersMap);

		RemittanceAppBenificiary remittanceAppBeneficairy = remitAppBeneManager
				.createRemittanceAppBeneficiary(remittanceApplication);
		List<AdditionalInstructionData> additionalInstrumentData = remittanceAppAddlDataManager
				.createAdditionalInstnData(remittanceApplication);
		remitAppDao.saveAllApplicationData(remittanceApplication, remittanceAppBeneficairy, additionalInstrumentData);
		remiteAppModel.setRemittanceAppId(remittanceApplication.getRemittanceApplicationId());
		remiteAppModel.setNetPayableAmount(netAmountPayable);
		remiteAppModel.setDocumentIdForPayment(remittanceApplication.getPaymentId());
		remiteAppModel.setDocumentFinancialYear(remittanceApplication.getDocumentFinancialyear());
		remiteAppModel.setMerchantTrackId(meta.getCustomerId());
		remiteAppModel.setDocumentIdForPayment(remittanceApplication.getDocumentNo().toString());
		logger.info("Application saved successfully, response: " + remiteAppModel.toString());
		return remiteAppModel;

	}

	private void deactivatePreviousApplications() {
		BigDecimal customerId = meta.getCustomerId();
		remittanceApplicationService.deActivateApplication(customerId);
	}

	private void validateAdditionalBeneDetails() {
		Map<String, Object> output = applicationProcedureDao
				.toFetchDetilaFromAddtionalBenficiaryDetails(remitApplParametersMap);
		remitApplParametersMap.putAll(output);
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
		if (remittanceTransaction != null) {
			BigDecimal cutomerReference = remittanceTransaction.getCustomerId();
			BigDecimal remittancedocfyr = remittanceTransaction.getDocumentFinancialyear();
			BigDecimal remittancedocNumber = remittanceTransaction.getDocumentNo();
			TransactionHistroyDTO transactionHistoryDto = transactionHistroyService
					.getTransactionHistoryDto(cutomerReference, remittancedocfyr, remittancedocNumber);
			model.setTransactionHistroyDTO(transactionHistoryDto);
		}
		model.setNetAmount(application.getLocalNetTranxAmount());
		JaxTransactionStatus status = getJaxTransactionStatus(application);
		model.setStatus(status);
		return model;
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
		if ("CANCELED".equalsIgnoreCase(resultCode)) {
			status = JaxTransactionStatus.PAYMENT_CANCELED_BY_USER;
		}

		return status;
	}

}
