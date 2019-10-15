package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

public class ExchangeDiscountInfo implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -549548404004608601L;

	private BigDecimal id;
	private DISCOUNT_TYPE discountType;
	private String discountTypeValue;
	private BigDecimal discountPipsValue;

	public DISCOUNT_TYPE getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DISCOUNT_TYPE discountType) {
		this.discountType = discountType;
	}

	public String getDiscountTypeValue() {
		return discountTypeValue;
	}

	public void setDiscountTypeValue(String discountTypeValue) {
		this.discountTypeValue = discountTypeValue;
	}

	public BigDecimal getDiscountPipsValue() {
		return discountPipsValue;
	}

	public void setDiscountPipsValue(BigDecimal discountPipsValue) {
		this.discountPipsValue = discountPipsValue;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Override
	public ExchangeDiscountInfo clone() {

		try {
			return (ExchangeDiscountInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			ExchangeDiscountInfo info = new ExchangeDiscountInfo();

			info.discountPipsValue = this.discountPipsValue;
			info.discountType = this.discountType;
			info.discountPipsValue = this.discountPipsValue;

			return info;
		}

	}

}
