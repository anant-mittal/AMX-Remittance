package com.amx.jax.pricer;

public interface AbstractProbotInterface {
	public static class ApiEndPoints {

		/** The Constant SERVICE_PREFIX. */
		// private static final String SERVICE_PREFIX = "/probot";

		//// @formatter:off
 
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
		
		public static final String GET_CUR_GROUPING_DATA = SERVICE_PREFIX + API_VERSION_V1 + "/get-currency-group-details";
		
		public static final String UPDATE_CUR_GROUP_ID = SERVICE_PREFIX + API_VERSION_V1 + "/update-currency-group-id";
		
		public static final String GET_CUR_BY_GROUP_ID = SERVICE_PREFIX + API_VERSION_V1 + "/get-currency-by-group-id";
		
		// Service Provider for transaction
		public static final String GET_SERVICE_PROVIDER_QUOTE = SERVICE_PREFIX + API_VERSION_V1 + "/get-service-provider-quote";
		
		//Service Controllers for markup 
		public static final String GET_MARKUP_DETAILS = SERVICE_PREFIX + API_VERSION_V1 + "/get-markup-details";
		public static final String SAVE_MARKUP_DETAILS = SERVICE_PREFIX + API_VERSION_V1 + "/save-markup-details";
		
		// Grouping APIs
		public static final String GET_GROUPS_OF_TYPE = SERVICE_PREFIX + API_VERSION_V1 + "/get-groups-of-type";
		public static final String SAVE_GROUPS = SERVICE_PREFIX + API_VERSION_V1 + "/save-groups";
		
		// Exchnage Rate Enquiry
		public static final String ENQUIRE_EXCH_RATE = SERVICE_PREFIX + API_VERSION_V1 + "/enquire-exch-rate";
		
		public static final String RATE_UPLOAD_RULE_MAKER = SERVICE_PREFIX + API_VERSION_V1 + "/rate-upload-rule-maker";
		
		public static final String RATE_UPLOAD_RULE_CHECKER = SERVICE_PREFIX + API_VERSION_V1 + "/rate-upload-rule-checker";
		
		public static final String GET_RATE_UPLOAD_RULES = SERVICE_PREFIX + API_VERSION_V1 + "/get-rate-upload-rules";

	}
}
