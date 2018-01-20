package com.amx.jax.ui.model;

import java.util.List;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.SecurityQuestionModel;

public class UserUpdateData {

	private String mOtp = null;
	private String eOtp = null;

	public String geteOtp() {
		return eOtp;
	}

	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	public String getmOtp() {
		return mOtp;
	}

	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
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

}
