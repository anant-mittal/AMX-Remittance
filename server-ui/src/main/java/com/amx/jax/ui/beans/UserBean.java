package com.amx.jax.ui.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.jax.ui.session.UserSession;

import groovy.transform.Synchronized;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(UserBean.class);

	@Autowired
	private UserSession userSession;

	@Autowired
	private TenantBean tenantBean;

	CurrencyMasterDTO defaultForCurrency;

	public CurrencyMasterDTO getDefaultForCurrency() {
		if (defaultForCurrency == null) {
			this.loadDefaultForCurrency();
		}
		return defaultForCurrency;
	}

	@Synchronized
	public void loadDefaultForCurrency() {
		BigDecimal nationalityId = userSession.getCustomerModel().getPersoninfo().getNationalityId();
		if (nationalityId == null) {
			defaultForCurrency = tenantBean.getOnlineCurrencies().get(0);
		} else {
			for (CurrencyMasterDTO currency : tenantBean.getOnlineCurrencies()) {
				if (nationalityId.equals(currency.getCountryId())) {
					defaultForCurrency = currency;
					break;
				}
			}
		}
	}

}
