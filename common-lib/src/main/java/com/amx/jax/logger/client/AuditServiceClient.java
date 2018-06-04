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
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.AuditEvent;
import com.amx.jax.logger.AuditLoggerResponse;
import com.amx.jax.logger.AuditService;
import com.amx.utils.JsonUtil;
import com.amx.utils.TimeUtils;

@Component
public class AuditServiceClient implements AuditService {

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);
	private static final Marker auditmarker = MarkerFactory.getMarker("AUDIT");
	private static final Marker trackmarker = MarkerFactory.getMarker("TRACK");
	private static final Marker gaugemarker = MarkerFactory.getMarker("GAUGE");
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

	private void excuteFilters(AuditEvent event) {
		if (filtersMap.containsKey(event.getClass().getName())) {
			AuditFilter<AuditEvent> filter = filtersMap.get(event.getClass().getName());
			filter.doFilter(event);
		}
	}

	private static AuditEvent captureException(AuditEvent event, Exception e) {
		event.setException(String.format("%s : %s", e.getClass().getName(), e.getMessage()));
		return event;
	}

	private static AuditEvent captureDetails(AuditEvent event) {
		event.setComponent(appName);
		event.setTraceTime(TimeUtils.timeSince(AppContextUtil.getTraceTime()));
		return event;
	}

	/**
	 * 
	 * No filters will be called
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse logStatic(AuditEvent event) {
		captureDetails(event);
		LOGGER.info(auditmarker, JsonUtil.toJson(event));
		return null;
	}

	@Override
	public AuditLoggerResponse log(AuditEvent event) {
		this.excuteFilters(event);
		return logStatic(event);
	}

	@Override
	public AuditLoggerResponse log(AuditEvent event, Exception e) {
		AuditServiceClient.captureException(event, e);
		return this.log(event);
	}

	// TRACK LOGS

	public static AuditLoggerResponse trackStatic(AuditEvent event) {
		captureDetails(event);
		LOGGER.info(trackmarker, JsonUtil.toJson(event));
		return null;
	}

	@Override
	public AuditLoggerResponse track(AuditEvent event) {
		this.excuteFilters(event);
		return trackStatic(event);
	}

	@Override
	public AuditLoggerResponse track(AuditEvent event, Exception e) {
		AuditServiceClient.captureException(event, e);
		return this.track(event);
	}

	// GAUGE LOGS

	/**
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse gaugeStatic(AuditEvent event) {
		captureDetails(event);
		LOGGER.info(gaugemarker, JsonUtil.toJson(event));
		return null;
	}

	@Override
	public AuditLoggerResponse gauge(AuditEvent event) {
		this.excuteFilters(event);
		return gaugeStatic(event);
	}

	@Override
	public AuditLoggerResponse gauge(AuditEvent event, Exception e) {
		AuditServiceClient.captureException(event, e);
		return this.gauge(event);
	}

}
