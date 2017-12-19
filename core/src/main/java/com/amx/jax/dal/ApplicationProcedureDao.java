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

@Component
public class ApplicationProcedureDao {
	
	private Logger logger = Logger.getLogger(ApplicationProcedureDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	@Transactional
	public HashMap<String, Object> toFetchDetilaFromAddtionalBenficiaryDetails(BigDecimal beneficaryMasterId, BigDecimal beneficaryBankId, BigDecimal beneficaryBankBranchId,
			BigDecimal beneAccNumSeqId, BigDecimal routingCountry, BigDecimal routingBank, BigDecimal routingBranch, BigDecimal serviceMasterId, BigDecimal applicationCountryId,
			BigDecimal currencyId, BigDecimal remitMode, BigDecimal deliveryMode) {
		HashMap<String, Object> addtionalProcValues = new HashMap<>();
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
		
		
		
		
		Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
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
		
		
		return addtionalProcValues;
	}
	
	//int countryId, int companyId, int documentId, int financialYear, String processIn, BigDecimal branchId
	
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
	
/*	
	@Transactional
	public HashMap<String ,Object> exPBankIndicatorsProcedureCheck(HashMap<String, String> inputValues, List<AddAdditionalBankData> listAdditionalBankDataTable){
		HashMap<String, Object> addtionalProcValues = new HashMap<>();
		
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
		Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				
				String proc = " { call call EX_P_BANK_INDICATORS (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
				CallableStatement cs = con.prepareCall(proc);
				cs.setBigDecimal(1, beneficaryMasterId);
				cs.setBigDecimal(2, beneficaryBankId);
				cs.setBigDecimal(3, beneficaryBankBranchId);
				cs.setBigDecimal(4, beneAccNumSeqId);
				cs.setBigDecimal(5, routingCountry);
				cs.setBigDecimal(6, routingBank);
				
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
		
		
		return addtionalProcValues;
		
	
	}*/
}
