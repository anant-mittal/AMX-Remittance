package com.amx.amxlib.meta.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.amx.jax.model.AbstractModel;

public class CustomerRatingDTO extends AbstractModel {
	
	private static final long serialVersionUID = -254755940678862078L;
	
	private BigDecimal customerId;
	private BigDecimal ratingId;
	private BigDecimal applicationCountryId;
	
	@NotNull(message="remittanceApplicationId may not be null")
	private BigDecimal remittanceApplicationId;
	
	private BigDecimal remittanceTransactionId;
	private Date createdDate;
	
	@NotNull
	@Range(min=1, max=10,message="rating should be between 1 and 10 range")
	private BigDecimal rating;
	
	private String ratingRemark;

	@Override
	public String getModelType() {
		return "customer-rating";
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getRatingId() {
		return ratingId;
	}

	public void setRatingId(BigDecimal ratingId) {
		this.ratingId = ratingId;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getRemittanceApplicationId() {
		return remittanceApplicationId;
	}

	public void setRemittanceApplicationId(BigDecimal remittanceApplicationId) {
		this.remittanceApplicationId = remittanceApplicationId;
	}

	public BigDecimal getRemittanceTransactionId() {
		return remittanceTransactionId;
	}

	public void setRemittanceTransactionId(BigDecimal remittanceTransactionId) {
		this.remittanceTransactionId = remittanceTransactionId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public String getRatingRemark() {
		return ratingRemark;
	}

	public void setRatingRemark(String ratingRemark) {
		this.ratingRemark = ratingRemark;
	}
	
	
}
