package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;

public class CustomerCategoryDetails implements Serializable,Comparable<CustomerCategoryDetails>{

	private static final long serialVersionUID = 1L;

	private BigDecimal id;

	private CUSTOMER_CATEGORY customerCategory;

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

	
	public CUSTOMER_CATEGORY getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(CUSTOMER_CATEGORY customerCategory) {
		this.customerCategory = customerCategory;
	}

	public BigDecimal getDiscountPips() {
		return discountPips;
	}

	public String getDiscountPipsPT() {
		return discountPips == null ? null : discountPips.toPlainString();
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

	public String getMinDiscountPipsPT() {
		return minDiscountPips == null ? null : minDiscountPips.toPlainString();
	}

	public void setMinDiscountPips(BigDecimal minDiscountPips) {
		this.minDiscountPips = minDiscountPips;
	}

	public BigDecimal getMaxDiscountPips() {
		return maxDiscountPips;
	}

	public String getMaxDiscountPipsPT() {
		return maxDiscountPips == null ? null : maxDiscountPips.toPlainString();
	}

	public void setMaxDiscountPips(BigDecimal maxDiscountPips) {
		this.maxDiscountPips = maxDiscountPips;
	}

	@Override
	public int compareTo(CustomerCategoryDetails o) {
			
		return this.customerCategory.compareTo(o.customerCategory);
	}
	
}
