package com.amx.jax.sso;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;

@Component
@TenantScoped
@PropertySource("classpath:application-sso.properties")
public class SSOConfig {
	@Value("${amx.server.username}")
	String adminuser;

	@Value("${amx.server.password}")
	String adminpass;

	@Value("${adapter.url}")
	String adapterUrl;

	@Value("${server.session.browser.persistent}")
	boolean browserSessionPersist;

	public String getAdapterUrl() {
		return adapterUrl;
	}

	public String getAdminuser() {
		return adminuser;
	}

	public String getAdminpass() {
		return adminpass;
	}

	public boolean isBrowserSessionPersist() {
		return browserSessionPersist;
	}

}
