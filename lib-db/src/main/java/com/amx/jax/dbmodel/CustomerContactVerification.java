package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "EX_CONTACT_VERIFICATION")
@Proxy(lazy = false)
public class CustomerContactVerification implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public CustomerContactVerification() {
	}

	public CustomerContactVerification(BigDecimal id) {
		this.id = id;
	}

	private BigDecimal id;

	@Id
	@GeneratedValue(generator = "ex_contact_verification_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_contact_verification_seq", sequenceName = "EX_CONTACT_VERIFICATION_SEQ",
			allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	BigDecimal appCountryId;

	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getAppCountryId() {
		return appCountryId;
	}

	public void setAppCountryId(BigDecimal appCountryId) {
		this.appCountryId = appCountryId;
	}

	String contactType;

	@Column(name = "CONTACT_TYPE", length = 20)
	public String getContatcType() {
		return this.contactType;
	}

	public void setContatcType(String contactType) {
		this.contactType = contactType;
	}

	@Column(name = "VERIFICATION_CODE", length = 20)
	String verificationCode;

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	BigDecimal customerId;

	@Column(name = "CUSTOMER_ID", precision = 6, scale = 0)
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	String activated;

	@Column(name = "ISACTIVE", length = 1)
	public String getActivated() {
		return this.activated;
	}

	public void setActivated(String activated) {
		this.activated = activated;
	}

	Date createdDate;

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	Date verifiedDate;

	@Column(name = "VERIFIED_DATE")
	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}
}
