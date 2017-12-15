package com.amx.jax.ui.model;

import com.amx.amxlib.model.SecurityQuestionModel;

public class LoginData  {

	private String nounce = null;
	private String otp = null;
	private String imageId = null;
	private String imageCaption = null;
	private String question = null;
	private SecurityQuestionModel answer = null;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImageCaption() {
		return imageCaption;
	}

	public void setImageCaption(String imageCaption) {
		this.imageCaption = imageCaption;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public SecurityQuestionModel getAnswer() {
		return answer;
	}

	public void setAnswer(SecurityQuestionModel answer) {
		this.answer = answer;
	}

	public String getNounce() {
		return nounce;
	}

	public void setNounce(String nounce) {
		this.nounce = nounce;
	}

}