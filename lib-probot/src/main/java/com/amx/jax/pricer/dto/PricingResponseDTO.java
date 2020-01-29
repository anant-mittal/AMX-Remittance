package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;

public class PricingResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3237498833216893709L;

	private List<ExchangeRateDetails> sellRateDetails;

	private Map<BigDecimal, BankDetailsDTO> bankDetails;

	private Map<BigDecimal, String> serviceIdDescription;

	private Map<String, Object> info;

	private CUSTOMER_CATEGORY customerCategory;

	/*public PricingResponseDTO() {
		super();
	}

	public PricingResponseDTO(PricingResponseDTO that) {
		this.sellRateDetails = that.sellRateDetails;
		this.bankDetails = that.bankDetails;
		this.serviceIdDescription = that.serviceIdDescription;
		this.info = that.info;
		this.customerCategory = that.customerCategory;
	}*/

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

	public CUSTOMER_CATEGORY getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(CUSTOMER_CATEGORY customerCategory) {
		this.customerCategory = customerCategory;
	}

	public Map<BigDecimal, String> getServiceIdDescription() {
		return serviceIdDescription;
	}

	public void setServiceIdDescription(Map<BigDecimal, String> serviceIdDescription) {
		this.serviceIdDescription = serviceIdDescription;
	}

}
