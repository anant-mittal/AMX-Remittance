package com.amx.jax.ui.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.scope.TenantScoped;

/**
 * The Class TenantService.
 */
@Component
@TenantScoped
public class TenantService implements Serializable {

	private static final long serialVersionUID = -5588109359887763628L;
	
	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/**
	 * Gets the dom currency.
	 *
	 * @return the dom currency
	 */
	//@CacheForTenant
	public CurrencyMasterDTO getDomCurrency() {
		return jaxService.setDefaults().getMetaClient()
				.getCurrencyByCountryId(TenantContextHolder.currentSite().getBDCode()).getResult();
	}

	/**
	 * Gets the online currencies.
	 *
	 * @return the online currencies
	 */
	//@CacheForTenant
	public List<CurrencyMasterDTO> getOnlineCurrencies() {
		return jaxService.setDefaults().getMetaClient().getAllOnlineCurrency().getResults();
	}

}
