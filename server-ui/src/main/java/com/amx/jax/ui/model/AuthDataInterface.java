package com.amx.jax.ui.model;

import java.util.List;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The Class AuthDataInterface.
 */
public final class AuthDataInterface {

	/**
	 * The Interface AuthRequestIdentity.
	 */
	public interface AuthRequestIdentity {

		/**
		 * Gets the identity.
		 *
		 * @return the identity
		 */
		@ApiMockModelProperty(example = "289053104436")
		public String getIdentity();

		/**
		 * Sets the identity.
		 *
		 * @param identity the new identity
		 */
		public void setIdentity(String identity);

	}

	/**
	 * The Interface AuthRequestPassword.
	 */
	public interface AuthRequestPassword {

		/**
		 * Gets the password.
		 *
		 * @return the password
		 */
		@ApiMockModelProperty(example = "Amx@1234")
		public String getPassword();

		/**
		 * Sets the password.
		 *
		 * @param password the new password
		 */
		void setPassword(String password);
	}

	/**
	 * The Interface AuthRequestSecAns.
	 */
	public interface AuthRequestSecAns {

		/**
		 * Gets the answer.
		 *
		 * @return the answer
		 */
		public SecurityQuestionModel getAnswer();

		/**
		 * Sets the answer.
		 *
		 * @param answer the new answer
		 */
		void setAnswer(SecurityQuestionModel answer);
	}

	/**
	 * The Interface AuthRequestOTP.
	 */
	@JsonDeserialize(as = AuthData.class)
	public interface AuthRequestOTP {

		/**
		 * Gets the m otp.
		 *
		 * @return the m otp
		 */
		@Deprecated
		@ApiMockModelProperty(example = "345678")
		String getmOtp();

		/**
		 * Sets the m otp.
		 *
		 * @param mOtp the new m otp
		 */
		@Deprecated
		void setmOtp(String mOtp);

		/**
		 * Gets the e otp.
		 *
		 * @return the e otp
		 */
		@Deprecated
		@ApiMockModelProperty(example = "654321")
		String geteOtp();

		/**
		 * Sets the e otp.
		 *
		 * @param eOtp the new e otp
		 */
		@Deprecated
		void seteOtp(String eOtp);
	}

	/**
	 * The Interface AuthResponseOTPprefix.
	 */
	public interface AuthResponseOTPprefix {

		/**
		 * Gets the m otp prefix.
		 *
		 * @return the m otp prefix
		 */
		public String getmOtpPrefix();

		/**
		 * Sets the m otp prefix.
		 *
		 * @param getmOtpPrefix the new m otp prefix
		 */
		public void setmOtpPrefix(String getmOtpPrefix);

		/**
		 * Gets the e otp prefix.
		 *
		 * @return the e otp prefix
		 */
		public String geteOtpPrefix();

		/**
		 * Sets the e otp prefix.
		 *
		 * @param geteOtpPrefix the new e otp prefix
		 */
		public void seteOtpPrefix(String geteOtpPrefix);
	}

	@JsonDeserialize(as = AuthData.class)
	public interface AuthRequestFingerprint {

		public void setDeviceToken(String deviceToken);

		public String getDeviceToken();

		public String getLockId();

		public void setLockId(String lockId);

	}

	/**
	 * The Interface AuthRequest.
	 */
	@JsonDeserialize(as = AuthData.class)
	public interface AuthRequest extends AuthRequestIdentity, AuthRequestPassword, AuthRequestSecAns, AuthRequestOTP,
			AuthRequestFingerprint {

	}

	/**
	 * The Interface AuthResponse.
	 */
	public interface AuthResponse extends AuthResponseOTPprefix {

		/**
		 * Gets the state.
		 *
		 * @return the state
		 */
		public AuthState getState();

		/**
		 * Sets the state.
		 *
		 * @param state the new state
		 */
		public void setState(AuthState state);

		/**
		 * Gets the question.
		 *
		 * @return the question
		 * @deprecated - keeping it only for backward compatibility
		 */
		@Deprecated
		public String getQuestion();

		/**
		 * Sets the question.
		 *
		 * @param description the new question
		 * @deprecated - keeping it only for backward compatibility
		 */
		@Deprecated
		public void setQuestion(String description);

		/**
		 * Gets the ques.
		 *
		 * @return the ques
		 */
		public QuestModelDTO getQues();

		/**
		 * Sets the ques.
		 *
		 * @param questModelDTO the new ques
		 */
		public void setQues(QuestModelDTO questModelDTO);

	}

	/**
	 * The Interface UserUpdateRequest.
	 */
	@JsonDeserialize(as = UserUpdateData.class)
	public interface UserUpdateRequest extends AuthRequestPassword, AuthRequestOTP {

		/**
		 * Gets the sec ques ans.
		 *
		 * @return the sec ques ans
		 */
		public List<SecurityQuestionModel> getSecQuesAns();

		/**
		 * Sets the sec ques ans.
		 *
		 * @param secQuesAns the new sec ques ans
		 */
		public void setSecQuesAns(List<SecurityQuestionModel> secQuesAns);

		/**
		 * Gets the email.
		 *
		 * @return the email
		 */
		@ApiMockModelProperty(example = "user@amx.com")
		public String getEmail();

		/**
		 * Sets the email.
		 *
		 * @param email the new email
		 */
		public void setEmail(String email);

		/**
		 * Gets the phone.
		 *
		 * @return the phone
		 */
		@ApiMockModelProperty(example = "9876543210")
		public String getPhone();

		/**
		 * Sets the phone.
		 *
		 * @param phone the new phone
		 */
		public void setPhone(String phone);

		/**
		 * Gets the image url.
		 *
		 * @return the image url
		 */
		public String getImageUrl();

		/**
		 * Sets the image url.
		 *
		 * @param imageUrl the new image url
		 */
		public void setImageUrl(String imageUrl);

		/**
		 * Gets the caption.
		 *
		 * @return the caption
		 */
		public String getCaption();

		/**
		 * Sets the caption.
		 *
		 * @param caption the new caption
		 */
		public void setCaption(String caption);
	}

	/**
	 * The Interface UserUpdateResponse.
	 */
	public interface UserUpdateResponse extends AuthResponseOTPprefix {

	}
}
