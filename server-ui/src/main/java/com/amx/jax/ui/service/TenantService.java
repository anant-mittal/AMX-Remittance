package com.amx.jax.ui.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.jax.scope.AbstractTenantService;
import com.amx.jax.scope.TenantScoped;

import groovy.transform.Synchronized;

@Component
@TenantScoped
public class TenantService extends AbstractTenantService {

	@Autowired
	private JaxService jaxService;

	CurrencyMasterDTO domCurrency = null;

	List<CurrencyMasterDTO> onlineCurrencies = null;

	@Synchronized
	public void loadDomCurrency() {
		this.domCurrency = jaxService.setDefaults().getMetaClient().getCurrencyByCountryId(getTenant().getBDCode())
				.getResult();
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
