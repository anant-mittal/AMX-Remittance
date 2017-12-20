package com.amx.jax.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.jax.dao.BankServiceRuleDao;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.BankCharges;
import com.amx.jax.dbmodel.BankServiceRule;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.dbmodel.remittance.ViewTransfer;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.exrateservice.dao.ExchangeRateDao;
import com.amx.jax.exrateservice.dao.PipsMasterDao;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.VTransferRepository;
import com.amx.jax.service.ParameterService;
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
	private BankServiceRuleDao bankServiceRuleDao;

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

	protected Map<String, Object> validatedObjects = new HashMap<>();

	private Logger logger = LoggerFactory.getLogger(getClass());

	public RemittanceTransactionResponsetModel validateTransactionData(RemittanceTransactionRequestModel model) {

		BigDecimal beneId = model.getBeneId();
		Customer customer = custDao.getCustById(model.getCustomerId());
		validatedObjects.put("CUSTOMER", customer);
		RemittanceTransactionResponsetModel responseModel = new RemittanceTransactionResponsetModel();
		BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(beneId);
		validateBlackListedBene(beneficiary);
		validatedObjects.put("BENEFICIARY", beneficiary);
		HashMap<String, String> beneBankDetails = getBeneBankDetails(beneficiary);
		Map<String, Object> routingDetails = this.getRoutingDetails(beneBankDetails);
		validatedObjects.put("ROUTINGDETAILS", routingDetails);
		BigDecimal serviceMasterId = new BigDecimal(routingDetails.get("P_SERVICE_MASTER_ID").toString());
		BigDecimal routingBankId = new BigDecimal(routingDetails.get("P_ROUTING_BANK_ID").toString());
		BigDecimal rountingBankbranchId = new BigDecimal(routingDetails.get("P_ROUTING_BANK_BRANCH_ID").toString());
		BigDecimal remittanceMode = new BigDecimal(routingDetails.get("P_REMITTANCE_MODE_ID").toString());
		BigDecimal deliveryMode = new BigDecimal(routingDetails.get("P_DELIVERY_MODE_ID").toString());
		BigDecimal currencyId = beneficiary.getCurrencyId();
		BigDecimal countryId = beneficiary.getCountryId();
		BigDecimal applicationCountryId = meta.getCountryId();
		List<BankServiceRule> rules = bankServiceRuleDao.getBankServiceRule(routingBankId, countryId, currencyId,
				remittanceMode, deliveryMode);
		if (rules == null || rules.isEmpty()) {
			throw new GlobalException("Routing Rules not defined for Routing Bank Id:- " + routingBankId,
					JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
		}
		BankServiceRule appliedRule = rules.get(0);
		List<BankCharges> charges = appliedRule.getBankCharges();
		BankCharges bankCharge = getApplicableCharge(charges);
		if (bankCharge == null) {
			throw new GlobalException("Routing Bank Charges not defined for Routing Bank Id:- " + routingBankId,
					JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
		}
		BigDecimal comission = bankCharge.getChargeAmount();

		// commission
		responseModel.setTxnFee(comission);
		List<ExchangeRateApprovalDetModel> exchangeRates = exchangeRateDao.getExchangeRatesForRoutingBank(currencyId,
				meta.getCountryBranchId(), countryId, applicationCountryId, routingBankId, serviceMasterId);
		if (exchangeRates == null || exchangeRates.isEmpty()) {
			throw new GlobalException("No exchange rate found for bank- " + routingBankId,
					JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
		}
		validateNumberOfTransactionLimits();
		ExchangeRateBreakup breakup = getExchangeRateBreakup(exchangeRates, model);
		validateTransactionAmount(breakup.getConvertedLCAmount());
		// exrate
		responseModel.setExRateBreakup(breakup);
		responseModel.setTotalLoyalityPoints(customer.getLoyaltyPoints());
		responseModel.setMaxLoyalityPointsAvailableForTxn(new BigDecimal(1000));
		return responseModel;

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
		Map<String, Integer> customerTxnAmounts = getCustomerTransactionAmounts();
		List<AuthenticationLimitCheckView> txnLimits = parameterService.getAllNumberOfTxnLimits();
		for (AuthenticationLimitCheckView limitView : txnLimits) {
			Integer txnCount = customerTxnAmounts.get(limitView.getAuthorizationType());
			if (txnCount > limitView.getAuthLimit().intValue()) {
				throw new GlobalException(limitView.getAuthMessage(), JaxError.NO_OF_TRANSACTION_LIMIT_EXCEEDED);
			}
		}
	}

	private ExchangeRateBreakup getExchangeRateBreakup(List<ExchangeRateApprovalDetModel> exchangeRates,
			RemittanceTransactionRequestModel model) {
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

	private HashMap<String, String> getBeneBankDetails(BenificiaryListView beneficiary) {

		HashMap<String, String> beneBankDetails = new HashMap<>();
		beneBankDetails.put("P_APPLICATION_COUNTRY_ID", meta.getCountryId().toString());
		beneBankDetails.put("P_USER_TYPE", "I");
		beneBankDetails.put("P_BENE_COUNTRY_ID", beneficiary.getBenificaryCountry().toString());
		beneBankDetails.put("P_BENE_BANK_ID", beneficiary.getBankId().toString());
		beneBankDetails.put("P_BENE_BANK_BRANCH_ID", beneficiary.getBranchId().toString());
		beneBankDetails.put("P_BENE_BANK_ACCOUNT", beneficiary.getBankAccountNumber().toString());
		beneBankDetails.put("P_CUSTOMER_ID", beneficiary.getCustomerId().toString());
		beneBankDetails.put("P_SERVICE_GROUP_CODE", beneficiary.getServiceGroupCode().toString());
		beneBankDetails.put("P_CURRENCY_ID", beneficiary.getCurrencyId().toString());
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

	@Transactional
	public Map<String, Object> getRoutingDetails(HashMap<String, String> inputValue) {

		logger.info("In getRoutingDetails params:" + inputValue.toString());

		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT),
				new SqlParameter(Types.VARCHAR), new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
				new SqlParameter(Types.BIGINT), new SqlParameter(Types.VARCHAR), new SqlParameter(Types.BIGINT),
				new SqlParameter(Types.VARCHAR), new SqlParameter(Types.BIGINT));
		List<SqlParameter> ouptutParams = new ArrayList<>();
		ouptutParams.addAll(declareInAndOutputParameters);
		String[] outParams = { "P_SERVICE_MASTER_ID", "P_ROUTING_COUNTRY_ID", "P_ROUTING_BANK_ID",
				"P_ROUTING_BANK_BRANCH_ID", "P_REMITTANCE_MODE_ID", "P_DELIVERY_MODE_ID", "P_SWIFT",
				"P_ERROR_MESSAGE" };
		for (int i = 1; i <= 8; i++) {
			ouptutParams.add(new SqlOutParameter(outParams[i - 1], Types.BIGINT));
		}

		Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				String proc = " { call EX_GET_ROUTING_SET_UP_OTH (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) } ";
				CallableStatement cs = con.prepareCall(proc);
				// In Parameters
				cs.setBigDecimal(1, new BigDecimal(inputValue.get("P_APPLICATION_COUNTRY_ID")));
				cs.setString(2, inputValue.get("P_USER_TYPE"));
				cs.setBigDecimal(3, new BigDecimal(inputValue.get("P_BENE_COUNTRY_ID")));
				cs.setBigDecimal(4, new BigDecimal(inputValue.get("P_BENE_BANK_ID")));
				cs.setBigDecimal(5, new BigDecimal(inputValue.get("P_BENE_BANK_BRANCH_ID")));
				cs.setString(6, inputValue.get("P_BENE_BANK_ACCOUNT"));
				cs.setBigDecimal(7, new BigDecimal(inputValue.get("P_CUSTOMER_ID")));
				cs.setString(8, inputValue.get("P_SERVICE_GROUP_CODE"));
				cs.setBigDecimal(9, new BigDecimal(inputValue.get("P_CURRENCY_ID"))); // Out
				// Parameters
				cs.registerOutParameter(10, java.sql.Types.INTEGER);
				cs.registerOutParameter(11, java.sql.Types.INTEGER);
				cs.registerOutParameter(12, java.sql.Types.INTEGER);
				cs.registerOutParameter(13, java.sql.Types.INTEGER);
				cs.registerOutParameter(14, java.sql.Types.INTEGER);
				cs.registerOutParameter(15, java.sql.Types.INTEGER);
				cs.registerOutParameter(16, java.sql.Types.VARCHAR);
				cs.registerOutParameter(17, java.sql.Types.VARCHAR);
				cs.execute();
				return cs;
			}

		}, ouptutParams);

		logger.info("Out put Parameters :" + output.toString());

		return output;
	}

	/**
	 * Returns customer's transaction amounts in 3 forms 1. daily 2. weekly 3.
	 * monthly
	 */
	public Map<String, Integer> getCustomerTransactionAmounts() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Integer> output = new HashMap<>();
		Customer customer = custDao.getCustById(meta.getCustomerId());
		List<ViewTransfer> monthlyTxns = transferRepo.findBycusRef(customer.getCustomerReference());
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
		remitAppManager.createRemittanceApplication(model,validatedObjects, validationResults);
		return null;

	}
}
