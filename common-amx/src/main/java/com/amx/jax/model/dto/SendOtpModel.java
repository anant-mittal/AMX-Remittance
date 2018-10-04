package com.amx.jax.model.dto;

import com.amx.jax.model.AbstractModel;

public class SendOtpModel extends AbstractModel {

	private static final long serialVersionUID = 9200894928151652022L;
	private String mOtpPrefix;
	private String eOtpPrefix;

	private String mOtp;
	private String eOtp;

	public String getmOtpPrefix() {
		return mOtpPrefix;
	}

	public void setmOtpPrefix(String mOtpPrefix) {
		this.mOtpPrefix = mOtpPrefix;
	}

	public String geteOtpPrefix() {
		return eOtpPrefix;
	}

	public void seteOtpPrefix(String eOtpPrefix) {
		this.eOtpPrefix = eOtpPrefix;
	}

	public String getmOtp() {
		return mOtp;
	}

	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}

	public String geteOtp() {
		return eOtp;
	}

	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	@Override
	public String toString() {
		return "M-OTP: " + mOtpPrefix + "-" + mOtp + " || E-OTP: " + eOtpPrefix + "-" + eOtp;
	}

}
