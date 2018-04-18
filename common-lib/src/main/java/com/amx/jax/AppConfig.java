package com.amx.jax;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.filter.AppClientInterceptor;

@Configuration
@PropertySource("classpath:application-lib.properties")
public class AppConfig {

	@Value("${app.name}")
	private String appName;

	@Value("${app.prod}")
	private Boolean prodMode;

	@Value("${app.swagger}")
	private boolean swaggerEnabled;

	@Value("${app.debug}")
	private Boolean debug;

	@Value("${app.cache}")
	private Boolean cache;

	@Value("${jax.cdn.url}")
	private String cdnURL;

	@Value("${jax.app.url}")
	private String appURL;

	@Value("${jax.service.url}")
	private String jaxURL;

	@Value("${jax.postman.url}")
	private String postmapURL;

	@Value("${jax.payment.url}")
	private String paygURL;

	@Value("${jax.logger.url}")
	private String loggerURL;

	@Value("${server.session.cookie.http-only}")
	private boolean cookieHttpOnly;

	@Value("${server.session.cookie.secure}")
	private boolean cookieSecure;

	public boolean isCookieHttpOnly() {
		return cookieHttpOnly;
	}

	public boolean isCookieSecure() {
		return cookieSecure;
	}

	public String getAppName() {
		return appName;
	}

	public Boolean isProdMode() {
		return prodMode;
	}

	public Boolean isSwaggerEnabled() {
		return swaggerEnabled;
	}

	public Boolean isDebug() {
		return debug;
	}

	public Boolean isCache() {
		return debug;
	}

	public String getCdnURL() {
		return cdnURL;
	}

	public String getAppURL() {
		return appURL;
	}

	public String getJaxURL() {
		return jaxURL;
	}

	public String getPostmapURL() {
		return postmapURL;
	}

	public String getPaygURL() {
		return paygURL;
	}

	public String getLoggerURL() {
		return loggerURL;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		builder.rootUri("https://localhost.com");
		RestTemplate restTemplate = builder.build();
		restTemplate.setInterceptors(Collections.singletonList(new AppClientInterceptor()));
		return restTemplate;
	}

}
