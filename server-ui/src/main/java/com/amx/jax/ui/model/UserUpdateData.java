package com.amx.jax.ui.model;

import java.util.List;

import javax.validation.constraints.Pattern;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.AppConstants;
import com.amx.jax.dict.ContactType;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateResponse;

/**
 * The Class UserUpdateData.
 */
public class UserUpdateData implements UserUpdateRequest, UserUpdateResponse {

	/** The m otp. */
	@Pattern(regexp = AppConstants.Validator.OTP)
	private String mOtp = null;

	/** The e otp. */
	@Pattern(regexp = AppConstants.Validator.OTP)
	private String eOtp = null;

	/** The password. */
	private String password = null;

	/** The email. */
	@Pattern(regexp = AppConstants.Validator.EMAIL)
	private String email = null;

	/** The phone. */
	@Pattern(regexp = AppConstants.Validator.PHONE)
	private String phone = null;

	/** The image url. */
	private String imageUrl = null;

	/** The caption. */
	private String caption = null;

	/** The m otp prefix. */
	private String mOtpPrefix = null;

	/** The e otp prefix. */
	private String eOtpPrefix = null;

	private String wOtpPrefix = null;

	private ContactType contactType = null;

	/** The sec ques meta. */
	private List<QuestModelDTO> secQuesMeta = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#setEmail(java.lang.
	 * String)
	 */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#getPhone()
	 */
	@Override
	public String getPhone() {
		return phone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#setPhone(java.lang.
	 * String)
	 */
	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/** The state. */
	private AuthState state = null;

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public AuthState getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(AuthState state) {
		this.state = state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix#getmOtpPrefix()
	 */
	public String getmOtpPrefix() {
		return this.mOtpPrefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix#setmOtpPrefix(
	 * java.lang.String)
	 */
	public void setmOtpPrefix(String mOtpPrefix) {
		this.mOtpPrefix = mOtpPrefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix#geteOtpPrefix()
	 */
	public String geteOtpPrefix() {
		return this.eOtpPrefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix#seteOtpPrefix(
	 * java.lang.String)
	 */
	public void seteOtpPrefix(String eOtpPrefix) {
		this.eOtpPrefix = eOtpPrefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthRequestOTP#geteOtp()
	 */
	@Override
	public String geteOtp() {
		return this.eOtp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthRequestOTP#seteOtp(java.lang.
	 * String)
	 */
	@Override
	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthRequestOTP#getmOtp()
	 */
	@Override
	public String getmOtp() {
		return mOtp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthRequestOTP#setmOtp(java.lang.
	 * String)
	 */
	@Override
	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}

	/**
	 * Gets the sec ques meta.
	 *
	 * @return the sec ques meta
	 */
	public List<QuestModelDTO> getSecQuesMeta() {
		return secQuesMeta;
	}

	/**
	 * Sets the sec ques meta.
	 *
	 * @param secQuesMeta the new sec ques meta
	 */
	public void setSecQuesMeta(List<QuestModelDTO> secQuesMeta) {
		this.secQuesMeta = secQuesMeta;
	}

	/** The sec ques ans. */
	private List<SecurityQuestionModel> secQuesAns = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#getSecQuesAns()
	 */
	@Override
	public List<SecurityQuestionModel> getSecQuesAns() {
		return secQuesAns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#setSecQuesAns(java.
	 * util.List)
	 */
	@Override
	public void setSecQuesAns(List<SecurityQuestionModel> secQuesAns) {
		this.secQuesAns = secQuesAns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthRequestPassword#getPassword()
	 */
	@Override
	public String getPassword() {
		return this.password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthRequestPassword#setPassword(java.
	 * lang.String)
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#getImageUrl()
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#setImageUrl(java.
	 * lang.String)
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#getCaption()
	 */
	public String getCaption() {
		return caption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest#setCaption(java.lang
	 * .String)
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	@Override
	public String getwOtpPrefix() {
		return wOtpPrefix;
	}

	@Override
	public void setwOtpPrefix(String wOtpPrefix) {
		this.wOtpPrefix = wOtpPrefix;
	}

}
