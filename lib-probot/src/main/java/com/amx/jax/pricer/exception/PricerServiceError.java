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

	NO_VALID_TNX_ROUTES_AVAILABLE,

	INVALID_TNX_AMOUNT_TOO_LOW,

	INVALID_TNX_AMOUNT_TOO_HIGH,

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

	MISSING_VALID_RULES,

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

	INVALID_RULE_UPDATE_STATUS,

	MISSING_RULE_IDS,

	CONFLICTING_RULE_UPDATE_STATUS,

	INACTIVE_RULES,

	RULE_STATUS_ALREADY_UPDATED,

	INVALID_DATA_IDS_FOR_RULES,

	MULTIPLE_BANK_SERVICE_RULE,

	INVALID_FETCH_SERVICE_PROVIDE_DATA,

	MISSING_ROUTING_BANK_DETAILS,

	MULTIPLE_ROUTING_BANK_DETAILS,

	INVALID_SRV_PROV_ACTION_IND_C,

	INVALID_SRV_PROV_ACTION_IND_R,

	INVALID_SRV_PROV_ACTION_IND_T,

	INVALID_SRV_PROV_ACTION_IND_U,

	INVALID_SRV_PROV_ACTION_IND,

	MISSING_SETTLEMENT_EXCHANGE_RATES,

	MISSING_SETTLEMENT_FUND_MIN_EXCHANGE_RATES,

	MISSING_SETTLEMENT_FUND_MAX_EXCHANGE_RATES,

	INVALID_EXCHANGE_RATES,

	INVALID_FOREIGN_CURRENCY,

	INVALID_LOCAL_CURRENCY,

	INVALID_SELECTED_CURRENCY,

	MISSING_DESTINATION_COUNTRY_ID,

	MISSING_APPLICATION_COUNTRY_ID,

	INVALID_MARGIN,

	INVALID_QUOTATION_RESPONSE,

	INVALID_BANK_API_CALL,

	INVALID_BANK_ID,

	INVALID_AMOUNT,

	MISSING_PAYMENT_LIMIT_DATA,

	INVALID_MARKUP,

	INVALID_GROUP_TYPE,

	DUPLICATE_GROUP,

	INVALID_GROUP,

	INCORRECT_GROUP_DETAILS,

	INVALID_GROUP_VAL_TYPE,

	EMPTY_OR_NULL_VAL_SET,

	INVALID_OR_MISSING_PARAM,

	SELL_RATE_MIN_MAX_RANGE_VIOLATED,

	BUY_RATE_MIN_MAX_RANGE_VIOLATED,
	
	SERVICE_PROVIDER_CHANNEL_BLOCKED,

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
