package com.amx.jax.offsite;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.amx.jax.AppConfig;
import com.amx.jax.AppMVConfig;

@Configuration
public class OffsiteMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(interceptor);
	}

	@Autowired
	AppConfig appConfig;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("noindex");
		if (appConfig.isSwaggerEnabled()) {
			registry.addViewController("/signpad.html").setViewName("signpad");
			registry.addViewController("/notipy.html").setViewName("notipy");
		}
	}

	@Bean
	public SpringResourceTemplateResolver jsMessageTemplateResolver() {
		SpringResourceTemplateResolver theResourceTemplateResolver = new SpringResourceTemplateResolver();
		theResourceTemplateResolver.setPrefix(AppMVConfig.TEMPLATES_BASE);
		theResourceTemplateResolver
				.setResolvablePatterns(Collections.singleton(AppMVConfig.JS_TEMPLATES_RESOLVE_PATTERN));
		theResourceTemplateResolver.setSuffix(".js");
		theResourceTemplateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
		theResourceTemplateResolver.setCharacterEncoding("UTF-8");
		theResourceTemplateResolver.setCacheable(false);
		theResourceTemplateResolver.setOrder(1);
		return theResourceTemplateResolver;
	}

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