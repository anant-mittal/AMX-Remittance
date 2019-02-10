package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

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
	
	/** added
	 * purpos : to get the service  deatails list.
	 * 
	 **/
	
	
	public List<Map<String, Object>> getServiceList(Map<String, Object> inputValues){
		

		LOGGER.info("in getDeliveryModeIdForCash,input values: {}", inputValues);
		
		String sql = "SELECT DISTINCT f.SERVICE_CODE,F.SERVICE_DESCRIPTION,  F.SERVICE_MASTER_ID "
					+ " FROM   V_EX_ROUTING_DETAILS F " 
					+ " WHERE  BENE_BANK_ID = ?"
					+ " AND    BENE_BANK_BRANCH_ID = ?  AND    F.COUNTRY_ID =?"
					+ " AND    F.CURRENCY_ID  = ? AND    F.SERVICE_GROUP_CODE  = ?"
					+ " AND    APPLICATION_COUNTRY_ID=? ";
		 	List<Object> inputList = new ArrayList<>();
			inputList.add(inputValues.get("P_BENE_BANK_ID")==null?BigDecimal.ZERO:(BigDecimal)inputValues.get("P_BENE_BANK_ID"));
			inputList.add(inputValues.get("P_BENE_BANK_BRANCH_ID")==null?BigDecimal.ZERO:(BigDecimal)inputValues.get("P_BENE_BANK_BRANCH_ID"));
			inputList.add(inputValues.get("P_BENE_COUNTRY_ID")==null?BigDecimal.ZERO:(BigDecimal)inputValues.get("P_BENE_COUNTRY_ID"));
			inputList.add(inputValues.get("P_CURRENCY_ID")==null?BigDecimal.ZERO:(BigDecimal)inputValues.get("P_CURRENCY_ID"));
			inputList.add(inputValues.get("P_SERVICE_GROUP_CODE")==null?"":inputValues.get("P_SERVICE_GROUP_CODE").toString());
			inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID")==null?BigDecimal.ZERO:(BigDecimal)inputValues.get("P_APPLICATION_COUNTRY_ID"));
			List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
			LOGGER.info("in getDeliveryModeIdForCash,output values: {}", outputList);
			
		    return outputList;
		
	}
	
	
	
	
	
	public BigDecimal  getWbLimit() {
		BigDecimal wbLimit = BigDecimal.ZERO;
		String sql = "  SELECT A.NUMERIC_FIELD1  FROM   EX_PARAMETER_DETAILS  A, EX_APPLICATION_SETUP B  WHERE  A.RECORD_ID = 'CLMT' "
				+ " AND    NVL(A.ISACTIVE,' ')  =   'Y'";
		List<BigDecimal> list = new ArrayList<>();
		try {
			list = jdbcTemplate.queryForList(sql, BigDecimal.class);
		} catch (Exception e) {
			LOGGER.info("error in getDistinctCurrencyList : ", e);
		}
		if(list!= null && !list.isEmpty()) {
			wbLimit = list.get(0);
		}
		return wbLimit;
	}
	
	

	
	public List<Map<String, Object>> getRoutingCountryId(Map<String, Object> inputValues) {
		LOGGER.info("in getRoutingBankBranchIdFromDb,input values: {}", inputValues);
		
		String sql = "SELECT ROUTING_COUNTRY_ID ,COUNTRY_NAME "
				+ "FROM ( " + " SELECT DISTINCT F.ROUTING_COUNTRY_ID,F.COUNTRY_NAME   "
				+ " FROM   V_EX_ROUTING_DETAILS F " + " "
				+ " WHERE  F.APPLICATION_COUNTRY_ID= ?" 
				+ " AND    F.BENE_BANK_ID =  ? "
				+ " AND    F.BENE_BANK_BRANCH_ID= ?" 
				+ " AND    F.COUNTRY_ID = ? "
				+ " AND    F.CURRENCY_ID  =  ?"
				+ " AND    F.SERVICE_MASTER_ID  = ? "
				+ " AND    F.ROUTING_BANK_ID   =  DECODE( ?,101,?,F.ROUTING_BANK_ID))";
		
		LOGGER.info("in getRoutingCountryId,input values: {}", sql);	
		
		List<Object> inputList = new ArrayList<>();
		inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID"));
		inputList.add(inputValues.get("P_BENE_BANK_ID"));
		inputList.add(inputValues.get("P_BENE_BANK_BRANCH_ID"));
		inputList.add(inputValues.get("P_BENE_COUNTRY_ID"));
		inputList.add(inputValues.get("P_CURRENCY_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_BENE_BANK_ID"));
		List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
		return outputList;

	}
	
	//ROUTING_BANK_ID
	public List<Map<String, Object>> getRoutingCountryBank(Map<String, Object> inputValues) {
		LOGGER.info("in getRoutingBankBranchIdFromDb,input values: {}", inputValues);
		
		String sql = "SELECT ROUTING_BANK_ID ,ROUTING_BANK_NAME,ROUTING_BANK_CODE "
				+ "FROM ( " + " SELECT DISTINCT F.ROUTING_BANK_ID,F.ROUTING_BANK_NAME,F.ROUTING_BANK_CODE   "
				+ " FROM   V_EX_ROUTING_DETAILS F " + " "
				+ " WHERE  F.APPLICATION_COUNTRY_ID= ?" 
				+ " AND    F.BENE_BANK_ID =  ? "
				+ " AND    F.BENE_BANK_BRANCH_ID= ?" 
				+ " AND    F.COUNTRY_ID = ? "
				+ " AND    F.CURRENCY_ID  =  ?"
				+ " AND    F.SERVICE_MASTER_ID  = ? "
				+ " AND    F.ROUTING_BANK_ID   =  DECODE( ?,101,?,F.ROUTING_BANK_ID)"
				+ " AND    F.ROUTING_COUNTRY_ID =?)";
		
		LOGGER.info("in getRoutingCountryBank,input values: {}", sql);	
		
		List<Object> inputList = new ArrayList<>();
		inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID"));
		inputList.add(inputValues.get("P_BENE_BANK_ID"));
		inputList.add(inputValues.get("P_BENE_BANK_BRANCH_ID"));
		inputList.add(inputValues.get("P_BENE_COUNTRY_ID"));
		inputList.add(inputValues.get("P_CURRENCY_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
		inputList.add(inputValues.get("P_BENE_BANK_ID"));
		inputList.add(inputValues.get("P_ROUTING_COUNTRY_ID"));
		List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
		return outputList;

	}
	
	
	//Remittance list
		public List<Map<String, Object>> getRemittanceModeList(Map<String, Object> inputValues) {
			LOGGER.info("in getRoutingBankBranchIdFromDb,input values: {}", inputValues);
			
			String sql = "SELECT REMITTANCE_MODE_ID ,REMITTANCE_CODE,REMITTANCE_DESCRIPTION "
					+ "FROM ( " + " SELECT DISTINCT F.REMITTANCE_MODE_ID,F.REMITTANCE_CODE,REMITTANCE_DESCRIPTION   "
					+ " FROM   V_EX_ROUTING_DETAILS F " + " "
					+ " WHERE  F.APPLICATION_COUNTRY_ID= ?" 
					+ " AND    F.BENE_BANK_ID =  ? "
					+ " AND    F.BENE_BANK_BRANCH_ID= ?" 
					+ " AND    F.COUNTRY_ID = ? "
					+ " AND    F.CURRENCY_ID  =  ?"
					+ " AND    F.SERVICE_MASTER_ID  = ? "
					+ " AND    F.ROUTING_BANK_ID   =  DECODE( ?,101,?,F.ROUTING_BANK_ID)"
					+ " AND    F.ROUTING_COUNTRY_ID =?"
					+ " AND F.ROUTING_BANK_ID =?)";
			
			LOGGER.info("in getRoutingCountryBank,input values: {}", sql);	
			
			List<Object> inputList = new ArrayList<>();
			inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID"));
			inputList.add(inputValues.get("P_BENE_BANK_ID"));
			inputList.add(inputValues.get("P_BENE_BANK_BRANCH_ID"));
			inputList.add(inputValues.get("P_BENE_COUNTRY_ID"));
			inputList.add(inputValues.get("P_CURRENCY_ID"));
			inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
			inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
			inputList.add(inputValues.get("P_BENE_BANK_ID"));
			inputList.add(inputValues.get("P_ROUTING_COUNTRY_ID"));
			inputList.add(inputValues.get("P_ROUTING_BANK_ID"));
			List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
			return outputList;

		}
		
		
		//Delivery  list
		 public List<Map<String, Object>> getDeliveryModeList(Map<String, Object> inputValues) {
					LOGGER.info("in getRoutingBankBranchIdFromDb,input values: {}", inputValues);
					
					String sql = "SELECT DELIVERY_MODE_ID ,DELIVERY_CODE,DELIVERY_DESCRIPTION "
							+ "FROM ( " + " SELECT DISTINCT F.DELIVERY_MODE_ID,F.DELIVERY_CODE ,DELIVERY_DESCRIPTION  "
							+ " FROM   V_EX_ROUTING_DETAILS F " + " "
							+ " WHERE  F.APPLICATION_COUNTRY_ID= ?" 
							+ " AND    F.BENE_BANK_ID =  ? "
							+ " AND    F.BENE_BANK_BRANCH_ID= ?" 
							+ " AND    F.COUNTRY_ID = ? "
							+ " AND    F.CURRENCY_ID  =  ?"
							+ " AND    F.SERVICE_MASTER_ID  = ? "
							+ " AND    F.ROUTING_BANK_ID   =  DECODE( ?,101,?,F.ROUTING_BANK_ID)"
							+ " AND    F.ROUTING_COUNTRY_ID =?"
							+ " AND    F.ROUTING_BANK_ID =?"
							+ " AND    F.REMITTANCE_MODE_ID =?)";
					
					LOGGER.info("in getRoutingCountryBank,input values: {}", sql);	
					
					List<Object> inputList = new ArrayList<>();
					inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID"));
					inputList.add(inputValues.get("P_BENE_BANK_ID"));
					inputList.add(inputValues.get("P_BENE_BANK_BRANCH_ID"));
					inputList.add(inputValues.get("P_BENE_COUNTRY_ID"));
					inputList.add(inputValues.get("P_CURRENCY_ID"));
					inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
					inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
					inputList.add(inputValues.get("P_BENE_BANK_ID"));
					inputList.add(inputValues.get("P_ROUTING_COUNTRY_ID"));
					inputList.add(inputValues.get("P_ROUTING_BANK_ID"));
					inputList.add(inputValues.get("P_REMITTANCE_MODE_ID"));
					List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
					return outputList;

				}
				
			
		//Delivery  list
		 public List<Map<String, Object>> getRoutingBranchList(Map<String, Object> inputValues) {
					LOGGER.info("in getRoutingBankBranchIdFromDb,input values: {}", inputValues);
					
					String sql = "SELECT BANK_BRANCH_ID ,BRANCH_CODE,BRANCH_FULL_NAME "
							+ "FROM ( " + " SELECT DISTINCT F.BANK_BRANCH_ID,BRANCH_CODE,F.BRANCH_FULL_NAME   "
							+ " FROM   V_EX_ROUTING_DETAILS F " + " "
							+ " WHERE  F.APPLICATION_COUNTRY_ID= ?" 
							+ " AND    F.BENE_BANK_ID =  ? "
							+ " AND    F.BENE_BANK_BRANCH_ID= ?" 
							+ " AND    F.COUNTRY_ID = ? "
							+ " AND    F.CURRENCY_ID  =  ?"
							+ " AND    F.SERVICE_MASTER_ID  = ? "
							+ " AND    F.ROUTING_BANK_ID   =  DECODE( ?,101,?,F.ROUTING_BANK_ID)"
							+ " AND    F.ROUTING_COUNTRY_ID =?"
							+ " AND    F.ROUTING_BANK_ID =?"
							+ " AND    F.REMITTANCE_MODE_ID =?"
							+ "	AND    F.DELIVERY_MODE_ID =?)";
					
					LOGGER.info("in getRoutingCountryBank,input values: {}", sql);	
					
					List<Object> inputList = new ArrayList<>();
					inputList.add(inputValues.get("P_APPLICATION_COUNTRY_ID"));
					inputList.add(inputValues.get("P_BENE_BANK_ID"));
					inputList.add(inputValues.get("P_BENE_BANK_BRANCH_ID"));
					inputList.add(inputValues.get("P_BENE_COUNTRY_ID"));
					inputList.add(inputValues.get("P_CURRENCY_ID"));
					inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
					inputList.add(inputValues.get("P_SERVICE_MASTER_ID"));
					inputList.add(inputValues.get("P_BENE_BANK_ID"));
					inputList.add(inputValues.get("P_ROUTING_COUNTRY_ID"));
					inputList.add(inputValues.get("P_ROUTING_BANK_ID"));
					inputList.add(inputValues.get("P_REMITTANCE_MODE_ID"));
					inputList.add(inputValues.get("P_DELIVERY_MODE_ID"));
					List<Map<String, Object>> outputList = jdbcTemplate.queryForList(sql, inputList.toArray());
					return outputList;

				}
		 
		 
		 /**
		  * 
		  * @param inputMap : checking bank service rule whether file upload or web service
		  * @return
		  */
		 @Transactional
			public Map<String, Object> checkBankServiceRule(RemittanceTransaction remitTrnx) {
				LOGGER.info("in Please check bank service rule, input mpa:  " );
				
				String sql = " SELECT B.TRANSFER_MODE,B.TRANSFER_MODE_ID "
						+ " FROM   EX_BANK_SERVICE_RULE A, EX_TRANSFER_MODE B "
						+" WHERE  A.TRANSFER_MODE_ID = B.TRANSFER_MODE_ID "
						+" AND    A.APPLICATION_COUNTRY_ID = ? "
						+" AND    A.COUNTRY_ID             = ? "
						+" AND    A.CURRENCY_ID            = ? "
						+" AND    A.BANK_ID                = ? "
						+" AND    A.REMITTANCE_MODE_ID     = ? "
						+" AND    A.DELIVERY_MODE_ID       = ? ";
				LOGGER.debug("sql :"+sql);
				
				List<BigDecimal> inputList = new ArrayList<>();
				inputList.add(remitTrnx.getApplicationCountryId().getCountryId());
				inputList.add(remitTrnx.getBankCountryId().getCountryId());
				inputList.add(remitTrnx.getForeignCurrencyId().getCurrencyId());
				inputList.add(remitTrnx.getBankId().getBankId());
				inputList.add(remitTrnx.getRemittanceModeId().getRemittanceModeId());
				inputList.add(remitTrnx.getDeliveryModeId().getDeliveryModeId());
				
				Map<String, Object> output = new HashMap<>();
				try {
					LOGGER.info("SQL  Please check bank service rule outrput : " +sql+"\n inputList.toArray() :"+inputList.toArray());
					Map<String, Object> outputMap = jdbcTemplate.queryForMap(sql, inputList.toArray());
					output.put("P_TRANSFER_MODE", outputMap.get("TRANSFER_MODE"));
					output.put("P_TRANSFER_MODE_ID", outputMap.get("TRANSFER_MODE_ID"));
				} catch (Exception e) {
					LOGGER.info("error in Please check bank service rule : " +e);
				}
				return output;

			}

		 
	
}
