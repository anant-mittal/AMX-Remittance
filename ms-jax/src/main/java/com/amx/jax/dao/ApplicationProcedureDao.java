package com.amx.jax.dao;

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
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.AddAdditionalBankDataDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.multitenant.MultiTenantConnectionProviderImpl;
import com.amx.jax.util.DBUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApplicationProcedureDao {

	private static Logger LOGGER = Logger.getLogger(ApplicationProcedureDao.class);

	private static String P_BENEFICIARY_MASTER_ID = "P_BENEFICIARY_MASTER_ID";
	private static String P_BENEFICIARY_BANK_ID = "P_BENEFICIARY_BANK_ID";
	private static String P_ROUTING_COUNTRY_ID = "P_ROUTING_COUNTRY_ID";
	private static String OUT_PARAMETERS = "Out put Parameters : ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	MultiTenantConnectionProviderImpl connectionProvider;

	/**
	 * Purpose : toFetchDetilaFromAddtionalBenficiaryDetails
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
	public Map<String, Object> toFetchDetilaFromAddtionalBenficiaryDetails(Map<String, Object> inputValues) {

		BigDecimal beneficaryMasterId = (BigDecimal) inputValues.get(P_BENEFICIARY_MASTER_ID);
		BigDecimal beneficaryBankId = (BigDecimal) inputValues.get(P_BENEFICIARY_BANK_ID);
		BigDecimal beneficaryBankBranchId = (BigDecimal) inputValues.get("P_BENEFICIARY_BRANCH_ID");
		BigDecimal beneAccNumSeqId = (BigDecimal) inputValues.get("P_BENEFICARY_ACCOUNT_SEQ_ID");
		BigDecimal routingCountry = (BigDecimal) inputValues.get(P_ROUTING_COUNTRY_ID);
		BigDecimal routingBank = (BigDecimal) inputValues.get("P_ROUTING_BANK_ID");
		BigDecimal routingBranch = (BigDecimal) inputValues.get("P_ROUTING_BANK_BRANCH_ID");
		BigDecimal serviceMasterId = (BigDecimal) inputValues.get("P_SERVICE_MASTER_ID");
		BigDecimal applicationCountryId = (BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal currencyId = (BigDecimal) inputValues.get("P_CURRENCY_ID");
		BigDecimal remitMode = (BigDecimal) inputValues.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryMode = (BigDecimal) inputValues.get("P_DELIVERY_MODE_ID");
		/** Added by Rabil on 03 May 2018 **/
		BigDecimal beneficaryRelationSeqId = (BigDecimal) inputValues.get("P_BENE_RELATION_SEQ_ID");
		/** Code end Here */
		LOGGER.info("=====EX_GET_ADDL_BENE_DETAILS =Start toFetchDetilaFromAddtionalBenficiaryDetails ");
		LOGGER.info("Procedure Name= EX_GET_ADDL_BENE_DETAILS :" + inputValues.toString());

		Map<String, Object> output = null;
		try {

			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
					new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT), new SqlParameter(Types.BIGINT),
					new SqlOutParameter("P_BENEFICIARY_BANK_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_BRANCH_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_STATE_ID", Types.NUMERIC),
					new SqlOutParameter("P_BENEFICIARY_DISTRICT_ID", Types.NUMERIC),
					new SqlOutParameter("P_BENEFICIARY_CITY_ID", Types.NUMERIC),
					new SqlOutParameter("P_BENEFICIARY_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_FIRST_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_SECOND_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_THIRD_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_FOURTH_NAME", Types.VARCHAR),
					new SqlOutParameter("P_BENEFICIARY_FIFTH_NAME", Types.VARCHAR),
					new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR));

			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {

					String proc = " { call EX_GET_ADDL_BENE_DETAILS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? )} ";
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
					cs.setBigDecimal(13, beneficaryRelationSeqId);
					cs.registerOutParameter(14, java.sql.Types.VARCHAR);
					cs.registerOutParameter(15, java.sql.Types.VARCHAR);
					cs.registerOutParameter(16, java.sql.Types.NUMERIC);
					cs.registerOutParameter(17, java.sql.Types.NUMERIC);
					cs.registerOutParameter(18, java.sql.Types.NUMERIC);
					cs.registerOutParameter(19, java.sql.Types.VARCHAR);
					cs.registerOutParameter(20, java.sql.Types.VARCHAR);
					cs.registerOutParameter(21, java.sql.Types.VARCHAR);
					cs.registerOutParameter(22, java.sql.Types.VARCHAR);
					cs.registerOutParameter(23, java.sql.Types.VARCHAR);
					cs.registerOutParameter(24, java.sql.Types.VARCHAR);
					cs.registerOutParameter(25, java.sql.Types.VARCHAR);
					return cs;
				}

			}, declareInAndOutputParameters);

			LOGGER.info("EX_GET_ADDL_BENE_DETAILS Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			LOGGER.error("error shile getting additional beneficiary details : ", e);
			e.printStackTrace();
		}
		if (output.get("P_ERROR_MESSAGE") != null) {
			throw new GlobalException(output.get("P_ERROR_MESSAGE").toString(), JaxError.TRANSACTION_VALIDATION_FAIL);
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

		LOGGER.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO countryId :" + applCountryId);
		LOGGER.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO companyId :" + companyId);
		LOGGER.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO documentId :" + documentId);
		LOGGER.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO financialYear :" + financialYear);
		LOGGER.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO processIn :" + processIn);
		LOGGER.info("EX_TO_GEN_NEXT_DOC_SERIAL_NO branchId :" + branchId);

		Map<String, Object> output = new HashMap<>();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = connectionProvider.getDataSource().getConnection();
			String proc = "{call EX_TO_GEN_NEXT_DOC_SERIAL_NO(?,?,?,?,?,?,?,?,?)}";
			cs = connection.prepareCall(proc);
			cs.setBigDecimal(1, applCountryId);
			cs.setBigDecimal(2, branchId);
			cs.setBigDecimal(3, companyId);
			cs.setBigDecimal(4, documentId);
			cs.setBigDecimal(5, financialYear);
			cs.setString(6, processIn);
			cs.registerOutParameter(7, java.sql.Types.NUMERIC);
			cs.registerOutParameter(8, java.sql.Types.VARCHAR);
			cs.registerOutParameter(9, java.sql.Types.VARCHAR);
			cs.execute();
			output.put("P_DOC_NO", cs.getBigDecimal(7));
			output.put("P_ERROR_FLAG", cs.getString(8));
			output.put("P_ERROR_MESG", cs.getString(9));

			LOGGER.info(OUT_PARAMETERS + output.toString());

		} catch (DataAccessException | SQLException e) {
			LOGGER.error("error in generate docNo", e);
			LOGGER.info(OUT_PARAMETERS + e.getMessage());
		} finally {
			DBUtil.closeResources(cs, connection);
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
	public Map<String, Object> exPBankIndicatorsProcedureCheck(Map<String, String> inputValues,
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

		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
				new SqlParameter(Types.NUMERIC), // 2
				new SqlParameter(Types.NUMERIC), // 3
				new SqlParameter(Types.NUMERIC), // 4
				new SqlParameter(Types.NUMERIC), // 5
				new SqlParameter(Types.NUMERIC), // 6
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
				cs.setBigDecimal(2, new BigDecimal(inputValues.get(P_ROUTING_COUNTRY_ID)));
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

		LOGGER.info("EX_P_BANK_INDICATORS Out put Parameters :" + output.toString());

		return addtionalProcValues;
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
	public Map<String, Object> getAdditionalCheckProcedure(Map<String, Object> inputValues) {

		LOGGER.info("======Start getAdditionalCheckProcedure EX_APPL_ADDL_CHECKS========:" + inputValues.toString());

		BigDecimal appLicationCountryId = (BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal customerId = (BigDecimal) inputValues.get("P_CUSTOMER_ID");
		BigDecimal branchId = (BigDecimal) inputValues.get("P_BRANCH_ID");
		BigDecimal beneId = (BigDecimal) inputValues.get(P_BENEFICIARY_MASTER_ID);

		BigDecimal beneCountryId = (BigDecimal) inputValues.get("P_BENEFICIARY_COUNTRY_ID");
		BigDecimal beneBankId = (BigDecimal) inputValues.get(P_BENEFICIARY_BANK_ID);
		BigDecimal beneBankBranchId = (BigDecimal) inputValues.get("P_BENEFICIARY_BRANCH_ID");
		String beneAccountNo = inputValues.get("P_BENEFICIARY_ACCOUNT_NO") == null ? null
				: inputValues.get("P_BENEFICIARY_ACCOUNT_NO").toString();
		BigDecimal serviceMasterId = (BigDecimal) inputValues.get("P_SERVICE_MASTER_ID");
		BigDecimal routingCountryId = (BigDecimal) inputValues.get(P_ROUTING_COUNTRY_ID);
		BigDecimal routingBankId = (BigDecimal) inputValues.get("P_ROUTING_BANK_ID");
		BigDecimal routingBankBranchId = (BigDecimal) inputValues.get("P_ROUTING_BANK_BRANCH_ID");
		BigDecimal remittanceModeId = (BigDecimal) inputValues.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) inputValues.get("P_DELIVERY_MODE_ID");
		BigDecimal sourceOfIncomeId = (BigDecimal) inputValues.get("P_SOURCE_OF_INCOME_ID");
		BigDecimal exchangeRateApplied = (BigDecimal) inputValues.get("P_EXCHANGE_RATE_APPLIED");
		BigDecimal localCommisionCurrencyId = (BigDecimal) inputValues.get("P_LOCAL_COMMISION_CURRENCY_ID");
		BigDecimal localCommisionAmount = (BigDecimal) inputValues.get("P_LOCAL_COMMISION_AMOUNT");
		BigDecimal localChargeCurrencyId = (BigDecimal) inputValues.get("P_LOCAL_CHARGE_CURRENCY_ID");
		BigDecimal localchargeAmount = (BigDecimal) inputValues.get("P_LOCAL_CHARGE_AMOUNT");
		BigDecimal localDelivCurrencyId = (BigDecimal) inputValues.get("P_LOCAL_DELIVERY_CURRENCY_ID");
		BigDecimal localDeliAmount = (BigDecimal) inputValues.get("P_LOCAL_DELIVERY_AMOUNT");
		BigDecimal serviceProvider = (BigDecimal) inputValues.get("P_SERVICE_PROVIDER");
		BigDecimal foreignCurrencyId = (BigDecimal) inputValues.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal foreignTrnxAmount = (BigDecimal) inputValues.get("P_FOREIGN_TRANX_AMOUNT");
		BigDecimal localNetCurrecnyId = (BigDecimal) inputValues.get("P_LOCAL_NET_CURRENCY_ID");
		BigDecimal localNetTrnxAmount = (BigDecimal) inputValues.get("P_LOCAL_NET_TRANX_AMOUNT");
		String beneSwiftBank1 = inputValues.get("P_BENEFICIARY_SWIFT_BANK1") == null ? null
				: inputValues.get("P_BENEFICIARY_SWIFT_BANK1").toString();
		String beneSwiftBank2 = inputValues.get("P_BENEFICIARY_SWIFT_BANK2") == null ? null
				: inputValues.get("P_BENEFICIARY_SWIFT_BANK2").toString();

		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
					new SqlParameter(Types.NUMERIC), // 2
					new SqlParameter(Types.NUMERIC), // 3
					new SqlParameter(Types.NUMERIC), // 4
					new SqlParameter(Types.NUMERIC), // 5
					new SqlParameter(Types.VARCHAR), // 6
					new SqlParameter(Types.NUMERIC), // 7
					new SqlParameter(Types.VARCHAR), // 8
					new SqlParameter(Types.NUMERIC), // 9
					new SqlParameter(Types.NUMERIC), // 10
					new SqlParameter(Types.NUMERIC), // 11
					new SqlParameter(Types.NUMERIC), // 12
					new SqlParameter(Types.NUMERIC), // 13
					new SqlParameter(Types.NUMERIC), // 14
					new SqlParameter(Types.NUMERIC), // 15
					new SqlParameter(Types.NUMERIC), // 16
					new SqlParameter(Types.NUMERIC), // 17
					new SqlParameter(Types.NUMERIC), // 18
					new SqlParameter(Types.NUMERIC), // 19
					new SqlParameter(Types.NUMERIC), // 20
					new SqlParameter(Types.NUMERIC), // 21
					new SqlParameter(Types.NUMERIC), // 22
					new SqlParameter(Types.NUMERIC), // 23
					new SqlParameter(Types.NUMERIC), // 24
					new SqlParameter(Types.NUMERIC), // 25
					new SqlParameter(Types.NUMERIC), // 26
					new SqlParameter(Types.NUMERIC), // 27
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

			LOGGER.info("EX_APPL_ADDL_CHECKS Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			LOGGER.error(OUT_PARAMETERS, e);
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
	public Map<String, Object> toFetchPurtherInstractionErrorMessaage(Map<String, Object> inputValues) {

		BigDecimal applicationCountyId = (BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) inputValues.get(P_ROUTING_COUNTRY_ID);
		BigDecimal routingBankId = (BigDecimal) inputValues.get("P_ROUTING_BANK_ID");
		BigDecimal currencyId = (BigDecimal) inputValues.get("P_CURRENCY_ID");
		BigDecimal remittanceId = (BigDecimal) inputValues.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryId = (BigDecimal) inputValues.get("P_DELIVERY_MODE_ID");
		String furtherInstruction = inputValues.get("P_FURTHER_INSTR") == null ? null
				: inputValues.get("P_FURTHER_INSTR").toString();

		LOGGER.info("=====EX_P_FURTHER_INSTRe ========" + inputValues.toString());

		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
					new SqlParameter(Types.NUMERIC), // 2
					new SqlParameter(Types.NUMERIC), // 3
					new SqlParameter(Types.NUMERIC), // 4
					new SqlParameter(Types.NUMERIC), // 5
					new SqlParameter(Types.NUMERIC), // 6
					new SqlParameter(Types.VARCHAR), // 7
					new SqlParameter(Types.NUMERIC), // 8
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
					return cs;
				}

			}, declareInAndOutputParameters);

			LOGGER.info("EX_P_FURTHER_INSTR Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			LOGGER.error(OUT_PARAMETERS + e);
			e.printStackTrace();
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
	public Map<String, Object> toFetchSwiftBankProcedure(Map<String, Object> inputValues) {

		BigDecimal applicationCountyId = (BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) inputValues.get(P_ROUTING_COUNTRY_ID);
		BigDecimal currencyId = (BigDecimal) inputValues.get("P_CURRENCY_ID");
		BigDecimal remittanceId = (BigDecimal) inputValues.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryId = (BigDecimal) inputValues.get("P_DELIVERY_MODE_ID");
		BigDecimal beneBankCountryId = (BigDecimal) inputValues.get("P_BENE_BANK_COUNTRY_ID");
		String fieldName = inputValues.get("P_FIELD_NAME") == null ? null : inputValues.get("P_FIELD_NAME").toString();
		String beneficiarySwiftBank = inputValues.get("P_BENEFICIARY_SWIFT_CODE") == null ? null
				: inputValues.get("P_BENEFICIARY_SWIFT_CODE").toString();

		LOGGER.info("Procedure Name = EX_P_CHECK_SWIFT_BANK :" + inputValues.toString());

		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
					new SqlParameter(Types.NUMERIC), // 2
					new SqlParameter(Types.NUMERIC), // 3
					new SqlParameter(Types.NUMERIC), // 4
					new SqlParameter(Types.NUMERIC), // 5
					new SqlParameter(Types.VARCHAR), // 6
					new SqlParameter(Types.VARCHAR), // 7
					new SqlParameter(Types.NUMERIC), // 8
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

			LOGGER.info("EX_P_CHECK_SWIFT_BANK Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			LOGGER.error(OUT_PARAMETERS, e);
		}
		return output;
	}

	/**
	 * purpose : Move application to remittance after successfull knet
	 * 
	 * @param applicationCountryId
	 * 
	 */

	@Transactional
	public Map<String, Object> insertRemittanceOnlineProcedure(Map<String, Object> inputValues) {

		BigDecimal applicationCountryId = (BigDecimal) inputValues.get("P_APPL_CNTY_ID");
		BigDecimal companyId = (BigDecimal) inputValues.get("P_COMPANY_ID");
		BigDecimal customerNo = (BigDecimal) inputValues.get("P_CUSTOMER_ID");
		String userName = inputValues.get("P_USER_NAME") == null ? null : inputValues.get("P_USER_NAME").toString();
		String paymentId = inputValues.get("P_PAYMENT_ID") == null ? "" : inputValues.get("P_PAYMENT_ID").toString();
		String authcode = inputValues.get("P_AUTHCOD") == null ? "" : inputValues.get("P_AUTHCOD").toString();
		String tranId = inputValues.get("P_TRANID") == null ? "" : inputValues.get("P_TRANID").toString();
		String refId = inputValues.get("P_REFID") == null ? "" : inputValues.get("P_REFID").toString();

		LOGGER.info("saveRemittance EX_INSERT_REMITTANCE_ONLINE getCustomerNo():" + inputValues.toString());

		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
					new SqlParameter(Types.NUMERIC), // 2
					new SqlParameter(Types.NUMERIC), // 3
					new SqlParameter(Types.VARCHAR), // 4
					new SqlParameter(Types.VARCHAR), // 5
					new SqlParameter(Types.VARCHAR), // 6
					new SqlParameter(Types.VARCHAR), // 7
					new SqlParameter(Types.VARCHAR), // 8
					new SqlOutParameter("P_COLLECT_FINYR", Types.NUMERIC), // 9
					new SqlOutParameter("P_COLLECTION_NO", Types.NUMERIC), // 10
					new SqlOutParameter("P_COLLECTION_DOCUMENT_CODE", Types.NUMERIC), // 11
					new SqlOutParameter("P_ERROR_MESG", Types.VARCHAR));// 12

			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_INSERT_REMITTANCE_ONLINE (?,?,?,?,?,?,?,?,?,?,?,?)}";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, applicationCountryId);
					cs.setBigDecimal(2, companyId);
					cs.setBigDecimal(3, customerNo);
					cs.setString(4, userName);
					cs.setString(5, paymentId);
					cs.setString(6, authcode);
					cs.setString(7, tranId);
					cs.setString(8, refId);
					cs.registerOutParameter(9, java.sql.Types.BIGINT);
					cs.registerOutParameter(10, java.sql.Types.BIGINT);
					cs.registerOutParameter(11, java.sql.Types.BIGINT);
					cs.registerOutParameter(12, java.sql.Types.VARCHAR);
					cs.executeQuery();
					return cs;
				}

			}, declareInAndOutputParameters);

			LOGGER.info("EX_INSERT_REMITTANCE_ONLINE Out put Parameters :" + output.toString());

		} catch (Exception e) {
			LOGGER.error(OUT_PARAMETERS, e);
		}
		return output;
	}

	/**
	 * 
	 * @param inputValues
	 * @return :Transfer from JAVA to OLD EMOS table
	 */
	@Transactional
	public Map<String, Object> insertEMOSLIVETransfer(Map<String, Object> inputValues) {

		BigDecimal applicationCountryId = (BigDecimal) inputValues.get("P_APPL_CNTY_ID");
		BigDecimal companyId = (BigDecimal) inputValues.get("P_COMPANY_ID");
		BigDecimal documentId = (BigDecimal) inputValues.get("P_DOCUMENT_ID");
		BigDecimal financialYr = (BigDecimal) inputValues.get("P_DOC_FINYR");
		BigDecimal documentNo = (BigDecimal) inputValues.get("P_DOCUMENT_NO");

		LOGGER.info("saveRemittance EX_INSERT_REMITTANCE_ONLINE getCustomerNo():" + inputValues.toString());

		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
					new SqlParameter(Types.NUMERIC), // 2
					new SqlParameter(Types.NUMERIC), // 3
					new SqlParameter(Types.VARCHAR), // 4
					new SqlParameter(Types.VARCHAR), // 5
					new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR));// 6

			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_INSERT_EMOS_TRANSFER_LIVE(?, ?, ?, ?, ?,?)}";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, applicationCountryId);
					cs.setBigDecimal(2, companyId);
					cs.setBigDecimal(3, documentId);
					cs.setBigDecimal(4, financialYr);
					cs.setBigDecimal(5, documentNo);
					cs.registerOutParameter(6, java.sql.Types.VARCHAR);
					cs.execute();
					return cs;
				}

			}, declareInAndOutputParameters);

			LOGGER.info("EX_INSERT_EMOS_TRANSFER_LIVE Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			LOGGER.error(OUT_PARAMETERS, e);
		}
		return output;
	}

	/**
	 * fetchAdditionalBankRuleIndicators
	 */
	@Transactional
	public Map<String, Object> fetchAdditionalBankRuleIndicators(Map<String, Object> inputValues) {
		LOGGER.info("EX_REMIT_ADDL_INFO :" + inputValues.toString());
		Map<String, Object> output = null;

		try {

			BigDecimal applicationCountryId = (BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
			BigDecimal serviceMasterId = (BigDecimal) inputValues.get("P_SERVICE_MASTER_ID");
			BigDecimal routingCountryId = (BigDecimal) inputValues.get(P_ROUTING_COUNTRY_ID);
			BigDecimal routingBankId = (BigDecimal) inputValues.get("P_ROUTING_BANK_ID");
			BigDecimal routingBankBranchId = (BigDecimal) inputValues.get("P_ROUTING_BANK_BRANCH_ID");
			BigDecimal deliveryModeId = (BigDecimal) inputValues.get("P_DELIVERY_MODE_ID");
			BigDecimal remittanceModeId = (BigDecimal) inputValues.get("P_REMITTANCE_MODE_ID");
			BigDecimal foreignCurrencyId = (BigDecimal) inputValues.get("P_FOREIGN_CURRENCY_ID");
			BigDecimal foreignAmount = (BigDecimal) inputValues.get("P_FOREIGN_AMOUNT");
			BigDecimal customerId = (BigDecimal) inputValues.get("P_CUSTOMER_ID");
			BigDecimal beneficaryAccountSeqId = (BigDecimal) inputValues.get("P_BENEFICARY_ACCOUNT_SEQ_ID");

			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
					new SqlParameter(Types.NUMERIC), // 2
					new SqlParameter(Types.NUMERIC), // 3
					new SqlParameter(Types.NUMERIC), // 3
					new SqlParameter(Types.NUMERIC), // 5
					new SqlParameter(Types.NUMERIC), // 6
					new SqlParameter(Types.NUMERIC), // 7
					new SqlParameter(Types.NUMERIC), // 8
					new SqlParameter(Types.NUMERIC), // 9
					new SqlParameter(Types.NUMERIC), // 10
					new SqlParameter(Types.NUMERIC), // 11

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
					cs.setBigDecimal(11, beneficaryAccountSeqId);
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

			LOGGER.info("EX_REMIT_ADDL_INFO Out put Parameters :" + output.toString());

		} catch (DataAccessException e) {
			LOGGER.info(OUT_PARAMETERS, e);
		}
		return output;
	}

	public Map<String, Object> getRoutingDetails(Map<String, Object> inputValue) {

		LOGGER.info("In getRoutingDetails params:" + inputValue.toString());

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> output = new HashMap<>();
		try {
			connection = connectionProvider.getDataSource().getConnection();
			String proc = " { call EX_GET_ROUTING_SETUP_ONLINE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) } ";
			cs = connection.prepareCall(proc);
			// In Parameters
			cs.setBigDecimal(1, (BigDecimal) inputValue.get("P_APPLICATION_COUNTRY_ID"));
			cs.setString(2, inputValue.get("P_USER_TYPE").toString());
			cs.setBigDecimal(3, (BigDecimal) inputValue.get("P_BENEFICIARY_COUNTRY_ID"));
			cs.setBigDecimal(4, (BigDecimal) inputValue.get(P_BENEFICIARY_BANK_ID));
			cs.setBigDecimal(5, (BigDecimal) inputValue.get("P_BENEFICIARY_BRANCH_ID"));
			cs.setString(6, inputValue.get("P_SERVICE_GROUP_CODE").toString());
			cs.setBigDecimal(7, (BigDecimal) inputValue.get("P_CURRENCY_ID"));
			cs.setBigDecimal(8, (BigDecimal) inputValue.get("P_LOCAL_AMT"));
			cs.setBigDecimal(9, (BigDecimal) inputValue.get("P_FOREIGN_AMT"));
			// Out Parameters
			cs.registerOutParameter(10, java.sql.Types.NUMERIC);
			cs.registerOutParameter(11, java.sql.Types.NUMERIC);
			cs.registerOutParameter(12, java.sql.Types.NUMERIC);
			cs.registerOutParameter(13, java.sql.Types.NUMERIC);
			cs.registerOutParameter(14, java.sql.Types.NUMERIC);
			cs.registerOutParameter(15, java.sql.Types.NUMERIC);
			cs.registerOutParameter(16, java.sql.Types.VARCHAR);
			cs.registerOutParameter(17, java.sql.Types.VARCHAR);
			cs.execute();
			output.put("P_SERVICE_MASTER_ID", cs.getBigDecimal(10));
			output.put(P_ROUTING_COUNTRY_ID, cs.getBigDecimal(11));
			output.put("P_ROUTING_BANK_ID", cs.getBigDecimal(12));
			output.put("P_ROUTING_BANK_BRANCH_ID", cs.getBigDecimal(13));
			output.put("P_REMITTANCE_MODE_ID", cs.getBigDecimal(14));
			output.put("P_DELIVERY_MODE_ID", cs.getBigDecimal(15));
			output.put("P_SWIFT", cs.getString(16));
			output.put("P_ERROR_MESSAGE", cs.getString(17));
		} catch (DataAccessException | SQLException e) {
			LOGGER.error("error in getRoutingDetails", e);
			LOGGER.info(OUT_PARAMETERS + e.getMessage());
		} finally {
			DBUtil.closeResources(cs, connection);
		}

		LOGGER.info(OUT_PARAMETERS + output.toString());

		return output;
	}

	/**
	 * 
	 * @param inputValues
	 * @return : For banned bank check
	 */

	public Map<String, Object> getBannedBankCheckProcedure(Map<String, Object> inputValues) {
		BigDecimal applicationCountryId = (BigDecimal) inputValues.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal beneBankId = (BigDecimal) inputValues.get(P_BENEFICIARY_BANK_ID);
		BigDecimal beneMasSeqId = (BigDecimal) inputValues.get(P_BENEFICIARY_MASTER_ID);

		LOGGER.info("saveRemittance EX_P_BANNED_BANK_CHECK getCustomerNo():" + inputValues.toString());

		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
					new SqlParameter(Types.NUMERIC), // 2
					new SqlParameter(Types.NUMERIC), // 3
					new SqlOutParameter("P_ALERT_MESSAGE", Types.VARCHAR), // 4
					new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR), // 5
					new SqlOutParameter("P_BLIST_IND", Types.VARCHAR));// 6
			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_P_BANNED_BANK_CHECK(?,?,?,?,?,?)} ";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, applicationCountryId);
					cs.setBigDecimal(2, beneBankId);
					cs.setBigDecimal(3, beneMasSeqId);
					cs.registerOutParameter(4, java.sql.Types.VARCHAR);
					cs.registerOutParameter(5, java.sql.Types.VARCHAR);
					cs.registerOutParameter(6, java.sql.Types.VARCHAR);
					cs.execute();
					return cs;
				}

			}, declareInAndOutputParameters);
			LOGGER.info("EX_P_BANNED_BANK_CHECK Out put Parameters :" + output.toString());
		} catch (DataAccessException e) {
			LOGGER.info(OUT_PARAMETERS, e);
		}
		return output;
	}

	public Map<String, Object> callProcedureCustReferenceNumber(BigDecimal companyCode, BigDecimal documentCode,
			BigDecimal docFinYear, String branchId) {

		LOGGER.info("!!!!!!callProcedureCustReferenceNumber UPDNXT" + companyCode);
		Map<String, Object> output = null;

		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC), // 1
					new SqlParameter(Types.NUMERIC), // 2
					new SqlParameter(Types.NUMERIC), // 3
					new SqlOutParameter("P_DOCNO", Types.NUMERIC), // 4
					new SqlParameter(Types.NUMERIC) // 5
			);
			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = " { call UPDNXT (?, ?, ?, ?, ?) } ";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, companyCode);
					cs.setBigDecimal(2, documentCode);
					cs.setBigDecimal(3, new BigDecimal(2001));
					cs.registerOutParameter(4, java.sql.Types.NUMERIC);
					if (companyCode != null && companyCode.equals((new BigDecimal(20)))) {
						cs.setBigDecimal(5, new BigDecimal(1));
					} else if (companyCode != null && companyCode.equals((new BigDecimal(21)))) {
						cs.setBigDecimal(5, new BigDecimal(99));
					}
					cs.execute();
					return cs;
				}

			}, declareInAndOutputParameters);
			LOGGER.info("UPDNXT Out put Parameters :" + output.toString());
		} catch (DataAccessException e) {
			LOGGER.info(OUT_PARAMETERS, e);
		}
		return output;
	}

	public Map<String, Object> getRoutingDetailFromOthProcedure(Map<String, Object> inputValue) {

		LOGGER.info("In getRoutingDetails params:" + inputValue.toString());

		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC),
				new SqlParameter(Types.VARCHAR), new SqlParameter(Types.NUMERIC), new SqlParameter(Types.NUMERIC),
				new SqlParameter(Types.NUMERIC), new SqlParameter(Types.VARCHAR), new SqlParameter(Types.NUMERIC),
				new SqlParameter(Types.VARCHAR), new SqlParameter(Types.NUMERIC));
		List<SqlParameter> ouptutParams = new ArrayList<>();
		ouptutParams.addAll(declareInAndOutputParameters);
		String[] outParams = { "P_SERVICE_MASTER_ID", P_ROUTING_COUNTRY_ID, "P_ROUTING_BANK_ID",
				"P_ROUTING_BANK_BRANCH_ID", "P_REMITTANCE_MODE_ID", "P_DELIVERY_MODE_ID", "P_SWIFT",
				"P_ERROR_MESSAGE" };
		for (int i = 1; i <= 8; i++) {
			ouptutParams.add(new SqlOutParameter(outParams[i - 1], Types.NUMERIC));
		}

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> output = new HashMap<>();
		try {
			connection = connectionProvider.getDataSource().getConnection();

			String proc = " { call EX_GET_ROUTING_SET_UP_OTH (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) } ";
			cs = connection.prepareCall(proc);
			// In Parameters
			cs.setBigDecimal(1, (BigDecimal) inputValue.get("P_APPLICATION_COUNTRY_ID"));
			cs.setString(2, inputValue.get("P_USER_TYPE").toString());
			cs.setBigDecimal(3, (BigDecimal) inputValue.get("P_BENEFICIARY_COUNTRY_ID"));
			cs.setBigDecimal(4, (BigDecimal) inputValue.get(P_BENEFICIARY_BANK_ID));
			cs.setBigDecimal(5, (BigDecimal) inputValue.get("P_BENEFICIARY_BRANCH_ID"));
			// cs.setString(6, inputValue.get("P_BENEFICIARY_BANK_ACCOUNT").toString());
			cs.setString(6, inputValue.get("P_BENEFICIARY_BANK_ACCOUNT") == null ? null
					: inputValue.get("P_BENEFICIARY_BANK_ACCOUNT").toString());
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
			output.put("P_SERVICE_MASTER_ID", cs.getBigDecimal(10));
			output.put(P_ROUTING_COUNTRY_ID, cs.getBigDecimal(11));
			output.put("P_ROUTING_BANK_ID", cs.getBigDecimal(12));
			output.put("P_ROUTING_BANK_BRANCH_ID", cs.getBigDecimal(13));
			output.put("P_REMITTANCE_MODE_ID", cs.getBigDecimal(14));
			output.put("P_DELIVERY_MODE_ID", cs.getBigDecimal(15));
			output.put("P_SWIFT", cs.getString(16));
			output.put("P_ERROR_MESSAGE", cs.getString(17));
		} catch (DataAccessException | SQLException e) {
			LOGGER.error("error in getRoutingDetailFromOthProcedure", e);
			LOGGER.info(OUT_PARAMETERS + e.getMessage());
		} finally {
			DBUtil.closeResources(cs, connection);
		}

		LOGGER.info(OUT_PARAMETERS + output.toString());

		return output;
	}


	public Map<String, Object> getRoutingDetailFromOthRateProcedure(Map<String, Object> inputValue) {

		LOGGER.info("In getRoutingDetails params:" + inputValue.toString());

		Connection connection = null;
		CallableStatement cs = null;
		Map<String, Object> output = new HashMap<>();
		try {
			connection = connectionProvider.getDataSource().getConnection();

			String proc = " { call EX_GET_ROUTING_SET_UP_OTH_RATE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) } ";
			cs = connection.prepareCall(proc);
			// In Parameters
			cs.setBigDecimal(1, (BigDecimal) inputValue.get("P_APPLICATION_COUNTRY_ID"));
			cs.setString(2, inputValue.get("P_USER_TYPE").toString());
			cs.setBigDecimal(3, (BigDecimal) inputValue.get("P_BENEFICIARY_COUNTRY_ID"));
			cs.setBigDecimal(4, (BigDecimal) inputValue.get(P_BENEFICIARY_BANK_ID));
			cs.setBigDecimal(5, (BigDecimal) inputValue.get("P_BENEFICIARY_BRANCH_ID"));
			// cs.setString(6, inputValue.get("P_BENEFICIARY_BANK_ACCOUNT").toString());
			cs.setString(6, inputValue.get("P_BENEFICIARY_BANK_ACCOUNT") == null ? null
					: inputValue.get("P_BENEFICIARY_BANK_ACCOUNT").toString());
			cs.setBigDecimal(7, (BigDecimal) inputValue.get("P_CUSTOMER_ID"));
			cs.setString(8, inputValue.get("P_SERVICE_GROUP_CODE").toString());
			cs.setBigDecimal(9, (BigDecimal) inputValue.get("P_CURRENCY_ID"));
			cs.setBigDecimal(10, (BigDecimal) inputValue.get("P_FOREIGN_AMT"));
			cs.setBigDecimal(11, (BigDecimal) inputValue.get("P_LOCAL_AMT"));
			// Out
			// Parameters
			cs.registerOutParameter(12, java.sql.Types.NUMERIC);
			cs.registerOutParameter(13, java.sql.Types.NUMERIC);
			cs.registerOutParameter(14, java.sql.Types.NUMERIC);
			cs.registerOutParameter(15, java.sql.Types.NUMERIC);
			cs.registerOutParameter(16, java.sql.Types.NUMERIC);
			cs.registerOutParameter(17, java.sql.Types.NUMERIC);
			cs.registerOutParameter(18, java.sql.Types.VARCHAR);
			cs.registerOutParameter(19, java.sql.Types.NUMERIC);
			cs.registerOutParameter(20, java.sql.Types.VARCHAR);
			cs.execute();
			output.put("P_SERVICE_MASTER_ID", cs.getBigDecimal(12));
			output.put(P_ROUTING_COUNTRY_ID, cs.getBigDecimal(13));
			output.put("P_ROUTING_BANK_ID", cs.getBigDecimal(14));
			output.put("P_ROUTING_BANK_BRANCH_ID", cs.getBigDecimal(15));
			output.put("P_REMITTANCE_MODE_ID", cs.getBigDecimal(16));
			output.put("P_DELIVERY_MODE_ID", cs.getBigDecimal(17));
			output.put("P_SWIFT", cs.getString(18));
			output.put("P_DERIVED_SELL_RATE", cs.getBigDecimal(19));
			output.put("P_ERROR_MESSAGE", cs.getString(20));
		} catch (DataAccessException | SQLException e) {
			LOGGER.error("error in getRoutingDetailFromOthRateProcedure", e);
			LOGGER.info(OUT_PARAMETERS + e.getMessage());
		} finally {
			DBUtil.closeResources(cs, connection);
		}

		LOGGER.info(OUT_PARAMETERS + output.toString());

		return output;
	}
}
