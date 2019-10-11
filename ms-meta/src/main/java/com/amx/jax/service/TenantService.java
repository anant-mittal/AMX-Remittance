package com.amx.jax.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.CurrencyMasterMdlv1;

/**
 * @author Prashant
 *
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)

public class TenantService {

	@Autowired
	CompanyService companyService;
	@Autowired
	CurrencyMasterService currencyMasterService;

	/**
	 * @return default currency master for current tenant
	 * 
	 */
	public CurrencyMasterMdlv1 getDefaultCurrencyMaster() {
		BigDecimal defaultCurrencyId = companyService.getCompanyDetail().getCurrencyId();
		return currencyMasterService.getCurrencyMasterById(defaultCurrencyId);
	}
}
