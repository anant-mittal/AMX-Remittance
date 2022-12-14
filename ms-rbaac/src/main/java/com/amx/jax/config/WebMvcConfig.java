package com.amx.jax.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// Disable Header Interceptor
//import com.amx.jax.interceptor.HeaderInterceptor;
import com.amx.jax.interceptor.TenantInterceptor;

/**
 * The Class WebMvcConfig.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	// @Autowired
	// private HeaderInterceptor interceptor;

	/** The tenant interceptor. */
	@Autowired
	private TenantInterceptor tenantInterceptor;

	/**
	 * Adds the interceptors.
	 *
	 * @param registry the registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(tenantInterceptor);
		// registry.addInterceptor(interceptor);
	}

	/**
	 * Remit appl parameters map.
	 *
	 * @return the map
	 */
	@Bean(autowire = Autowire.BY_NAME)
	// @Qualifier("remitApplParametersMap")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Map<String, Object> remitApplParametersMap() {
		return new HashMap<String, Object>();
	}
}