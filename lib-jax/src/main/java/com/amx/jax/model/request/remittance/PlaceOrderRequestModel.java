package com.amx.jax.model.request.remittance;
/**
 * @author rabil 
 * @date 10/29/2019
 */
import java.math.BigDecimal;

import com.amx.jax.model.response.ExchangeRateBreakup;

public class PlaceOrderRequestModel {
	
	BranchRemittanceApplRequestModel applRequestModel = new BranchRemittanceApplRequestModel();
	String remarks;
	BigDecimal exchangeRateOffered;
	Boolean booGsm;
	public BranchRemittanceApplRequestModel getApplRequestModel() {
		return applRequestModel;
	}

	public void setApplRequestModel(BranchRemittanceApplRequestModel applRequestModel) {
		this.applRequestModel = applRequestModel;
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

	public Boolean getBooGsm() {
		return booGsm;
	}

	public void setBooGsm(Boolean booGsm) {
		this.booGsm = booGsm;
	}
	
	
}
