package com.amx.jax.ui.response;

import java.util.List;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.SecurityQuestionModel;

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

	private List<QuestModelDTO> secQuesMeta = null;

	public List<QuestModelDTO> getSecQuesMeta() {
		return secQuesMeta;
	}

	public void setSecQuesMeta(List<QuestModelDTO> secQuesMeta) {
		this.secQuesMeta = secQuesMeta;
	}

	private List<SecurityQuestionModel> secQuesAns = null;

	public List<SecurityQuestionModel> getSecQuesAns() {
		return secQuesAns;
	}

	public void setSecQuesAns(List<SecurityQuestionModel> secQuesAns) {
		this.secQuesAns = secQuesAns;
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

}
