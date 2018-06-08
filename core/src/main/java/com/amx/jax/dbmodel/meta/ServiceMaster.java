package com.amx.jax.dbmodel.meta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_SERVICE_MASTER")
public class ServiceMaster implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal serviceId;
	private String serviceCode;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String approvedBy;
	private Date approvedDate;
	private String remarks;
	private ServiceGroupMaster serviceGroupId;

	public ServiceMaster() {

	}

	public ServiceMaster(BigDecimal serviceId) {
		super();
		this.serviceId = serviceId;
	}

	@Id
	@GeneratedValue(generator = "ex_service_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_service_seq", sequenceName = "EX_SERVICE_SEQ", allocationSize = 1)
	@Column(name = "SERVICE_MASTER_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getServiceId() {
		return serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	@Column(name = "SERVICE_CODE")
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	@Column(name = "IS_ACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name = "APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_GROUP_ID")
	public ServiceGroupMaster getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(ServiceGroupMaster serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

}
