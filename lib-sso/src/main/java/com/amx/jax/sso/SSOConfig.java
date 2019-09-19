package com.amx.jax.sso;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

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

	@TenantValue("${app.sso.login.with.rop}")
	boolean ropEnabled;

	@TenantValue("${app.sso.login.without.card}")
	boolean loginWithoutCard;

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

	public boolean isRopEnabled() {
		return ropEnabled;
	}

	public boolean isLoginWithoutCard() {
		return loginWithoutCard;
	}

}
