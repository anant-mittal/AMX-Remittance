package com.amx.jax.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaxProperties {

	@Value("#{'${support.soa.email}'.split(',')}")
	List<String> supportSoaEmail;

	@Value("${jax.properties.othroutingproc.disable}")
	Boolean routingProcOthDisable;

	@Value("${jax.properties.cash.disable}")
	Boolean cashDisable;

	@Value("${jax.properties.bene.threecountrycheck.enable}")
	Boolean beneThreeCountryCheck;
	
	@Value("${jax.properties.exrate.bestratelogic.enable}")
	Boolean exrateBestRateLogicEnable;
	
	@Value("${jax.properties.remittance.flexfield.enable}")
	Boolean flexFieldEnabled;

	@Value("${jax.properties.device.autoactivate}")
	Boolean deviceAutoActivate;
	

	public List<String> getSupportSoaEmail() {
		return supportSoaEmail;
	}

	public void setSupportSoaEmail(List<String> supportSoaEmail) {
		this.supportSoaEmail = supportSoaEmail;
	}

	public Boolean getRoutingProcOthDisable() {
		return routingProcOthDisable;
	}

	public void setRoutingProcOthDisable(Boolean routingProcOthDisable) {
		this.routingProcOthDisable = routingProcOthDisable;
	}

	public Boolean getCashDisable() {
		return cashDisable;
	}

	public void setCashDisable(Boolean cashDiable) {
		this.cashDisable = cashDiable;
	}

	public Boolean getBeneThreeCountryCheck() {
		return beneThreeCountryCheck;
	}

	public void setBeneThreeCountryCheck(Boolean beneThreeCountryCheck) {
		this.beneThreeCountryCheck = beneThreeCountryCheck;
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

	public Boolean getDeviceAutoActivate() {
		return deviceAutoActivate;
	}

	public void setDeviceAutoActivate(Boolean deviceAutoActivate) {
		this.deviceAutoActivate = deviceAutoActivate;
	}

}
