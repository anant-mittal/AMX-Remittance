package com.amx.jax.event;

import com.amx.jax.tunnel.ITunnelEventsDict;

public enum AmxTunnelEvents implements ITunnelEventsDict {
	CIVIL_ID_EXPIRY, CUSTOMER_BIRTHDATE_GREETING, XRATE_BEST_RATE_CHANGE, TRNX_BENE_CREDIT, TRNX_BENE_CREDIT_DELAY,
	UPDATE_DEVICE_STATUS,INS_POLICY_ELIGIBLE,INS_OPTOUT,INS_EXPIRY,CASH_TRNX_COMM,
	IPSOS_DISCOUNT,INS_PENDING_TRNX, 
	// tests events
	TEST_EVENT, PING_SEND, PING_SHOUT, PING_TASK;

	public static final class Names {
		public static final String CIVIL_ID_EXPIRY = "CIVIL_ID_EXPIRY";
		public static final String CUSTOMER_BIRTHDATE_GREETING = "CUSTOMER_BIRTHDATE_GREETING";
		public static final String CUSTOMER_COMM ="CUSTOMER_COMM";
		public static final String XRATE_BEST_RATE_CHANGE = "XRATE_BEST_RATE_CHANGE";
		public static final String TEST_EVENT = "TEST_EVENT";
		public static final String PING_SEND = "PING_SEND";
		public static final String PING_SHOUT = "PING_SHOUT";
		public static final String PING_TASK = "PING_TASK";
		public static final String TRNX_BENE_CREDIT = "TRNX_BENE_CREDIT";
		public static final String TRNX_BENE_CREDIT_DELAY = "TRNX_BENE_CREDIT_DELAY";
		public static final String DATAUPD_CUSTOMER = "DATAUPD_CUSTOMER";
		public static final String DATAUPD_TRNX = "DATAUPD_TRNX";
		public static final String UPDATE_DEVICE_STATUS = "UPDATE_DEVICE_STATUS";
		public static final String INS_POLICY_ELIGIBLE = "INS_POLICY_ELIGIBLE";
		public static final String INS_OPTOUT = "INS_OPTOUT";
		public static final String INS_EXPIRY = "INS_EXPIRY";
		public static final String CASH_TRNX_COMM = "CASH_TRNX_COMM";
		public static final String INS_PENDING_TRNX = "INS_PENDING_TRNX";
		public static final String IPSOS_DISCOUNT = "IPSOS_DISCOUNT";

	}

	@Override
	public String getEventCode() {
		return this.name();
	}
}
