package com.amx.jax.offsite;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

/**
 * Properties specific to Offsite component
 * 
 * @author lalittanwar
 *
 */
@TenantScoped
@Component
public class OffsiteAppConfig {

	@TenantValue("${app.meta.country.id}")
	BigDecimal countryId;

	@TenantValue("${app.meta.company.id}")
	BigDecimal companyId;

	@TenantValue("${app.meta.lang.id}")
	BigDecimal languageId;

	@TenantValue("${app.meta.country.branch.id}")
	BigDecimal countrybranchId;

	
	public BigDecimal getCountryId() {
		return countryId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public BigDecimal getCountrybranchId() {
		return countrybranchId;
	}

}
