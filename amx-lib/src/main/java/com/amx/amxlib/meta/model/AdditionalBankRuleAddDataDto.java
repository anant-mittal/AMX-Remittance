package com.amx.amxlib.meta.model;

import java.math.BigDecimal;
import java.util.Date;

public class AdditionalBankRuleAddDataDto {

	private BigDecimal additionalBankRuleDataId;
	private BigDecimal countryId;
	private BigDecimal bankId;
	private String flexField;
	private String additionalData;
	private String additionalDescription;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String isActive;
	private BigDecimal additionalBankFieldId;
	private Date approvedDate;
	private String approvedBy;
	public BigDecimal getAdditionalBankRuleDataId() {
		return additionalBankRuleDataId;
	}
	public void setAdditionalBankRuleDataId(BigDecimal additionalBankRuleDataId) {
		this.additionalBankRuleDataId = additionalBankRuleDataId;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	public String getFlexField() {
		return flexField;
	}
	public void setFlexField(String flexField) {
		this.flexField = flexField;
	}
	public String getAdditionalData() {
		return additionalData;
	}
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}
	public String getAdditionalDescription() {
		return additionalDescription;
	}
	public void setAdditionalDescription(String additionalDescription) {
		this.additionalDescription = additionalDescription;
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
	public BigDecimal getAdditionalBankFieldId() {
		return additionalBankFieldId;
	}
	public void setAdditionalBankFieldId(BigDecimal additionalBankFieldId) {
		this.additionalBankFieldId = additionalBankFieldId;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	
}
