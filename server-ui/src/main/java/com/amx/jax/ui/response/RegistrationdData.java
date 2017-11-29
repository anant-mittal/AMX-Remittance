package com.amx.jax.ui.response;

import java.util.List;

import com.amx.amxlib.meta.model.QuestModelDTO;

public class RegistrationdData implements ResponseData {

	private Boolean valid = false;

	private Boolean otpsent = false;
	private String otp = null;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	private List<QuestModelDTO> secQues = null;

	public List<QuestModelDTO> getSecQues() {
		return secQues;
	}

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

	public void setSecQues(List<QuestModelDTO> questModel) {
		this.secQues = questModel;
	}

}
