package com.amx.jax.logger.events;

import java.math.BigDecimal;

import com.amx.jax.logger.AuditEvent;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

public class CActivityEvent extends AuditEvent {

	private static final long serialVersionUID = -3189696554945071766L;

	public static enum Type implements EventType {

		BENE_ADD, BENE_UPDATE, FC_UPDATE,

		APPLICATION_CREATED,

		DELETE, ENABLED, DISABLED,
		GEO_LOCATION;
		@Override
		public EventMarker marker() {
			return EventMarker.AUDIT;
		}
	}

	public CActivityEvent(Type type, Object target) {
		super(type);
		this.target = ArgUtil.parseAsString(target);
	}

	public CActivityEvent(Type type, BigDecimal targetId, Object target) {
		super(type);
		this.targetId = targetId;
		this.target = ArgUtil.parseAsString(target);
	}

	public CActivityEvent(Type type, BigDecimal targetId) {
		super(type);
		this.targetId = targetId;
	}

	public CActivityEvent(Type type) {
		super(type);
	}

	private String target = null;
	private BigDecimal targetId = null;
	private String field = null;
	private String fromValue = null;
	private String toValue = null;
	private String actor = null;
	private String customer = null;

	@Override
	public String getDescription() {
		return this.type + ":" + this.result;
	}

	public String getFromValue() {
		return fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	public String getToValue() {
		return toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public CActivityEvent field(Object field) {
		this.setField(ArgUtil.parseAsString(field, Constants.BLANK).toUpperCase());
		return this;
	}

	public CActivityEvent toggle(Object field) {
		this.setField(ArgUtil.parseAsString(field, Constants.BLANK).toUpperCase());
		return this;
	}

	public CActivityEvent from(Object fromValue) {
		this.setFromValue(ArgUtil.parseAsString(fromValue));
		return this;
	}

	public CActivityEvent to(Object toValue) {
		this.setFromValue(ArgUtil.parseAsString(toValue));
		return this;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public BigDecimal getTargetId() {
		return targetId;
	}

	public void setTargetId(BigDecimal targetId) {
		this.targetId = targetId;
	}

}
