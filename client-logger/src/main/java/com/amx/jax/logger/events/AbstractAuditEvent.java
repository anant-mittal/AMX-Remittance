package com.amx.jax.logger.events;

public class AbstractAuditEvent implements AuditLogEvent {

	public long timestamp;
	private String moduleName;
	private Type type;
	private String message;

	public AbstractAuditEvent() {
		this.timestamp = System.currentTimeMillis();
	}

	public AbstractAuditEvent(Type type) {
		super();
		this.type = type;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

}
