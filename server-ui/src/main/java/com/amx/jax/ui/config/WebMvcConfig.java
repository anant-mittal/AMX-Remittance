package com.amx.jax.ui.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * The Class WebMvcConfig.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	public static final String TEMPLATES_BASE = "classpath:/templates/";
	/** Pattern relative to templates base used to match XML templates. */
	public static final String XML_TEMPLATES_RESOLVE_PATTERN = "xml/*";
	/** Pattern relative to templates base used to match JSON templates. */
	public static final String JSON_TEMPLATES_RESOLVE_PATTERN = "json/*";
	/** Pattern relative to templates base used to match text templates. */
	public static final String TEXT_TEMPLATES_RESOLVE_PATTERN = "text/*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#
	 * addInterceptors(org.springframework.web.servlet.config.annotation.
	 * InterceptorRegistry)
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(new CacheControlHandlerInterceptor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#
	 * addViewControllers(org.springframework.web.servlet.config.annotation.
	 * ViewControllerRegistry)
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// registry.addViewController("/register/**").setViewName("app");
		// registry.addViewController("/app/**").setViewName("app");
		// registry.addViewController("/home/**").setViewName("app");
		// registry.addViewController("/").setViewName("app");
		// registry.addViewController("/hello").setViewName("hello");
		// registry.addViewController("/login").setViewName("login");
	}

	@Bean
	public SpringResourceTemplateResolver jsonMessageTemplateResolver() {
		SpringResourceTemplateResolver theResourceTemplateResolver = new SpringResourceTemplateResolver();
		theResourceTemplateResolver.setPrefix(TEMPLATES_BASE);
		theResourceTemplateResolver.setResolvablePatterns(Collections.singleton(JSON_TEMPLATES_RESOLVE_PATTERN));
		theResourceTemplateResolver.setSuffix(".json");
		theResourceTemplateResolver.setTemplateMode(TemplateMode.TEXT);
		theResourceTemplateResolver.setCharacterEncoding("UTF-8");
		theResourceTemplateResolver.setCacheable(false);
		theResourceTemplateResolver.setOrder(1);
		return theResourceTemplateResolver;
	}

	/**
	 * Creates the template engine for all message templates.
	 *
	 * @param inTemplateResolvers
	 *            Template resolver for different types of messages etc. Note that
	 *            any template resolvers defined elsewhere will also be included in
	 *            this collection.
	 * @return Template engine.
	 */
	@Bean
	public SpringTemplateEngine messageTemplateEngine(
			final Collection<SpringResourceTemplateResolver> inTemplateResolvers) {
		final SpringTemplateEngine theTemplateEngine = new SpringTemplateEngine();
		for (SpringResourceTemplateResolver theTemplateResolver : inTemplateResolvers) {
			theTemplateEngine.addTemplateResolver(theTemplateResolver);
		}
		return theTemplateEngine;
	}
}
