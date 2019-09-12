package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_GROUPING_MASTER")
public class GroupingMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5519877984392715225L;

	@Id
	// @GeneratedValue(generator = "cc_discount_seq", strategy =
	// GenerationType.SEQUENCE)
	// @SequenceGenerator(name = "cc_discount_seq", sequenceName =
	// "JAX_PR_CUSTOMER_DISCOUNT_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;

	@Column(name = "APPLICATION_COUNTRY_ID")
	private BigDecimal aplicationCountryId;

	@Column(name = "GROUP_TYPE")
	private String groupType;

	@Column(name = "GROUP_NAME")
	private String groupName;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getAplicationCountryId() {
		return aplicationCountryId;
	}

	public void setAplicationCountryId(BigDecimal aplicationCountryId) {
		this.aplicationCountryId = aplicationCountryId;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

}
