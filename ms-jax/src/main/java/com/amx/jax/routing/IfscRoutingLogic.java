package com.amx.jax.routing;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.amx.jax.dal.ExchangeRateProcedureDao;

public class IfscRoutingLogic implements IRoutingLogic {

	private static final Logger LOGGER = LoggerFactory.getLogger(IfscRoutingLogic.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void apply(Map<String, Object> input, Map<String, Object> output) {
		LOGGER.info("in ifsc routing logic with input, {}", input);
		
		LOGGER.info("in ifsc routing logic with output, {}", output);
	}

}
