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
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.util.DateUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoyaltyInsuranceProDao {
	private Logger logger = Logger.getLogger(LoyaltyInsuranceProDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	LoyaltyInsuranceProDao loyaltyInsuranceProDao;

	@Transactional
	public Map<String, Object> loyaltyInsuranceProcedure(final BigDecimal customerReference,
			final String documentDate) {

		logger.info("In put Parameters : customerReference :" + customerReference + "\t documentDate :" + documentDate);

		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.BIGINT),
				new SqlParameter(Types.DATE), new SqlOutParameter("P_LTY_STR1", Types.VARCHAR),
				new SqlOutParameter("P_LTY_STR2", Types.VARCHAR), new SqlOutParameter("P_INS_STR1", Types.VARCHAR),
				new SqlOutParameter("P_INS_STR2", Types.VARCHAR), new SqlOutParameter("P_INS_STR_AR1", Types.VARCHAR),
				new SqlOutParameter("P_INS_STR_AR2", Types.VARCHAR));
		Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				String proc = " { call JAX_EX_P_REMIT_RECEIPT_REPORT (?, ?, ?, ?, ?, ?, ?, ?) } ";
				CallableStatement cs = con.prepareCall(proc);
				cs.setBigDecimal(1, customerReference);
				cs.setDate(2, DateUtil.convretStringToSqlDate(documentDate));
				cs.registerOutParameter(3, java.sql.Types.VARCHAR);
				cs.registerOutParameter(4, java.sql.Types.VARCHAR);
				cs.registerOutParameter(5, java.sql.Types.VARCHAR);
				cs.registerOutParameter(6, java.sql.Types.VARCHAR);
				cs.registerOutParameter(7, java.sql.Types.VARCHAR);
				cs.registerOutParameter(8, java.sql.Types.VARCHAR);
				cs.execute();
				return cs;
			}

		}, declareInAndOutputParameters);

		logger.info("Out put Parameters :" + output.toString());

		return output;

	}

}
