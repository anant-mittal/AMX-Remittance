package com.amx.jax.model.response.fx;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.model.AbstractModel;



public class PurposeOfTransactionDto  extends AbstractModel {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1257742249013357611L;
	@Override
	public String getModelType() {
		return "fx-purpose-of-trnx";
	}
	
	public BigDecimal getPurposeId() {
		return purposeId;
	}
	public void setPurposeId(BigDecimal purposeId) {
		this.purposeId = purposeId;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	public String getPurposeFullDesc() {
		return purposeFullDesc;
	}
	public void setPurposeFullDesc(String purposeFullDesc) {
		this.purposeFullDesc = purposeFullDesc;
	}
	public String getPurposeShortDesc() {
		return purposeShortDesc;
	}
	public void setPurposeShortDesc(String purposeShortDesc) {
		this.purposeShortDesc = purposeShortDesc;
	}
	public String getLocalFullDesc() {
		return localFullDesc;
	}
	public void setLocalFullDesc(String localFullDesc) {
		this.localFullDesc = localFullDesc;
	}
	public String getLocalShortDesc() {
		return localShortDesc;
	}
	public void setLocalShortDesc(String localShortDesc) {
		this.localShortDesc = localShortDesc;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	private BigDecimal purposeId;
	private BigDecimal countryId;
	private BigDecimal companyId;
	private String purposeFullDesc;
	private String purposeShortDesc;
	private String localFullDesc;
	private String localShortDesc;
	private String createdBy;
	private Date creationDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String isActive;
	
	
}
