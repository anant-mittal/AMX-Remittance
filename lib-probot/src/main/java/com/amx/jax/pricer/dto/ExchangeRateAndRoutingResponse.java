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

	private Map<BigDecimal, Map<BigDecimal, ExchangeRateDetails>> bankServiceModeSellRates;

	private Map<BigDecimal, BankDetailsDTO> bankDetails;

	private Map<String, TrnxRoutingDetails> trnxRoutingPaths;

	private List<String> bestExchangeRatePaths;

	private String localTimezone;

	private String foreignTimezone;

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

	public Map<BigDecimal, BankDetailsDTO> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(Map<BigDecimal, BankDetailsDTO> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public List<String> getBestExchangeRatePaths() {
		return bestExchangeRatePaths;
	}

	public void setBestExchangeRatePaths(List<String> bestExchangeRatePaths) {
		this.bestExchangeRatePaths = bestExchangeRatePaths;
	}

	public Map<BigDecimal, Map<BigDecimal, ExchangeRateDetails>> getBankServiceModeSellRates() {
		return bankServiceModeSellRates;
	}

	public void setBankServiceModeSellRates(
			Map<BigDecimal, Map<BigDecimal, ExchangeRateDetails>> bankServiceModeSellRates) {
		this.bankServiceModeSellRates = bankServiceModeSellRates;
	}

	public Map<String, TrnxRoutingDetails> getTrnxRoutingPaths() {
		return trnxRoutingPaths;
	}

	public void setTrnxRoutingPaths(Map<String, TrnxRoutingDetails> trnxRoutingPaths) {
		this.trnxRoutingPaths = trnxRoutingPaths;
	}

	public String getLocalTimezone() {
		return localTimezone;
	}

	public void setLocalTimezone(String localTimezone) {
		this.localTimezone = localTimezone;
	}

	public String getForeignTimezone() {
		return foreignTimezone;
	}

	public void setForeignTimezone(String foreignTimezone) {
		this.foreignTimezone = foreignTimezone;
	}

}
