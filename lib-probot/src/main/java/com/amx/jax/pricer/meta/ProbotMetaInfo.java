package com.amx.jax.pricer.meta;

import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.rest.RequestMetaInfo;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ProbotMetaInfo extends RequestMetaInfo {

	private BigDecimal countryBranchId;
	private Channel channel = Channel.ONLINE; // default is online channel
	private String referrer = null;
	private String deviceId;
	private BigDecimal terminalId;

	private BigDecimal employeeId;

	public ProbotMetaInfo copyTo(ProbotMetaInfo info) {
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
		info.setTerminalId(this.terminalId);
		return info;
	}

	public ProbotMetaInfo copy() {
		return this.copyTo(new ProbotMetaInfo());
	}

	public BigDecimal getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(BigDecimal terminalId) {
		this.terminalId = terminalId;
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

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
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
