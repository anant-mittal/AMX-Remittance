package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
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
				+ " 	   AND A.REMITTANCE_MODE_ID  <> (select REMITTANCE_MODE_ID from ex_remittance_mode where REMITTANCE_CODE=13)    " /**
																																			 * Added
																																			 * by
																																			 * Rabil
																																			 * for
																																			 * IMPS
																																			 **/
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

	@Transactional
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

	@Transactional
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

}
