package com.amx.jax.logger;

import java.io.Serializable;

import com.amx.utils.EnumType;

public abstract class AbstractEvent implements Serializable {

	private static final long serialVersionUID = -3042991299608634785L;

	public enum EventMarker implements EnumType {

		/**
		 * In case some alert is required SOS
		 */
		ALERT,

		/**
		 * Failure cases, usually for developers
		 */
		EXCEP,

		/**
		 * Event Important from business point of view, these event will be saved in
		 * DataBase
		 */
		AUDIT,

		/**
		 * Just information logs, NO DB, these are logs which can become candidate for
		 * AUDIT
		 */
		NOTICE,

		/**
		 * Events meant to be logged from method or event performance evaluation
		 * perspective
		 * 
		 */
		GAUGE,

		/**
		 * Events from tracking perspective, for example request/response coming/going
		 * in/out to/from service or
		 */
		TRACK,

		/**
		 * Application Performance logs
		 * 
		 */
		METER

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

	abstract public void clean();
}
