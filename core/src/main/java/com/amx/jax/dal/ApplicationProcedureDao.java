package com.amx.jax.dal;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amx.amxlib.meta.model.AddAdditionalBankData;
import com.amx.jax.constant.ConstantDocument;

@Component
public class ApplicationProcedureDao {
	
	private Logger logger = Logger.getLogger(ApplicationProcedureDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @param beneficaryMasterId
	 * @param beneficaryBankId
	 * @param beneficaryBankBranchId
	 * @param beneAccNumSeqId
	 * @param routingCountry
	 * @param routingBank
	 * @param routingBranch
	 * @param serviceMasterId
	 * @param applicationCountryId
	 * @param currencyId
	 * @param remitMode
	 * @param deliveryMode
	 * @return
	 */
	
	@Transactional
	public Map<String, Object> toFetchDetilaFromAddtionalBenficiaryDetails(BigDecimal beneficaryMasterId, BigDecimal beneficaryBankId, BigDecimal beneficaryBankBranchId,
			BigDecimal beneAccNumSeqId, BigDecimal routingCountry, BigDecimal routingBank, BigDecimal routingBranch, BigDecimal serviceMasterId, BigDecimal applicationCountryId,
			BigDecimal currencyId, BigDecimal remitMode, BigDecimal deliveryMode) {
		
		logger.info("=====EX_GET_ADDL_BENE_DETAILS =Start toFetchDetilaFromAddtionalBenficiaryDetails ========");
		logger.info("Procedure Name= EX_GET_ADDL_BENE_DETAILS ");
		logger.info("beneficaryMasterId :" + beneficaryMasterId);
		logger.info("beneficaryBankId :" + beneficaryBankId);
		logger.info("beneficaryBankBranchId :" + beneficaryBankBranchId);
		logger.info("beneAccNumSeqId :" + beneAccNumSeqId);
		logger.info("routingCountry :" + routingCountry);
		logger.info("routingBank :" + routingBank);
		logger.info("routingBranch :" + routingBranch);
		logger.info("serviceMasterId :" + serviceMasterId);
		logger.info("applicationCountryId :" + applicationCountryId);
		logger.info("currencyId :" + currencyId);
		logger.info("remitMode :" + remitMode);
		logger.info("deliveryMode :" + deliveryMode);
		logger.info("======End toFetchDetilaFromAddtionalBenficiaryDetails ========");
		
		Map<String, Object> output =null;
		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlOutParameter("P_BENE_BANK_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENE_BRANCH_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENE_STATE_ID", Types.VARCHAR),
					new SqlOutParameter("P_BENE_DISTRICT_ID", Types.VARCHAR), 
					new SqlOutParameter("P_BENE_CITY_ID", Types.VARCHAR),
					new SqlOutParameter("P_BENE_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_FIRST_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_SECOND_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_THIRD_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_FOURTH_NAME", Types.VARCHAR), 
					new SqlOutParameter("P_BENEFICIARY_FIFTH_NAME", Types.VARCHAR),
					new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR));
			
		        output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					
					String proc = " { call EX_GET_ADDL_BENE_DETAILS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )} ";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, beneficaryMasterId);
					cs.setBigDecimal(2, beneficaryBankId);
					cs.setBigDecimal(3, beneficaryBankBranchId);
					cs.setBigDecimal(4, beneAccNumSeqId);
					cs.setBigDecimal(5, routingCountry);
					cs.setBigDecimal(6, routingBank);
					cs.setBigDecimal(7, routingBranch);
					cs.setBigDecimal(8, serviceMasterId);
					cs.setBigDecimal(9, applicationCountryId);
					cs.setBigDecimal(10, currencyId);
					cs.setBigDecimal(11, remitMode);
					cs.setBigDecimal(12, deliveryMode);
					cs.registerOutParameter(13, java.sql.Types.VARCHAR);
					cs.registerOutParameter(14, java.sql.Types.VARCHAR);
					cs.registerOutParameter(15, java.sql.Types.INTEGER);
					cs.registerOutParameter(16, java.sql.Types.INTEGER);
					cs.registerOutParameter(17, java.sql.Types.INTEGER);
					cs.registerOutParameter(18, java.sql.Types.VARCHAR);
					cs.registerOutParameter(19, java.sql.Types.VARCHAR);
					cs.registerOutParameter(20, java.sql.Types.VARCHAR);
					cs.registerOutParameter(21, java.sql.Types.VARCHAR);
					cs.registerOutParameter(22, java.sql.Types.VARCHAR);
					cs.registerOutParameter(23, java.sql.Types.VARCHAR);
					cs.registerOutParameter(24, java.sql.Types.VARCHAR);
					return cs;
				}

			}, declareInAndOutputParameters);

			logger.info("EX_GET_ADDL_BENE_DETAILS Out put Parameters :" + output.toString());
			
			
			
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * 
	 * @param applCountryId
	 * @param companyId
	 * @param documentId
	 * @param financialYear
	 * @param processIn
	 * @param branchId
	 * @return
	 */
	public Map<String ,Object> getDocumentSeriality(BigDecimal applCountryId,BigDecimal companyId,
			BigDecimal documentId,BigDecimal financialYear,String processIn,BigDecimal branchId){
		
		
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO countryId :" + applCountryId);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO companyId :" + companyId);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO documentId :" + documentId);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO financialYear :" + financialYear);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO processIn :" + processIn);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO branchId :" + branchId);
		
		Map<String, Object> output =null;
		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.VARCHAR),
					new SqlOutParameter("P_DOC_NO",Types.BIGINT),
					new SqlOutParameter("P_ERROR_FLAG",Types.VARCHAR),
					new SqlOutParameter("P_ERROR_MESG",Types.VARCHAR));
			
			
			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_TO_GEN_NEXT_DOC_SERIAL_NO(?,?,?,?,?,?,?,?,?)}";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, applCountryId);
					cs.setBigDecimal(2, branchId);
					cs.setBigDecimal(3, companyId);
					cs.setBigDecimal(4, documentId);
					cs.setBigDecimal(5, financialYear);
					cs.setString(6, processIn);
					cs.registerOutParameter(7, java.sql.Types.INTEGER);
					cs.registerOutParameter(8, java.sql.Types.VARCHAR);
					cs.registerOutParameter(9, java.sql.Types.VARCHAR);
					cs.execute();
					return cs;
				
				}

			}, declareInAndOutputParameters);
			
			logger.info("Out put Parameters :" + output.toString());
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.info("Out put Parameters :" + e.getMessage());
		}
		return output;
	}
	
	/**
	 * 
	 * @param inputValues
	 * @param listAdditionalBankDataTable
	 * @return
	 */
	

	@Transactional
	public HashMap<String ,Object> exPBankIndicatorsProcedureCheck(HashMap<String, String> inputValues, List<AddAdditionalBankData> listAdditionalBankDataTable){
		HashMap<String, Object> addtionalProcValues = new HashMap<>();
		
		HashMap<String, String> amicCodeLst = new HashMap<String, String>();
		for (AddAdditionalBankData dynamicList : listAdditionalBankDataTable) {
			String amiecdec = dynamicList.getVariableName();
			String amicCode = null;
			if (amiecdec != null) {
				String[] amiecdecValues = amiecdec.split("-");
				if (amiecdecValues.length > 0) {
					amicCode = amiecdecValues[0];
					String flexField = dynamicList.getFlexiField();
					if (amicCode != null) {
						if (flexField != null && flexField.equalsIgnoreCase(ConstantDocument.INDIC1)) {
							amicCodeLst.put("P_INDIC1", amicCode);
						}
						if (flexField != null && flexField.equalsIgnoreCase(ConstantDocument.INDIC2)) {
							amicCodeLst.put("P_INDIC2", amicCode);
						}
						if (flexField != null && flexField.equalsIgnoreCase(ConstantDocument.INDIC3)) {
							amicCodeLst.put("P_INDIC3", amicCode);
						}
						if (flexField != null && flexField.equalsIgnoreCase(ConstantDocument.INDIC4)) {
							amicCodeLst.put("P_INDIC4", amicCode);
						}
						if (flexField != null && flexField.equalsIgnoreCase(ConstantDocument.INDIC5)) {
							amicCodeLst.put("P_INDIC5", amicCode);
						}
					}
				}
			}
		}
		
		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(
				new SqlParameter(Types.BIGINT), //1
				new SqlParameter(Types.BIGINT), //2
				new SqlParameter(Types.BIGINT), //3
				new SqlParameter(Types.BIGINT), //4
				new SqlParameter(Types.BIGINT), //5
				new SqlParameter(Types.BIGINT), //6
				new SqlParameter(Types.VARCHAR), //7
				new SqlOutParameter("P_ERROR_MESSAGE1",Types.VARCHAR),  //8
				new SqlParameter(Types.VARCHAR),//9
				new SqlOutParameter("P_ERROR_MESSAGE2",Types.VARCHAR),//OUT PUT -10
				new SqlParameter(Types.VARCHAR), //11
				new SqlOutParameter("P_ERROR_MESSAGE3",Types.VARCHAR),//OUT PUT -12
				new SqlParameter(Types.VARCHAR), //13
				new SqlOutParameter("P_ERROR_MESSAGE4",Types.VARCHAR),//OUT PUT -14
				new SqlParameter(Types.VARCHAR), //15
				new SqlOutParameter("P_ERROR_MESSAGE5",Types.VARCHAR));//OUT PUT -16
			
		Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				
				String proc = " { call EX_P_BANK_INDICATORS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
				CallableStatement cs = con.prepareCall(proc);
				cs.setBigDecimal(1, new BigDecimal(inputValues.get("P_APPLICATION_COUNTRY_ID")));
				cs.setBigDecimal(2, new BigDecimal(inputValues.get("P_ROUTING_COUNTRY_ID")));
				cs.setBigDecimal(3, new BigDecimal(inputValues.get("P_CURRENCY_ID")));
				cs.setBigDecimal(4, new BigDecimal(inputValues.get("P_ROUTING_BANK_ID")));
				cs.setBigDecimal(5, new BigDecimal(inputValues.get("P_REMITTANCE_MODE_ID")));
				cs.setBigDecimal(6, new BigDecimal(inputValues.get("P_DELIVERY_MODE_ID")));
				cs.setString(7, amicCodeLst.get("P_INDIC1") == null ? "" : amicCodeLst.get("P_INDIC1"));
				cs.registerOutParameter(8, java.sql.Types.VARCHAR);
				cs.setString(9, amicCodeLst.get("P_INDIC2") == null ? "" : amicCodeLst.get("P_INDIC2"));
				cs.registerOutParameter(10, java.sql.Types.VARCHAR);
				cs.setString(11, amicCodeLst.get("P_INDIC3") == null ? "" : amicCodeLst.get("P_INDIC3"));
				cs.registerOutParameter(12, java.sql.Types.VARCHAR);
				cs.setString(13, amicCodeLst.get("P_INDIC4") == null ? "" : amicCodeLst.get("P_INDIC4"));
				cs.registerOutParameter(14, java.sql.Types.VARCHAR);
				cs.setString(15, amicCodeLst.get("P_INDIC5") == null ? "" : amicCodeLst.get("P_INDIC5"));
				cs.registerOutParameter(16, java.sql.Types.VARCHAR);
				cs.execute();
				return cs;
			}

		}, declareInAndOutputParameters);

		logger.info("EX_P_BANK_INDICATORS Out put Parameters :" + output.toString());
		
		return addtionalProcValues;
	}
	
	/**
	 * 
	 * @param inputValue
	 * @return
	 */
	@Transactional
	public Map<String ,Object> getRoutingBankSetupDetails(HashMap<String, String> inputValue){
		
		logger.info("EX_GET_ROUTING_SET_UP_OTH IN VALUES :"+inputValue.toString());
		
		
		Map<String, Object> output =null;
		
		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(
					new SqlParameter(Types.BIGINT), //1
					new SqlParameter(Types.VARCHAR),//2
					new SqlParameter(Types.BIGINT), //3
					new SqlParameter(Types.BIGINT), //4
					new SqlParameter(Types.BIGINT),	//5
					new SqlParameter(Types.VARCHAR),//6
					new SqlParameter(Types.BIGINT),	//7
					new SqlParameter(Types.VARCHAR),//8
					new SqlParameter(Types.BIGINT),	//9
					new SqlOutParameter("P_SERVICE_MASTER_ID",Types.BIGINT), //10
					new SqlOutParameter("P_ROUTING_COUNTRY_ID",Types.BIGINT), //11
					new SqlOutParameter("P_ROUTING_BANK_ID",Types.BIGINT), //12
					new SqlOutParameter("P_ROUTING_BANK_BRANCH_ID",Types.BIGINT), //13
					new SqlOutParameter("P_REMITTANCE_MODE_ID",Types.BIGINT), //14
					new SqlOutParameter("P_DELIVERY_MODE_ID",Types.BIGINT), //15
					new SqlOutParameter("P_BENEFICIARY_SWIFT_BANK1",Types.VARCHAR), //16
					new SqlOutParameter("P_ERROR_MESSAGE",Types.VARCHAR) //17
					);
			
			
			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_GET_ROUTING_SET_UP_OTH (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, new BigDecimal(inputValue.get("P_APPLICATION_COUNTRY_ID")));
					cs.setString(2, inputValue.get("P_USER_TYPE"));
					cs.setBigDecimal(3, new BigDecimal(inputValue.get("P_BENE_COUNTRY_ID")));
					cs.setBigDecimal(4, new BigDecimal(inputValue.get("P_BENE_BANK_ID")));
					cs.setBigDecimal(5, new BigDecimal(inputValue.get("P_BENE_BANK_BRANCH_ID")));
					cs.setString(6, inputValue.get("P_BENE_BANK_ACCOUNT"));
					cs.setBigDecimal(7, new BigDecimal(inputValue.get("P_CUSTOMER_ID")));
					cs.setString(8, inputValue.get("P_SERVICE_GROUP_CODE"));
					cs.setBigDecimal(9, new BigDecimal(inputValue.get("P_CURRENCY_ID"))); 
					// Out Parameters
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

			}, declareInAndOutputParameters);
			
			logger.info("EX_GET_ROUTING_SET_UP_OTH Out put Parameters :" + output.toString());
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.info("Out put Parameters :" + e.getMessage());
		}
		return output;
	}
	/**
	 * 
	 * @param appLicationCountryId
	 * @param customerId
	 * @param branchId
	 * @param beneId
	 * @param beneCountryId
	 * @param beneBankId
	 * @param beneBankBranchId
	 * @param beneAccountNo
	 * @param serviceMasterId
	 * @param routingCountryId
	 * @param routingBankId
	 * @param routingBankBranchId
	 * @param remittanceModeId
	 * @param deliveryModeId
	 * @param sourceOfIncomeId
	 * @param exchangeRateApplied
	 * @param localCommisionCurrencyId
	 * @param localCommisionAmount
	 * @param localChargeCurrencyId
	 * @param localchargeAmount
	 * @param localDelivCurrencyId
	 * @param localDeliAmount
	 * @param serviceProvider
	 * @param foreignCurrencyId
	 * @param foreignTrnxAmount
	 * @param localNetCurrecnyId
	 * @param localNetTrnxAmount
	 * @param beneSwiftBank1
	 * @param beneSwiftBank2
	 * @param errorMessage
	 * @return
	 * Additional check proedure
	 */
	
	@Transactional
	public Map<String ,Object> getAdditionalCheckProcedure(BigDecimal appLicationCountryId, BigDecimal customerId, BigDecimal branchId, BigDecimal beneId, BigDecimal beneCountryId, BigDecimal beneBankId,
			BigDecimal beneBankBranchId, String beneAccountNo, BigDecimal serviceMasterId, BigDecimal routingCountryId, BigDecimal routingBankId, BigDecimal routingBankBranchId,
			BigDecimal remittanceModeId, BigDecimal deliveryModeId, BigDecimal sourceOfIncomeId, BigDecimal exchangeRateApplied, BigDecimal localCommisionCurrencyId, BigDecimal localCommisionAmount,
			BigDecimal localChargeCurrencyId, BigDecimal localchargeAmount, BigDecimal localDelivCurrencyId, BigDecimal localDeliAmount, BigDecimal serviceProvider, BigDecimal foreignCurrencyId,
			BigDecimal foreignTrnxAmount, BigDecimal localNetCurrecnyId, BigDecimal localNetTrnxAmount, String beneSwiftBank1, String beneSwiftBank2, String errorMessage){
		logger.info("======Start getAdditionalCheckProcedure ========");
		logger.info("Procedure Name =EX_APPL_ADDL_CHECKS");
		logger.info("appLicationCountryId :" + appLicationCountryId);
		logger.info("customerId :" + customerId);
		logger.info("branchId :" + branchId);
		logger.info("beneId :" + beneId);
		logger.info("beneCountryId :" + beneCountryId);
		logger.info("beneBankId :" + beneBankId);
		logger.info("beneBankBranchId :" + beneBankBranchId);
		logger.info("beneAccountNo :" + beneAccountNo);
		logger.info("serviceMasterId :" + serviceMasterId);
		logger.info("routingCountryId :" + routingCountryId);
		logger.info("routingBankId :" + routingBankId);
		logger.info("routingBankBranchId :" + routingBankBranchId);
		logger.info("remittanceModeId :" + remittanceModeId);
		logger.info("deliveryModeId :" + deliveryModeId);
		logger.info("sourceOfIncomeId :" + sourceOfIncomeId);
		logger.info("exchangeRateApplied :" + exchangeRateApplied);
		logger.info("localCommisionCurrencyId :" + localCommisionCurrencyId);
		logger.info("localCommisionAmount :" + localCommisionAmount);
		logger.info("localChargeCurrencyId :" + localChargeCurrencyId);
		logger.info("localchargeAmount :" + localchargeAmount);
		logger.info("localDelivCurrencyId :" + localDelivCurrencyId);
		logger.info("localDeliAmount :" + localDeliAmount);
		logger.info("serviceProvider :" + serviceProvider);
		logger.info("foreignCurrencyId :" + foreignCurrencyId);
		logger.info("foreignTrnxAmount :" + foreignTrnxAmount);
		logger.info("localNetCurrecnyId :" + localNetCurrecnyId);
		logger.info("localNetTrnxAmount :" + localNetTrnxAmount);
		logger.info("beneSwiftBank1 :" + beneSwiftBank1);
		logger.info("beneSwiftBank2 :" + beneSwiftBank2);
		logger.info("errorMessage :" + errorMessage);
		logger.info("======End getAdditionalCheckProcedure ========");
		Map<String, Object> output =null;
		
		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(
					new SqlParameter(Types.BIGINT), //1
					new SqlParameter(Types.BIGINT),//2
					new SqlParameter(Types.BIGINT), //3
					new SqlParameter(Types.BIGINT), //4
					new SqlParameter(Types.BIGINT),	//5
					new SqlParameter(Types.VARCHAR),//6
					new SqlParameter(Types.BIGINT),	//7
					new SqlParameter(Types.VARCHAR),//8
					new SqlParameter(Types.BIGINT),	//9
					new SqlParameter(Types.BIGINT),	//10
					new SqlParameter(Types.BIGINT),	//11
					new SqlParameter(Types.BIGINT),	//12
					new SqlParameter(Types.BIGINT),	//13
					new SqlParameter(Types.BIGINT),	//14
					new SqlParameter(Types.BIGINT),	//15
					new SqlParameter(Types.BIGINT),	//16
					new SqlParameter(Types.BIGINT),	//17
					new SqlParameter(Types.BIGINT),	//18
					new SqlParameter(Types.BIGINT),	//19
					new SqlParameter(Types.BIGINT),	//20
					new SqlParameter(Types.BIGINT),	//21
					new SqlParameter(Types.BIGINT),	//22
					new SqlParameter(Types.BIGINT),	//23
					new SqlParameter(Types.BIGINT),	//24
					new SqlParameter(Types.BIGINT),	//25
					new SqlParameter(Types.BIGINT),	//26
					new SqlParameter(Types.BIGINT),	//27
					new SqlParameter(Types.VARCHAR),//28
					new SqlParameter(Types.VARCHAR),//29
					new SqlOutParameter("P_ERROR_MESSAGE",Types.VARCHAR) //30
					);
			
			
			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call call EX_APPL_ADDL_CHECKS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, \" + \"?, ?, ?,?, ?,?, ?, ?, ?,?) } ";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, appLicationCountryId);
					cs.setBigDecimal(2, customerId);
					cs.setBigDecimal(3, branchId);
					cs.setBigDecimal(4, beneId);
					cs.setBigDecimal(5, beneCountryId);
					cs.setBigDecimal(6, beneBankId);
					cs.setBigDecimal(7, beneBankBranchId);
					cs.setString(8, beneAccountNo);
					cs.setBigDecimal(9, serviceMasterId);
					cs.setBigDecimal(10, routingCountryId);
					cs.setBigDecimal(11, routingBankId);
					cs.setBigDecimal(12, routingBankBranchId);
					cs.setBigDecimal(13, remittanceModeId);
					cs.setBigDecimal(14, deliveryModeId);
					cs.setBigDecimal(15, sourceOfIncomeId);
					cs.setBigDecimal(16, exchangeRateApplied);
					cs.setBigDecimal(17, localCommisionCurrencyId);
					cs.setBigDecimal(18, localCommisionAmount);
					cs.setBigDecimal(19, localChargeCurrencyId);
					cs.setBigDecimal(20, localchargeAmount);
					cs.setBigDecimal(21, localDelivCurrencyId);
					cs.setBigDecimal(22, localDeliAmount);
					cs.setBigDecimal(23, serviceProvider);
					cs.setBigDecimal(24, foreignCurrencyId);
					cs.setBigDecimal(25, foreignTrnxAmount);
					cs.setBigDecimal(26, localNetCurrecnyId);
					cs.setBigDecimal(27, localNetTrnxAmount);
					cs.setString(28, beneSwiftBank1);
					cs.setString(29, beneSwiftBank2);
					cs.registerOutParameter(30, java.sql.Types.VARCHAR);
					cs.execute();
					return cs;
				}

			}, declareInAndOutputParameters);
			
			logger.info("EX_GET_ROUTING_SET_UP_OTH Out put Parameters :" + output.toString());
			
		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.info("Out put Parameters :" + e.getMessage());
		}
		return output;
	}
	
	
/**
 * toFetchPurtherInstractionErrorMessaage
 * @param applicationCountyId
 * @param routingCountryId
 * @param routingBankId
 * @param currencyId
 * @param remittanceId
 * @param deliveryId
 * @param furtherInstruction
 * @param beneBankCountryId
 * @return
 */
	
	@Transactional
	public Map<String ,Object>  toFetchPurtherInstractionErrorMessaage(BigDecimal applicationCountyId, BigDecimal routingCountryId, BigDecimal routingBankId,
			BigDecimal currencyId, BigDecimal remittanceId, BigDecimal deliveryId, String furtherInstruction,BigDecimal beneBankCountryId){
		logger.info("======Start toFtechPurtherInstractionErrorMessaage ========");
		logger.info("Procedure Name = EX_P_FURTHER_INSTR ");
		logger.info("applicationCountyId :" + applicationCountyId);
		logger.info("routingCountryId :" + routingCountryId);
		logger.info("routingBankId :" + routingBankId);
		logger.info("currencyId :" + currencyId);
		logger.info("remittanceId :" + remittanceId);
		logger.info("deliveryId :" + deliveryId);
		logger.info("furtherInstruction :" + furtherInstruction);
		logger.info("BeneBankCountry :" + beneBankCountryId);
		logger.info("======End toFtechPurtherInstractionErrorMessaage ========");
Map<String, Object> output =null;
		
		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(
					new SqlParameter(Types.BIGINT), //1
					new SqlParameter(Types.BIGINT),//2
					new SqlParameter(Types.BIGINT), //3
					new SqlParameter(Types.BIGINT), //4
					new SqlParameter(Types.BIGINT),	//5
					new SqlParameter(Types.BIGINT),//6
					new SqlParameter(Types.VARCHAR),	//7
					new SqlParameter(Types.BIGINT),//8
					new SqlOutParameter("P_ERRMSG",Types.VARCHAR));//9
					

					output = jdbcTemplate.call(new CallableStatementCreator() {
						@Override
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							String proc = "{call EX_P_FURTHER_INSTR (?, ?, ?, ?, ?, ?, ?, ?, ?) } ";
							CallableStatement cs = con.prepareCall(proc);
							cs.setBigDecimal(1, applicationCountyId);
							cs.setBigDecimal(2, routingCountryId);
							cs.setBigDecimal(3, routingBankId);
							cs.setBigDecimal(4, currencyId);
							cs.setBigDecimal(5, remittanceId);
							cs.setBigDecimal(6, deliveryId);
							cs.setString(7, furtherInstruction == null ? "" : furtherInstruction);
							cs.setBigDecimal(8, deliveryId);
							cs.registerOutParameter(9, java.sql.Types.VARCHAR);
							cs.execute();cs.execute();
							return cs;
						}

					}, declareInAndOutputParameters);
					
					logger.info("EX_GET_ROUTING_SET_UP_OTH Out put Parameters :" + output.toString());
					
				} catch (DataAccessException e) {
					e.printStackTrace();
					logger.info("Out put Parameters :" + e.getMessage());
				}
				return output;
			}
}
