package com.amx.jax.ui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Component
public class AppEnvironment {

	private Boolean debug = false;

	@Value("${application.debug}")
	private Boolean appDebug = false;

	public Boolean getAppDebug() {
		return appDebug;
	}

	public void setAppDebug(Boolean appDebug) {
		this.appDebug = appDebug;
	}

	private Boolean isSet(ConfigurableEnvironment environment, String property) {
		String value = environment.getProperty(property);
		return (value != null && !value.equals("false"));
	}

	@Autowired
	private Boolean isSet(ConfigurableEnvironment environment) {
		this.debug = this.isSet(environment, "application.debug");
		return true;
	}

	public Boolean isDebug() {
		return debug;
	}

}
