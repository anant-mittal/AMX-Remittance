package com.amx.jax.logger.events;

import com.amx.jax.logger.AuditEvent;
import com.amx.utils.EnumType;

public class CActivityEvent extends AuditEvent {

	public static enum Type implements EnumType {
		PASSOWRD_UPDATE, EMAIL_UPDATE, PHONE_UPDATE, SECQUES_UPDATE, PHISHIN_UPDATE;
	}

	public enum Result {
		PASS, FAIL;
	}

	CActivityEvent(Type type, String fromValue, String toValue) {
		super(type);
		this.fromValue = fromValue;
		this.toValue = toValue;
	}

	CActivityEvent(Type type, String fromValue, String toValue, Result result) {
		this(type, fromValue, toValue);
		this.result = result;
	}

	private String fromValue = null;
	private String toValue = null;
	private String actor = null;
	private Result result = Result.PASS;

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

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

}
