package com.amx.jax.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletRequestListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.service.PostManServiceImpl;
import com.amx.jax.ui.config.JaxProperties;
import com.amx.jax.ui.config.WebRequestFilter;
import com.amx.jax.ui.config.WebRequestListener;
import com.amx.jax.ui.service.HealthService;

@ServletComponentScan
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.amx.jax")
@EnableAsync
@EnableCaching
public class WebApplication extends SpringBootServletInitializer {

	public static final String USE_HAZELCAST = "false";
	public static final String USE_REDIS = "true";

	@Autowired
	private JaxProperties props;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(WebApplication.class, args);

		context.getBean(HealthService.class).sendApplicationLiveMessage();
	}

	@Bean("base_url")
	public URL baseUrl() throws MalformedURLException {
		return new URL(props.getJaxServiceUrl());
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
		return applicationBuilder.sources(WebApplication.class);
	}

	@Bean(name = "checkSession")
	public FilterRegistrationBean filterRegistrationBean() {
		WebRequestFilter f = new WebRequestFilter();
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

	@Bean
	ServletListenerRegistrationBean<ServletRequestListener> myServletRequestListener() {
		ServletListenerRegistrationBean<ServletRequestListener> srb = new ServletListenerRegistrationBean<>();
		srb.setListener(new WebRequestListener());
		return srb;
	}

}
