package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateProcedureDao {

	private Logger logger = Logger.getLogger(ExchangeRateProcedureDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public Map<String, Object> findRemittanceAndDevlieryModeId(Map<String, Object> inputMap) {
		logger.info("in findRemittanceAndDevlieryModeId, input mpa:  " + inputMap.toString());
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
			Map<String, Object> outputMap = jdbcTemplate.queryForMap(sql, inputList.toArray());
			output.put("P_REMITTANCE_MODE_ID", outputMap.get("REMITTANCE_MODE_ID"));
			output.put("P_DELIVERY_MODE_ID", outputMap.get("DELIVERY_MODE_ID"));
		} catch (Exception e) {
			logger.info("error in findRemittanceAndDevlieryModeId " + e.getMessage());
		}
		return output;

	}

	@Transactional
	public BigDecimal getCommission(Map<String, Object> inputMap) {

		String sql = " SELECT B.CHARGE_AMOUNT" + "        FROM   EX_BANK_SERVICE_RULE A,"
				+ "               EX_BANK_CHARGES B"
				+ "        WHERE  A.BANK_SERVICE_RULE_ID  =   B.BANK_SERVICE_RULE_ID"
				+ "        AND    A.COUNTRY_ID            =   ?" + "        AND    A.CURRENCY_ID           =   ?"
				+ "        AND    A.BANK_ID               =   ?" + "        AND    A.REMITTANCE_MODE_ID    =   ?"
				+ "        AND    A.DELIVERY_MODE_ID      =   ?" + "        AND    B.CHARGES_FOR           =   ?"
				+ "        AND    ? BETWEEN B.FROM_AMOUNT AND B.TO_AMOUNT"
				+ "        AND    A.ISACTIVE              =   'Y'" + "        AND    B.ISACTIVE              =   'Y'"
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
			logger.info("error in getCommission", e);
		}

		return comission;

	}
}