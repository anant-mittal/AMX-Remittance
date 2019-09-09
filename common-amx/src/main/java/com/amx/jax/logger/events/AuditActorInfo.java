package com.amx.jax.logger.events;

import java.math.BigDecimal;

import com.amx.jax.logger.AuditActor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditActorInfo extends AuditActor {

	public AuditActorInfo(ActorType actorType, Object actorId) {
		super(actorType, actorId);
	}

	public AuditActorInfo() {
		super(ActorType.G, 0);
	}

	@JsonProperty("tid")
	String terminalId;

	@JsonProperty("tip")
	String terminalIp;

	@JsonProperty("rid")
	String deviceRegId;

	String identity;

	String username;

	String empno;

	String branchName;
	BigDecimal branchId;

	String areaName;
	BigDecimal areaId;

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getTerminalIp() {
		return terminalIp;
	}

	public void setTerminalIp(String terminalIp) {
		this.terminalIp = terminalIp;
	}

	public String getDeviceRegId() {
		return deviceRegId;
	}

	public void setDeviceRegId(String deviceRegId) {
		this.deviceRegId = deviceRegId;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmpno() {
		return empno;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}

	public BigDecimal getBranchId() {
		return branchId;
	}

	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}

	public BigDecimal getAreaId() {
		return areaId;
	}

	public void setAreaId(BigDecimal areaId) {
		this.areaId = areaId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}
