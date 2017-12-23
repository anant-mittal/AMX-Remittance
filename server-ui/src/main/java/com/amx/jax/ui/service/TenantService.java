package com.amx.jax.ui.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.jax.scope.TenantScoped;

import groovy.transform.Synchronized;

@Component
@TenantScoped
public class TenantService {

	@Autowired
	private JaxService jaxService;

	CurrencyMasterDTO domCurrency = null;

	List<CurrencyMasterDTO> onlineCurrencies = null;

	@Synchronized
	public CurrencyMasterDTO getDomCurrency() {
		if (domCurrency == null) {
			domCurrency = jaxService.setDefaults().getMetaClient()
					.getCurrencyByCountryId(new BigDecimal(JaxService.DEFAULT_COUNTRY_ID)).getResult();
		}
		return domCurrency;
	}

	@Synchronized
	public List<CurrencyMasterDTO> getOnlineCurrencies() {
		if (onlineCurrencies == null) {
			onlineCurrencies = jaxService.setDefaults().getMetaClient().getAllOnlineCurrency().getResults();
		}
		return onlineCurrencies;
	}

}
