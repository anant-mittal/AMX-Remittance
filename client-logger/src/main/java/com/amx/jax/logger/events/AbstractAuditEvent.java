package com.amx.jax.logger.events;

public class AbstractAuditEvent implements AuditLogEvent {

	public long timestamp;
	private String moduleName;
	private EventType type;
	private String message;

	public AbstractAuditEvent() {
		this.timestamp = System.currentTimeMillis();
	}

	public AbstractAuditEvent(EventType type) {
		super();
		this.type = type;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
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
