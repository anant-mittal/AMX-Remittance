package com.amx.jax.ui.model;

import java.util.List;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.auth.AuthState;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModelProperty;

public final class AuthDataInterface {

	public interface AuthRequestIdentity {

		@ApiModelProperty(example = "289053104436")
		public String getIdentity();

		void setIdentity(String identity);
	}

	public interface AuthRequestPassword {
		@ApiModelProperty(example = "Amx@1234")
		public String getPassword();

		void setPassword(String password);
	}

	public interface AuthRequestSecAns {

		public SecurityQuestionModel getAnswer();

		void setAnswer(SecurityQuestionModel answer);
	}

	@JsonDeserialize(as = AuthData.class)
	public interface AuthRequestOTP {

		@ApiModelProperty(example = "345678")
		String getmOtp();

		void setmOtp(String mOtp);

		@ApiModelProperty(example = "654321")
		String geteOtp();

		void seteOtp(String eOtp);
	}

	public interface AuthResponseOTPprefix {

		public String getmOtpPrefix();

		public void setmOtpPrefix(String getmOtpPrefix);

		public String geteOtpPrefix();

		public void seteOtpPrefix(String geteOtpPrefix);
	}

	@JsonDeserialize(as = AuthData.class)
	public interface AuthRequest extends AuthRequestIdentity, AuthRequestPassword, AuthRequestSecAns, AuthRequestOTP {

	}

	public interface AuthResponse extends AuthResponseOTPprefix {
		public AuthState getState();

		public void setState(AuthState state);

		/**
		 * @deprecated - keeping it only for backward compatibility
		 * @param description
		 */
		@Deprecated
		public String getQuestion();

		/**
		 * @deprecated - keeping it only for backward compatibility
		 * @param description
		 */
		@Deprecated
		public void setQuestion(String description);

		public QuestModelDTO getQues();

		public void setQues(QuestModelDTO questModelDTO);

	}

	@JsonDeserialize(as = UserUpdateData.class)
	public interface UserUpdateRequest extends AuthRequestPassword, AuthRequestOTP {

		public List<SecurityQuestionModel> getSecQuesAns();

		public void setSecQuesAns(List<SecurityQuestionModel> secQuesAns);

		@ApiModelProperty(example = "user@amx.com")
		public String getEmail();

		public void setEmail(String email);

		@ApiModelProperty(example = "9876543210")
		public String getPhone();

		public void setPhone(String phone);

		public String getImageUrl();

		public void setImageUrl(String imageUrl);

		public String getCaption();

		public void setCaption(String caption);
	}

	public interface UserUpdateResponse extends AuthResponseOTPprefix {

	}
}
