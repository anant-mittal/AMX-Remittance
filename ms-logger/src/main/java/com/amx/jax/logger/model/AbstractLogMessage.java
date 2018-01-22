package com.amx.jax.logger.model;

import org.springframework.data.mongodb.core.index.TextIndexed;

import com.amx.jax.logger.events.AuditLogEvent;

public class AbstractLogMessage implements AuditLogEvent {

	public long timestamp;

	@TextIndexed
	private String moduleName;

	private Type type;

	private String message;

	public AbstractLogMessage(AuditLogEvent event) {
		this.timestamp = event.getTimestamp();
		this.moduleName = event.getModuleName();
		this.type = event.getType();
		this.message = event.getMessage();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
