package com.amx.jax.model;

import java.io.Serializable;

import com.amx.jax.logger.AbstractEvent.EventMarker;
import com.amx.jax.logger.AbstractEvent.EventType;

/**
 * The Class AuthState.
 */
public class AuthState implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6704113792186284121L;

	/**
	 * The Enum AuthFlow.
	 */
	public static enum AuthFlow implements EventType {

		/** The default. */
		DEFAULT,
		/** The login. */
		LOGIN,
		/** The activation. */
		ACTIVATION,
		/** The registration. */
		REGISTRATION,
		/** The reset pass. */
		RESET_PASS,
		/** The logout. */
		LOGOUT;

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.amx.jax.logger.AbstractEvent.EventType#marker()
		 */
		@Override
		public EventMarker marker() {
			return null;
		}
	}

	/**
	 * The Enum AuthStep.
	 */
	public static enum AuthStep {

		DEVICEPASS,
		/** The userpass. */
		USERPASS,
		/** The secques. */
		SECQUES,
		/** The idvalid. */
		IDVALID,
		/** The dotpvfy. */
		DOTPVFY,

		/** The motpvfy. */
		// Activation
		MOTPVFY,
		/** The data verify. */
		DATA_VERIFY,
		/** The secq set. */
		SECQ_SET,
		/** The caption set. */
		CAPTION_SET,
		/** The creds set. */
		CREDS_SET,

		/** The save home. */
		// Registration
		SAVE_HOME,

		/** The missing. */
		// Logout
		MISSING,
		/** The unauth. */
		UNAUTH,
		/** The locked. */
		LOCKED,
		UNAUTH_DEVICE,

		/** The completed. */
		// DONE
		COMPLETED
	}

	/** The flow. */
	public AuthFlow flow = AuthFlow.DEFAULT;

	/** The c step. */
	public AuthStep cStep = null;

	/** The n step. */
	public AuthStep nStep = null;

	/** The valid id. */
	public boolean validId = false;

	/** The valid pswd. */
	public boolean validPswd = false;

	/** The valid sec ques. */
	public boolean validSecQues = false;

	/** The valid motp. */
	public boolean validMotp = false;

	/** The valid eotp. */
	public boolean validEotp = false;

	/** The valid data ver. */
	public boolean validDataVer = false;

	/** The present email. */
	public boolean presentEmail = false;

	/** The timestamp. */
	public long timestamp = 0L;

	/**
	 * Checks if is present email.
	 *
	 * @return true, if is present email
	 */
	public boolean isPresentEmail() {
		return presentEmail;
	}

	/**
	 * Sets the present email.
	 *
	 * @param presentEmail
	 *            the new present email
	 */
	public void setPresentEmail(boolean presentEmail) {
		this.presentEmail = presentEmail;
	}

	/**
	 * Checks if is valid id.
	 *
	 * @return true, if is valid id
	 */
	public boolean isValidId() {
		return validId;
	}

	/**
	 * Sets the valid id.
	 *
	 * @param validId
	 *            the new valid id
	 */
	public void setValidId(boolean validId) {
		this.validId = validId;
	}

	/**
	 * Checks if is valid sec ques.
	 *
	 * @return true, if is valid sec ques
	 */
	public boolean isValidSecQues() {
		return validSecQues;
	}

	/**
	 * Sets the valid sec ques.
	 *
	 * @param validSecQues
	 *            the new valid sec ques
	 */
	public void setValidSecQues(boolean validSecQues) {
		this.validSecQues = validSecQues;
	}

	/**
	 * Checks if is valid motp.
	 *
	 * @return true, if is valid motp
	 */
	public boolean isValidMotp() {
		return validMotp;
	}

	/**
	 * Sets the valid motp.
	 *
	 * @param validMotp
	 *            the new valid motp
	 */
	public void setValidMotp(boolean validMotp) {
		this.validMotp = validMotp;
	}

	/**
	 * Checks if is valid eotp.
	 *
	 * @return true, if is valid eotp
	 */
	public boolean isValidEotp() {
		return validEotp;
	}

	/**
	 * Sets the valid eotp.
	 *
	 * @param validEotp
	 *            the new valid eotp
	 */
	public void setValidEotp(boolean validEotp) {
		this.validEotp = validEotp;
	}

	/**
	 * Checks if is valid data ver.
	 *
	 * @return true, if is valid data ver
	 */
	public boolean isValidDataVer() {
		return validDataVer;
	}

	/**
	 * Sets the valid data ver.
	 *
	 * @param validDataVer
	 *            the new valid data ver
	 */
	public void setValidDataVer(boolean validDataVer) {
		this.validDataVer = validDataVer;
	}

	/**
	 * Checks if is valid pswd.
	 *
	 * @return true, if is valid pswd
	 */
	public boolean isValidPswd() {
		return validPswd;
	}

	/**
	 * Sets the valid pswd.
	 *
	 * @param validPswd
	 *            the new valid pswd
	 */
	public void setValidPswd(boolean validPswd) {
		this.validPswd = validPswd;
	}

	/**
	 * Checks if is flow.
	 *
	 * @param flow
	 *            the flow
	 * @return true, if is flow
	 */
	public boolean isFlow(AuthFlow flow) {
		return this.flow == flow;
	}

	/**
	 * Checks if is step.
	 *
	 * @param authStep
	 *            the auth step
	 * @return true, if is step
	 */
	public boolean isStep(AuthStep authStep) {
		return this.cStep == authStep;
	}

	/**
	 * Gets the flow.
	 *
	 * @return the flow
	 */
	public AuthFlow getFlow() {
		return flow;
	}

	/**
	 * Sets the flow.
	 *
	 * @param flow
	 *            the new flow
	 */
	public void setFlow(AuthFlow flow) {
		this.flow = flow;
	}

	/**
	 * Gets the c step.
	 *
	 * @return the c step
	 */
	public AuthStep getcStep() {
		return cStep;
	}

	/**
	 * Sets the c step.
	 *
	 * @param cStep
	 *            the new c step
	 */
	public void setcStep(AuthStep cStep) {
		this.cStep = cStep;
	}

	/**
	 * Gets the n step.
	 *
	 * @return the n step
	 */
	public AuthStep getnStep() {
		return nStep;
	}

	/**
	 * Sets the n step.
	 *
	 * @param nStep
	 *            the new n step
	 */
	public void setnStep(AuthStep nStep) {
		this.nStep = nStep;
	}

}
