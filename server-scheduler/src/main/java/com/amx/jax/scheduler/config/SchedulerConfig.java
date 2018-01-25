package com.amx.jax.scheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.Tenant;

@Component
public class SchedulerConfig {

	@Value("${ratealert.polltime:5}")
	Integer rateAlertTaskPollingTime;
	
	@Value("${ratealert.tenants:KWT}")
	Tenant[] tenants;
	

	public Integer getRateAlertTaskPollingTime() {
		return rateAlertTaskPollingTime;
	}

	public void setRateAlertTaskPollingTime(Integer rateAlertTaskPollingTime) {
		this.rateAlertTaskPollingTime = rateAlertTaskPollingTime;
	}

	public Tenant[] getTenants() {
		return tenants;
	}

	public void setTenants(Tenant[] tenants) {
		this.tenants = tenants;
	}
}
