package com.amx.jax.routing;

import java.util.Map;

/**
 * @author Prashant
 *
 */
public interface IRoutingLogic {

	
	/**
	 * @param input - input values
	 * @param output - output values obtained by applying logic
	 * 
	 */
	public void apply(Map<String, Object> input, Map<String, Object> output);

	default public boolean isApplicable() {
		return true;
	}
}
