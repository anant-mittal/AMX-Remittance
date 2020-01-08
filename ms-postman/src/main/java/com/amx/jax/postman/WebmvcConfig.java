package com.amx.jax.postman;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

/**
 * The Class WebmvcConfig.
 */
@Configuration
public class WebmvcConfig extends WebMvcConfigurerAdapter {

	public static final String TEMPLATES_BASE = "classpath:/templates/";
	/** Pattern relative to templates base used to match XML templates. */
	public static final String XML_TEMPLATES_RESOLVE_PATTERN = "xml/*";
	/** Pattern relative to templates base used to match JSON templates. */
	public static final String JSON_TEMPLATES_RESOLVE_PATTERN = "json/*";
	public static final String HTML_TEMPLATES_RESOLVE_PATTERN = "html/*";
	/** Pattern relative to templates base used to match text templates. */
	public static final String TEXT_TEMPLATES_RESOLVE_PATTERN = "text/*";
	public static final String OWA_TEMPLATES_RESOLVE_PATTERN = "owa-content/*";

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
		registry.addInterceptor(localeChangeInterceptor());
	}

	/**
	 * Locale resolver.
	 *
	 * @return the locale resolver
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);
		return slr;
	}

	/**
	 * Locale change interceptor.
	 *
	 * @return the locale change interceptor
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	/**
	 * Message source.
	 *
	 * @return the reloadable resource bundle message source
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18ns/messages");
		messageSource.setCacheSeconds(3600); // refresh cache once per hour
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
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

	@Value("${jax.static.url}")
	String jaxStaticUrl;
	
	@Value("${jax.static.context}")
	String jaxStaticContext;

	private FileTemplateResolver fileTemplateResolver() {
		FileTemplateResolver resolver = new FileTemplateResolver();
		resolver.setPrefix(jaxStaticUrl+"/");
		resolver.setResolvablePatterns(Collections.singleton(jaxStaticContext+"/templates/html/*"));
		resolver.setSuffix(".html");
		resolver.setCacheable(false);
		return resolver;
	}
	
	private FileTemplateResolver fileJsonTemplateResolver() {
		FileTemplateResolver resolver = new FileTemplateResolver();
		resolver.setPrefix(jaxStaticUrl+"/");
		resolver.setResolvablePatterns(Collections.singleton(jaxStaticContext+"/templates/json/*"));
		resolver.setSuffix(".json");
		resolver.setCacheable(false);
		return resolver;
	}

	private UrlTemplateResolver urlTemplateResolver() {
		UrlTemplateResolver urlTemplateResolver = new UrlTemplateResolver();
		urlTemplateResolver.setCacheable(false); // explicit set cacheable, otherwise it will be always cached
		// urlTemplateResolver.setResolvablePatterns(Collections.singleton(OWA_TEMPLATES_RESOLVE_PATTERN));
		return urlTemplateResolver;
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
		theTemplateEngine.addTemplateResolver(fileTemplateResolver());
		theTemplateEngine.addTemplateResolver(fileJsonTemplateResolver());
		return theTemplateEngine;
	}

}
