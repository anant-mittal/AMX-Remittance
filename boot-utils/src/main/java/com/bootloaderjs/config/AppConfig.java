package com.bootloaderjs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("app-config.properties")
public class AppConfig {

	@Value("${app.name}")
	private String appName;

	public String getAppName() {
		return appName;
	}

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
}
