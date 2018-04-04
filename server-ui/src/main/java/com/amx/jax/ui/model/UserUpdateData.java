package com.amx.jax.ui.model;

import java.util.List;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest;

public class UserUpdateData implements UserUpdateRequest {

	private String mOtp = null;
	private String eOtp = null;
	private String password = null;
	private String email = null;
	private String phone = null;

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}

	private String mOtpPrefix = null;
	private String eOtpPrefix = null;

	private AuthState state = null;

	public AuthState getState() {
		return state;
	}

	public void setState(AuthState state) {
		this.state = state;
	}

	public String getmOtpPrefix() {
		return this.mOtpPrefix;
	}

	public void setmOtpPrefix(String mOtpPrefix) {
		this.mOtpPrefix = mOtpPrefix;
	}

	public String geteOtpPrefix() {
		return this.eOtpPrefix;
	}

	public void seteOtpPrefix(String eOtpPrefix) {
		this.eOtpPrefix = eOtpPrefix;
	}

	@Override
	public String geteOtp() {
		return this.eOtp;
	}

	@Override
	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	@Override
	public String getmOtp() {
		return mOtp;
	}

	@Override
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

	@Override
	public List<SecurityQuestionModel> getSecQuesAns() {
		return secQuesAns;
	}

	@Override
	public void setSecQuesAns(List<SecurityQuestionModel> secQuesAns) {
		this.secQuesAns = secQuesAns;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

}
