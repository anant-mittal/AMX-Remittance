package com.amx.jax.dbmodel.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_STATUS_MASTER")
public class StatusMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STATUS_MASTER_ID")
	BigDecimal statusMasterId;

	@Column(name = "STATUS_CODE")
	String statusCode;

	@Column(name = "STATUS_DESCRIPTION")
	String statusDescription;

	@Column(name = "CREATED_BY")
	String createdBy;

	@Column(name = "CREATION_DATE")
	Date creationDate;

	@Column(name = "MODIFIED_BY")
	String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	Date modifiedDate;

	@Column(name = "ISACTIVE")
	String isActive;

	public BigDecimal getStatusMasterId() {
		return statusMasterId;
	}

	public void setStatusMasterId(BigDecimal statusMasterId) {
		this.statusMasterId = statusMasterId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
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

}
