package com.amx.jax.ui.auth;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.ui.auth.AuthState.AuthFlow;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.amx.utils.ArgUtil;

public class CAuthEvent extends AuditEvent {

	public enum Result {
		PASS, FAIL;
	}

	public CAuthEvent(AuthFlow flow, AuthStep step) {
		super(flow);
		this.step = step;
	}

	public CAuthEvent(AuthState state, Result result, long tranxTime) {
		this(state.flow, state.getnStep());
		this.step = state.getnStep();
		this.result = result;
		this.description = this.getDescription();
		this.tranxTime = tranxTime;
	}

	public CAuthEvent(AuthState state, Result result, Object message, long tranxTime) {
		this(state, result, tranxTime);
		this.message = ArgUtil.parseAsString(message);
	}

	public CAuthEvent(AuthState state, Result result, Object message) {
		this(state, result, message, 0L);
	}

	public CAuthEvent(AuthState state) {
		this(state, Result.PASS, 0L);
	}

	AuthStep step = null;
	String identiy = null;
	String userId = null;
	Map<String, Object> device = new HashMap<String, Object>();

	public Map<String, Object> getDevice() {
		return device;
	}

	public void setDevice(Map<String, Object> device) {
		this.device = device;
	}

	@Override
	public String getDescription() {
		return (this.type == null ? AuthFlow.DEFAULT : this.type) + ":" + this.step + ":" + this.result;
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
