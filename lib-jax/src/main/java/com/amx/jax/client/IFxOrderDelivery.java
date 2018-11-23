package com.amx.jax.client;

import com.amx.jax.IJaxService;

public interface IFxOrderDelivery extends IJaxService {

	public static class Path {
		public static final String PREFIX = "/fc/sale/delivery";
		public static final String FX_DELIVERY_LIST_ORDER = PREFIX + "/list-orders/";
	}

	public static class Params {

		public static final String EMPLOYEE_ID = "employeeId";
		public static final String RECEIPT_APPL_ID = "receiptApplId";

	}

}
