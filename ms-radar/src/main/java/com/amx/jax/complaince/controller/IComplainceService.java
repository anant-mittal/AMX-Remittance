package com.amx.jax.complaince.controller;

public interface IComplainceService {
	
	public class ComplainceApiEndpoints {

		public static final String COMPLAINCE_REPORT_UPLOAD = "/complaince-report/upload";
		public static final String COMPLAINCE_TOKEN_GENERATION = "/complaince-token/generation";
		public static final String COMPLAINCE_DETAILS_TXN_REF ="/complaince-deatils/txnRefNo";
		public static final String COMPLAINCE_DETAILS_INQUIRY ="/complaince-deatils/inquiry";
		public static final String COMPLAINCE_DETAILS_REASON ="/complaince-deatils/reason";
		public static final String COMPLAINCE_DETAILS_ACTION ="/complaince-deatils/action";
		public static final String COMPLAINCE_REPORT_UPLOAD1 = "/complaince-report/upload1";
		public static final String COMPLAINCE_DETAILS_DOCFYR ="/complaince-deatils/docfyr";
		
		
		

		public static class Paramss {
			public static final String TOKEN_VALUE="tokens";
						
			public static final String COMPLAINCE_REASON_CODE = "CORE";
			
			public static final String COMPLAINCE_ACTN_CODE = "COAC";
			
			public static final String COMPLAINCE_INDICATOR = "CSSN";
			
			public static final String COMPLAINCE_BANK_CODE = "KWFIU";
			
			
		}
}
}
