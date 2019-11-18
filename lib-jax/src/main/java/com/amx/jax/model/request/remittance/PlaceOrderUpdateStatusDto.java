package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;
import java.util.List;

public class PlaceOrderUpdateStatusDto {
	
	List<BigDecimal> ratePlaceOrderIdList;
	String flag;
	String remarks;
	BigDecimal exchangeRateOffered;
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public BigDecimal getExchangeRateOffered() {
		return exchangeRateOffered;
	}
	public void setExchangeRateOffered(BigDecimal exchangeRateOffered) {
		this.exchangeRateOffered = exchangeRateOffered;
	}
	public List<BigDecimal> getRatePlaceOrderIdList() {
		return ratePlaceOrderIdList;
	}
	public void setRatePlaceOrderIdList(List<BigDecimal> ratePlaceOrderIdList) {
		this.ratePlaceOrderIdList = ratePlaceOrderIdList;
	}
}
