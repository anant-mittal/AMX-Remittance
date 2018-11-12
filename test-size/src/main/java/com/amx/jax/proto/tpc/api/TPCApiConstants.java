package com.amx.jax.proto.tpc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.amx.jax.swagger.ApiMockParam;
import com.amx.jax.swagger.ApiMockParams;
import com.amx.jax.swagger.MockParamBuilder.MockParamType;

public class TPCApiConstants {

	public static final class Config {
		public static final long REQUEST_TOKEN_VALIDITY = 30;
		public static final long SESSION_TOKEN_VALIDITY = 3 * 3600;
	}

	public static class Keys {
		public static final String CLIENT_ID_XKEY = "x-client-id";
		public static final String CLIENT_SECRET_XKEY = "x-client-secret";
		public static final String CLIENT_SESSION_TOKEN_XKEY = "x-session-token";
		public static final String CUSTOMER_SESSION_TOKEN_XKEY = "x-customer-token";

	}

	public static class Path {
		public static final String CLIENT_AUTH = "/tpc/client/auth";
		public static final String CUSTOMER_LOGIN = "/tpc/customer/auth";
		public static final String CUSTOMER_LOGIN_CALLBACK = "/tpc/customer/auth_callback";
		public static final String CUSTOMER_DETAILS = "/tpc/customer/details";
		public static final String CUSTOMER_BENE_LIST = "/tpc/customer/bene/list";
		public static final String CUSTOMER_SOURCE_LIST = "/tpc/customer/source/list";
		public static final String CUSTOMER_PURPOSE_LIST = "/tpc/customer/purpose/list";
		public static final String CUSTOMER_REMIT_INQUIRY = "/tpc/customer/remit/inquiry";
		public static final String CUSTOMER_REMIT_CONFIRM = "/tpc/customer/remit/confirm";
		public static final String CUSTOMER_REMIT_VERIFY = "/tpc/customer/remit/verify";
	}

	public static class Params {
		public static final String PARAM_CLIENT_TYPE = "clientType";
		public static final String PARAM_CLIENT_ID = "clientId";
		public static final String PARAM_SYSTEM_ID = "systemid";
	}

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@ApiMockParams({
			@ApiMockParam(name = TPCApiConstants.Keys.CLIENT_SESSION_TOKEN_XKEY, value = "Client Session Token",
				paramType = MockParamType.HEADER) })
	public @interface TPCApiClientHeaders {

	}

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@ApiMockParams({
			@ApiMockParam(name = TPCApiConstants.Keys.CLIENT_SESSION_TOKEN_XKEY, value = "Client Session Token",
				paramType = MockParamType.HEADER),
			@ApiMockParam(name = TPCApiConstants.Keys.CUSTOMER_SESSION_TOKEN_XKEY, value = "Customer Token",
				paramType = MockParamType.HEADER),
	})
	public @interface TPCApiCustomerHeaders {

	}

}
