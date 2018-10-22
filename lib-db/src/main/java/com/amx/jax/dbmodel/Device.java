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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.dict.UserClient.DeviceType;

@Entity
@Table(name = "JAX_DEVICE")
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JAX_DEVICE_SEQ")
	@SequenceGenerator(name = "JAX_DEVICE_SEQ", sequenceName = "JAX_DEVICE_SEQ", allocationSize = 1)
	@Column(name = "REGISTRATION_ID")
	BigDecimal registrationId;

	@Column(name = "DEVICE_ID")
	String deviceId;

	@Column(name = "DEVICE_TYPE")
	@Enumerated(value=EnumType.STRING)
	DeviceType deviceType;

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

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

}
