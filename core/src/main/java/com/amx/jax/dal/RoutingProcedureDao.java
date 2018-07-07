package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoutingProcedureDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCoreProcedureDao.class);

	public BigDecimal getRoutingBankBranchIdForCash(Map<String, Object> inputValues) {
		BigDecimal routingBankBranchId = null;

		String branchApplicability = getBranchApplicability(inputValues);
		if (ConstantDocument.CONSTANT_ALL.equals(branchApplicability)) {
			routingBankBranchId = getRoutingBankBranchIdFromDb(inputValues);
		}

		return routingBankBranchId;

	}

	private BigDecimal getRoutingBankBranchIdFromDb(Map<String, Object> inputValues) {
		LOGGER.info("in getRoutingBankBranchIdFromDb,input values: {}", inputValues);
		String sql = "SELECT DISTINCT F.BANK_BRANCH_ID  FROM  EX_ROUTING_DETAILS F "
				+ " WHERE  F.APPLICATION_COUNTRY_ID = ? AND    F.COUNTRY_ID = ? AND    F.CURRENCY_ID  =  ?"
				+ " AND    F.SERVICE_MASTER_ID  = ? AND    F.ROUTING_COUNTRY_ID = ?"
				+ " AND    F.ROUTING_BANK_ID = ? AND F.ISACTIVE = ?";

		List<Object> inputList = new ArrayList<>();
		inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_COUNTRY_ID"));
		inputList.add(inputValues.get("P_CURRENCY_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_ROUTING_COUNTRY_ID"));
		inputList.add(inputValues.get("P_ROUTING_BANK_ID"));
		inputList.add(ConstantDocument.Yes);
		List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
		LOGGER.info("in getRoutingBankBranchIdFromDb,output values: {}", outputList);
		Iterator<Map<String, Object>> itr = outputList.iterator();
		BigDecimal branchid = null;
		while (itr.hasNext()) {
			branchid = (BigDecimal) itr.next().get("BANK_BRANCH_ID");
		}
		return branchid;

	}

	private String getBranchApplicability(Map<String, Object> inputValues) {
		LOGGER.info("in getBranchApplicability,input values: {}", inputValues);
		String sql = "SELECT DISTINCT F.BRANCH_APPLICABILITY  FROM  EX_ROUTING_DETAILS F "
				+ " WHERE  F.APPLICATION_COUNTRY_ID = ? AND    F.COUNTRY_ID = ? AND    F.CURRENCY_ID  =  ?"
				+ " AND    F.SERVICE_MASTER_ID  = ? AND    F.ROUTING_COUNTRY_ID = ?"
				+ " AND    F.ROUTING_BANK_ID = ? AND F.ISACTIVE = ?";

		List<Object> inputList = new ArrayList<>();
		inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_COUNTRY_ID"));
		inputList.add(inputValues.get("P_CURRENCY_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_ROUTING_COUNTRY_ID"));
		inputList.add(inputValues.get("P_ROUTING_BANK_ID"));
		inputList.add(ConstantDocument.Yes);
		List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
		LOGGER.info("in getBranchApplicability,output values: {}", outputList);
		Iterator<Map<String, Object>> itr = outputList.iterator();
		String branchApplicability = null;
		while (itr.hasNext()) {
			branchApplicability = (String) itr.next().get("BRANCH_APPLICABILITY");
		}
		return branchApplicability;
	}

	public BigDecimal getRemittanceModeIdForCash(Map<String, Object> inputValues) {

		LOGGER.info("in getRemittanceModeIdForCash,input values: {}", inputValues);
		String sql = "SELECT REMITTANCE_MODE_ID FROM ( SELECT DISTINCT F.REMITTANCE_MODE_ID "
				+ " FROM   V_EX_ROUTING_DETAILS F  WHERE  F.APPLICATION_COUNTRY_ID= ?"
				+ " AND    F.BENE_BANK_ID =  ? AND    F.BENE_BANK_BRANCH_ID=?  AND    F.COUNTRY_ID = ?"
				+ " AND    F.CURRENCY_ID  =  ? AND    F.SERVICE_MASTER_ID  = ?"
				+ " AND    F.ROUTING_BANK_ID   =  DECODE( ?, 101, ? ,F.ROUTING_BANK_ID)"
				+ " AND    F.ROUTING_COUNTRY_ID = ? AND    F.ROUTING_BANK_ID = ? AND F.BANK_BRANCH_ID = ? )";

		List<Object> inputList = new ArrayList<>();
		inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_BANK_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_BRANCH_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_COUNTRY_ID"));
		inputList.add(inputValues.get("P_CURRENCY_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_BANK_ID"));
		inputList.add(inputValues.get("P_ROUTING_COUNTRY_ID"));
		inputList.add(inputValues.get("P_ROUTING_BANK_ID"));
		inputList.add(inputValues.get("P_ROUTING_BANK_BRANCH_ID"));
		List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
		LOGGER.info("in getRemittanceModeIdForCash,output values: {}", outputList);
		Iterator<Map<String, Object>> itr = outputList.iterator();
		BigDecimal remittanceModeId = null;
		while (itr.hasNext()) {
			remittanceModeId = (BigDecimal) itr.next().get("REMITTANCE_MODE_ID");
		}
		return remittanceModeId;
	}

	public Object getDeliveryModeIdForCash(Map<String, Object> inputValues) {

		LOGGER.info("in getDeliveryModeIdForCash,input values: {}", inputValues);
		String sql = "SELECT DELIVERY_MODE_ID FROM ( SELECT DISTINCT F.DELIVERY_MODE_ID "
				+ " FROM   V_EX_ROUTING_DETAILS F  WHERE  F.APPLICATION_COUNTRY_ID= ?"
				+ " AND    F.BENE_BANK_ID =  ? AND    F.BENE_BANK_BRANCH_ID= ? AND    F.COUNTRY_ID = ?"
				+ " AND    F.CURRENCY_ID  =  ? AND    F.SERVICE_MASTER_ID  = ?"
				+ " AND    F.ROUTING_BANK_ID   =  DECODE( ?,101,?,F.ROUTING_BANK_ID)"
				+ " AND    F.ROUTING_COUNTRY_ID =?  AND    F.ROUTING_BANK_ID =? "
				+ " AND F.BANK_BRANCH_ID =?  AND    F.REMITTANCE_MODE_ID = ? )";

		List<Object> inputList = new ArrayList<>();
		inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_BANK_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_BRANCH_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_COUNTRY_ID"));
		inputList.add(inputValues.get("P_CURRENCY_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_BENEFICIARY_BANK_ID"));
		inputList.add(inputValues.get("P_ROUTING_COUNTRY_ID"));
		inputList.add(inputValues.get("P_ROUTING_BANK_ID"));
		inputList.add(inputValues.get("P_ROUTING_BANK_BRANCH_ID"));
		inputList.add(inputValues.get("P_REMITTANCE_MODE_ID"));

		List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
		LOGGER.info("in getDeliveryModeIdForCash,output values: {}", outputList);
		Iterator<Map<String, Object>> itr = outputList.iterator();
		BigDecimal deliveryModeId = null;
		while (itr.hasNext()) {
			deliveryModeId = (BigDecimal) itr.next().get("DELIVERY_MODE_ID");
		}
		return deliveryModeId;
	}

}
