package com.amx.jax.pricer.repository.custom;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.object.StoredProcedure;

//@Transactional
public class ExchRatePopulateProcedure extends StoredProcedure {

	SimpleJdbcCall exchRatePopulateCall;

	public ExchRatePopulateProcedure(DataSource dataSource) {
		super(dataSource, "EX_P_POPULATE_EXRATE_APRDET");

		// declareParameter(new SqlParameter("P_APPLICATION_COUNTRY_ID",
		// Types.NUMERIC));
		// declareParameter(new SqlParameter("P_STRING", Types.VARCHAR));
		// declareParameter(new SqlParameter("P_APPROVED_BY", Types.VARCHAR));

		this.exchRatePopulateCall = new SimpleJdbcCall(dataSource).withProcedureName("EX_P_POPULATE_EXRATE_APRDET")
				.withoutProcedureColumnMetaDataAccess()
				.declareParameters(new SqlParameter("P_APPLICATION_COUNTRY_ID", Types.NUMERIC),
						new SqlParameter("P_STRING", Types.VARCHAR),
						new SqlParameter("P_APPROVED_BY", Types.VARCHAR));

	}

	public void execute(BigDecimal applCountryId, String[] ruleIds, String approvedBy) {

		// SqlArrayValue<String> sqlArray = new SqlArrayValue<String>(ruleIds,
		// "EXCH_RATE_RULE_ID");

		if (ruleIds == null || ruleIds.length == 0) {
			return;
		}

		String concat = String.join(",", ruleIds);

		Map<String, Object> in = new HashMap<String, Object>();
		in.put("P_APPLICATION_COUNTRY_ID", applCountryId);
		in.put("P_STRING", concat);
		in.put("P_APPROVED_BY", approvedBy);

		this.execute(in);

	}

	public void populateExchRates(BigDecimal applCountryId, String[] ruleIds, String approvedBy) {

		if (ruleIds == null || ruleIds.length == 0) {
			return;
		}

		String concat = String.join(",", ruleIds);
		Map<String, Object> in = new HashMap<String, Object>();
		in.put("P_APPLICATION_COUNTRY_ID", applCountryId);
		in.put("P_STRING", concat);
		in.put("P_APPROVED_BY", approvedBy);

		this.exchRatePopulateCall.execute(in);
	}

}
