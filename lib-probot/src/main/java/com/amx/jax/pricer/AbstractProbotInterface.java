package com.amx.jax.pricer;

public interface AbstractProbotInterface {
	public static class ApiEndPoints {

		/** The Constant SERVICE_PREFIX. */
		// private static final String SERVICE_PREFIX = "/probot";

		private static final String SERVICE_PREFIX = "";

		/** The Constant API_VERSION_V1. */
		private static final String API_VERSION_V1 = "/v1";

		// Service Controllers for Exchange Rate Service
		public static final String FETCH_PRICE_CUSTOMER = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-price-customer";

		public static final String FETCH_BASE_PRICE = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-base-price";

		public static final String FETCH_DISCOUNTED_PRICE = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-discounted-price";

		public static final String FETCH_DISCOUNTED_RATES = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-discounted-rates";
		
		// Service Controllers for Exchange Rate Related Data
		public static final String GET_CB_AND_PRODUCTS = SERVICE_PREFIX + API_VERSION_V1 + "/get-cb-and-products";
		
		public static final String PRICE_TEST = SERVICE_PREFIX + API_VERSION_V1 + "/price-test";
		
		public static final String GET_HOLIDAYS_DATE_RANGE = SERVICE_PREFIX + API_VERSION_V1 + "/get-holidays-for-date-range";

		// Service Controllers for Dynamic Routing and Pricing
		public static final String FETCH_REMIT_ROUTES_PRICES = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-remit-routes-prices";
		
		// Service Controllers for Discount Management Data
		public static final String GET_DISCOUNT_DETAILS = SERVICE_PREFIX + API_VERSION_V1 + "/get-discount-details";
		
		public static final String GET_ROUTBANKS_AND_SEVICES = SERVICE_PREFIX + API_VERSION_V1 + "/get-routbanks-and-services";
		
		public static final String SAVE_DISCOUNT_DETAILS = SERVICE_PREFIX + API_VERSION_V1 + "/save-discount-details";
		
		public static final String SERVICE_TEST = SERVICE_PREFIX + API_VERSION_V1 + "/service-test";
		
		// Service Provider for transaction
		public static final String Partner_Fee_Inquiry = SERVICE_PREFIX + API_VERSION_V1 + "/partner-fee-inquiry";
		

	}
}
