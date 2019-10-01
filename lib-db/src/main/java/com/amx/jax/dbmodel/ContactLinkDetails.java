package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_LINK_CONTACT_DETAILS")
public class ContactLinkDetails implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private BigDecimal id;
	private String linkId;
	private String contactDetail;
//	private String isactive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	
	
	public ContactLinkDetails() {
		
	}
	
	@Id	
	@GeneratedValue(generator="fs_link_contact_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="fs_link_contact_seq",sequenceName="FS_LINK_CONTACT_SEQ",allocationSize=1)
	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	@Column(name = "link_id")
	public String getLinkId() {
		return linkId;
	}

	

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	@Column(name = "contact_detail")
	public String getContactDetail() {
		return contactDetail;
	}

	public void setContactDetail(String contactDetails) {
		this.contactDetail = contactDetails;
	}
	
//	@Column(name = "isactive")
//	public String getIsActive() {
//		return isactive;
//	}
//
//	public void setIsActive(String isActive) {
//		this.isactive = isActive;
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
