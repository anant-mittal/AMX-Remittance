package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_LINK_DETAILS")
public class LinkDetails implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String linkId;
	private BigDecimal customerId;
	private Integer openCounter;
	private Integer noOfContacts;
//	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	
	public LinkDetails() {
		
	}
	
	@Id
	@Column(name = "link_id")
	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	@Column(name = "customer_id")
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	
	@Column(name = "open_counter")
	public Integer getOpenCounter() {
		return openCounter;
	}

	public void setOpenCounter(Integer openCounter) {
		this.openCounter = openCounter;
	}

	@Column(name = "no_of_contacts")
	public Integer getNoOfContacts() {
		return noOfContacts;
	}

	public void setNoOfContacts(Integer noOfContacts) {
		this.noOfContacts = noOfContacts;
	}
	
//	@Column(name = "isactive")
//	public String getIsActive() {
//		return isActive;
//	}
//
//	public void setIsActive(String isActive) {
//		this.isActive = isActive;
//	}

	@Column(name = "created_by")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "created_date")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "modified_by")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "modified_date")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
   
	
}
