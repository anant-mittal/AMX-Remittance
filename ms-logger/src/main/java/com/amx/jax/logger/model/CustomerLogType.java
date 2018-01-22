package com.amx.jax.logger.model;

/**
 * Log messages types, Current customer action, for customer logging messages,
 * used for message classification.
 *
 */
public enum CustomerLogType {
	GENERIC // Undefined action.
	, LOGIN // Logging in.
	, VIEW_PRODUCT // Viewing a product.
	, SEARCH // Searching site or products.
	, ADD_PRODUCT // Adding product to basket.
	, CHECK_OUT // Checking out.
	, HELP // Customer asking for support.
}
