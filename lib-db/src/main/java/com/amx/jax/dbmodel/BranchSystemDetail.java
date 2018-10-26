package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_BRANCH_SYSTEM_INVENTORY")
public class BranchSystemDetail {

	@Id
	@Column(name = "EX_BRANCH_SYSTEM_INVENTORY_ID")
	BigDecimal countryBranchSystemInventoryId;

	@Column(name = "COUNTRY_BRANCH_ID")
	BigDecimal countryBranchId;

	@Column(name = "SYSTEM_NAME")
	String systemName;

	@Column(name = "IP_ADDRESS")
	String ipAddress;

	@Column(name = "IS_ACTIVE")
	String isActive;

	@Column(name = "CREATION_DATE")
	Date createdDate;

	public BigDecimal getCountryBranchSystemInventoryId() {
		return countryBranchSystemInventoryId;
	}

	public void setCountryBranchSystemInventoryId(BigDecimal countryBranchSystemInventoryId) {
		this.countryBranchSystemInventoryId = countryBranchSystemInventoryId;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
