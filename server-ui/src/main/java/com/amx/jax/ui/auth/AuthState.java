package com.amx.jax.ui.auth;

import java.io.Serializable;

import com.amx.utils.EnumType;

public class AuthState implements Serializable {

	private static final long serialVersionUID = 6704113792186284121L;

	public static enum AuthFlow implements EnumType {
		DEFAULT, LOGIN, ACTIVATION, REGISTRATION, RESET_PASS, LOGOUT
	}

	public static enum AuthStep {
		USERPASS, SECQUES, IDVALID, DOTPVFY,
		// Activation
		MOTPVFY, DATA_VERIFY, SECQ_SET, CAPTION_SET, CREDS_SET,
		// Registration
		SAVE_HOME,
		// Logout
		MISSING, UNAUTH, LOCKED,
		// DONE
		COMPLETED
	}

	public AuthFlow flow = AuthFlow.DEFAULT;
	public AuthStep cStep = null;
	public AuthStep nStep = null;

	public boolean validId = false;
	public boolean validPswd = false;
	public boolean validSecQues = false;
	public boolean validMotp = false;
	public boolean validEotp = false;
	public boolean validDataVer = false;
	public boolean presentEmail = false;
	public long timestamp = 0L;

	public boolean isPresentEmail() {
		return presentEmail;
	}

	public void setPresentEmail(boolean presentEmail) {
		this.presentEmail = presentEmail;
	}

	public boolean isValidId() {
		return validId;
	}

	public void setValidId(boolean validId) {
		this.validId = validId;
	}

	public boolean isValidSecQues() {
		return validSecQues;
	}

	public void setValidSecQues(boolean validSecQues) {
		this.validSecQues = validSecQues;
	}

	public boolean isValidMotp() {
		return validMotp;
	}

	public void setValidMotp(boolean validMotp) {
		this.validMotp = validMotp;
	}

	public boolean isValidEotp() {
		return validEotp;
	}

	public void setValidEotp(boolean validEotp) {
		this.validEotp = validEotp;
	}

	public boolean isValidDataVer() {
		return validDataVer;
	}

	public void setValidDataVer(boolean validDataVer) {
		this.validDataVer = validDataVer;
	}

	public boolean isValidPswd() {
		return validPswd;
	}

	public void setValidPswd(boolean validPswd) {
		this.validPswd = validPswd;
	}

	public boolean isFlow(AuthFlow flow) {
		return this.flow == flow;
	}

	public boolean isStep(AuthStep authStep) {
		return this.cStep == authStep;
	}

	public AuthFlow getFlow() {
		return flow;
	}

	public void setFlow(AuthFlow flow) {
		this.flow = flow;
	}

	public AuthStep getcStep() {
		return cStep;
	}

	public void setcStep(AuthStep cStep) {
		this.cStep = cStep;
	}

	public AuthStep getnStep() {
		return nStep;
	}

	public void setnStep(AuthStep nStep) {
		this.nStep = nStep;
	}

}
