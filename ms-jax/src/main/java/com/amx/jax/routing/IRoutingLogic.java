package com.amx.jax.routing;

import java.util.Map;

public interface IRoutingLogic {

	public void apply(Map<String, Object> input, Map<String, Object> output);

	default public boolean isApplicable() {
		return true;
	}
}
