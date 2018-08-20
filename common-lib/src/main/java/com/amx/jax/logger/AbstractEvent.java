package com.amx.jax.logger;

import java.io.Serializable;

import com.amx.utils.EnumType;

public abstract class AbstractEvent implements Serializable {

	private static final long serialVersionUID = -3042991299608634785L;

	public enum EventMarker implements EnumType {
		AUDIT, TRACK, GAUGE, EXCEP
	}

	public interface EventType extends EnumType {
		EventMarker marker();
	}

	protected String component;
	protected String category = getClass().getSimpleName();
	protected EventType type;
	protected long timestamp;

	public AbstractEvent() {
		this.timestamp = System.currentTimeMillis();
	}

	public AbstractEvent(EventType type) {
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

}
