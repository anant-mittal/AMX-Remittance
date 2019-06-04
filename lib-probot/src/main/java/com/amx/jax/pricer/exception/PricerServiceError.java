package com.amx.jax.pricer.exception;

import com.amx.jax.exception.IExceptionEnum;

/**
 * The Enum PricerServiceError.
 */
public enum PricerServiceError implements IExceptionEnum {

	INVALID_CUSTOMER,

	INVALID_COUNTRY,

	INVALID_CURRENCY,

	MISSING_AMOUNT,

	INVALID_BRANCH_ID,

	INVALID_CHANNEL,

	INVALID_PRICING_LEVEL,

	MISSING_ROUTING_BANK_IDS,

	INVALID_ROUTING_BANK_IDS,

	INVALID_OR_MISSING_ROUTE,

	MISSING_GLCBAL_ENTRIES,

	MISSING_VALID_EXCHANGE_RATES,

	INVALID_CHANNEL_DISC_PIPS,

	INVALID_CUST_CAT_DISC_PIPS, MISSING_COUNTRY_ID,

	MISSING_EVENT_YEAR,

	INVALID_COUNTRY_ID,

	INVALID_EVENT_YEAR,

	MISSING_TIMEZONE,

	NULL_ROUTING_MATRIX,

	INVALID_AMT_SLAB_DISC_PIPS,
	/** The unknown exception. */
	// MISC
	UNKNOWN_EXCEPTION;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.pricer.exception.IExceptionEnum#getCode()
	 */
	public String getStatusKey() {
		return this.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.pricer.exception.IExceptionEnum#getStatusCode()
	 */
	@Override
	public int getStatusCode() {
		return this.ordinal();
	}

}
