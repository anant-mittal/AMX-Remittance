package com.amx.jax.radar;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

/**
 * The Class PostManConfig.
 */
@TenantScoped
@Component
public class RadarConfig {

	@TenantValue("${company.name}")
	private String companyName;

	@TenantValue("${company.website.url}")
	private String companyWebSiteUrl;

	@TenantValue("${company.idtype}")
	private String companyIDType;

	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyWebSiteUrl() {
		return companyWebSiteUrl;
	}

	public String getCompanyIDType() {
		return companyIDType;
	}

}
