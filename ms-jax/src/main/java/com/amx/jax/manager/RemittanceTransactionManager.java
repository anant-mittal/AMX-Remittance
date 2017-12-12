package com.amx.jax.manager;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dao.BankServiceRuleDao;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.service.BankMasterService;
import com.amx.jax.util.DateUtil;

@Component
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
	private BankMasterService bankMasterService;

	private Logger logger = Logger.getLogger(RemittanceTransactionManager.class);

	public Object validateTransactionData(RemittanceTransactionRequestModel model) {

		BigDecimal beneId = model.getBeneId();
		BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(beneId);
		validateBlackListedBene(beneficiary);
		HashMap<String, String> beneBankDetails = getBeneBankDetails(beneficiary);
		Map<String, Object> routingDetails = this.getRoutingDetails(beneBankDetails);
		BigDecimal routingBankId = new BigDecimal(routingDetails.get("3").toString());
		BigDecimal rountingBankbranchId = new BigDecimal(routingDetails.get("4").toString());
		BigDecimal remittanceMode = new BigDecimal(routingDetails.get("5").toString());
		BigDecimal deliveryMode = new BigDecimal(routingDetails.get("6").toString());
//		bankServiceRuleDao.getBankServiceRule(routingBankId, beneficiary.getCountryId(), beneficiary.getCurrencyId(),
//				remittanceMode, deliveryMode);
		return model;

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
		blist = blistDao.getBlackByName(beneficiary.getArbenificaryName());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException("Beneficiary local name found matching with black list ",
					JaxError.BLACK_LISTED_CUSTOMER.getCode());
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
		for (int i = 1; i <= 8; i++) {
			ouptutParams.add(new SqlOutParameter(i + "", Types.BIGINT));
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

}
