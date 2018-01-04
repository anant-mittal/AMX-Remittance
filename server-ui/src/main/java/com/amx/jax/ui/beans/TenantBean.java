package com.amx.jax.ui.beans;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.ui.service.JaxService;

import groovy.transform.Synchronized;

@Component
@TenantScoped
public class TenantBean {

	@Autowired
	private JaxService jaxService;

	CurrencyMasterDTO domCurrency = null;

	List<CurrencyMasterDTO> onlineCurrencies = null;

	@Synchronized
	public void loadDomCurrency() {
		this.domCurrency = jaxService.setDefaults().getMetaClient()
				.getCurrencyByCountryId(new BigDecimal(JaxService.DEFAULT_COUNTRY_ID)).getResult();
	}

	public CurrencyMasterDTO getDomCurrency() {
		if (domCurrency == null) {
			this.loadDomCurrency();
		}
		return domCurrency;
	}

	@Synchronized
	public void loadOnlineCurrencies() {
		this.onlineCurrencies = jaxService.setDefaults().getMetaClient().getAllOnlineCurrency().getResults();
	}

	public List<CurrencyMasterDTO> getOnlineCurrencies() {
		if (onlineCurrencies == null) {
			this.loadOnlineCurrencies();
		}
		return onlineCurrencies;
	}
}
