package com.amx.jax.pricer.repository.custom;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import oracle.jdbc.driver.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExchRatePopulateProcDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> callProcedurePopulateExchRate(BigDecimal applCountryId, String[] ruleIds) {

		Map<String, Object> output = null;

		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(
				new SqlParameter("P_APPLICATION_COUNTRY_ID", Types.NUMERIC), // 1
				new SqlParameter("P_RULE_ID", Types.ARRAY, "EXCH_RATE_RULE_ID") // 2
		);

		output = jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				// if (con.isWrapperFor(OracleConnection.class)){
				OracleConnection oCon = con.unwrap(OracleConnection.class);
				// }

				String proc = " { call EX_P_POPULATE_EXRATE_APRDET (?, ?) } ";
				CallableStatement cs = con.prepareCall(proc);

				// Array sArray = con.createArrayOf("EXCH_RATE_RULE_ID", ruleIds);
				ArrayDescriptor des = ArrayDescriptor.createDescriptor("EXCH_RATE_RULE_ID", oCon);
				ARRAY cArray = new ARRAY(des, oCon, ruleIds);

				cs.setBigDecimal(1, applCountryId);
				cs.setArray(2, cArray);

				cs.execute();
				return cs;
			}

		}, declareInAndOutputParameters);

		return output;
	}

}
