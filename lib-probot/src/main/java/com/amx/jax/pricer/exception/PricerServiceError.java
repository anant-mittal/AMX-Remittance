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

	INVALID_CUST_CAT_DISC_PIPS, 
	
	MISSING_COUNTRY_ID,

	MISSING_EVENT_YEAR,

	INVALID_COUNTRY_ID,

	INVALID_EVENT_YEAR,

	MISSING_TIMEZONE,

	NULL_ROUTING_MATRIX,

	INVALID_AMT_SLAB_DISC_PIPS,
	
	MISSING_DISCOUNT_OR_GROUP_ID,
	// Service Provider error
	INVALID_BENEFICIARY_RELATIONSHIP_ID,
	
	INVALID_BENEFICIARY,
	
	INVALID_REMITTANCE_ID,
	
	INVALID_DELIVERY_ID,
	
	INVALID_BANK_CHARGES,
	
	MULTIPLE_BANK_CHARGES,
	
	INVALID_BANK_SERVICE_RULE,
	
	MULTIPLE_BANK_SERVICE_RULE,
	
	INVALID_FETCH_SERVICE_PROVIDE_DATA,
	
	MISSING_ROUTING_BANK_DETAILS,
	
	INVALID_SRV_PROV_ACTION_IND_C,
	
	INVALID_SRV_PROV_ACTION_IND_R,
	
	INVALID_SRV_PROV_ACTION_IND_T,
	
	INVALID_SRV_PROV_ACTION_IND_U,
	
	INVALID_SRV_PROV_ACTION_IND,
	
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
