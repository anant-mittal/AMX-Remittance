package com.amx.jax.client.fx;

import com.amx.jax.IJaxService;

public interface IFxBranchOrderService extends IJaxService {

	public static class Path {
		public static final String PREFIX = "/fc/sale/branch";
		public static final String FC_PENDING_ORDER_MANAGEMENT = PREFIX + "/pending-order-management/";
		public static final String FC_FETCH_ORDER_MANAGEMENT = PREFIX + "/fetch-order-details/";
		public static final String FC_FETCH_STOCK_CURRENCY = PREFIX + "/fetch-stock-currency/";
		public static final String FC_FETCH_STOCK = PREFIX + "/fetch-stock/";
		public static final String FC_EMLOYEE_DRIVERS = PREFIX + "/employee-drivers/";
		public static final String FC_ASSIGN_DRIVER = PREFIX + "/assign-driver/";
	 }
}
