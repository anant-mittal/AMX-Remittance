package com.amx.jax.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.amx.jax.interceptor.HeaderInterceptor;
import com.amx.jax.interceptor.TenantInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private HeaderInterceptor interceptor;

	@Autowired
	private TenantInterceptor tenantInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor);
		registry.addInterceptor(tenantInterceptor);
	}
	
	@Bean(name = "remitApplParametersMap")
	public Map<String, Object> getParametersMap() {
		return new HashMap<>();
	}
}