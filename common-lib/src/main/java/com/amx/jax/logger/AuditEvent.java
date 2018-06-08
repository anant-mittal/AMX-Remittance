package com.amx.jax.logger;

import com.amx.utils.EnumType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "description", "component", "category", "type", "timestamp", "message" })
public abstract class AuditEvent {

	protected String component;
	protected String category = getClass().getSimpleName();
	protected EnumType type;
	protected long timestamp;
	protected long tranxTime;
	protected long traceTime;
	protected String description = null;
	protected String message;
	protected String exception;
	protected String exceptionType;
	protected String actorId;

	public AuditEvent() {
		this.timestamp = System.currentTimeMillis();
	}

	public AuditEvent(EnumType type) {
		this();
		this.type = type;
	}

	public AuditEvent(EnumType type, String description) {
		this(type);
		this.description = description;
	}

	public AuditEvent(EnumType type, String description, String message) {
		this(type);
		this.description = description;
		this.message = message;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public EnumType getType() {
		return type;
	}

	public void setType(EnumType type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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

}
