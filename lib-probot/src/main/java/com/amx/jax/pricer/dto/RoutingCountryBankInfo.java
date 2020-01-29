package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RoutingCountryBankInfo implements Serializable {

	private static final long serialVersionUID = 2066407983022114001L;

	private Map<BigDecimal, List<BigDecimal>> banksForCountry;

	private List<CountryMasterDTO> countries;

	private List<BankDetailsDTO> routingBanks;

	public Map<BigDecimal, List<BigDecimal>> getBanksForCountry() {
		return banksForCountry;
	}

	public void setBanksForCountry(Map<BigDecimal, List<BigDecimal>> banksForCountry) {
		this.banksForCountry = banksForCountry;
	}

	public List<CountryMasterDTO> getCountries() {
		return countries;
	}

	public void setCountries(List<CountryMasterDTO> countries) {
		this.countries = countries;
	}

	public List<BankDetailsDTO> getRoutingBanks() {
		return routingBanks;
	}

	public void setRoutingBanks(List<BankDetailsDTO> routingBanks) {
		this.routingBanks = routingBanks;
	}

}
