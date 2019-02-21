package com.amx.jax.pricer;

public interface AbstractProbotInterface {
	public static class ApiEndPoints {

		/** The Constant SERVICE_PREFIX. */
		private static final String SERVICE_PREFIX = "/probot";

		/** The Constant API_VERSION_V1. */
		private static final String API_VERSION_V1 = "/v1";

		// Service Controllers for Exchange Rate Service
		public static final String FETCH_PRICE_CUSTOMER = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-price-customer";

		public static final String FETCH_BASE_PRICE = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-base-price";

		public static final String FETCH_DISCOUNTED_RATES = SERVICE_PREFIX + API_VERSION_V1 + "/fetch-discounted-rates";
		
		// Service Controllers for Exchange Rate Related Data
		
		public static final String GET_CB_AND_PRODUCTS = SERVICE_PREFIX + API_VERSION_V1 + "/get-cb-and-products";
		
		public static final String PRICE_TEST = SERVICE_PREFIX + API_VERSION_V1 + "/price-test";
		
		// Service Controllers for Discount Management Data
		public static final String GET_DISCOUNT_MGMT = SERVICE_PREFIX + API_VERSION_V1 + "/get-discount-mgmt";
		
		public static final String GET_ROUTBANK_AND_SEVICE = SERVICE_PREFIX + API_VERSION_V1 + "/get-routbank-and-service";

	}
}
