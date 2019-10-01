package com.amx.jax.radar;

import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * The Class WebmvcConfig.
 */
@Configuration
public class RadarMVCConfig extends WebMvcConfigurerAdapter {

	public static final String TEMPLATES_BASE = "classpath:/templates/";
	/** Pattern relative to templates base used to match XML templates. */
	public static final String XML_TEMPLATES_RESOLVE_PATTERN = "xml/*";
	/** Pattern relative to templates base used to match JSON templates. */
	public static final String JSON_TEMPLATES_RESOLVE_PATTERN = "json/*";
	/** Pattern relative to templates base used to match text templates. */
	public static final String TEXT_TEMPLATES_RESOLVE_PATTERN = "text/*";

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
			"classpath:/META-INF/resources/", "classpath:/resources/",
			"classpath:/static/", "classpath:/public/" };

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
		registry.addViewController("/index/**").setViewName("mains");
	}

	@Bean
	public SpringResourceTemplateResolver jsonMessageTemplateResolver() {
		SpringResourceTemplateResolver theResourceTemplateResolver = new SpringResourceTemplateResolver();
		theResourceTemplateResolver.setPrefix(TEMPLATES_BASE);
		theResourceTemplateResolver.setResolvablePatterns(Collections.singleton(JSON_TEMPLATES_RESOLVE_PATTERN));
		theResourceTemplateResolver.setSuffix(".json");
		theResourceTemplateResolver.setTemplateMode(TemplateMode.TEXT);
		theResourceTemplateResolver.setCharacterEncoding("UTF-8");
		theResourceTemplateResolver.setCacheable(true);
		theResourceTemplateResolver.setOrder(1);
		return theResourceTemplateResolver;
	}

	/**
	 * Creates the template engine for all message templates.
	 *
	 * @param inTemplateResolvers Template resolver for different types of messages
	 *                            etc. Note that any template resolvers defined
	 *                            elsewhere will also be included in this
	 *                            collection.
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

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations(
					"classpath:/META-INF/resources/webjars/");
		}
		if (!registry.hasMappingForPattern("/**")) {
			registry.addResourceHandler("/**").addResourceLocations(
					CLASSPATH_RESOURCE_LOCATIONS);
		}
	}

}
