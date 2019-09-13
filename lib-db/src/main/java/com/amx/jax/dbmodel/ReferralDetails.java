package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "JAX_REFERRAL_DETAILS")
public class ReferralDetails implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private BigDecimal customerId;
	private String customerReferralCode;
	private BigDecimal referredByCustomerId;
	private String isConsumed;	
//	private Boolean isWinner;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	
	
	public ReferralDetails() {
		
	}
	
	@Id
	@Column(name = "customer_id")
	public BigDecimal getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	
	
	@Column(name = "customer_referral_code")
	public String getCustomerReferralCode() {
		return this.customerReferralCode;
	}

	public void setCustomerReferralCode(String customerReferralCode) {
		this.customerReferralCode = customerReferralCode;
	}
	
	@Column(name = "referred_by_customer_id")
	public BigDecimal getRefferedByCustomerId() {
		return this.referredByCustomerId;
	}

	public void setRefferedByCustomerId(BigDecimal referedByCustomerId) {
		this.referredByCustomerId = referedByCustomerId;
	}
	
	@Column(name = "is_consumed")
	public String getIsConsumed() {
		return this.isConsumed;
	}

	public void setIsConsumed(String isConsumed) {
		this.isConsumed = isConsumed;
	}

	@Column(name = "isactive")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

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
	
//	@Column(name = "is_winner")
//	public Boolean isWinner() {
//		return this.isWinner;
//	}
//
//	public void setIsWinner(Boolean isWinner) {
//		this.isWinner = isWinner;
//	}
	
	
	
}
