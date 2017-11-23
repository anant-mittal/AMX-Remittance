package com.amx.ui.response;

import com.amx.amxlib.model.CivilIdOtpModel;

public class VerifyIdData implements ResponseData {

	private CivilIdOtpModel otpdata;

	public CivilIdOtpModel getOtpdata() {
		return otpdata;
	}

	public void setOtpdata(CivilIdOtpModel otpdata) {
		this.otpdata = otpdata;
	}

}
