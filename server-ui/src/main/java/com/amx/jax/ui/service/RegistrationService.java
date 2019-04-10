package com.amx.jax.ui.service;

import java.util.ArrayList;
import java.util.List;

import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constants.CommunicationChannel;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.AuthState.AuthStep;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.session.UserSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class RegistrationService.
 */
@Service
public class RegistrationService {

	/** The log. */
	private Logger LOG = Logger.getLogger(UserService.class);

	/** The user session info. */
	@Autowired
	private UserSession userSessionInfo;

	/** The session service. */
	@Autowired
	SessionService sessionService;

	/** The jax client. */
	@Autowired
	private JaxService jaxClient;

	/**
	 * Validate customer.
	 *
	 * @param identity
	 *            the identity
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthData> validateCustomer(String identity) {

		/**
		 * Clearing old session before proceeding
		 */
		sessionService.clear();
		sessionService.invalidate();

		sessionService.getGuestSession().setIdentity(identity);
		sessionService.getGuestSession().initFlow(AuthState.AuthFlow.ACTIVATION);

		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());

		CivilIdOtpModel model = jaxClient.setDefaults().getUserclient().initRegistration(identity, null).getResult();
		// Check if response was successful
		if (model.getIsActiveCustomer()) {
			wrapper.setMessage(OWAStatusStatusCodes.ALREADY_ACTIVE, ResponseMessage.USER_ALREADY_ACTIVE);
		} else {
			sessionService.getGuestSession().getState().setValidId(true);

			wrapper.getData().setmOtpPrefix((model.getmOtpPrefix()));
			wrapper.getData().seteOtpPrefix((model.geteOtpPrefix()));
			wrapper.setMessage(OWAStatusStatusCodes.OTP_SENT);

			sessionService.getGuestSession().endStep(AuthStep.IDVALID);
			wrapper.getData().setState(sessionService.getGuestSession().getState());
		}
		return wrapper;
	}


	public ResponseWrapper<AuthData> validateCustomerInit(String identity, CommunicationChannel communicationChannel) {

		/**
		 * Clearing old session before proceeding
		 */
		sessionService.clear();
		sessionService.invalidate();

		sessionService.getGuestSession().setIdentity(identity);
		sessionService.getGuestSession().initFlow(AuthState.AuthFlow.ACTIVATION);

		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());

		CivilIdOtpModel model = jaxClient.setDefaults().getUserclient().initRegistration(identity, communicationChannel).getResult();
		// Check if response was successful
		if (model.getIsActiveCustomer()) {
			wrapper.setMessage(OWAStatusStatusCodes.ALREADY_ACTIVE, ResponseMessage.USER_ALREADY_ACTIVE);
		} else {
			sessionService.getGuestSession().getState().setValidId(true);

			wrapper.getData().setmOtpPrefix((model.getmOtpPrefix()));
			wrapper.getData().seteOtpPrefix((model.geteOtpPrefix()));
			wrapper.setMessage(OWAStatusStatusCodes.OTP_SENT);

			sessionService.getGuestSession().endStep(AuthStep.IDVALID);
			wrapper.getData().setState(sessionService.getGuestSession().getState());
		}
		return wrapper;
	}

	/**
	 * Login with otp.
	 *
	 * @param idnetity
	 *            the idnetity
	 * @param mOtp
	 *            the m otp
	 * @return the response wrapper
	 */
	@Deprecated
	public ResponseWrapper<UserUpdateData> loginWithOtp(String idnetity, String mOtp) {
		sessionService.getGuestSession().initStep(AuthStep.MOTPVFY);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(idnetity, mOtp, null);
		CustomerModel model = response.getResult();
		sessionService.authorize(model, true);
		initActivation(wrapper);
		sessionService.getGuestSession().endStep(AuthStep.MOTPVFY);
		wrapper.setMessage(OWAStatusStatusCodes.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		return wrapper;
	}

	/**
	 * Validate customer.
	 *
	 * @param idnetity
	 *            the idnetity
	 * @param mOtp
	 *            the m otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthData> validateCustomer(String idnetity, String mOtp) {
		if (mOtp == null) {
			return validateCustomer(idnetity, null);
		}
		sessionService.getGuestSession().initStep(AuthStep.MOTPVFY);
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());
		ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(idnetity, mOtp, null);

		CustomerModel model = response.getResult();

		sessionService.getGuestSession().setCustomerModel(model);
		sessionService.authorize(model, false); // TODO:- validate this
		sessionService.getGuestSession().getState().setValidMotp(true);

		if (model.getEmail() != null) {
			sessionService.getGuestSession().getState().setPresentEmail(true);
		} else {
			ApiResponse<QuestModelDTO> response2 = jaxClient.setDefaults().getUserclient()
					.getDataVerificationQuestions();
			QuestModelDTO ques = response2.getResult();
			wrapper.getData().setQues(ques);
		}

		wrapper.setMessage(OWAStatusStatusCodes.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		sessionService.getGuestSession().endStep(AuthStep.MOTPVFY);
		wrapper.getData().setState(sessionService.getGuestSession().getState());

		return wrapper;
	}


	/**
	 * Validate customer.
	 *
	 * @param idnetity
	 *            the idnetity
	 * @param otp
	 *            the otp: can be mobile/email/whatsapp
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthData> validateCustomer(String idnetity, String otp, CommunicationChannel communicationChannel) {
		if (otp == null) {
			return validateCustomer(idnetity, null);
		}
		sessionService.getGuestSession().initStep(AuthStep.MOTPVFY);
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());
		String mOtp = communicationChannel == CommunicationChannel.MOBILE ? otp : null;
		String eOtp = communicationChannel == CommunicationChannel.EMAIL ? otp : null;
		String wOtp = communicationChannel == CommunicationChannel.WHATSAPP ? otp : null;
		ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(idnetity, mOtp, eOtp, wOtp);

		CustomerModel model = response.getResult();

		sessionService.getGuestSession().setCustomerModel(model);
		sessionService.authorize(model, false); // TODO:- validate this
		sessionService.getGuestSession().getState().setValidMotp(true);

		if (model.getEmail() != null) {
			sessionService.getGuestSession().getState().setPresentEmail(true);
		} else {
			ApiResponse<QuestModelDTO> response2 = jaxClient.setDefaults().getUserclient()
					.getDataVerificationQuestions();
			QuestModelDTO ques = response2.getResult();
			wrapper.getData().setQues(ques);
		}

		wrapper.setMessage(OWAStatusStatusCodes.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		sessionService.getGuestSession().endStep(AuthStep.MOTPVFY);
		wrapper.getData().setState(sessionService.getGuestSession().getState());

		return wrapper;
	}

	/**
	 * Validate customer.
	 *
	 * @param idnetity
	 *            the idnetity
	 * @param mOtp
	 *            the m otp
	 * @param answer
	 *            the answer
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthData> validateCustomer(String idnetity, String mOtp, SecurityQuestionModel answer) {
		if (answer == null) {
			return validateCustomer(idnetity, mOtp);
		}
		sessionService.getGuestSession().initStep(AuthStep.DATA_VERIFY);
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());
		List<SecurityQuestionModel> answers = new ArrayList<SecurityQuestionModel>();
		answers.add(answer);
		CustomerModel response = jaxClient.setDefaults().getUserclient().validateDataVerificationQuestions(answers)
				.getResult();
		// update Session/State
		sessionService.getGuestSession().getState().setValidDataVer(true);
		wrapper.setMessage(OWAStatusStatusCodes.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		sessionService.getGuestSession().endStep(AuthStep.DATA_VERIFY);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * Inits the activation.
	 *
	 * @param wrapper
	 *            the wrapper
	 */
	private void initActivation(ResponseWrapper<UserUpdateData> wrapper) {
		List<QuestModelDTO> questModel = jaxClient.setDefaults().getMetaClient().getSequrityQuestion().getResults();
		wrapper.getData().setSecQuesMeta(questModel);
		if (sessionService.getGuestSession().getCustomerModel() != null) {
			wrapper.getData().setSecQuesAns(sessionService.getGuestSession().getCustomerModel().getSecurityquestions());
		} else if (userSessionInfo.getCustomerModel() != null) {
			wrapper.getData().setSecQuesAns(userSessionInfo.getCustomerModel().getSecurityquestions());
		}
	}

	/**
	 * Gets the sec ques.
	 *
	 * @param validate
	 *            the validate
	 * @return the sec ques
	 */
	public ResponseWrapper<UserUpdateData> getSecQues(boolean validate) {
		if (validate) {
			sessionService.getGuestSession().initStep(AuthStep.SECQ_SET);
		}
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		initActivation(wrapper);
		return wrapper;
	}

	/**
	 * Update sec ques.
	 *
	 * @param securityquestions
	 *            the securityquestions
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateData> updateSecQues(List<SecurityQuestionModel> securityquestions, String mOtp,
			String eOtp) {
		sessionService.getGuestSession().initStep(AuthStep.SECQ_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		CustomerModel customerModel = jaxClient.setDefaults().getUserclient()
				.saveSecurityQuestions(securityquestions, mOtp, eOtp).getResult();

		wrapper.getData().setSecQuesAns(customerModel.getSecurityquestions());
		wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_SUCCESS, "Question Answer Saved Scfuly");
		sessionService.getGuestSession().endStep(AuthStep.SECQ_SET);
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
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateData> updatePhising(String imageUrl, String caption, String mOtp, String eOtp) {
		sessionService.getGuestSession().initStep(AuthStep.CAPTION_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults().getUserclient().savePhishiingImage(caption, imageUrl, mOtp, eOtp).getResult();

		wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_SUCCESS, "Phishing Image Updated");
		sessionService.getGuestSession().endStep(AuthStep.CAPTION_SET);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * Sets the credentials.
	 *
	 * @param loginId
	 *            the login id
	 * @param password
	 *            the password
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @param email
	 *            the email
	 * @param doLogin
	 *            the do login
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateData> setCredentials(String loginId, String password, String mOtp, String eOtp,
			String email, boolean doLogin) {
		sessionService.getGuestSession().initStep(AuthStep.CREDS_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults().getUserclient().saveCredentials(loginId, password, mOtp, eOtp, email).getResult();

		if (doLogin) {
			sessionService.authorize(sessionService.getGuestSession().getCustomerModel(), true);
			jaxClient.getUserclient().customerLoggedIn(sessionService.getAppDevice().getUserDevice());
		}

		wrapper.setMessage(OWAStatusStatusCodes.USER_UPDATE_SUCCESS, "LoginId and Password updated");
		sessionService.getGuestSession().endStep(AuthStep.CREDS_SET);
		wrapper.getData().setState(sessionService.getGuestSession().getState());

		return wrapper;
	}

}
