package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class PricingAndCostResponseDTO extends PricingResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2694304460995248914L;

	private Map<BigDecimal, CostRateDetails> bankCostRateDetails;

	public Map<BigDecimal, CostRateDetails> getBankCostRateDetails() {
		return bankCostRateDetails;
	}

	public void setBankCostRateDetails(Map<BigDecimal, CostRateDetails> bankCostRateDetails) {
		this.bankCostRateDetails = bankCostRateDetails;
	}

}
