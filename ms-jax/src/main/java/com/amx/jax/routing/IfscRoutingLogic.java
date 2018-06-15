package com.amx.jax.routing;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class IfscRoutingLogic implements IRoutingLogic {

	private static final Logger LOGGER = LoggerFactory.getLogger(IfscRoutingLogic.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void apply(Map<String, Object> input, Map<String, Object> output) {
		LOGGER.info("in ifsc routing logic with input, {}", input);
		BigDecimal rouringBankId = null;
		BigDecimal routingCountryId = (BigDecimal) input.get("P_ROUTING_COUNTRY_ID");
		BigDecimal beneBankId = (BigDecimal) input.get("P_BENEFICIARY_BANK_ID");
		try {
			rouringBankId = jdbcTemplate.queryForObject("SELECT   ROUTING_BANK_ID"
					+ "          FROM     EX_ROUTING_HEADER" + "          WHERE    SERVICE_MASTER_ID = 102"
					+ "          AND      ROUTING_COUNTRY_ID =  94 " + "          AND      NVL(ISACTIVE,'')='Y' ;",
					BigDecimal.class);
		} catch (Exception e) {
			LOGGER.error("Duplicate Routing setup  for  Indian Bank", e.getMessage());
		}

		LOGGER.info("in ifsc routing logic with output, {}", output);
	}

	@Override
	public boolean isApplicable() {
		return false;
	}

}
