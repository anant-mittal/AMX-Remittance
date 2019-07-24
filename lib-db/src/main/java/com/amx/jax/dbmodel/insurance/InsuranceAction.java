package com.amx.jax.dbmodel.insurance;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_INSURANCE_ACTIONS")
public class InsuranceAction {

	@Id
	@Column(name = "ACTION_ID")
	@SequenceGenerator(sequenceName = "EX_INS_ACTION_SEQ", name = "EX_INS_ACTION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="EX_INS_ACTION_SEQ")
	BigDecimal actionId;
	
	@Column(name="APPLICATION_COUNTRY_ID")
	BigDecimal applicationCountryId;

	@Column(name = "INSURANCE_ID")
	BigDecimal insuranceId;

	@Column(name = "OPT_IN_DATE")
	Date optInDate;

	@Column(name = "OPT_IN_ACYM")
	Date optInDateAccount;

	@Column(name = "OPT_OUT_DATE")
	Date optOutDate;

	@Column(name = "OPT_OUT_ACYM")
	Date optOutDateAccount;

	@Column(name = "CREATED_BY")
	String createdBy;

	@Column(name = "CREATED_DATE")
	Date createdDate;

	@Column(name = "CREATED_DVCTYP")
	String createdDeviceType;

	@Column(name = "CREATED_DEVICEID")
	String createdDeviceId;

	@Column(name = "MODIFIED_BY")
	String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	Date modifiedDate;

	@Column(name = "MODIFIED_DVCTYP")
	String modifiedDeviceType;

	@Column(name = "MODIFIED_DEVICEID")
	String modifiedDeviceId;

	public BigDecimal getActionId() {
		return actionId;
	}

	public void setActionId(BigDecimal actionId) {
		this.actionId = actionId;
	}

	public BigDecimal getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(BigDecimal insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Date getOptInDate() {
		return optInDate;
	}

	public void setOptInDate(Date otpInDate) {
		this.optInDate = otpInDate;
	}

	public Date getOptInDateAccount() {
		return optInDateAccount;
	}

	public void setOptInDateAccount(Date otpInDateAccount) {
		this.optInDateAccount = otpInDateAccount;
	}

	public Date getOptOutDate() {
		return optOutDate;
	}

	public void setOptOutDate(Date optOutDate) {
		this.optOutDate = optOutDate;
	}

	public Date getOptOutDateAccount() {
		return optOutDateAccount;
	}

	public void setOptOutDateAccount(Date optOutDateAccount) {
		this.optOutDateAccount = optOutDateAccount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedDeviceType() {
		return createdDeviceType;
	}

	public void setCreatedDeviceType(String createdDeviceType) {
		this.createdDeviceType = createdDeviceType;
	}

	public String getCreatedDeviceId() {
		return createdDeviceId;
	}

	public void setCreatedDeviceId(String createdDeviceId) {
		this.createdDeviceId = createdDeviceId;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedDeviceType() {
		return modifiedDeviceType;
	}

	public void setModifiedDeviceType(String modifiedDeviceType) {
		this.modifiedDeviceType = modifiedDeviceType;
	}

	public String getModifiedDeviceId() {
		return modifiedDeviceId;
	}

	public void setModifiedDeviceId(String modifiedDeviceId) {
		this.modifiedDeviceId = modifiedDeviceId;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

}
