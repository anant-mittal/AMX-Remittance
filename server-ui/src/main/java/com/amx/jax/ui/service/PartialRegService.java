package com.amx.jax.ui.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.CustomerCredential;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.AuthState.AuthStep;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;

/**
 * The Class PartialRegService.
 */
@Service
public class PartialRegService {

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/** The jax client. */
	@Autowired
	private JaxService jaxClient;

	// New Registration

	/**
	 * New user register init.
	 *
	 * @param personalDetail
	 *            the personal detail
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthData> newUserRegisterInit(CustomerPersonalDetail personalDetail) {

		/**
		 * Clearing old session before proceeding
		 */
		sessionService.clear();
		sessionService.getGuestSession().setIdentity(personalDetail.getIdentityInt());
		sessionService.getGuestSession().initFlow(AuthState.AuthFlow.REGISTRATION);
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());
		SendOtpModel model = jaxClient.setDefaults().getCustRegClient().sendOtp(personalDetail).getResult();
		sessionService.getGuestSession().getState().setValidId(true);
		wrapper.getData().setmOtpPrefix((model.getmOtpPrefix()));
		wrapper.getData().seteOtpPrefix((model.geteOtpPrefix()));
		wrapper.setMessage(WebResponseStatus.OTP_SENT);

		sessionService.getGuestSession().endStep(AuthStep.IDVALID);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * New user register validate.
	 *
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthData> newUserRegisterValidate(String mOtp, String eOtp) {
		sessionService.getGuestSession().initStep(AuthStep.DOTPVFY);
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());

		BooleanResponse resp = jaxClient.setDefaults().getCustRegClient().validateOtp(mOtp, eOtp).getResult();
		if (resp.isSuccess()) {
			// update Session/State
			sessionService.getGuestSession().getState().setValidMotp(true);
			sessionService.getGuestSession().getState().setValidEotp(true);
			wrapper.setMessage(WebResponseStatus.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
			sessionService.getGuestSession().endStep(AuthStep.DOTPVFY);
		} else {
			wrapper.setMessage(WebResponseStatus.VERIFY_FAILED);
		}

		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * Save home address.
	 *
	 * @param customerHomeAddress
	 *            the customer home address
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthData> saveHomeAddress(CustomerHomeAddress customerHomeAddress) {
		sessionService.getGuestSession().initStep(AuthStep.SAVE_HOME);
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());

		BooleanResponse resp = jaxClient.setDefaults().getCustRegClient().saveHomeAddress(customerHomeAddress)
				.getResult();
		if (resp.isSuccess()) {
			// update Session/State
			sessionService.getGuestSession().getState().setValidMotp(true);
			sessionService.getGuestSession().getState().setValidEotp(true);
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS);
			sessionService.getGuestSession().endStep(AuthStep.SAVE_HOME);
		} else {
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_FAILED);
		}

		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * New Registration does not require OTPs (already validated).
	 *
	 * @param securityquestions
	 *            the securityquestions
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateData> updateSecQues(List<SecurityQuestionModel> securityquestions) {

		sessionService.getGuestSession().initStep(AuthStep.SECQ_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		BooleanResponse boolResp = jaxClient.setDefaults().getCustRegClient().saveSecurityQuestions(securityquestions)
				.getResult();

		if (boolResp.isSuccess()) {
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS);
			sessionService.getGuestSession().endStep(AuthStep.SECQ_SET);
		}

		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * Update phising.
	 *
	 * @param imageUrl
	 *            the image url
	 * @param caption
	 *            the caption
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateData> updatePhising(String imageUrl, String caption) {
		sessionService.getGuestSession().initStep(AuthStep.CAPTION_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults().getCustRegClient().savePhishiingImage(caption, imageUrl).getResult();

		wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "Phishing Image Updated");
		sessionService.getGuestSession().endStep(AuthStep.CAPTION_SET);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * Sets the credentials.
	 *
	 * @param customerCredential
	 *            the customer credential
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateData> setCredentials(CustomerCredential customerCredential) {
		sessionService.getGuestSession().initStep(AuthStep.CREDS_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults().getCustRegClient().saveLoginDetail(customerCredential, Boolean.TRUE).getResult();

		wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "LoginId and Password updated");
		sessionService.getGuestSession().endStep(AuthStep.CREDS_SET);
		wrapper.getData().setState(sessionService.getGuestSession().getState());

		return wrapper;
	}
}
