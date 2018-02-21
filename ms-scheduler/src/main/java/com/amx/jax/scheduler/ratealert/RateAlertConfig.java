package com.amx.jax.scheduler.ratealert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.scope.Tenant;

public class RateAlertConfig {

	public static List<Tenant> tenants = new ArrayList<>();

	static {
		tenants.add(Tenant.KWT);
	}

	public static Map<Tenant, RateAlertData> RATE_ALERT_DATA = new HashMap<Tenant, RateAlertData>();

}
