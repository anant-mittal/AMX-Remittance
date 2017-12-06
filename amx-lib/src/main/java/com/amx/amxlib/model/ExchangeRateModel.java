package com.amx.amxlib.model;

public class ExchangeRateModel extends AbstractModel {

	String fromCurrency;
	String toCurrency;
	Double exchangeRate;
	Double inverseExchangeRate;

	public String getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	@Override
	public String getModelType() {
		return "exchange_rate";
	}

	public Double getInverseExchangeRate() {
		return inverseExchangeRate;
	}

	public void setInverseExchangeRate(Double inverseExchangeRate) {
		this.inverseExchangeRate = inverseExchangeRate;
	}

}
