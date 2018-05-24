package com.amx.jax.dal;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
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
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApplicationCoreProcedureDao {

	private static Logger LOGGER = Logger.getLogger(ApplicationCoreProcedureDao.class);

	private static String OUT_PARAMETERS = "Out put Parameters : ";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> callProcedureCustReferenceNumber(BigDecimal companyCode, BigDecimal documentCode,
			BigDecimal docFinYear) {

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
					cs.setBigDecimal(3, docFinYear);
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
}
