package com.amx.jax.model.request.fx;

import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;

public class FcSaleBranchDispatchModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal currencyId;
	private BigDecimal denominationId;
	private BigDecimal denominationAmount;
	private BigDecimal denominationQuatity;
	private BigDecimal denominationPrice;
	private BigDecimal documentNumber;
	private String inventoryId;
	
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	
	public BigDecimal getDenominationId() {
		return denominationId;
	}
	public void setDenominationId(BigDecimal denominationId) {
		this.denominationId = denominationId;
	}
	
	public BigDecimal getDenominationAmount() {
		return denominationAmount;
	}
	public void setDenominationAmount(BigDecimal denominationAmount) {
		this.denominationAmount = denominationAmount;
	}
	
	public BigDecimal getDenominationQuatity() {
		return denominationQuatity;
	}
	public void setDenominationQuatity(BigDecimal denominationQuatity) {
		this.denominationQuatity = denominationQuatity;
	}
	
	public BigDecimal getDenominationPrice() {
		return denominationPrice;
	}
	public void setDenominationPrice(BigDecimal denominationPrice) {
		this.denominationPrice = denominationPrice;
	}
	
	public BigDecimal getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(BigDecimal documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	public String getInventoryId() {
		return inventoryId;
	}
	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}
	
}
