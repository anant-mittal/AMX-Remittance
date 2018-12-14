package com.amx.jax.branch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class WebAppConfig {
	
	@Value("${jax.cdn.url}")
	private String cleanCDNUrl;
	
	@Value("${oldbranch.url}")
	private String oldBranchUrl;
	
	/** The app title. */
	@TenantValue("${application.title}")
	private String appTitle;
	
	public String getOldBranchUrl() {
		return oldBranchUrl;
	}
	
	public String getCleanCDNUrl() {
		return cleanCDNUrl;
	}

	public Object getAppTitle() {
		// TODO Auto-generated method stub
		return appTitle;
	}

}
