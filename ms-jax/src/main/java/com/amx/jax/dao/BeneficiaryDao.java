package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.SwiftMasterView;
import com.amx.jax.repository.ISwiftMasterDao;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BeneficiaryDao {

	@Autowired
	ISwiftMasterDao swiftMasterRepo;

	public SwiftMasterView getSwiftMasterBySwiftBic(String swiftBic) {
		return swiftMasterRepo.getSwiftMasterDetails(swiftBic).get(0);
	}

	private static final Logger LOGGER = Logger.getLogger(BeneficiaryDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<BigDecimal> getRoutingBankMasterList() {
		String sql = "select distinct(COUNTRY_ID) from V_EX_ROUTING_AGENTS where SERVICE_GROUP_ID = "
				+ "(select service_group_id from ex_service_group where service_group_code='C' and " + "isactive='Y')";
		List<BigDecimal> outputList = new ArrayList<>();
		try {
			outputList = jdbcTemplate.queryForList(sql, BigDecimal.class);
		} catch (Exception e) {
			LOGGER.info("error in getRoutingBankMasterList : ", e);
		}
		return outputList;
	}

	/**
	 * this method fetch beneficiary details to old emos
	 * 
	 * @param inputValues
	 * @return output map
	 * 
	 */
	public Map<String, Object> populateBeneDt(Map<String, Object> inputValues) {
		BigDecimal beneMasterId = (BigDecimal) inputValues.get("P_BENE_MASTER_ID");
		BigDecimal beneBankId = (BigDecimal) inputValues.get("P_BANK_ID");
		BigDecimal beneBankBranchId = (BigDecimal) inputValues.get("P_BANK_BRANCH_ID");
		BigDecimal beneAccountSeqId = (BigDecimal) inputValues.get("P_BENEFICARY_ACCOUNT_SEQ_ID");
		BigDecimal currencyId = (BigDecimal) inputValues.get("P_CURRENCY_ID");
		BigDecimal customerId = (BigDecimal) inputValues.get("P_CUSTOMER_ID");

		LOGGER.info("populateBeneDt EX_POPULATE_BENE_DT input:" + inputValues.toString());

		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
					new SqlParameter(Types.NUMERIC), // 2
					new SqlParameter(Types.NUMERIC), // 3
					new SqlParameter(Types.NUMERIC), // 4
					new SqlParameter(Types.NUMERIC), // 5
					new SqlParameter(Types.NUMERIC), // 6
					new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR)); // 7
			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_POPULATE_BENE_DT(?,?,?,?,?,?,?)} ";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, beneMasterId);
					cs.setBigDecimal(2, beneBankId);
					cs.setBigDecimal(3, beneBankBranchId);
					cs.setBigDecimal(4, beneAccountSeqId);
					cs.setBigDecimal(5, currencyId);
					cs.setBigDecimal(6, customerId);
					cs.registerOutParameter(7, java.sql.Types.VARCHAR);
					cs.execute();
					return cs;
				}

			}, declareInAndOutputParameters);
			if (output.get("P_ERROR_MESSAGE") != null) {
				throw new GlobalException(output.toString(), "Failed to populate EMOS data, {}");
			}
			LOGGER.info("EX_POPULATE_BENE_DT Out put Parameters :" + output.toString());
		} catch (DataAccessException e) {
			LOGGER.info("error in ", e);
		}
		return output;
	}

}
