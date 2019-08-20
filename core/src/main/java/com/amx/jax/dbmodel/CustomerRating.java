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
@Table(name = "EX_CUSTOMER_RATING" )
@Proxy(lazy = false)
public class CustomerRating implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private BigDecimal customerId;
	private BigDecimal ratingId;
	private BigDecimal applicationCountryId;
	private BigDecimal remittanceApplicationId;
	private BigDecimal remittanceTransactionId;
	private Date createdDate;
	private BigDecimal rating;
	private String ratingRemark;
	private BigDecimal fxOrderTransactionId;
	private BigDecimal fxOrderApplicationId;
	private String feedbackType;
	
	
	@Id
	@GeneratedValue(generator="ex_custrating_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_custrating_seq" ,sequenceName="EX_CUSTRATING_SEQ",allocationSize=1)
	@Column(name = "RATING_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRatingId() {
		return ratingId;
	}
	public void setRatingId(BigDecimal ratingId) {
		this.ratingId = ratingId;
	}

	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	@Column(name = "REMITTANCE_APPLICATION_ID")
	public BigDecimal getRemittanceApplicationId() {
		return remittanceApplicationId;
	}
	public void setRemittanceApplicationId(BigDecimal remittanceApplicationId) {
		this.remittanceApplicationId = remittanceApplicationId;
	}
	
	@Column(name = "REMITTANCE_TRANSACTION_ID")
	public BigDecimal getRemittanceTransactionId() {
		return remittanceTransactionId;
	}
	public void setRemittanceTransactionId(BigDecimal remittanceTransactionId) {
		this.remittanceTransactionId = remittanceTransactionId;
	}
	
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "RATING")
	public BigDecimal getRating() {
		return rating;
	}
	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}
	
	@Column(name = "RATING_REMARK ")
	public String getRatingRemark() {
		return ratingRemark;
	}
	public void setRatingRemark(String ratingRemark) {
		this.ratingRemark = ratingRemark;
	}
	
	
	@Column(name = "FXORDER_TRANSACTION_ID")
	public BigDecimal getFxOrderTransactionId() {
		return fxOrderTransactionId;
	}
	public void setFxOrderTransactionId(BigDecimal fxOrderTransactionId) {
		this.fxOrderTransactionId = fxOrderTransactionId;
	}
	
	@Column(name = "FXORDER_APPLICATION_ID")
	public BigDecimal getFxOrderApplicationId() {
		return fxOrderApplicationId;
	}
	public void setFxOrderApplicationId(BigDecimal fxOrderApplicationId) {
		this.fxOrderApplicationId = fxOrderApplicationId;
	}
	public String getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}
	
	
	
	
}
