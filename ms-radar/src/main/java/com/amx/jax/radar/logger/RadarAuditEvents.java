package com.amx.jax.radar.logger;

import java.util.Map;

import com.amx.jax.exception.AmxApiException;
import com.amx.jax.logger.AuditEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class PMGaugeEvent.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RadarAuditEvents extends AuditEvent<RadarAuditEvents> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6667775998834926934L;

	/**
	 * The Enum Type.
	 */
	public static enum Type implements EventType {

		NEW_LOGIN_DEVICE;

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.amx.jax.logger.AbstractEvent.EventType#marker()
		 */
		@Override
		public EventMarker marker() {
			return EventMarker.NOTICE;
		}

	}

	private Map<String, Object> loginDeviceDetails;

	public RadarAuditEvents() {
		super();
	}

	public RadarAuditEvents(EventType type) {
		super(type);
	}

	public RadarAuditEvents set(Result result) {
		this.result = result;
		return this;
	}

	public RadarAuditEvents result(Result result, AmxApiException excep) {
		super.result(result, excep);
		return this;
	}

	@Override
	public String getDescription() {
		if (this.description == null) {
			return String.format("%s_%s", this.type, this.result);
		}
		return this.description;
	}

	public Map<String, Object> getLoginDeviceDetails() {
		return loginDeviceDetails;
	}

	public void setLoginDeviceDetails(Map<String, Object> loginDeviceDetails) {
		this.loginDeviceDetails = loginDeviceDetails;
	}

	public RadarAuditEvents loginDeviceDetails(Map<String, Object> loginDeviceDetails) {
		this.loginDeviceDetails = loginDeviceDetails;
		return this;
	}

}
