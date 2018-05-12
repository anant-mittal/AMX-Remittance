package com.amx.jax.ui.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.jax.def.CacheForUser;
import com.amx.jax.ui.service.TenantService;
import com.amx.jax.ui.session.UserSession;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(UserBean.class);

	@Autowired
	private UserSession userSession;

	@Autowired
	private TenantService tenantContext;

	CurrencyMasterDTO defaultForCurrency;

	public CurrencyMasterDTO getDefaultForCurrency() {
		BigDecimal nationalityId = userSession.getCustomerModel().getPersoninfo().getNationalityId();
		if (nationalityId == null) {
			defaultForCurrency = tenantContext.getOnlineCurrencies().get(0);
		} else {
			for (CurrencyMasterDTO currency : tenantContext.getOnlineCurrencies()) {
				if (nationalityId.equals(currency.getCountryId())) {
					defaultForCurrency = currency;
					break;
				}
			}
		}
		return defaultForCurrency;
	}

	@CacheForUser
	public CurrencyMasterDTO getDefaultForCurrency(BigDecimal forCur) {
		if (forCur == null) {
			return this.getDefaultForCurrency();
		} else {
			for (CurrencyMasterDTO currency : tenantContext.getOnlineCurrencies()) {
				if (currency.getCurrencyId().equals(forCur)) {
					return currency;
				}
			}
		}
		return null;
	}

}
