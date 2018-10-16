package com.amx.jax.configs;

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

import com.amx.jax.audit.intercepter.JaxAuditInterceptor;

@Configuration
public class WebMvcConfigData extends WebMvcConfigurerAdapter {

	@Autowired
	JaxAuditInterceptor jaxAuditInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {		
		registry.addInterceptor(jaxAuditInterceptor);
	}
	
	@Bean(autowire=Autowire.BY_NAME)
	//@Qualifier("remitApplParametersMap")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Map<String, Object> remitApplParametersMap() {
		return new HashMap<String, Object>();
	}
}