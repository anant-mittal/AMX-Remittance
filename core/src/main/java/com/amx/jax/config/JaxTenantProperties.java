package com.amx.jax.config;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class JaxTenantProperties {

	@TenantValue("${support.compliance.email}")
	private String complianceEmail;
	
	@TenantValue("${dynamic.pricing.enabled}")
	private Boolean isDynamicPricingEnabled;

	public Boolean getIsDynamicPricingEnabled() {
		if (isDynamicPricingEnabled == null) {
			return Boolean.FALSE;
		}
		return isDynamicPricingEnabled;
	}

	public void setIsDynamicPricingEnabled(Boolean isDynamicPricingEnabled) {
		this.isDynamicPricingEnabled = isDynamicPricingEnabled;
	}

	public String getComplianceEmail() {
		return complianceEmail;
	}

	public void setComplianceEmail(String complianceEmail) {
		this.complianceEmail = complianceEmail;
	}
}
