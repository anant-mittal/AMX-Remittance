package com.amx.jax.logger.events;

import com.amx.jax.logger.AuditEvent;
import com.bootloaderjs.ArgUtil;

public class AuthEvent extends AuditEvent {

	public enum Type implements EventType {
		AUTH_DEFAULT, LOGIN_ATTEMPT, LOGIN_SUCCESS, LOGIN_FAIL;
	}

	public AuthEvent(Type type, Object step, boolean pass) {
		super();
		this.setType(type, step, pass);
	}

	private String result = null;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 
	 * @param type
	 *            Send Event type
	 * 
	 * @param step
	 *            Send Boolean or String as Step
	 * @param pass
	 *            true for PASS or false for FAIL
	 * 
	 */
	public void setType(Type type, Object step, boolean pass) {
		this.setType(type);
		this.result = ArgUtil.parseAsString(step) + (pass ? "PASS" : "FAIL");
	}

}
