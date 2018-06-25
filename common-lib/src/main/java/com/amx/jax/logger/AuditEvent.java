package com.amx.jax.logger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "description", "component", "category", "type", "timestamp", "message" })
public abstract class AuditEvent extends AbstractEvent {

	private static final long serialVersionUID = -1539116953165424464L;
	protected long tranxTime;
	protected long traceTime;
	protected long eventTime;
	protected String description = null;
	protected String message;
	protected String exception;
	protected String exceptionType;
	protected String actorId;

	public AuditEvent() {
		super();
	}

	public AuditEvent(EventType type) {
		super(type);
	}

	public AuditEvent(EventType type, String description) {
		this(type);
		this.description = description;
	}

	public AuditEvent(EventType type, String description, String message) {
		this(type);
		this.description = description;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getTranxTime() {
		return tranxTime;
	}

	public void setTranxTime(long tranxTime) {
		this.tranxTime = tranxTime;
	}

	public long getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(long traceTime) {
		this.traceTime = traceTime;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

}
