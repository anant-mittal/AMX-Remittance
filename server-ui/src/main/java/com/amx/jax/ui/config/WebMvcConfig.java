package com.amx.jax.ui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(new CacheControlHandlerInterceptor());
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// registry.addViewController("/register/**").setViewName("app");
		// registry.addViewController("/app/**").setViewName("app");
		// registry.addViewController("/home/**").setViewName("app");
		// registry.addViewController("/").setViewName("app");
		// registry.addViewController("/hello").setViewName("hello");
		// registry.addViewController("/login").setViewName("login");
	}

}
