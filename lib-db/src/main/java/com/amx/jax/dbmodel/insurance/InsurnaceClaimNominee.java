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
@Table(name = "JAX_INSURANCE_CLAIM_NOMINEE")
public class InsurnaceClaimNominee {

	@Column(name = "APPLICATION_COUNTRY_ID")
	BigDecimal applicationCountryId;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EX_NOMINEE_SEQ")
	@SequenceGenerator(name = "EX_NOMINEE_SEQ", allocationSize = 1, sequenceName="EX_NOMINEE_SEQ")
	@Column(name = "NOMINEE_ID")
	BigDecimal nomineeId;
	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;
	@Column(name = "BENEFICARY_MASTER_SEQ_ID")
	BigDecimal beneMasterSeqId;
	@Column(name = "SHARE_PERCENTAGE")
	BigDecimal sharePercent;
	@Column(name = "ISACTIVE")
	String isActive;
	@Column(name = "CREATED_BY")
	String createdBy;
	@Column(name = "MODIFIED_BY")
	String modifiedBy;
	@Column(name = "CREATED_DATE")
	Date createdDate;
	@Column(name = "MODIFIED_DATE")
	Date modifiedDate;
	@Column(name = "CRE_DEVICEID")
	String createdDeviceId;
	@Column(name = "MODIFIED_DEVICEID")
	String modifiedDeviceId;
	@Column(name = "MODIFIED_DVCTYP")
	String modifiedDeviceType;

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getNomineeId() {
		return nomineeId;
	}

	public void setNomineeId(BigDecimal nomineeId) {
		this.nomineeId = nomineeId;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getBeneMasterSeqId() {
		return beneMasterSeqId;
	}

	public void setBeneMasterSeqId(BigDecimal beneMasterSeqId) {
		this.beneMasterSeqId = beneMasterSeqId;
	}

	public BigDecimal getSharePercent() {
		return sharePercent;
	}

	public void setSharePercent(BigDecimal sharePercent) {
		this.sharePercent = sharePercent;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
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

	public String getCreatedDeviceId() {
		return createdDeviceId;
	}

	public void setCreatedDeviceId(String createdDeviceId) {
		this.createdDeviceId = createdDeviceId;
	}

	public String getModifiedDeviceId() {
		return modifiedDeviceId;
	}

	public void setModifiedDeviceId(String modifiedDeviceId) {
		this.modifiedDeviceId = modifiedDeviceId;
	}

	public String getModifiedDeviceType() {
		return modifiedDeviceType;
	}

	public void setModifiedDeviceType(String modifiedDeviceType) {
		this.modifiedDeviceType = modifiedDeviceType;
	}

}
