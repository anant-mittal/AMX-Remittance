package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.amx.jax.constants.DeviceState;
import com.amx.jax.constants.DeviceStateDataType;

@Entity
@Table(name = "JAX_DEVICE_CLIENT_STATE")
public class DeviceStateInfo {

	@Id
	@Column(name = "DEVICE_CLIENT_REG_ID")
	BigDecimal deviceRegId;

	@Column(name = "STATE")
	@Enumerated(value = EnumType.STRING)
	DeviceState state;

	@Column(name = "CREATED_DATE")
	Date createdDate;

	@Column(name = "UPDATED_DATE")
	Date modifiedDate;

	@Column(name = "CREATED_BY")
	String createdBy;

	@Column(name = "STATE_DATA")
	String stateData;

	@Column(name = "STATE_DATA_TYPE")
	@Enumerated(value = EnumType.STRING)
	DeviceStateDataType stateDataType;

	@Column(name = "PAIR_TOKEN")
	String pairToken;

	@Column(name = "SESSION_TOKEN")
	String sessionToken;

	@Column(name = "OTP_TOKEN")
	String otpToken;
	
	@Column(name = "OTP_TOKEN_CREATED_DATE")
	Date otpTokenCreatedDate;
	
	@Column(name = "SIGNATURE_CLOB")
	String signature;
	
	@Column(name = "EMPLOYEE_ID")
	BigDecimal employeeId;
	
	@Column(name = "STATE_DATA_MODIFIED_DATE")
	Date stateDataModifiedDate;

	public BigDecimal getDeviceRegId() {
		return deviceRegId;
	}

	public void setDeviceRegId(BigDecimal deviceRegId) {
		this.deviceRegId = deviceRegId;
	}

	public DeviceState getState() {
		return state;
	}

	public void setState(DeviceState state) {
		this.state = state;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getStateData() {
		return stateData;
	}

	public void setStateData(String stateData) {
		this.stateData = stateData;
	}

	public DeviceStateDataType getStateDataType() {
		return stateDataType;
	}

	public void setStateDataType(DeviceStateDataType stateDataType) {
		this.stateDataType = stateDataType;
	}

	public String getOtpToken() {
		return otpToken;
	}

	public void setOtpToken(String otpToken) {
		this.otpToken = otpToken;
	}

	public String getPairToken() {
		return pairToken;
	}

	public void setPairToken(String pairToken) {
		this.pairToken = pairToken;
	}

	@PrePersist
	public void prePersist() {
		this.modifiedDate = new Date();
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	public Date getOtpTokenCreatedDate() {
		return otpTokenCreatedDate;
	}

	public void setOtpTokenCreatedDate(Date otpTokenCreatedDate) {
		this.otpTokenCreatedDate = otpTokenCreatedDate;
	}

	public Date getStateDataModifiedDate() {
		return stateDataModifiedDate;
	}

	public void setStateDataModifiedDate(Date stateDataModifiedDate) {
		this.stateDataModifiedDate = stateDataModifiedDate;
	}

}
