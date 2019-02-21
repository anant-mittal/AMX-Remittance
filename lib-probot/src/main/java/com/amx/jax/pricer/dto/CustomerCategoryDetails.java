package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomerCategoryDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal id;

	private String customerCategory;

	private BigDecimal discountPips;

	private String isActive;

	private BigDecimal minDiscountPips;

	private BigDecimal maxDiscountPips;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}

	public BigDecimal getDiscountPips() {
		return discountPips;
	}

	public void setDiscountPips(BigDecimal discountPips) {
		this.discountPips = discountPips;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getMinDiscountPips() {
		return minDiscountPips;
	}

	public void setMinDiscountPips(BigDecimal minDiscountPips) {
		this.minDiscountPips = minDiscountPips;
	}

	public BigDecimal getMaxDiscountPips() {
		return maxDiscountPips;
	}

	public void setMaxDiscountPips(BigDecimal maxDiscountPips) {
		this.maxDiscountPips = maxDiscountPips;
	}

}
