package com.amx.jax.dbmodel.insurance;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_CUSTOMER_INSURANCE")
public class CustomerInsurance {

	@Column(name = "APPLICATION_COUNTRY_ID")
	BigDecimal applicationCountryId;

	@Column(name = "INSURANCE_ID")
	@Id
	BigDecimal insuranceId;

	@JoinColumn(name = "INS_SETPUP_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	InsuranceSetupMaster insuranceSetupMaster;

	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;

	@Column(name = "ENTRY_DATE")
	Date entryDate;

	@Column(name = "COVER_EFF_DATE")
	Date effectiveDate;

	@Column(name = "COVER_EXPIRY_DATE")
	Date expiryDate;

	@Column(name = "CURRENT_ELIGIBILITY_ID")
	BigDecimal eligibilityId;

	@Column(name = "ISACTIVE")
	String isActive;

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(BigDecimal insuranceId) {
		this.insuranceId = insuranceId;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public BigDecimal getEligibilityId() {
		return eligibilityId;
	}

	public void setEligibilityId(BigDecimal eligibilityId) {
		this.eligibilityId = eligibilityId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public InsuranceSetupMaster getInsuranceSetupMaster() {
		return insuranceSetupMaster;
	}

	public void setInsuranceSetupMaster(InsuranceSetupMaster insuranceSetupMaster) {
		this.insuranceSetupMaster = insuranceSetupMaster;
	}

}
