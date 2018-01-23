package com.amx.jax.scheduler.ratealert;

import java.util.List;
import java.util.Map;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.RateAlertDTO;

public final class RateAlertData {

	// list of foreign currency
	List<CurrencyMasterDTO> foreignCurrencyList;
	// currency vs exchange rate map
	Map<CurrencyMasterDTO, List<BankMasterDTO>> exchangeRates;
	// domestic currency
	CurrencyMasterDTO domesticCurrency;
	// rate alerts
	List<RateAlertDTO> rateAlerts;
	
	public List<CurrencyMasterDTO> getForeignCurrencyList() {
		return foreignCurrencyList;
	}
	public void setForeignCurrencyList(List<CurrencyMasterDTO> foreignCurrencyList) {
		this.foreignCurrencyList = foreignCurrencyList;
	}
	public Map<CurrencyMasterDTO, List<BankMasterDTO>> getExchangeRates() {
		return exchangeRates;
	}
	public void setExchangeRates(Map<CurrencyMasterDTO, List<BankMasterDTO>> exchangeRates) {
		this.exchangeRates = exchangeRates;
	}
	public CurrencyMasterDTO getDomesticCurrency() {
		return domesticCurrency;
	}
	public void setDomesticCurrency(CurrencyMasterDTO domesticCurrency) {
		this.domesticCurrency = domesticCurrency;
	}
	public List<RateAlertDTO> getRateAlerts() {
		return rateAlerts;
	}
	public void setRateAlerts(List<RateAlertDTO> rateAlerts) {
		this.rateAlerts = rateAlerts;
	}
	
}
