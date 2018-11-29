package com.amx.jax.model.response.fx;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserStockDto implements Serializable {

   private static final long serialVersionUID = 1L;
   
   private BigDecimal stockId;
   private BigDecimal currencyId;
   private BigDecimal denominationId;
   private BigDecimal currentStock;
   private BigDecimal denominationAmount;
   private String denominationDesc;
   private BigDecimal denominationQuatity;
   private BigDecimal denominationPrice;
   
   public BigDecimal getStockId() {
		return stockId;
	}
	public void setStockId(BigDecimal stockId) {
		this.stockId = stockId;
	}

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

	public BigDecimal getCurrentStock() {
		return currentStock;
	}
	public void setCurrentStock(BigDecimal currentStock) {
		this.currentStock = currentStock;
	}

	public BigDecimal getDenominationAmount() {
		return denominationAmount;
	}
	public void setDenominationAmount(BigDecimal denominationAmount) {
		this.denominationAmount = denominationAmount;
	}
	
	public String getDenominationDesc() {
		return denominationDesc;
	}
	public void setDenominationDesc(String denominationDesc) {
		this.denominationDesc = denominationDesc;
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
    
}
