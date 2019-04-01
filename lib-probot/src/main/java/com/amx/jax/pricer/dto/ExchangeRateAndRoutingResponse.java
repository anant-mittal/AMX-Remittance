package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.amx.jax.io.JSONable;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;

public class ExchangeRateAndRoutingResponse implements Serializable, JSONable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8361396106764657660L;

	private long trnxBeginTimeEpoch;

	private CUSTOMER_CATEGORY customerCategory;

	private List<ExchangeRateDetails> sellRateDetails;

	private Map<BigDecimal, BankDetailsDTO> bankDetails;

	private List<TrnxRoutingDetails> trnxRoutingPaths;

	public long getTrnxBeginTimeEpoch() {
		return trnxBeginTimeEpoch;
	}

	public void setTrnxBeginTimeEpoch(long trnxBeginTimeEpoch) {
		this.trnxBeginTimeEpoch = trnxBeginTimeEpoch;
	}

	public CUSTOMER_CATEGORY getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(CUSTOMER_CATEGORY customerCategory) {
		this.customerCategory = customerCategory;
	}

	public List<ExchangeRateDetails> getSellRateDetails() {
		return sellRateDetails;
	}

	public void setSellRateDetails(List<ExchangeRateDetails> sellRateDetails) {
		this.sellRateDetails = sellRateDetails;
	}

	public Map<BigDecimal, BankDetailsDTO> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(Map<BigDecimal, BankDetailsDTO> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public List<TrnxRoutingDetails> getTrnxRoutingPaths() {
		return trnxRoutingPaths;
	}

	public void setTrnxRoutingPaths(List<TrnxRoutingDetails> trnxRoutingPaths) {
		this.trnxRoutingPaths = trnxRoutingPaths;
	}

}
