package com.amx.jax.ui.config;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bootloaderjs.ContextUtil;

public class WebRequestListener implements ServletRequestListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestFilter.class);

	/**
	 * Reset the metadata for the current request thread
	 */
	public void requestDestroyed(ServletRequestEvent event) {
		LOGGER.info("Trace Id in filter requestDestroyed {}", ContextUtil.getTraceId());
		ContextUtil.map().clear();
	}

	/**
	 * Don't do anything when the request is initialized
	 */
	public void requestInitialized(ServletRequestEvent event) {
		LOGGER.info("Trace Id in filter requestInitialized {}", ContextUtil.getTraceId());
	}

}
