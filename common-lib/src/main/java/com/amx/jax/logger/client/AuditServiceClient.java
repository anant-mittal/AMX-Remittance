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
	private static final Marker excepmarker = MarkerFactory.getMarker("EXCEP");
	private static final Marker failmarker = MarkerFactory.getMarker("FAIL");
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
		event.setExceptionType(e.getClass().getName());
		event.setException(e.getMessage());
		return event;
	}

	private static AuditEvent captureDetails(AuditEvent event) {
		event.setComponent(appName);
		event.setTraceTime(TimeUtils.timeSince(AppContextUtil.getTraceTime()));
		event.setActorId(AppContextUtil.getActorId());
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

	/**
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse failStatic(AuditEvent event) {
		captureDetails(event);
		LOGGER.info(failmarker, JsonUtil.toJson(event));
		return null;
	}

	@Override
	public AuditLoggerResponse fail(AuditEvent event) {
		this.excuteFilters(event);
		return failStatic(event);
	}

	// Excep LOGS

	/**
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse excepStatic(AuditEvent event) {
		captureDetails(event);
		LOGGER.info(excepmarker, JsonUtil.toJson(event));
		return null;
	}

	public AuditLoggerResponse excep(AuditEvent event) {
		this.excuteFilters(event);
		return excepStatic(event);
	}

	@Override
	public AuditLoggerResponse excep(AuditEvent event, Exception e) {
		AuditServiceClient.captureException(event, e);
		return this.excep(event);
	}

	@Override
	public AuditLoggerResponse excep(AuditEvent event, Logger logger, Exception e) {
		logger.error(event.getType().toString(), e);
		return this.excep(event, e);
	}

}
