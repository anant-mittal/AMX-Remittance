package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.constants.DeviceState;

@Entity
@Table(name = "JAX_DEVICE_STATE")
public class DeviceStateInfo {

	@Id
	@Column(name = "DEVICE_REG_ID")
	BigDecimal deviceRegId;

	@Column(name = "STATE")
	DeviceState state;

	@Column(name = "CREATED_DATE")
	Date createdDate;

	@Column(name = "UPDATED_DATE")
	Date modifiedDate;

	@Column(name = "CREATED_BY")
	String createdBy;

	@Column(name = "STATE_INFO")
	String stateInfo;

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

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

}
