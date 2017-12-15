package com.amx.jax.dbmodel.bene;

import java.io.Serializable;
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
@Table(name="EX_BENEFICARY_CONTACT")
public class BeneficaryContact implements Serializable {
	
	private static final long serialVersionUID = 2315791709068216697L;
	
	private BigDecimal beneficaryTelephoneSeqId;
	private BigDecimal applicationCountryId;
	private String telephoneNumber;
	private BigDecimal mobileNumber;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private BigDecimal beneficaryMasterId;
	private String countryTelCode;
	@Id
	@GeneratedValue(generator="ex_beneficary_telephone_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_beneficary_telephone_seq",sequenceName="EX_BENEFICARY_TELEPHONE_SEQ",allocationSize=1)
	@Column(name="BENEFICARY_TELEPHONE_SEQ_ID",unique=true,nullable=false)
	public BigDecimal getBeneficaryTelephoneSeqId() {
	return beneficaryTelephoneSeqId;
	}

	public void setBeneficaryTelephoneSeqId(BigDecimal  beneficaryTelephoneSeqId) {
	this.beneficaryTelephoneSeqId =  beneficaryTelephoneSeqId;
	}

	
	@Column(name="BENEFICARY_MASTER_SEQ_ID")
	public BigDecimal getBeneficaryMasterId() {
	return beneficaryMasterId;
	}
	public void setBeneficaryMasterId(BigDecimal beneficaryMasterId) {
	this.beneficaryMasterId = beneficaryMasterId;
	}
	
	@Column(name = "APPLICATION_COUNTRY_ID")	
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	@Column(name="ISACTIVE")
	public String getIsActive() {
	return isActive;
	}

	public void setIsActive(String isActive) {
	this.isActive = isActive;
	}

	@Column(name="CREATED_BY")
	public String getCreatedBy() {
	return createdBy;
	}

	public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
	}

	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
	return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
	}

	@Column(name="MODIFIED_BY")
	public String getModifiedBy() {
	return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
	}

	@Column(name="MODIFIED_DATE")
	public Date getModifiedDate() {
	return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
	this.modifiedDate = modifiedDate;
	}

	@Column(name="COUNTRY_TEL_CODE")
	public String getCountryTelCode() {
		return countryTelCode;
	}

	public void setCountryTelCode(String countryTelCode) {
		this.countryTelCode = countryTelCode;
	}
	
	@Column(name="TELEPHONE_NUMBER")
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Column(name="MOBILE_NUMBER")
	public BigDecimal getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(BigDecimal mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}