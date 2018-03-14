package com.amx.jax.logger;

public abstract class AuditEvent {

	public interface EventType {
		String name();
	}

	protected String component;
	protected String category = getClass().getName();
	protected EventType type;
	protected long timestamp;
	protected String message;

	public AuditEvent() {
		this.timestamp = System.currentTimeMillis();
	}

	public AuditEvent(EventType type) {
		this();
		this.type = type;
	}

	public AuditEvent(EventType type, String message) {
		this(type);
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

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
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

}
