package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_CUSTOMER_DISCOUNTS")
public class CustomerDiscountsView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "IDNO", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;

	@Column(name = "CUSTOMER_ID")
	private BigDecimal customerId;

	@Column(name = "CUSTOMER_CATEGORY_ID")
	private BigDecimal customerCategoryId;
	
	@Column(name = "JAX_PR_CUST_CAT_DISCOUNT_ID")
	private BigDecimal jaxPrCustCatDiscountId;

	@Column(name = "CUSTOMER_CATEGORY")
	private String customerCategory;
	
	@Column(name = "JAX_PR_DISCOUNT_MASTER_ID")
	private BigDecimal jaxPrDiscountMasterId;

	@Column(name = "GROUP_ID")
	private BigDecimal groupId;

	@Column(name = "DISCOUNT_TYPE")
	private String discountType;

	@Column(name = "DISCOUNT_TYPE_ID")
	private BigDecimal discountTypeId;

	@Column(name = "DISCOUNT_PIPS")
	private BigDecimal discountPips;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCustomerCategoryId() {
		return customerCategoryId;
	}

	public void setCustomerCategoryId(BigDecimal customerCategoryId) {
		this.customerCategoryId = customerCategoryId;
	}

	public String getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(String customerCategory) {
		this.customerCategory = customerCategory;
	}

	public BigDecimal getGroupId() {
		return groupId;
	}

	public void setGroupId(BigDecimal groupId) {
		this.groupId = groupId;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public BigDecimal getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(BigDecimal discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	public BigDecimal getDiscountPips() {
		return discountPips;
	}

	public void setDiscountPips(BigDecimal discountPips) {
		this.discountPips = discountPips;
	}

	public BigDecimal getJaxPrCustCatDiscountId() {
		return jaxPrCustCatDiscountId;
	}

	public void setJaxPrCustCatDiscountId(BigDecimal jaxPrCustCatDiscountId) {
		this.jaxPrCustCatDiscountId = jaxPrCustCatDiscountId;
	}

	public BigDecimal getJaxPrDiscountMasterId() {
		return jaxPrDiscountMasterId;
	}

	public void setJaxPrDiscountMasterId(BigDecimal jaxPrDiscountMasterId) {
		this.jaxPrDiscountMasterId = jaxPrDiscountMasterId;
	}

}
