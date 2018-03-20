package com.amx.jax.logger;

import com.bootloaderjs.EnumType;

public abstract class AuditEvent {

	protected String component;
	protected String category = getClass().getSimpleName();
	protected EnumType type;
	protected long timestamp;
	protected String message;
	protected String description = null;

	public AuditEvent() {
		this.timestamp = System.currentTimeMillis();
	}

	public AuditEvent(EnumType type) {
		this();
		this.type = type;
	}

	public AuditEvent(EnumType type, String message) {
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

}
