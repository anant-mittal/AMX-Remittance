package com.amx.jax.sso;

public class SSOLoginFormData {

	String action = null;
	String ecnumber = null;
	String motp = null;
	String identity = null;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEcnumber() {
		return ecnumber;
	}

	public void setEcnumber(String ecnumber) {
		this.ecnumber = ecnumber;
	}

	public String getMotp() {
		return motp;
	}

	public void setMotp(String motp) {
		this.motp = motp;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
}
