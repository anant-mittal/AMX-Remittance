package com.amx.jax.ui.auth;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.ui.auth.AuthState.AuthFlow;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.bootloaderjs.ArgUtil;

public class AuthEvent extends AuditEvent {

	public enum Type implements EventType {
		AUTH_OK, AUTH_FAIL;
	}

	public AuthEvent(Type type, AuthState state) {
		super(type);
		this.flow = state.flow;
		this.step = state.getnStep();
	}

	public AuthEvent(Type type, AuthState state, Object message) {
		this(type, state);
		this.message = ArgUtil.parseAsString(message);
	}

	public AuthEvent(AuthState state) {
		this(Type.AUTH_OK, state);
	}

	AuthFlow flow = null;
	AuthStep step = null;
	String identiy = null;
	String userId = null;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIdentiy() {
		return identiy;
	}

	public void setIdentiy(String identiy) {
		this.identiy = identiy;
	}

	public AuthFlow getFlow() {
		return flow;
	}

	public void setFlow(AuthFlow flow) {
		this.flow = flow;
	}

	public AuthStep getStep() {
		return step;
	}

	public void setStep(AuthStep step) {
		this.step = step;
	}

	private String result = null;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
