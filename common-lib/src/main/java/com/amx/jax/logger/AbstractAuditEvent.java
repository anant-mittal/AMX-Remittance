package com.amx.jax.logger;

import com.amx.utils.EnumType;

public abstract class AbstractAuditEvent {

	protected String component;
	protected String category = getClass().getSimpleName();
	protected EnumType type;
	protected long timestamp;

	public AbstractAuditEvent() {
		this.timestamp = System.currentTimeMillis();
	}

	public AbstractAuditEvent(EnumType type) {
		this();
		this.type = type;
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

}
