package com.amx.jax.sso;

import java.util.HashMap;
import java.util.Map;

public class SSOLoginFormData {

	String action = null;
	String ecnumber = null;
	String motp = null;
	String identity = null;

	Map<String, Object> cardata = new HashMap<String, Object>();

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

	public Map<String, Object> getCardata() {
		return cardata;
	}

	public void setCardata(Map<String, Object> cardata) {
		this.cardata = cardata;
	}
}
