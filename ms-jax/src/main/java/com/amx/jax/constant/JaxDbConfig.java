package com.amx.jax.constant;

public enum JaxDbConfig {

	BLOCK_BENE_RISK_TRANSACTION, FX_DELIVERY_HISTORICAL_LIST_RANGE_DAYS(7);

	private JaxDbConfig(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	private JaxDbConfig() {
	}

	private Object defaultValue;

	@SuppressWarnings("unchecked")
	public <T> T getDefaultValue() {
		return (T) defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

}
