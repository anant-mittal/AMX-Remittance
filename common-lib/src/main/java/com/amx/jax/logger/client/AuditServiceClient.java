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
import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.AbstractEvent;
import com.amx.jax.logger.AbstractEvent.EventMarker;
import com.amx.jax.logger.AbstractEvent.EventType;
import com.amx.jax.logger.AuditEvent;
import com.amx.jax.logger.AuditLoggerResponse;
import com.amx.jax.logger.AuditService;
import com.amx.jax.tunnel.ITunnelService;
import com.amx.utils.JsonUtil;
import com.amx.utils.TimeUtils;

@Component
public class AuditServiceClient implements AuditService {

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);
	private static final Logger LOGGER2 = LoggerFactory.getLogger(AuditServiceClient.class);
	private static final Marker auditmarker = MarkerFactory.getMarker(EventMarker.AUDIT.toString());
	private static final Marker trackmarker = MarkerFactory.getMarker(EventMarker.TRACK.toString());
	private static final Marker gaugemarker = MarkerFactory.getMarker(EventMarker.GAUGE.toString());
	private static final Marker excepmarker = MarkerFactory.getMarker(EventMarker.EXCEP.toString());
	private static final Marker noticemarker = MarkerFactory.getMarker(EventMarker.NOTICE.toString());
	private static final Marker alertmarker = MarkerFactory.getMarker(EventMarker.ALERT.toString());
	private static final Map<String, Boolean> allowedMarkersMap = new HashMap<String, Boolean>();
	private final Map<String, AuditFilter<AuditEvent>> filtersMap = new HashMap<>();
	private static boolean FILTER_MAP_DONE = false;
	private static String APP_NAME = null;
	private static boolean AUDIT_LOGGER_ENABLED = false;

	private static ITunnelService ITUNNEL_SERVICE;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Autowired
	public AuditServiceClient(AppConfig appConfig, List<AuditFilter> filters,
			@Autowired(required = false) ITunnelService iTunnelService) {

		String[] allowedMarkersList = appConfig.getPrintableAuditMarkers();
		String[] skippedMarkersList = appConfig.getSkipAuditMarkers();

		for (String markerString : allowedMarkersList) {
			allowedMarkersMap.put(markerString, Boolean.TRUE);
		}

		for (String markerString : skippedMarkersList) {
			allowedMarkersMap.put(markerString, Boolean.FALSE);
		}

		if (!FILTER_MAP_DONE) {
			APP_NAME = appConfig.getAppName();
			AUDIT_LOGGER_ENABLED = appConfig.isLogger();
			for (AuditFilter filter : filters) {
				Matcher matcher = pattern.matcher(filter.getClass().getGenericInterfaces()[0].getTypeName());
				if (matcher.find()) {
					filtersMap.put(matcher.group(1), filter);
				}
			}
			ITUNNEL_SERVICE = iTunnelService;
			FILTER_MAP_DONE = true;
			LOGGER2.warn("Audit Filters scanned");
		} else {
			LOGGER2.warn("Skipping Audit Filters scanning");
		}
	}

	private void excuteFilters(AuditEvent event) {
		if (filtersMap.containsKey(event.getClass().getName())) {
			AuditFilter<AuditEvent> filter = filtersMap.get(event.getClass().getName());
			try {
				filter.doFilter(event);
			} catch (Exception e) {
				LOGGER2.error("Exception while executing Filters", e);
			}
		}
	}

	private static AuditEvent captureException(AuditEvent event, Exception e) {
		event.setExceptionType(e.getClass().getName());
		event.setException(e.getMessage());
		return event;
	}

	private static AuditEvent captureDetails(AuditEvent event) {
		event.setComponent(APP_NAME);
		Long traceTime = AppContextUtil.getTraceTime();
		if (traceTime != null) {
			event.setTraceTime(TimeUtils.timeSince(AppContextUtil.getTraceTime()));
		}
		event.setEventTime(TimeUtils.timeSince(event.getTimestamp()));
		event.setActorId(AppContextUtil.getActorId());
		return event;
	}

	public static void publishAbstractEvent(Map<String, Object> map) {
		try {
			AppContext appContext = AppContextUtil.getContext();
			map.put("traceId", appContext.getTraceId());
			map.put("tranxId", appContext.getTranxId());
			map.put("tenant", appContext.getTenant());
			ITUNNEL_SERVICE.audit(AUDIT_EVENT_TOPIC, map);
		} catch (Exception e) {
			LOGGER2.error("Exception while Publishing Event", e);
		}
	}

	public static AuditLoggerResponse logAbstractEvent(Marker marker, AbstractEvent event, boolean capture) {
		event.setComponent(APP_NAME);
		event.clean();
		String json = JsonUtil.toJson(event);

		String marketName = marker.getName();
		if (allowedMarkersMap.getOrDefault(marketName, Boolean.FALSE).booleanValue()) {
			LOGGER.info(marker, json);
		}
		if (capture && ITUNNEL_SERVICE != null && AUDIT_LOGGER_ENABLED) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = JsonUtil.fromJson(json, Map.class);
			publishAbstractEvent(map);
		}
		return null;
	}

	/**
	 * 
	 * @param marker
	 * @param event
	 * @param capture
	 *            - true to capture value in logger service, default is false
	 * @return
	 */
	public static AuditLoggerResponse logAuditEvent(Marker marker, AuditEvent event, boolean capture) {
		try {
			captureDetails(event);
			return logAbstractEvent(marker, event, capture);
		} catch (Exception e) {
			LOGGER2.error("Exception while logAuditEvent {}", JsonUtil.toJson(event));
		}
		return null;
	}

	/**
	 * 
	 * @param marker
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse logAuditEvent(Marker marker, AuditEvent event) {
		return logAuditEvent(marker, event, false);
	}

	public static AuditLoggerResponse logAuditEvent(EventMarker emarker, AuditEvent event) {
		Marker marker = auditmarker;
		boolean capture = false;
		if (emarker == EventMarker.AUDIT) {
			capture = true;
		} else if (emarker == EventMarker.TRACK) {
			marker = trackmarker;
		} else if (emarker == EventMarker.GAUGE) {
			marker = gaugemarker;
		} else if (emarker == EventMarker.EXCEP) {
			marker = excepmarker;
		} else if (emarker == EventMarker.ALERT) {
			marker = alertmarker;
		} else if (emarker == EventMarker.NOTICE) {
			marker = noticemarker;
		} else {
			capture = true;
		}
		return logAuditEvent(marker, event, capture);
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse logStatic(AuditEvent event) {
		EventType eventType = event.getType();
		if (eventType == null) {
			LOGGER2.error("Exception while logAuditEvent {}", JsonUtil.toJson(event));
			return null;
		}
		EventMarker eventMarker = eventType.marker();
		if (eventMarker == null) {
			eventMarker = EventMarker.AUDIT;
		}
		return logAuditEvent(eventMarker, event);
	}

	@Override
	public AuditLoggerResponse log(AuditEvent event) {
		excuteFilters(event);
		return logStatic(event);
	}

	// TRACK LOGS

	public static AuditLoggerResponse trackStatic(AuditEvent event) {
		return logAuditEvent(trackmarker, event);
	}

	@Override
	public AuditLoggerResponse track(AuditEvent event) {
		excuteFilters(event);
		return trackStatic(event);
	}

	// GAUGE LOGS
	/**
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse gaugeStatic(AuditEvent event) {
		return logAuditEvent(gaugemarker, event);
	}

	@Override
	public AuditLoggerResponse gauge(AuditEvent event) {
		excuteFilters(event);
		return gaugeStatic(event);
	}

	// Excep LOGS

	/**
	 * 
	 * @param event
	 * @return
	 */
	public static AuditLoggerResponse excepStatic(AuditEvent event) {
		event.setResult(AuditEvent.Result.ERROR);
		return logAuditEvent(excepmarker, event);
	}

	public AuditLoggerResponse excep(AuditEvent event) {
		excuteFilters(event);
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
