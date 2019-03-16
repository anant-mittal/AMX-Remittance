package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExchangeRateProcedureDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateProcedureDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> findRemittanceAndDevlieryModeId(Map<String, Object> inputMap) {
		LOGGER.info("in findRemittanceAndDevlieryModeId, input mpa:  " + inputMap.toString());
		String sql = "SELECT DISTINCT A.REMITTANCE_MODE_ID," + "                A.DELIVERY_MODE_ID "
				+ " FROM V_EX_ROUTING_DETAILS A," + "     EX_BANK_SERVICE_RULE B," + "     EX_BANK_CHARGES C"
				+ " WHERE A.APPLICATION_COUNTRY_ID = ?" + "  AND A.COUNTRY_ID = ?" + "  AND A.BENE_BANK_ID = ?"
				+ "  AND A.BENE_BANK_BRANCH_ID = ?" + "  AND A.ROUTING_COUNTRY_ID = ?" + "  AND A.ROUTING_BANK_ID = ?"
				+ "  AND A.BANK_BRANCH_ID = ?" + "  AND A.CURRENCY_ID = ?" + "  AND A.SERVICE_MASTER_ID = ?"
				+ "  AND A.APPLICATION_COUNTRY_ID = B.APPLICATION_COUNTRY_ID"
				+ "  AND A.ROUTING_COUNTRY_ID = B.COUNTRY_ID" + "  AND A.CURRENCY_ID = B.CURRENCY_ID"
				+ "  AND A.ROUTING_BANK_ID = B.BANK_ID" + "  AND A.REMITTANCE_MODE_ID = B.REMITTANCE_MODE_ID"
				+ "  AND A.DELIVERY_MODE_ID = B.DELIVERY_MODE_ID" + "  AND B.APPROVED_BY IS NOT NULL"
				+ "  AND B.BANK_SERVICE_RULE_ID = C.BANK_SERVICE_RULE_ID" + "  AND C.CHARGES_TYPE = 'C'"
				+ "  AND A.REMITTANCE_MODE_ID  <> (select REMITTANCE_MODE_ID from ex_remittance_mode where REMITTANCE_CODE=13)    " /** Added by Rabil for IMPS **/
				+ "  AND C.CHARGES_FOR = ?" + "" + "  AND ? BETWEEN C.FROM_AMOUNT AND C.TO_AMOUNT";
		
		
		List<BigDecimal> inputList = new ArrayList<>();
		inputList.add((BigDecimal) inputMap.get("P_APPLICATION_COUNTRY_ID"));
		inputList.add((BigDecimal) inputMap.get("P_BENEFICIARY_COUNTRY_ID"));
		inputList.add((BigDecimal) inputMap.get("P_BENEFICIARY_BANK_ID"));
		inputList.add((BigDecimal) inputMap.get("P_BENEFICIARY_BRANCH_ID"));
		inputList.add((BigDecimal) inputMap.get("P_ROUTING_COUNTRY_ID"));
		inputList.add((BigDecimal) inputMap.get("P_ROUTING_BANK_ID"));
		inputList.add((BigDecimal) inputMap.get("P_ROUTING_BANK_BRANCH_ID"));
		inputList.add((BigDecimal) inputMap.get("P_FOREIGN_CURRENCY_ID"));
		inputList.add((BigDecimal) inputMap.get("P_SERVICE_MASTER_ID"));
		inputList.add((BigDecimal) inputMap.get("P_CUSTYPE_ID"));
		inputList.add((BigDecimal) inputMap.get("P_CALCULATED_FC_AMOUNT"));
		Map<String, Object> output = new HashMap<>();
		try {
			LOGGER.info("SQL  findRemittanceAndDevlieryModeId : " +sql+"\n inputList.toArray() :"+inputList.toArray());
			Map<String, Object> outputMap = jdbcTemplate.queryForMap(sql, inputList.toArray());
			output.put("P_REMITTANCE_MODE_ID", outputMap.get("REMITTANCE_MODE_ID"));
			output.put("P_DELIVERY_MODE_ID", outputMap.get("DELIVERY_MODE_ID"));
		} catch (Exception e) {
			LOGGER.info("error in findRemittanceAndDevlieryModeId : " +e);
		}
		return output;

	}

	public BigDecimal getCommission(Map<String, Object> inputMap) {

		String sql = " SELECT B.CHARGE_AMOUNT" + "        FROM   EX_BANK_SERVICE_RULE A,"
				+ "               EX_BANK_CHARGES B"
				+ "        WHERE  A.BANK_SERVICE_RULE_ID  =   B.BANK_SERVICE_RULE_ID"
				+ "        AND    A.COUNTRY_ID            =   ?" + "        AND    A.CURRENCY_ID           =   ?"
				+ "        AND    A.BANK_ID               =   ?" + "        AND    A.REMITTANCE_MODE_ID    =   ?"
				+ "        AND    A.DELIVERY_MODE_ID      =   ?" + "        AND    B.CHARGES_FOR           =   ?"
				+ "        AND    ? BETWEEN B.FROM_AMOUNT AND B.TO_AMOUNT"
				+ "        AND    A.ISACTIVE              =   'Y'" + "        AND    B.ISACTIVE              =   'Y'"
				+ " 	   AND A.REMITTANCE_MODE_ID  <> (select REMITTANCE_MODE_ID from ex_remittance_mode where REMITTANCE_CODE=13)    " /** Added by Rabil for IMPS **/
				+ "        AND    B.CHARGES_TYPE          =   'C'";
		List<BigDecimal> inputList = new ArrayList<>();
		inputList.add((BigDecimal) inputMap.get("P_ROUTING_COUNTRY_ID"));
		inputList.add((BigDecimal) inputMap.get("P_FOREIGN_CURRENCY_ID"));
		inputList.add((BigDecimal) inputMap.get("P_ROUTING_BANK_ID"));
		inputList.add((BigDecimal) inputMap.get("P_REMITTANCE_MODE_ID"));
		inputList.add((BigDecimal) inputMap.get("P_DELIVERY_MODE_ID"));
		inputList.add((BigDecimal) inputMap.get("P_CUSTYPE_ID"));
		inputList.add((BigDecimal) inputMap.get("P_CALCULATED_FC_AMOUNT"));
		BigDecimal comission = null;
		try {
			comission = jdbcTemplate.queryForObject(sql, inputList.toArray(), BigDecimal.class);
		} catch (Exception e) {
			LOGGER.info("error in getCommission : {}", e.getMessage());
		}

		return comission;

	}

	public List<BigDecimal> getBankIdsForExchangeRates(BigDecimal currencyid) {

		String sql = "select distinct(BANK_ID)  from VW_EX_TRATE where CURRENCY_ID=? ";
		List<BigDecimal> list = new ArrayList<>();
		try {
			list = jdbcTemplate.queryForList(sql, BigDecimal.class, currencyid);
		} catch (Exception e) {
			LOGGER.info("error in getBankIdsForExchangeRates : ", e);
		}

		return list;

	}
	
	
	public List<BigDecimal> getDistinctCurrencyList() {
		String sql = "select DISTINCT(CURRENCY_ID) from VW_EX_TRATE where BANK_ID IS NOT NULL";
		List<BigDecimal> list = new ArrayList<>();
		try {
			list = jdbcTemplate.queryForList(sql, BigDecimal.class);
		} catch (Exception e) {
			LOGGER.info("error in getDistinctCurrencyList : ", e);
		}
		return list;
	}
	
	public Map<String, Object> getCommissionRange(Map<String, Object> inputMap) {

		String sql = " SELECT MIN(B.FROM_AMOUNT) FROM_AMOUNT ,MAX(B.TO_AMOUNT) TO_AMOUNT " + "        FROM   EX_BANK_SERVICE_RULE A,"
				+ "               EX_BANK_CHARGES B"
				+ "        WHERE  A.BANK_SERVICE_RULE_ID  =   B.BANK_SERVICE_RULE_ID"
				+ "        AND    A.COUNTRY_ID            =   ?" + "        AND    A.CURRENCY_ID           =   ?"
				+ "        AND    A.BANK_ID               =   ?" + "        AND    A.REMITTANCE_MODE_ID    =   ?"
				+ "        AND    A.DELIVERY_MODE_ID      =   ?" + "        AND    B.CHARGES_FOR           =   ?"
				+ "        AND    A.ISACTIVE              =   'Y'" + "        AND    B.ISACTIVE            =  'Y'"
				+ " 	   AND    A.REMITTANCE_MODE_ID  <> (select REMITTANCE_MODE_ID from ex_remittance_mode where REMITTANCE_CODE=13)    " /** Added by Rabil for IMPS **/
				+ "        AND    B.CHARGES_TYPE          =   'C'";
		List<BigDecimal> inputList = new ArrayList<>();
		inputList.add((BigDecimal) inputMap.get("P_ROUTING_COUNTRY_ID"));
		inputList.add((BigDecimal) inputMap.get("P_FOREIGN_CURRENCY_ID"));
		inputList.add((BigDecimal) inputMap.get("P_ROUTING_BANK_ID"));
		inputList.add((BigDecimal) inputMap.get("P_REMITTANCE_MODE_ID"));
		inputList.add((BigDecimal) inputMap.get("P_DELIVERY_MODE_ID"));
		inputList.add((BigDecimal) inputMap.get("P_CUSTYPE_ID"));
		Map<String, Object> output = new HashMap<>();
		try {
			LOGGER.info("getCommissionRange INPUT VALUE : "+inputList.toString());
			Map<String, Object> outputMap = jdbcTemplate.queryForMap(sql, inputList.toArray());
			output.put("FROM_AMOUNT", outputMap.get("FROM_AMOUNT"));
			output.put("TO_AMOUNT", outputMap.get("TO_AMOUNT"));
			LOGGER.info("getCommissionRange OUTPUT VALUE : "+output.toString());
		} catch (Exception e) {
			LOGGER.info("error in getCommission : ", e);
		}
		return output;
	}

}
