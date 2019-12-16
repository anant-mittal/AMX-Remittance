package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_REASON_CODE_MASTER")
public class ReasonsModel {
	
	@Id
	@Column(name = "REASON_CODE_ID")
	private BigDecimal reasonCodeId;
	
	@Column(name = "REASON_CODE_DESC")
	private String reasonCodeDesc;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;	
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedData;
	
	@Column(name = "ISACTIVE")
	private String isActive;
	
	@Column(name = "APPROVED_BY")
	private String approvedBy;
	
	@Column(name = "APPROVED_DATE")
	private Date approvedDate;
	
	@Column(name = "REASON_CODE")
	private String reasonCode;
	
	@Column(name = "REASON_CODE_CATEGORY")
	private String reasonCodeCategory;
	
	public BigDecimal getReasonCodeId() {
		return reasonCodeId;
	}

	public void setReasonCodeId(BigDecimal reasonCodeId) {
		this.reasonCodeId = reasonCodeId;
	}

	public String getReasonCodeDesc() {
		return reasonCodeDesc;
	}

	public void setReasonCodeDesc(String reasonCodeDesc) {
		this.reasonCodeDesc = reasonCodeDesc;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Date getModifiedData() {
		return modifiedData;
	}

	public void setModifiedData(Date modifiedData) {
		this.modifiedData = modifiedData;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonCodeCategory() {
		return reasonCodeCategory;
	}

	public void setReasonCodeCategory(String reasonCodeCategory) {
		this.reasonCodeCategory = reasonCodeCategory;
	}


}
