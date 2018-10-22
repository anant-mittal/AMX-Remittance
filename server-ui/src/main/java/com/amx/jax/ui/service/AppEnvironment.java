package com.amx.jax.ui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * The Class AppEnvironment.
 */
@Component
public class AppEnvironment {

	/** The debug. */
	private Boolean debug = false;

	/** The app debug. */
	@Value("${application.debug}")
	private Boolean appDebug = false;

	/**
	 * Gets the app debug.
	 *
	 * @return the app debug
	 */
	public Boolean getAppDebug() {
		return appDebug;
	}

	/**
	 * Sets the app debug.
	 *
	 * @param appDebug
	 *            the new app debug
	 */
	public void setAppDebug(Boolean appDebug) {
		this.appDebug = appDebug;
	}

	/**
	 * Checks if is set.
	 *
	 * @param environment
	 *            the environment
	 * @param property
	 *            the property
	 * @return the boolean
	 */
	private Boolean isSet(ConfigurableEnvironment environment, String property) {
		String value = environment.getProperty(property);
		return (value != null && !value.equals("false"));
	}

	/**
	 * Checks if is set.
	 *
	 * @param environment
	 *            the environment
	 * @return the boolean
	 */
	@Autowired
	private Boolean isSet(ConfigurableEnvironment environment) {
		this.debug = this.isSet(environment, "application.debug");
		return true;
	}

	/**
	 * Checks if is debug.
	 *
	 * @return the boolean
	 */
	public Boolean isDebug() {
		return debug;
	}

}
