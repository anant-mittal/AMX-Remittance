package com.amx.jax.ui.model;

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

	public interface AuthRequestSecQues {

		public SecurityQuestionModel getAnswer();

		void setAnswer(SecurityQuestionModel answer);
	}

	@JsonDeserialize(as = AuthData.class)
	public interface AuthRequest extends AuthRequestIdentity, AuthRequestPassword, AuthRequestSecQues {

		@ApiModelProperty(example = "345678")
		String getmOtp();

		void setmOtp(String mOtp);

		@ApiModelProperty(example = "654321")
		String geteOtp();

		void seteOtp(String eOtp);
	}

	public interface AuthResponse {
		public AuthState getState();

		public void setState(AuthState state);

		@Deprecated
		public String getQuestion();

		@Deprecated
		public void setQuestion(String description);

		public String getmOtpPrefix();

		public void setmOtpPrefix(String getmOtpPrefix);

		public String geteOtpPrefix();

		public void seteOtpPrefix(String geteOtpPrefix);

		public QuestModelDTO getQues();

		public void setQues(QuestModelDTO questModelDTO);

	}
}
