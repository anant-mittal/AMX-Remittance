package com.amx.jax.config;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class JaxTenantProperties {

	@TenantValue("${support.compliance.email}")
	private String complianceEmail;

	public String getComplianceEmail() {
		return complianceEmail;
	}

	public void setComplianceEmail(String complianceEmail) {
		this.complianceEmail = complianceEmail;
	}
}
