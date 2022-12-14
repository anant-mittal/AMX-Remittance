package com.amx.jax.dal;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author : Rabil
 * Date	   : 	
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MigrationProcedure {
	
	private Logger logger = Logger.getLogger(MigrationProcedure.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	/**
	 * 
	 * @param cusref
	 * @return :Migration of Beneficiary
	 */
	@Transactional
	public String migrationBeneficiaryProcedure(final BigDecimal cusref) {
		String errorMsg = null;
		try {
			logger.info("start input parameters :"+cusref);
			List<SqlParameter> declaredInAndOutParameters = Arrays.asList(new SqlParameter(Types.BIGINT),new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR),new SqlParameter(Types.VARCHAR));
			Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {

					String proc = " { call MIG_BENEFICARY_MASTER_NEW (?,?) } ";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, cusref); 
					cs.registerOutParameter(2, Types.VARCHAR);
					cs.execute();
					return cs;
				}

			}, declaredInAndOutParameters);
			errorMsg = output.get("P_ERROR_MESSAGE").toString();
			logger.info("MIG_BENEFICARY_MASTER_NEW = " + declaredInAndOutParameters.get(0)+"\t P_ERROR_MESSAGE :"+errorMsg);
		} catch (Exception e) {
			logger.error("error in decrypt", e);
		}
		return errorMsg;
	}
	
	/**
	 * Migration Bank
	 */
	public String migrationBeneBank(final BigDecimal cusref) {
		String errorMsg = null;
		try {
			logger.info("start input parameters :"+cusref);
			List<SqlParameter> declaredInAndOutParameters = Arrays.asList(new SqlParameter(Types.BIGINT),new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR),new SqlParameter(Types.VARCHAR));
			Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {

					String proc = " { call MIG_CUSMAS_BNK (?,?) } ";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, cusref); 
					cs.registerOutParameter(2, Types.VARCHAR);
					cs.execute();
					return cs;
				}

			}, declaredInAndOutParameters);
			errorMsg = output.get("P_ERROR_MESSAGE").toString();
			logger.info("MIG_CUSMAS_BNK = " + declaredInAndOutParameters.get(0)+"\t P_ERROR_MESSAGE :"+errorMsg);
		} catch (Exception e) {
			logger.error("error in decrypt", e);
		}
		return errorMsg;
	}
	
	
}
