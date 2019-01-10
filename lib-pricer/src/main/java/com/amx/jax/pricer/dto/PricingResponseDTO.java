package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PricingResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3237498833216893709L;

	private List<ExchangeRateDetails> sellRateDetails;

	private Map<BigDecimal, BankDetailsDTO> bankDetails;

	private Map<String, Object> info;

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

	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}

}
