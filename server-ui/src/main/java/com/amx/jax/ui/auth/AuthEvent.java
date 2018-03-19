package com.amx.jax.ui.auth;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.ui.auth.AuthState.AuthFlow;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.bootloaderjs.ArgUtil;

public class AuthEvent extends AuditEvent {

	public enum Result {
		PASS, FAIL, UNAUTH, MISSING;
	}

	public AuthEvent(AuthFlow flow, AuthStep step) {
		super(flow);
		this.step = step;
	}

	public AuthEvent(AuthState state, Result result) {
		this(state.flow,state.getnStep());
		this.step = state.getnStep();
	}

	public AuthEvent(AuthState state, Result result, Object message) {
		this(state, result);
		this.message = ArgUtil.parseAsString(message);
	}

	public AuthEvent(AuthState state) {
		this(state, Result.PASS);
	}

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

	public AuthStep getStep() {
		return step;
	}

	public void setStep(AuthStep step) {
		this.step = step;
	}

}
