package com.amx.jax.ui.model;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.auth.AuthState.AuthFlow;
import com.amx.jax.ui.auth.AuthState.AuthStep;

public class AuthData extends AbstractModel {

	private static final long serialVersionUID = 3734088232108133496L;
	private String nounce = null;

	private String identity = null;

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	private String mOtp = null;
	private String eOtp = null;

	private String mOtpPrefix = null;
	private String eOtpPrefix = null;

	private String otp = null;
	private String otpPrefix = null;
	private String imageId = null;
	private String imageCaption = null;
	private String question = null;
	private QuestModelDTO ques = null;
	private SecurityQuestionModel answer = null;
	private AuthState state = null;

	public AuthState getState() {
		return state;
	}

	public void setState(AuthState state) {
		this.state = state;
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

	public String getOtpPrefix() {
		return otpPrefix;
	}

	public void setOtpPrefix(String otpPrefix) {
		this.otpPrefix = otpPrefix;
	}

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

	public QuestModelDTO getQues() {
		return ques;
	}

	public void setQues(QuestModelDTO ques) {
		this.ques = ques;
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
