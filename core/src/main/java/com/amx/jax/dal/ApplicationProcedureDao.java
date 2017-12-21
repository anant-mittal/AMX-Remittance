package com.amx.jax.dal;

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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amx.amxlib.meta.model.AddAdditionalBankDataDto;
import com.amx.jax.constant.ConstantDocument;

@Component
public class ApplicationProcedureDao {

	private Logger logger = Logger.getLogger(ApplicationProcedureDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Purpose : toFetchDetilaFromAddtionalBenficiaryDetails
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
	public Map<String, Object> toFetchDetilaFromAddtionalBenficiaryDetails(Map<String, Object> inputValues){
			
			
			BigDecimal beneficaryMasterId =(BigDecimal)inputValues.get("P_BENEFICIARY_ID");
			BigDecimal beneficaryBankId=(BigDecimal)inputValues.get("P_BENEFICIARY_BANK_ID");
			BigDecimal beneficaryBankBranchId=(BigDecimal)inputValues.get("P_BENEFICIARY_BRANCH_ID");
			BigDecimal beneAccNumSeqId=(BigDecimal)inputValues.get("P_BENEFICARY_ACCOUNT_SEQ_ID");
			BigDecimal routingCountry=(BigDecimal)inputValues.get("P_ROUTING_COUNTRY_ID");
			BigDecimal routingBank=(BigDecimal)inputValues.get("P_ROUTING_BANK_ID");
			BigDecimal routingBranch=(BigDecimal)inputValues.get("P_ROUTING_BANK_ID"); 
			BigDecimal serviceMasterId=(BigDecimal)inputValues.get("P_SERVICE_MASTER_ID");
			BigDecimal applicationCountryId=(BigDecimal)inputValues.get("P_APPLICATION_COUNTRY_ID");
			BigDecimal currencyId=(BigDecimal)inputValues.get("P_CURRENCY_ID");
			BigDecimal remitMode=(BigDecimal)inputValues.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryMode=(BigDecimal)inputValues.get("P_DELIVERY_MODE_ID"); 




		logger.info("=====EX_GET_ADDL_BENE_DETAILS =Start toFetchDetilaFromAddtionalBenficiaryDetails ");
		logger.info("Procedure Name= EX_GET_ADDL_BENE_DETAILS :"+inputValues.toString());
		

		Map<String, Object> output = null;
		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
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
	public Map<String, Object> getDocumentSeriality(BigDecimal applCountryId, BigDecimal companyId,
			BigDecimal documentId, BigDecimal financialYear, String processIn, BigDecimal branchId) {

		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO countryId :" + applCountryId);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO companyId :" + companyId);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO documentId :" + documentId);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO financialYear :" + financialYear);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO processIn :" + processIn);
		logger.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO branchId :" + branchId);

		Map<String, Object> output = null;
		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.VARCHAR),
					new SqlOutParameter("P_DOC_NO", Types.BIGINT), new SqlOutParameter("P_ERROR_FLAG", Types.VARCHAR),
					new SqlOutParameter("P_ERROR_MESG", Types.VARCHAR));

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
	public HashMap<String, Object> exPBankIndicatorsProcedureCheck(HashMap<String, String> inputValues,
			List<AddAdditionalBankDataDto> listAdditionalBankDataTable) {
		HashMap<String, Object> addtionalProcValues = new HashMap<>();

		HashMap<String, String> amicCodeLst = new HashMap<String, String>();
		for (AddAdditionalBankDataDto dynamicList : listAdditionalBankDataTable) {
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

		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT), // 1
				new SqlParameter(Types.BIGINT), // 2
				new SqlParameter(Types.BIGINT), // 3
				new SqlParameter(Types.BIGINT), // 4
				new SqlParameter(Types.BIGINT), // 5
				new SqlParameter(Types.BIGINT), // 6
				new SqlParameter(Types.VARCHAR), // 7
				new SqlOutParameter("P_ERROR_MESSAGE1", Types.VARCHAR), // 8
				new SqlParameter(Types.VARCHAR), // 9
				new SqlOutParameter("P_ERROR_MESSAGE2", Types.VARCHAR), // OUT PUT -10
				new SqlParameter(Types.VARCHAR), // 11
				new SqlOutParameter("P_ERROR_MESSAGE3", Types.VARCHAR), // OUT PUT -12
				new SqlParameter(Types.VARCHAR), // 13
				new SqlOutParameter("P_ERROR_MESSAGE4", Types.VARCHAR), // OUT PUT -14
				new SqlParameter(Types.VARCHAR), // 15
				new SqlOutParameter("P_ERROR_MESSAGE5", Types.VARCHAR));// OUT PUT -16

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
	public Map<String, Object> getRoutingBankSetupDetails(HashMap<String, String> inputValue) {

		logger.info("EX_GET_ROUTING_SET_UP_OTH IN VALUES :" + inputValue.toString());

		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT), // 1
					new SqlParameter(Types.VARCHAR), // 2
					new SqlParameter(Types.BIGINT), // 3
					new SqlParameter(Types.BIGINT), // 4
					new SqlParameter(Types.BIGINT), // 5
					new SqlParameter(Types.VARCHAR), // 6
					new SqlParameter(Types.BIGINT), // 7
					new SqlParameter(Types.VARCHAR), // 8
					new SqlParameter(Types.BIGINT), // 9
					new SqlOutParameter("P_SERVICE_MASTER_ID", Types.BIGINT), // 10
					new SqlOutParameter("P_ROUTING_COUNTRY_ID", Types.BIGINT), // 11
					new SqlOutParameter("P_ROUTING_BANK_ID", Types.BIGINT), // 12
					new SqlOutParameter("P_ROUTING_BANK_BRANCH_ID", Types.BIGINT), // 13
					new SqlOutParameter("P_REMITTANCE_MODE_ID", Types.BIGINT), // 14
					new SqlOutParameter("P_DELIVERY_MODE_ID", Types.BIGINT), // 15
					new SqlOutParameter("P_BENEFICIARY_SWIFT_BANK1", Types.VARCHAR), // 16
					new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR) // 17
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
	 * @return Additional check proedure
	 */

	@Transactional
	public Map<String, Object> getAdditionalCheckProcedure(Map<String, Object> inputValues){
		
		logger.info("======Start getAdditionalCheckProcedure EX_APPL_ADDL_CHECKS========:"+inputValues.toString());
		
		BigDecimal appLicationCountryId =(BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal customerId =(BigDecimal) inputValues.get("P_CUSTOMER_ID");
		BigDecimal branchId =(BigDecimal) inputValues.get("P_BRANCH_ID");
		BigDecimal beneId =(BigDecimal) inputValues.get("P_BENEFICIARY_ID");
		
		BigDecimal beneCountryId = (BigDecimal) inputValues.get("P_BENEFICIARY_COUNTRY_ID");
		BigDecimal beneBankId=(BigDecimal) inputValues.get("P_BENEFICIARY_BANK_ID");
		BigDecimal beneBankBranchId=(BigDecimal) inputValues.get("P_BENEFICIARY_BRANCH_ID");
		String beneAccountNo=inputValues.get("P_BENEFICIARY_ACCOUNT_NO")==null?null:inputValues.get("P_BENEFICIARY_ACCOUNT_NO").toString();
		BigDecimal serviceMasterId=(BigDecimal) inputValues.get("P_SERVICE_MASTER_ID");
		BigDecimal routingCountryId=(BigDecimal) inputValues.get("P_ROUTING_COUNTRY_ID");
		BigDecimal routingBankId=(BigDecimal) inputValues.get("P_ROUTING_BANK_ID");
		BigDecimal routingBankBranchId=(BigDecimal) inputValues.get("P_ROUTING_BANK_BRANCH_ID");
		BigDecimal remittanceModeId=(BigDecimal) inputValues.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId=(BigDecimal) inputValues.get("P_DELIVERY_MODE_ID");
		BigDecimal sourceOfIncomeId=(BigDecimal) inputValues.get("P_SOURCE_OF_INCOME_ID");
		BigDecimal exchangeRateApplied=(BigDecimal) inputValues.get("P_EXCHANGE_RATE_APPLIED");
		BigDecimal localCommisionCurrencyId=(BigDecimal) inputValues.get("P_LOCAL_COMMISION_CURRENCY_ID");
		BigDecimal localCommisionAmount=(BigDecimal) inputValues.get("P_LOCAL_COMMISION_AMOUNT");
		BigDecimal localChargeCurrencyId=(BigDecimal) inputValues.get("P_LOCAL_CHARGE_CURRENCY_ID");
		BigDecimal localchargeAmount=(BigDecimal) inputValues.get("P_LOCAL_CHARGE_AMOUNT");
		BigDecimal localDelivCurrencyId=(BigDecimal) inputValues.get("P_LOCAL_DELIVERY_CURRENCY_ID");
		BigDecimal localDeliAmount=(BigDecimal) inputValues.get("P_LOCAL_DELIVERY_AMOUNT");
		BigDecimal serviceProvider=(BigDecimal) inputValues.get("P_SERVICE_PROVIDER");
		BigDecimal foreignCurrencyId=(BigDecimal) inputValues.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal foreignTrnxAmount=(BigDecimal) inputValues.get("P_FOREIGN_TRANX_AMOUNT");
		BigDecimal localNetCurrecnyId=(BigDecimal) inputValues.get("P_LOCAL_NET_CURRENCY_ID");
		BigDecimal localNetTrnxAmount=(BigDecimal) inputValues.get("P_LOCAL_NET_TRANX_AMOUNT");
		String beneSwiftBank1=inputValues.get("P_BENEFICIARY_SWIFT_BANK1")==null?null:inputValues.get("P_BENEFICIARY_SWIFT_BANK1").toString();
		String beneSwiftBank2=inputValues.get("P_BENEFICIARY_SWIFT_BANK2")==null?null:inputValues.get("P_BENEFICIARY_SWIFT_BANK2").toString();
		
		
		
		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT), // 1
					new SqlParameter(Types.BIGINT), // 2
					new SqlParameter(Types.BIGINT), // 3
					new SqlParameter(Types.BIGINT), // 4
					new SqlParameter(Types.BIGINT), // 5
					new SqlParameter(Types.VARCHAR), // 6
					new SqlParameter(Types.BIGINT), // 7
					new SqlParameter(Types.VARCHAR), // 8
					new SqlParameter(Types.BIGINT), // 9
					new SqlParameter(Types.BIGINT), // 10
					new SqlParameter(Types.BIGINT), // 11
					new SqlParameter(Types.BIGINT), // 12
					new SqlParameter(Types.BIGINT), // 13
					new SqlParameter(Types.BIGINT), // 14
					new SqlParameter(Types.BIGINT), // 15
					new SqlParameter(Types.BIGINT), // 16
					new SqlParameter(Types.BIGINT), // 17
					new SqlParameter(Types.BIGINT), // 18
					new SqlParameter(Types.BIGINT), // 19
					new SqlParameter(Types.BIGINT), // 20
					new SqlParameter(Types.BIGINT), // 21
					new SqlParameter(Types.BIGINT), // 22
					new SqlParameter(Types.BIGINT), // 23
					new SqlParameter(Types.BIGINT), // 24
					new SqlParameter(Types.BIGINT), // 25
					new SqlParameter(Types.BIGINT), // 26
					new SqlParameter(Types.BIGINT), // 27
					new SqlParameter(Types.VARCHAR), // 28
					new SqlParameter(Types.VARCHAR), // 29
					new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR) // 30
			);

			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_APPL_ADDL_CHECKS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?,?, ?,?, ?, ?, ?,?) } ";
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

			logger.info("EX_APPL_ADDL_CHECKS Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.info("Out put Parameters :" + e.getMessage());
		}
		return output;
	}

	/**
	 * toFetchPurtherInstractionErrorMessaage
	 * 
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
	public Map<String, Object> toFetchPurtherInstractionErrorMessaage(HashMap<String, Object> inputValues){
			
			
			
			BigDecimal applicationCountyId =(BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
			BigDecimal routingCountryId= (BigDecimal) inputValues.get("P_ROUTING_COUNTRY_ID");
			BigDecimal routingBankId = (BigDecimal) inputValues.get("P_ROUTING_BANK_ID");
			BigDecimal currencyId= (BigDecimal) inputValues.get("P_CURRENCY_ID");
			BigDecimal remittanceId= (BigDecimal) inputValues.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryId= (BigDecimal) inputValues.get("P_DELIVERY_MODE_ID");
			String furtherInstruction =inputValues.get("P_FURTHER_INSTR")==null?null:inputValues.get("P_FURTHER_INSTR").toString();  
			
		logger.info("=====EX_P_FURTHER_INSTRe ========"+inputValues.toString());
		
		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT), // 1
					new SqlParameter(Types.BIGINT), // 2
					new SqlParameter(Types.BIGINT), // 3
					new SqlParameter(Types.BIGINT), // 4
					new SqlParameter(Types.BIGINT), // 5
					new SqlParameter(Types.BIGINT), // 6
					new SqlParameter(Types.VARCHAR), // 7
					new SqlParameter(Types.BIGINT), // 8
					new SqlOutParameter("P_ERRMSG", Types.VARCHAR));// 9

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
					cs.execute();
					cs.execute();
					return cs;
				}

			}, declareInAndOutputParameters);

			logger.info("EX_P_FURTHER_INSTR Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.info("Out put Parameters :" + e.getMessage());
		}
		return output;
	}

	/**
	 * Purpose : Check swift bank procedure
	 * 
	 * @param applicationCountryId
	 * @param routingCountryId
	 * @param currencyId
	 * @param remittanceId
	 * @param deliveryId
	 * @param fieldName
	 * @param beneficiarySwiftBank
	 * @param beneBankCountryId
	 * @return
	 */
	@Transactional
	public Map<String, Object> toFetchSwiftBankProcedure(HashMap<String, Object> inputValues){
			
			BigDecimal applicationCountyId =(BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
			BigDecimal routingCountryId= (BigDecimal) inputValues.get("P_ROUTING_COUNTRY_ID");
			BigDecimal currencyId= (BigDecimal) inputValues.get("P_CURRENCY_ID");
			BigDecimal remittanceId= (BigDecimal) inputValues.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryId= (BigDecimal) inputValues.get("P_DELIVERY_MODE_ID");
			BigDecimal beneBankCountryId =( BigDecimal) inputValues.get("P_BENE_BANK_COUNTRY_ID");
			String fieldName =inputValues.get("P_FIELD_NAME")==null?null:inputValues.get("P_FIELD_NAME").toString();  
			String beneficiarySwiftBank =inputValues.get("P_BENEFICIARY_SWIFT_CODE")==null?null:inputValues.get("P_BENEFICIARY_SWIFT_CODE").toString();  
			
		logger.info("Procedure Name = EX_P_CHECK_SWIFT_BANK :"+inputValues.toString());
		
		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT), // 1
					new SqlParameter(Types.BIGINT), // 2
					new SqlParameter(Types.BIGINT), // 3
					new SqlParameter(Types.BIGINT), // 4
					new SqlParameter(Types.BIGINT), // 5
					new SqlParameter(Types.VARCHAR), // 6
					new SqlParameter(Types.VARCHAR), // 7
					new SqlParameter(Types.BIGINT), // 8
					new SqlOutParameter("P_ERRMSG", Types.VARCHAR));// 9

			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_P_CHECK_SWIFT_BANK(?, ?, ?, ?, ?, ?, ?, ?, ?) } ";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, applicationCountyId);
					cs.setBigDecimal(2, routingCountryId);
					cs.setBigDecimal(3, currencyId);
					cs.setBigDecimal(4, remittanceId);
					cs.setBigDecimal(5, deliveryId);
					cs.setString(6, fieldName);
					cs.setString(7, beneficiarySwiftBank);
					cs.setBigDecimal(8, beneBankCountryId);
					cs.registerOutParameter(9, java.sql.Types.VARCHAR);
					cs.execute();
					return cs;
				}

			}, declareInAndOutputParameters);

			logger.info("EX_P_CHECK_SWIFT_BANK Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.info("Out put Parameters :" + e.getMessage());
		}
		return output;
	}

	/**
	 * purpose : Move application to remittance after successfull knet
	 * 
	 * @param applicationCountryId
	 * @param companyId
	 * @param customerNo
	 * @param userName
	 * @param paymentId
	 * @param authcode
	 * @param tranId
	 * @param refId
	 * @return
	 * 
	 * 
	 */
	@Transactional
	public Map<String, Object> insertRemittanceOnline(HashMap<String, Object> inputValues){
			
			
			
			
			BigDecimal applicationCountryId =(BigDecimal)inputValues.get("P_APPL_CNTY_ID");
			BigDecimal companyId=(BigDecimal)inputValues.get("P_COMPANY_ID");
			BigDecimal customerNo=(BigDecimal)inputValues.get("P_CUSTOMER_ID");
			String userName=inputValues.get("P_USER_NAME").toString();
			String paymentId =inputValues.get("P_PAYMENT_ID")==null?"":inputValues.get("P_PAYMENT_ID").toString();
			String authcode=inputValues.get("P_AUTHCOD")==null?"":inputValues.get("P_AUTHCOD").toString();
			String tranId=inputValues.get("P_TRANID")==null?"":inputValues.get("P_TRANID").toString();
			String refId=inputValues.get("P_REFID")==null?"":inputValues.get("P_REFID").toString();
			
		logger.info("saveRemittance EX_INSERT_REMITTANCE_ONLINE getCustomerNo():" +inputValues.toString());
		
		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT), // 1
					new SqlParameter(Types.BIGINT), // 2
					new SqlParameter(Types.BIGINT), // 3
					new SqlParameter(Types.VARCHAR), // 4
					new SqlParameter(Types.VARCHAR), // 5
					new SqlParameter(Types.VARCHAR), // 6
					new SqlParameter(Types.VARCHAR), // 7
					new SqlParameter(Types.VARCHAR), // 8
					new SqlOutParameter("P_COLLECT_FINYR", Types.BIGINT), // 9
					new SqlOutParameter("P_COLLECTION_NO", Types.BIGINT), // 10
					new SqlOutParameter("P_COLLECTION_DOCUMENT_CODE", Types.BIGINT), // 11
					new SqlOutParameter("P_ERROR_MESG", Types.VARCHAR));// 12

			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_INSERT_REMITTANCE_ONLINE(?,?,?,?,?,?,?,?,?,?,?,?)}";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, applicationCountryId);
					cs.setBigDecimal(2, companyId);
					cs.setBigDecimal(3, customerNo);
					cs.setString(4, userName);
					cs.setString(5, paymentId);
					cs.setString(6, authcode);
					cs.setString(7, tranId);
					cs.setString(8, refId);

					cs.registerOutParameter(9, java.sql.Types.NUMERIC);
					cs.registerOutParameter(10, java.sql.Types.NUMERIC);
					cs.registerOutParameter(11, java.sql.Types.NUMERIC);
					cs.registerOutParameter(12, java.sql.Types.VARCHAR);
					cs.execute();
					return cs;
				}

			}, declareInAndOutputParameters);

			logger.info("EX_INSERT_REMITTANCE_ONLINE Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.info("Out put Parameters :" + e.getMessage());
		}
		return output;
	}

	@Transactional
	public Map<String, Object> fetchAdditionalBankRuleIndicators(Map<String, Object> inputValues) {
		logger.info("EX_REMIT_ADDL_INFO :" + inputValues.toString());
		Map<String, Object> output = null;

		try {

			BigDecimal applicationCountryId = (BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
			BigDecimal serviceMasterId = (BigDecimal) inputValues.get("P_SERVICE_MASTER_ID");
			BigDecimal routingCountryId = (BigDecimal) inputValues.get("P_ROUTING_COUNTRY_ID");
			BigDecimal routingBankId = (BigDecimal) inputValues.get("P_ROUTING_BANK_ID");
			BigDecimal routingBankBranchId = (BigDecimal) inputValues.get("P_ROUTING_BANK_BRANCH_ID");
			BigDecimal deliveryModeId = (BigDecimal) inputValues.get("P_DELIVERY_MODE_ID");
			BigDecimal remittanceModeId = (BigDecimal) inputValues.get("P_REMITTANCE_MODE_ID");
			BigDecimal foreignCurrencyId = (BigDecimal) inputValues.get("P_FOREIGN_CURRENCY_ID");
			BigDecimal foreignAmount = (BigDecimal) inputValues.get("P_FOREIGN_AMOUNT");
			BigDecimal customerId = (BigDecimal) inputValues.get("P_CUSTOMER_ID");
			BigDecimal beneficaryRelationshipId = (BigDecimal) inputValues.get("P_BENEFICARY_RELATIONSHIP_ID");

			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT), // 1
					new SqlParameter(Types.BIGINT), // 2
					new SqlParameter(Types.BIGINT), // 3
					new SqlParameter(Types.BIGINT), // 3
					new SqlParameter(Types.BIGINT), // 5
					new SqlParameter(Types.BIGINT), // 6
					new SqlParameter(Types.BIGINT), // 7
					new SqlParameter(Types.BIGINT), // 8
					new SqlParameter(Types.BIGINT), // 9
					new SqlParameter(Types.BIGINT), // 10
					new SqlParameter(Types.BIGINT), // 11

					new SqlOutParameter("P_FURTHER_INSTR_DATA", Types.VARCHAR), // 12
					new SqlOutParameter("P_FURTHER_INSTR_REQ", Types.VARCHAR), // 13
					new SqlOutParameter("P_SWIFT_BANK1_DATA", Types.VARCHAR), // 14
					new SqlOutParameter("P_SWIFT_BANK1_REQ", Types.VARCHAR), // 15
					new SqlOutParameter("P_SWIFT_BANK2_DATA", Types.VARCHAR), // 16

					new SqlOutParameter("P_SWIFT_BANK2_REQ", Types.VARCHAR), // 17
					new SqlOutParameter("P_AMIEC_CODE_1", Types.VARCHAR), // 18
					new SqlOutParameter("P_FLEX_FIELD_VALUE_1", Types.VARCHAR), // 19
					new SqlOutParameter("P_ADDITIONAL_BANK_RULE_ID_1", Types.VARCHAR), // 20
					new SqlOutParameter("P_FLEX_FIELD_REQ_1", Types.VARCHAR), // 21

					new SqlOutParameter("P_AMIEC_CODE_2", Types.VARCHAR), // 22
					new SqlOutParameter("P_FLEX_FIELD_VALUE_2", Types.VARCHAR), // 23
					new SqlOutParameter("P_ADDITIONAL_BANK_RULE_ID_2", Types.VARCHAR), // 24
					new SqlOutParameter("P_FLEX_FIELD_REQ_2", Types.VARCHAR), // 25
					new SqlOutParameter("P_AMIEC_CODE_3", Types.VARCHAR), // 26

					new SqlOutParameter("P_FLEX_FIELD_VALUE_3", Types.VARCHAR), // 27
					new SqlOutParameter("P_ADDITIONAL_BANK_RULE_ID_3", Types.VARCHAR), // 28
					new SqlOutParameter("P_FLEX_FIELD_REQ_3", Types.VARCHAR), // 29
					new SqlOutParameter("P_AMIEC_CODE_4", Types.VARCHAR), // 30
					new SqlOutParameter("P_FLEX_FIELD_VALUE_4", Types.VARCHAR), // 31

					new SqlOutParameter("P_ADDITIONAL_BANK_RULE_ID_4", Types.VARCHAR), // 32
					new SqlOutParameter("P_FLEX_FIELD_REQ_4", Types.VARCHAR), // 33
					new SqlOutParameter("P_AMIEC_CODE_5", Types.VARCHAR), // 34
					new SqlOutParameter("P_FLEX_FIELD_VALUE_5", Types.VARCHAR), // 35
					new SqlOutParameter("P_ADDITIONAL_BANK_RULE_ID_5", Types.VARCHAR), // 36

					new SqlOutParameter("P_FLEX_FIELD_REQ_5", Types.VARCHAR), // 37
					new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR)); // 38

			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{ call EX_REMIT_ADDL_INFO (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, applicationCountryId);
					cs.setBigDecimal(2, serviceMasterId);
					cs.setBigDecimal(3, routingCountryId);
					cs.setBigDecimal(4, routingBankId);
					cs.setBigDecimal(5, routingBankBranchId);
					cs.setBigDecimal(6, deliveryModeId);
					cs.setBigDecimal(7, remittanceModeId);
					cs.setBigDecimal(8, foreignCurrencyId);
					cs.setBigDecimal(9, foreignAmount);
					cs.setBigDecimal(10, customerId);
					cs.setBigDecimal(11, beneficaryRelationshipId);
					cs.registerOutParameter(12, java.sql.Types.VARCHAR);
					cs.registerOutParameter(13, java.sql.Types.VARCHAR);
					cs.registerOutParameter(14, java.sql.Types.VARCHAR);
					cs.registerOutParameter(15, java.sql.Types.VARCHAR);
					cs.registerOutParameter(16, java.sql.Types.VARCHAR);
					cs.registerOutParameter(17, java.sql.Types.VARCHAR);
					cs.registerOutParameter(18, java.sql.Types.VARCHAR);
					cs.registerOutParameter(19, java.sql.Types.VARCHAR);
					cs.registerOutParameter(20, java.sql.Types.NUMERIC);
					cs.registerOutParameter(21, java.sql.Types.VARCHAR);
					cs.registerOutParameter(22, java.sql.Types.VARCHAR);
					cs.registerOutParameter(23, java.sql.Types.VARCHAR);
					cs.registerOutParameter(24, java.sql.Types.NUMERIC);
					cs.registerOutParameter(25, java.sql.Types.VARCHAR);
					cs.registerOutParameter(26, java.sql.Types.VARCHAR);
					cs.registerOutParameter(27, java.sql.Types.VARCHAR);
					cs.registerOutParameter(28, java.sql.Types.NUMERIC);
					cs.registerOutParameter(29, java.sql.Types.VARCHAR);
					cs.registerOutParameter(30, java.sql.Types.VARCHAR);
					cs.registerOutParameter(31, java.sql.Types.VARCHAR);
					cs.registerOutParameter(32, java.sql.Types.NUMERIC);
					cs.registerOutParameter(33, java.sql.Types.VARCHAR);
					cs.registerOutParameter(34, java.sql.Types.VARCHAR);
					cs.registerOutParameter(35, java.sql.Types.VARCHAR);
					cs.registerOutParameter(36, java.sql.Types.NUMERIC);
					cs.registerOutParameter(37, java.sql.Types.VARCHAR);
					cs.registerOutParameter(38, java.sql.Types.VARCHAR);
					cs.execute();
					return cs;
				}

			}, declareInAndOutputParameters);

			logger.info("EX_REMIT_ADDL_INFO Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.info("Out put Parameters :" + e.getMessage());
		}
		return output;
	}
	
	public Map<String, Object> getRoutingDetails(HashMap<String, Object> inputValue) {

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
			ouptutParams.add(new SqlOutParameter(outParams[i - 1], Types.NUMERIC));
		}

		Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				String proc = " { call EX_GET_ROUTING_SET_UP_OTH (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) } ";
				CallableStatement cs = con.prepareCall(proc);
				// In Parameters
				cs.setBigDecimal(1, (BigDecimal) inputValue.get("P_APPLICATION_COUNTRY_ID"));
				cs.setString(2, inputValue.get("P_USER_TYPE").toString());
				cs.setBigDecimal(3, (BigDecimal) inputValue.get("P_BENEFICIARY_COUNTRY_ID"));
				cs.setBigDecimal(4, (BigDecimal) inputValue.get("P_BENEFICIARY_BANK_ID"));
				cs.setBigDecimal(5, (BigDecimal) inputValue.get("P_BENEFICIARY_BRANCH_ID"));
				cs.setString(6, inputValue.get("P_BENEFICIARY_BANK_ACCOUNT").toString());
				cs.setBigDecimal(7, (BigDecimal) inputValue.get("P_CUSTOMER_ID"));
				cs.setString(8, inputValue.get("P_SERVICE_GROUP_CODE").toString());
				cs.setBigDecimal(9, (BigDecimal) inputValue.get("P_CURRENCY_ID")); // Out
				// Parameters
				cs.registerOutParameter(10, java.sql.Types.NUMERIC);
				cs.registerOutParameter(11, java.sql.Types.NUMERIC);
				cs.registerOutParameter(12, java.sql.Types.NUMERIC);
				cs.registerOutParameter(13, java.sql.Types.NUMERIC);
				cs.registerOutParameter(14, java.sql.Types.NUMERIC);
				cs.registerOutParameter(15, java.sql.Types.NUMERIC);
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