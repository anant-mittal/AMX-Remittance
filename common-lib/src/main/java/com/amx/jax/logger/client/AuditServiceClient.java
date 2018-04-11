package com.amx.jax.logger.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.logger.AuditEvent;
import com.amx.jax.logger.AuditLoggerResponse;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.RequestTrackEvent;
import com.amx.utils.JsonUtil;

@Component
public class AuditServiceClient implements AuditService {

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);
	private static final Marker auditmarker = MarkerFactory.getMarker("AUDIT");
	private static final Marker trackmarker = MarkerFactory.getMarker("TRACK");
	private final Map<String, AuditFilter<AuditEvent>> filtersMap = new HashMap<>();
	private static String appName = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Autowired
	public AuditServiceClient(AppConfig appConfig, List<AuditFilter> filters) {
		appName = appConfig.getAppName();
		for (AuditFilter filter : filters) {
			Matcher matcher = pattern.matcher(filter.getClass().getGenericInterfaces()[0].getTypeName());
			if (matcher.find()) {
				filtersMap.put(matcher.group(1), filter);
			}
		}
	}

	public AuditLoggerResponse log(AuditEvent event) {
		event.setComponent(appName);
		if (filtersMap.containsKey(event.getClass().getName())) {
			AuditFilter<AuditEvent> filter = filtersMap.get(event.getClass().getName());
			filter.doFilter(event);
		}
		LOGGER.info(auditmarker, JsonUtil.toJson(event));
		return null;
	}

	/**
	 * 
	 * No filters will be called
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse logStatic(AuditEvent event) {
		event.setComponent(appName);
		LOGGER.info(auditmarker, JsonUtil.toJson(event));
		return null;
	}

	public static AuditLoggerResponse trackStatic(RequestTrackEvent event) {
		event.setComponent(appName);
		LOGGER.info(trackmarker, JsonUtil.toJson(event));
		return null;
	}

	public AuditLoggerResponse log(RequestTrackEvent event) {
		return AuditServiceClient.trackStatic(event);
	}

}
