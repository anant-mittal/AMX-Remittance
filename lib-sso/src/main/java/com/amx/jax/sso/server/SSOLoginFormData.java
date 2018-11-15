package com.amx.jax.sso.server;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.sso.SSOConstants.SSOAuthStep;
import com.amx.jax.swagger.ApiMockModelProperty;

public class SSOLoginFormData {

	SSOAuthStep step = null;

	@ApiMockModelProperty(example = "235474")
	String ecnumber = null;
	
	@ApiMockModelProperty(example = "282102202584")
	String identity = null;
	
	String motp = null;

	Map<String, Object> cardata = new HashMap<String, Object>();

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

	public SSOAuthStep getStep() {
		return step;
	}

	public void setStep(SSOAuthStep step) {
		this.step = step;
	}
}
