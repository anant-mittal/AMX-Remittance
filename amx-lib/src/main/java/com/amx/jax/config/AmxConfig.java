package com.amx.jax.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.bootloaderjs.config.AppConfig;

@PropertySource(value = "classpath:amx-config.properties")
@Configuration
public class AmxConfig extends AppConfig {

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
