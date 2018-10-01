package com.amx.jax.model;

import javax.validation.constraints.Pattern;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.AppConstants;
import com.amx.jax.model.AuthDataInterface.AuthRequest;
import com.amx.jax.model.AuthDataInterface.AuthResponse;

/**
 * The Class AuthData.
 */
public class AuthData extends AbstractModel implements AuthResponse, AuthRequest {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3734088232108133496L;

	/** The nounce. */
	private String nounce = null;

	/** The identity. */
	@Pattern(regexp = AppConstants.Validator.IDENTITY)
	private String identity = null;

	/** The password. */
	private String password = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthRequestPassword#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
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
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthRequestIdentity#getIdentity()
	 */
	@Override
	public String getIdentity() {
		return identity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthRequestIdentity#setIdentity(java.
	 * lang.String)
	 */
	@Override
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/** The m otp. */
	@Pattern(regexp = AppConstants.Validator.OTP)
	private String mOtp = null;

	/** The e otp. */
	@Pattern(regexp = AppConstants.Validator.OTP)
	private String eOtp = null;

	/** The m otp prefix. */
	private String mOtpPrefix = null;

	/** The e otp prefix. */
	private String eOtpPrefix = null;

	/** The otp. */
	@Pattern(regexp = AppConstants.Validator.OTP)
	private String otp = null;

	/** The otp prefix. */
	private String otpPrefix = null;

	/** The image id. */
	private String imageId = null;

	/** The image caption. */
	private String imageCaption = null;

	/** The question. */
	private String question = null;

	/** The ques. */
	private QuestModelDTO ques = null;

	/** The answer. */
	private SecurityQuestionModel answer = null;

	/** The state. */
	private AuthState state = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthResponse#getState()
	 */
	@Override
	public AuthState getState() {
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponse#setState(com.amx.jax.ui.
	 * auth.AuthState)
	 */
	@Override
	public void setState(AuthState state) {
		this.state = state;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthRequestOTP#geteOtp()
	 */
	@Override
	public String geteOtp() {
		return eOtp;
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
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix#getmOtpPrefix()
	 */
	@Override
	public String getmOtpPrefix() {
		return mOtpPrefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix#setmOtpPrefix(
	 * java.lang.String)
	 */
	@Override
	public void setmOtpPrefix(String mOtpPrefix) {
		this.mOtpPrefix = mOtpPrefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix#geteOtpPrefix()
	 */
	@Override
	public String geteOtpPrefix() {
		return eOtpPrefix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix#seteOtpPrefix(
	 * java.lang.String)
	 */
	@Override
	public void seteOtpPrefix(String eOtpPrefix) {
		this.eOtpPrefix = eOtpPrefix;
	}

	/**
	 * Gets the otp prefix.
	 *
	 * @return the otp prefix
	 */
	public String getOtpPrefix() {
		return otpPrefix;
	}

	/**
	 * Sets the otp prefix.
	 *
	 * @param otpPrefix
	 *            the new otp prefix
	 */
	public void setOtpPrefix(String otpPrefix) {
		this.otpPrefix = otpPrefix;
	}

	/**
	 * Gets the otp.
	 *
	 * @return the otp
	 */
	public String getOtp() {
		return otp;
	}

	/**
	 * Sets the otp.
	 *
	 * @param otp
	 *            the new otp
	 */
	public void setOtp(String otp) {
		this.otp = otp;
	}

	/**
	 * Gets the image id.
	 *
	 * @return the image id
	 */
	public String getImageId() {
		return imageId;
	}

	/**
	 * Sets the image id.
	 *
	 * @param imageId
	 *            the new image id
	 */
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	/**
	 * Gets the image caption.
	 *
	 * @return the image caption
	 */
	public String getImageCaption() {
		return imageCaption;
	}

	/**
	 * Sets the image caption.
	 *
	 * @param imageCaption
	 *            the new image caption
	 */
	public void setImageCaption(String imageCaption) {
		this.imageCaption = imageCaption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthResponse#getQues()
	 */
	@Override
	public QuestModelDTO getQues() {
		return ques;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponse#setQues(com.amx.amxlib.
	 * meta.model.QuestModelDTO)
	 */
	@Override
	public void setQues(QuestModelDTO ques) {
		this.ques = ques;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthResponse#getQuestion()
	 */
	@Override
	public String getQuestion() {
		return question;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthResponse#setQuestion(java.lang.
	 * String)
	 */
	@Override
	public void setQuestion(String question) {
		this.question = question;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.ui.model.AuthDataInterface.AuthRequestSecAns#getAnswer()
	 */
	@Override
	public SecurityQuestionModel getAnswer() {
		return answer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.model.AuthDataInterface.AuthRequestSecAns#setAnswer(com.amx.
	 * amxlib.model.SecurityQuestionModel)
	 */
	@Override
	public void setAnswer(SecurityQuestionModel answer) {
		this.answer = answer;
	}

	/**
	 * Gets the nounce.
	 *
	 * @return the nounce
	 */
	public String getNounce() {
		return nounce;
	}

	/**
	 * Sets the nounce.
	 *
	 * @param nounce
	 *            the new nounce
	 */
	public void setNounce(String nounce) {
		this.nounce = nounce;
	}

}
