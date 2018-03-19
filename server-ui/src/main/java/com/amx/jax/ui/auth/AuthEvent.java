package com.amx.jax.ui.auth;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.ui.auth.AuthState.AuthFlow;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.bootloaderjs.ArgUtil;

public class AuthEvent extends AuditEvent {

	public enum Result {
		PASS, FAIL;
	}

	public AuthEvent(AuthFlow flow, AuthStep step) {
		super(flow);
		this.step = step;
	}

	public AuthEvent(AuthState state, Result result) {
		this(state.flow, state.getnStep());
		this.step = state.getnStep();
		this.result = result;
		this.authresult = state.flow + ":" + this.step + ":" + result;
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
	String authresult = null;
	Map<String, Object> device = new HashMap<String, Object>();

	public Map<String, Object> getDevice() {
		return device;
	}

	public void setDevice(Map<String, Object> device) {
		this.device = device;
	}

	public String getAuthresult() {
		return this.type + ":" + this.step + ":" + this.result;
	}

	public void setAuthresult(String authresult) {
		this.authresult = authresult;
	}

	Result result = Result.PASS;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

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
