package com.amx.jax.ui.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {

	public SecurityInitializer() {
		super(WebSecurityConfig.class, HazelcastConfiguration.class);
	}
}
