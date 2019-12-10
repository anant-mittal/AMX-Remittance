package com.amx.jax.complaince.controller;

public interface IComplainceService {
	
	public static class ComplainceApiEndpoints {

		public static final String COMPLAINCE_REPORT_UPLOAD = "/complaince-report/upload";
		public static final String COMPLAINCE_TOKEN_GENERATION = "/complaince-token/generation";

		public static class Paramss {
			public static final String TOKEN_VALUE="tokens";
		}
}
}
