package com.amx.jax.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("application-lib.properties")
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
	private String poaygURL;

	@Value("${jax.logger.url}")
	private String loggerURL;

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

	public String getPoaygURL() {
		return poaygURL;
	}

	public String getLoggerURL() {
		return loggerURL;
	}

}
