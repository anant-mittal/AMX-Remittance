package com.amx.jax.amxlib.model;

import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.JaxChannel;
import com.amx.jax.dict.Tenant;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxMetaInfo {

	private BigDecimal countryId;
	private Tenant tenant = Tenant.DEFAULT;

	private BigDecimal customerId;
	private BigDecimal companyId;
	private BigDecimal languageId;
	private BigDecimal countryBranchId;
	private JaxChannel channel = JaxChannel.ONLINE; // default is online channel
	private String referrer = null;

	private String deviceId;

	private String deviceType;
	private String appType;


	public JaxMetaInfo copy() {
		JaxMetaInfo info = new JaxMetaInfo();
		info.setCountryId(this.getCountryId());
		info.setChannel(this.getChannel());
		info.setCompanyId(this.getCompanyId());
		info.setCustomerId(this.getCustomerId());
		info.setLanguageId(this.getLanguageId());
		info.setCountryBranchId(this.getCountryBranchId());
		info.setTenant(this.getTenant());
		info.setDeviceId(this.getDeviceId());
		info.setDeviceIp(this.getDeviceIp());
		info.setReferrer(this.getReferrer());
		info.setDeviceType(this.getDeviceType());
		info.setAppType(this.getAppType());
		return info;
	};

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	private String deviceIp;

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	private String traceId;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public JaxChannel getChannel() {
		return channel;
	}

	public void setChannel(JaxChannel channel) {
		this.channel = channel;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

}
