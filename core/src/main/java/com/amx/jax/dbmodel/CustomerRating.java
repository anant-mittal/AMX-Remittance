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
	private BigDecimal collectionDocNo;
	private BigDecimal collectionDocFyr;
	private String feedbackType;
	private BigDecimal delvSeqId;
	
	
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
	@Column(name = "FEEDBACK_TYPE ")
	public String getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}
	@Column(name = "COLLECTION_DOCUMENT_NO")
	public BigDecimal getCollectionDocNo() {
		return collectionDocNo;
	}
	public void setCollectionDocNo(BigDecimal collectionDocNo) {
		this.collectionDocNo = collectionDocNo;
	}
	@Column(name = "COLLECTION_DOC_FINANCE_YEAR")
	public BigDecimal getCollectionDocFyr() {
		return collectionDocFyr;
	}
	public void setCollectionDocFyr(BigDecimal collectionDocFyr) {
		this.collectionDocFyr = collectionDocFyr;
	}
	
	@Column(name = "DELV_SEQ_ID")
	public BigDecimal getDelvSeqId() {
		return delvSeqId;
	}
	public void setDelvSeqId(BigDecimal delvSeqId) {
		this.delvSeqId = delvSeqId;
	}
	
	
	
	
	
	
	
}
