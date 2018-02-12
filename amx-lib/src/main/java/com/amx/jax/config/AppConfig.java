package com.amx.jax.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("common.properties")
@Configuration
public class AppConfig {

	@Value("${app.prod}")
	private Boolean prodMode;

	public Boolean isProdMode() {
		return prodMode;
	}

	@Value("${app.swagger}")
	private Boolean swaggerEnabled;

	public Boolean isSwaggerEnabled() {
		return swaggerEnabled;
	}

	@Value("${app.debug}")
	private Boolean debug;

	public Boolean isDebug() {
		return debug;
	}

	@Value("${jax.cdn.url}")
	private String cdnURL;

	@Value("${jax.app.url}")
	private String appURL;

	@Value("${jax.service.url}")
	private String jaxURL;

	@Value("${jax.postman.url}")
	private String postmapURL;

	@Value("${jax.payment.url}")
	private String poaygURL;

	@Value("${jax.logger.url}")
	private String loggerURL;

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

	public String getPoaygURL() {
		return poaygURL;
	}

	public String getLoggerURL() {
		return loggerURL;
	}

}
