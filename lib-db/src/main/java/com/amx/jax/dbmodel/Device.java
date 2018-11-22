package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.constant.DeviceState;
import com.amx.jax.dict.UserClient.ClientType;

@Entity
@Table(name = "JAX_DEVICE_CLIENT")
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JAX_DEVICE_SEQ")
	@SequenceGenerator(name = "JAX_DEVICE_SEQ", sequenceName = "JAX_DEVICE_SEQ", allocationSize = 1)
	@Column(name = "REGISTRATION_ID")
	BigDecimal registrationId;

	@Column(name = "DEVICE_CLIENT_ID")
	String deviceId;

	@Column(name = "DEVICE_CLIENT_TYPE")
	@Enumerated(value = EnumType.STRING)
	ClientType deviceType;

	@Column(name = "STATUS")
	String status;

	@Column(name = "CREATED_DATE")
	Date createdDate;

	@Column(name = "UPDATED_DATE")
	Date modifiedDate;

	@Column(name = "CREATED_BY")
	String createdBy;

	@Column(name = "BRANCH_SYSTEM_INVENTORY_ID")
	BigDecimal branchSystemInventoryId;
	
	@Column(name = "STATE")
	@Enumerated(value = EnumType.STRING)
	DeviceState state;
	
	@Column(name = "PAIR_TOKEN")
	String pairToken;

	@Column(name = "SESSION_TOKEN")
	String sessionToken;

	@Column(name = "OTP_TOKEN")
	String otpToken;
	
	@Column(name = "OTP_TOKEN_CREATED_DATE")
	Date otpTokenCreatedDate;

	public BigDecimal getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(BigDecimal registrationId) {
		this.registrationId = registrationId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public BigDecimal getBranchSystemInventoryId() {
		return branchSystemInventoryId;
	}

	public void setBranchSystemInventoryId(BigDecimal branchSystemInventoryId) {
		this.branchSystemInventoryId = branchSystemInventoryId;
	}

	public ClientType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(ClientType deviceType) {
		this.deviceType = deviceType;
	}
	
	@PrePersist
	public void prePersist() {
		this.modifiedDate = new Date();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((registrationId == null) ? 0 : registrationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (registrationId == null) {
			if (other.registrationId != null)
				return false;
		} else if (!registrationId.equals(other.registrationId))
			return false;
		return true;
	}

	public DeviceState getState() {
		return state;
	}

	public void setState(DeviceState state) {
		this.state = state;
	}

	public String getPairToken() {
		return pairToken;
	}

	public void setPairToken(String pairToken) {
		this.pairToken = pairToken;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getOtpToken() {
		return otpToken;
	}

	public void setOtpToken(String otpToken) {
		this.otpToken = otpToken;
	}

	public Date getOtpTokenCreatedDate() {
		return otpTokenCreatedDate;
	}

	public void setOtpTokenCreatedDate(Date otpTokenCreatedDate) {
		this.otpTokenCreatedDate = otpTokenCreatedDate;
	}

}
