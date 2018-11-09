package com.amx.jax.client.configs;

import java.math.BigDecimal;

import com.amx.jax.constants.JaxChannel;
import com.amx.jax.rest.RequestMetaInfo;

public class JaxMetaInfo extends RequestMetaInfo {

	public static final String DEFAULT_COMPANY_ID = "1";

	public static final String DEFAULT_CURRENCY_ID = "1";

	public static final String DEFAULT_COUNTRY_BRANCH_ID = "78"; // online

	private BigDecimal countryBranchId;
	private JaxChannel channel = JaxChannel.ONLINE; // default is online channel
	private String referrer = null;
	private String deviceId;

	private BigDecimal employeeId;

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
		info.setEmployeeId(this.getEmployeeId());
		return info;
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

	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

}
