package com.amx.jax.logger.util;

import static com.amx.jax.logger.util.ParameterName.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.amx.jax.logger.model.ApplicationLog;
import com.amx.jax.logger.model.CustomerLog;

/**
 * Constructs log messages out of request parameters.
 * <p/>
 */
public class LogMessageFactory {

	/**
	 * Create ApplicationLog out of map of parameters.
	 *
	 * @param originalParams
	 *            map of parameters
	 * @return Application log object
	 */
	public static ApplicationLog createApplicationLogMessage(Map<String, String[]> originalParams) {
		ApplicationLog applicationLog = new ApplicationLog(getValue(originalParams, APP_NAME),
				getValue(originalParams, MODULE_NAME), getValue(originalParams, THREAD_NO),
				getValue(originalParams, MESSAGE), getValue(originalParams, LOG_TYPE));

		return applicationLog;
	}

	/**
	 * Create CustomerLog out of map of parameters.
	 *
	 * @param originalParams
	 *            map of parameters
	 * @return Customer log object
	 */
	public static CustomerLog createCustomerLogMessage(Map<String, String[]> originalParams) {
		CustomerLog customerLog = new CustomerLog(getValue(originalParams, APP_NAME),
				getValue(originalParams, CUSTOMER_ID), getValue(originalParams, MESSAGE),
				getValue(originalParams, PRODUCT_ID), getValue(originalParams, SEARCH_TERM),
				getValue(originalParams, CURRENT_PAGE), getValue(originalParams, LOG_TYPE));

		return customerLog;
	}

	/**
	 * Retrieves all values for all entries in the map.
	 *
	 * @param originalParams
	 *            map of parameters
	 * @return array of values
	 */
	public static String[] getAllValues(Map<String, String[]> originalParams) {
		List<String> values = new ArrayList<String>();
		for (String key : originalParams.keySet()) {
			values.addAll(Arrays.asList(originalParams.get(key)));
		}
		String[] valuesArr = new String[values.size()];
		values.toArray(valuesArr);
		return valuesArr;
	}

	/**
	 * Get the first value for a specific key and removes this entry from the map.
	 */
	private static String getValue(Map<String, String[]> params, String key) {
		String value = null;
		if (params.containsKey(key)) {
			value = params.get(key)[0];
		}
		return value;
	}
}
