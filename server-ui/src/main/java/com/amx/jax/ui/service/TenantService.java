package com.amx.jax.ui.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.jax.def.CacheForTenant;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.scope.TenantScoped;

@Component
@TenantScoped
public class TenantService {

	@Autowired
	private JaxService jaxService;

	@CacheForTenant
	public CurrencyMasterDTO getDomCurrency() {
		return jaxService.setDefaults().getMetaClient()
				.getCurrencyByCountryId(TenantContextHolder.currentSite().getBDCode()).getResult();
	}

	@CacheForTenant
	public List<CurrencyMasterDTO> getOnlineCurrencies() {
		return jaxService.setDefaults().getMetaClient().getAllOnlineCurrency().getResults();
	}

}
