package com.amx.jax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.amx.jax.filter.AppRequestInterceptor;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppMVConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private AppRequestInterceptor appRequestInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(appRequestInterceptor);
	}
}
