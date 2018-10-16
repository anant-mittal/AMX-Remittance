package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_DEVICE")
public class Device {

	@Id
	@Column(name = "REGISTRATION_ID")
	BigDecimal registrationId;

	@Column(name = "DEVICE_ID")
	String deviceId;

	@Column(name = "DEVICE_TYPE")
	String deviceType;

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

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
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

}
