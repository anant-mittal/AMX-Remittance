package com.amx.jax.ui;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.ui.config.WebTenantFilter;

@ServletComponentScan
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.amx.jax")
@EnableAsync
@EnableCaching
public class WebApplication extends SpringBootServletInitializer {

	public static final String USE_HAZELCAST = "false";
	public static final String USE_REDIS = "true";

	public static void main(String[] args) {
		// ConfigurableApplicationContext context =
		SpringApplication.run(WebApplication.class, args);
		// context.getBean(HealthService.class).sendApplicationLiveMessage();
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
		return applicationBuilder.sources(WebApplication.class);
	}

	@Bean(name = "checkSession")
	public FilterRegistrationBean filterRegistrationBean() {
		WebTenantFilter f = new WebTenantFilter();
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(f);
		ArrayList<String> match = new ArrayList<>();
		match.add("/*");
		registrationBean.setUrlPatterns(match);
		return registrationBean;
	}

	@Bean
	@ConditionalOnBean(name = "checkSession")
	public DelegatingFilterProxyRegistrationBean securityFilterChainRegistration(
			SecurityProperties securityProperties) {
		DelegatingFilterProxyRegistrationBean registration = new DelegatingFilterProxyRegistrationBean("checkSession");
		registration.setOrder(securityProperties.getFilterOrder());
		// registration.setDispatcherTypes(getDispatcherTypes(securityProperties));
		return registration;
	}

	// @Bean
	// ServletListenerRegistrationBean<ServletRequestListener>
	// myServletRequestListener() {
	// ServletListenerRegistrationBean<ServletRequestListener> srb = new
	// ServletListenerRegistrationBean<>();
	// srb.setListener(new WebRequestListener());
	// return srb;
	// }

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public com.amx.jax.amxlib.model.JaxMetaInfo JaxMetaInfo() {
		com.amx.jax.amxlib.model.JaxMetaInfo metaInfo = new com.amx.jax.amxlib.model.JaxMetaInfo();
		return metaInfo;
	}

}
