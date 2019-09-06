package com.amx.jax.ui.audit;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.AuthState.AuthFlow;
import com.amx.jax.model.AuthState.AuthStep;
import com.amx.utils.ArgUtil;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * The Class CAuthEvent.
 */
public class CAuthEvent extends AuditEvent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5461833493967079133L;

	/**
	 * The Enum Result.
	 */
	/**
	 * Instantiates a new c auth event.
	 *
	 * @param flow the flow
	 * @param step the step
	 */
	public CAuthEvent(AuthFlow flow, AuthStep step) {
		super(ArgUtil.ifNotEmpty(flow, AuthFlow.DEFAULT));
		this.step = step;
	}

	/**
	 * Instantiates a new c auth event.
	 *
	 * @param state     the state
	 * @param result    the result
	 * @param tranxTime the tranx time
	 */
	public CAuthEvent(AuthState state, Result result, long tranxTime) {
		this(state.flow, state.getnStep());
		this.step = state.getnStep();
		this.result = result;
		this.description = this.getDescription();
		this.tranxTime = tranxTime;
	}

	/**
	 * Instantiates a new c auth event.
	 *
	 * @param state     the state
	 * @param result    the result
	 * @param message   the message
	 * @param tranxTime the tranx time
	 */
	public CAuthEvent(AuthState state, Result result, Object message, long tranxTime) {
		this(state, result, tranxTime);
		this.message = ArgUtil.parseAsString(message);
	}

	/**
	 * Instantiates a new c auth event.
	 *
	 * @param state   the state
	 * @param result  the result
	 * @param message the message
	 */
	public CAuthEvent(AuthState state, Result result, Object message) {
		this(state, result, message, 0L);
	}

	/**
	 * Instantiates a new c auth event.
	 *
	 * @param state the state
	 */
	public CAuthEvent(AuthState state) {
		this(state, Result.DONE, 0L);
	}

	AuthStep step = null;

	String identiy = null;

	String userId = null;

	UserAgent agent = null; // Noncompliant; Address isn't serializable

	/**
	 * Gets the device.
	 *
	 * @return the device
	 */
	public UserAgent getAgent() {
		return agent;
	}

	/**
	 * Sets the device.
	 *
	 * @param device the device
	 */
	public void setAgent(UserAgent agent) {
		this.agent = agent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.logger.AuditEvent#getDescription()
	 */
	@Override
	public String getDescription() {
		return (this.type == null ? AuthFlow.DEFAULT : this.type) + ":" + this.step + ":" + this.result;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the identiy.
	 *
	 * @return the identiy
	 */
	public String getIdentiy() {
		return identiy;
	}

	/**
	 * Sets the identiy.
	 *
	 * @param identiy the new identiy
	 */
	public void setIdentiy(String identiy) {
		this.identiy = identiy;
	}

	/**
	 * Gets the step.
	 *
	 * @return the step
	 */
	public AuthStep getStep() {
		return step;
	}

	/**
	 * Sets the step.
	 *
	 * @param step the new step
	 */
	public void setStep(AuthStep step) {
		this.step = step;
	}

}
