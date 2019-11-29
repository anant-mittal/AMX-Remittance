package com.amx.jax.pricer.repository.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.data.jdbc.support.oracle.SqlArrayValue;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.transaction.annotation.Transactional;

import oracle.jdbc.internal.OracleTypes;

@Transactional
public class ExchRatePopulateProcedure extends StoredProcedure {

	SimpleJdbcCall exchRatePopulateCall;

	public ExchRatePopulateProcedure(DataSource dataSource) {
		super(dataSource, "EX_P_POPULATE_EXRATE_APRDET");
		
		declareParameter(new SqlParameter("P_APPLICATION_COUNTRY_ID", OracleTypes.NUMERIC));
		declareParameter(new SqlParameter("P_RULE_ID", Types.ARRAY, "EXCH_RATE_RULE_ID"));

		this.exchRatePopulateCall = new SimpleJdbcCall(dataSource).withProcedureName("EX_P_POPULATE_EXRATE_APRDET")
				.withoutProcedureColumnMetaDataAccess()
				.declareParameters(new SqlParameter("P_APPLICATION_COUNTRY_ID", Types.NUMERIC),
						new SqlParameter("P_RULE_ID", Types.ARRAY, "EXCH_RATE_RULE_ID"));
	}

	public void execute(BigDecimal applCountryId, String[] ruleIds) {

		SqlArrayValue<String> sqlArray = new SqlArrayValue<String>(ruleIds, "EXCH_RATE_RULE_ID");

		Map<String, Object> in = new HashMap<String, Object>();
		in.put("P_APPLICATION_COUNTRY_ID", applCountryId);
		in.put("P_RULE_ID", sqlArray);

		this.execute(in);

	}

	public void populateExchRates(BigDecimal applCountryId, String[] ruleIds) {
		SqlArrayValue<String> sqlArray = new SqlArrayValue<String>(ruleIds, "EXCH_RATE_RULE_ID");

		Map<String, Object> in = new HashMap<String, Object>();
		in.put("P_APPLICATION_COUNTRY_ID", applCountryId);
		in.put("P_RULE_ID", sqlArray);

		this.exchRatePopulateCall.execute(in);
	}

}
