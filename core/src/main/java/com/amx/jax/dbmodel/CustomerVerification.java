package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_CUSTOMER_VERIFICATION")
public class CustomerVerification {

	public CustomerVerification() {
		super();
	}

	public CustomerVerification(String type, String value) {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JAX_CUSTOMER_VERIFICATION_SEQ")
	@SequenceGenerator(name = "JAX_CUSTOMER_VERIFICATION_SEQ", sequenceName = "JAX_CUSTOMER_VERIFICATION_SEQ", allocationSize = 1)
	@Column(name = "ID")
	BigDecimal id;

	@Column(name = "VERIFICATION_TYPE")
	String verificationType;

	@Column(name = "FIELD_VALUE")
	String fieldValue;

	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;

	@Column(name = "VERIFICATION_BY")
	String verificationBy;

	@Column(name = "VERIFICATION_DATE")
	Date verificationDate;

	@Column(name = "VERIFICATION_STATUS")
	String verificationStatus;

	@Column(name = "CREATE_DATE")
	Date createDate;

	@Column(name = "UPDATE_DATE")
	Date updateDate;

	@Column(name = "VERIFICATION_CHECK_1")
	String verificationCheck1;

	@Column(name = "VERIFICATION_CHECK_2")
	String verificationCheck2;

	@Column(name = "VERIFICATION_CHECK_3")
	String verificationCheck3;
	
	@PrePersist
	private void setDefaultValues() {
		this.updateDate = new Date();
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public String getVerificationBy() {
		return verificationBy;
	}

	public void setVerificationBy(String verificationBy) {
		this.verificationBy = verificationBy;
	}

	public Date getVerificationDate() {
		return verificationDate;
	}

	public void setVerificationDate(Date verificationDate) {
		this.verificationDate = verificationDate;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getVerificationCheck1() {
		return verificationCheck1;
	}

	public void setVerificationCheck1(String verificationCheck1) {
		this.verificationCheck1 = verificationCheck1;
	}

	public String getVerificationCheck2() {
		return verificationCheck2;
	}

	public void setVerificationCheck2(String verificationCheck2) {
		this.verificationCheck2 = verificationCheck2;
	}

	public String getVerificationCheck3() {
		return verificationCheck3;
	}

	public void setVerificationCheck3(String verificationCheck3) {
		this.verificationCheck3 = verificationCheck3;
	}

	public String getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(String verificationType) {
		this.verificationType = verificationType;
	}

}
