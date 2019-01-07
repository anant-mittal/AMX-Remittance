package com.amx.jax;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class AmxConfig {

	@TenantValue("${app.default.language.id}")
	BigDecimal defaultLanguageId;

	@TenantValue("${app.default.company.id}")
	BigDecimal defaultCompanyId;

	@TenantValue("${app.default.country.id}")
	BigDecimal defaultCountryId;

	@TenantValue("${app.default.currency.id}")
	BigDecimal defaultCurrencyId;

	@TenantValue("${app.default.branch.id}")
	BigDecimal defaultBranchId;

	public BigDecimal getDefaultLanguageId() {
		return defaultLanguageId;
	}

	public BigDecimal getDefaultCompanyId() {
		return defaultCompanyId;
	}

	public BigDecimal getDefaultCountryId() {
		return defaultCountryId;
	}

	public BigDecimal getDefaultCurrencyId() {
		return defaultCurrencyId;
	}

	public BigDecimal getDefaultBranchId() {
		return defaultBranchId;
	}

}
