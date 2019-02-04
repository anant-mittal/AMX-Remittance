package com.amx.jax.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class JaxTenantProperties {

	@TenantValue("${support.compliance.email}")
	private String complianceEmail;

	@TenantValue("${jax.properties.exrate.bestratelogic.enable}")
	Boolean exrateBestRateLogicEnable;

	@TenantValue("${jax.properties.remittance.flexfield.enable}")
	Boolean flexFieldEnabled;

	@TenantValue("${jax.properties.othroutingproc.disable}")
	Boolean routingProcOthDisable;

	@TenantValue("${jax.properties.cash.disable}")
	Boolean cashDisable;

	@TenantValue("${jax.properties.bene.threecountrycheck.enable}")
	Boolean beneThreeCountryCheck;

	@TenantValue("${jax.properties.device.autoactivate}")
	Boolean deviceAutoActivate;

	public Boolean getCashDisable() {
		return cashDisable;
	}

	public void setCashDisable(Boolean cashDisable) {
		this.cashDisable = cashDisable;
	}

	public Boolean getBeneThreeCountryCheck() {
		return beneThreeCountryCheck;
	}

	public void setBeneThreeCountryCheck(Boolean beneThreeCountryCheck) {
		this.beneThreeCountryCheck = beneThreeCountryCheck;
	}

	public Boolean getDeviceAutoActivate() {
		return deviceAutoActivate;
	}

	public void setDeviceAutoActivate(Boolean deviceAutoActivate) {
		this.deviceAutoActivate = deviceAutoActivate;
	}

	public Boolean getRoutingProcOthDisable() {
		return routingProcOthDisable;
	}

	public void setRoutingProcOthDisable(Boolean routingProcOthDisable) {
		this.routingProcOthDisable = routingProcOthDisable;
	}

	public Boolean getExrateBestRateLogicEnable() {
		return exrateBestRateLogicEnable;
	}

	public void setExrateBestRateLogicEnable(Boolean exrateBestRateLogicEnable) {
		this.exrateBestRateLogicEnable = exrateBestRateLogicEnable;
	}

	public Boolean getFlexFieldEnabled() {
		return flexFieldEnabled;
	}

	public void setFlexFieldEnabled(Boolean flexFieldEnabled) {
		this.flexFieldEnabled = flexFieldEnabled;
	}

	public String getComplianceEmail() {
		return complianceEmail;
	}

	public void setComplianceEmail(String complianceEmail) {
		this.complianceEmail = complianceEmail;
	}
}
