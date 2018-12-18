package com.amx.jax.pricer.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dto.BankRateDetailsDTO;

public class PricingRateDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<BankRateDetailsDTO> baseBankRatesNPrices;

	private Map<BigDecimal, ViewExGLCBAL> bankGlcBalMap;

	private OnlineMarginMarkup margin;

	private List<BankRateDetailsDTO> finalBanksRatesNPrices;

	public List<BankRateDetailsDTO> getBaseBankRatesNPrices() {
		return baseBankRatesNPrices;
	}

	public void setBaseBankRatesNPrices(List<BankRateDetailsDTO> baseBankRatesNPrices) {
		this.baseBankRatesNPrices = baseBankRatesNPrices;
	}

	public Map<BigDecimal, ViewExGLCBAL> getBankGlcBalMap() {
		return bankGlcBalMap;
	}

	public void setBankGlcBalMap(Map<BigDecimal, ViewExGLCBAL> bankGlcBalMap) {
		this.bankGlcBalMap = bankGlcBalMap;
	}

	public OnlineMarginMarkup getMargin() {
		return margin;
	}

	public void setMargin(OnlineMarginMarkup margin) {
		this.margin = margin;
	}

	public List<BankRateDetailsDTO> getFinalBanksRatesNPrices() {
		return finalBanksRatesNPrices;
	}

	public void setFinalBanksRatesNPrices(List<BankRateDetailsDTO> finalBanksRatesNPrices) {
		this.finalBanksRatesNPrices = finalBanksRatesNPrices;
	}
	
	

}
