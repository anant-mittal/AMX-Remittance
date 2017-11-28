package com.amx.jax.ui.response;

import com.amx.amxlib.model.CivilIdOtpModel;

public class VerifyIdData implements ResponseData {

	private Boolean valid = false;
	private Boolean otpsent = false;

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean getOtpsent() {
		return otpsent;
	}

	public void setOtpsent(Boolean otpsent) {
		this.otpsent = otpsent;
	}

	private CivilIdOtpModel otpdata;

	public CivilIdOtpModel getOtpdata() {
		return otpdata;
	}

	public void setOtpdata(CivilIdOtpModel otpdata) {
		this.otpdata = otpdata;
	}

}
