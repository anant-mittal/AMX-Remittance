package com.amx.jax.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.session.UserSession;

@Service
public class RegistrationService {

	private Logger LOG = Logger.getLogger(UserService.class);

	@Autowired
	private UserSession userSessionInfo;

	@Autowired
	SessionService sessionService;

	@Autowired
	private JaxService jaxClient;

	public ResponseWrapper<AuthData> validateCustomer(String identity) {

		/**
		 * Clearing old session before proceeding
		 */
		sessionService.clear();
		sessionService.getGuestSession().setIdentity(identity);
		sessionService.getGuestSession().getState().setFlow(AuthState.AuthFlow.ACTIVATION);

		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());

		CivilIdOtpModel model = jaxClient.setDefaults().getUserclient().initRegistration(identity).getResult();
		// Check if response was successful
		if (model.getIsActiveCustomer()) {
			wrapper.setMessage(WebResponseStatus.ALREADY_ACTIVE, ResponseMessage.USER_ALREADY_ACTIVE);
		} else {
			sessionService.getGuestSession().getState().setValidId(true);

			wrapper.getData().setmOtpPrefix((model.getmOtpPrefix()));
			wrapper.getData().seteOtpPrefix((model.geteOtpPrefix()));
			wrapper.setMessage(WebResponseStatus.OTP_SENT);

			sessionService.getGuestSession().endStep(AuthStep.IDVALID);
			wrapper.getData().setState(sessionService.getGuestSession().getState());
		}
		userSessionInfo.setUserid(identity);

		return wrapper;
	}

	@Deprecated
	public ResponseWrapper<UserUpdateData> loginWithOtp(String idnetity, String mOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(idnetity, mOtp, null);
		CustomerModel model = response.getResult();
		sessionService.authorize(model, true);
		initActivation(wrapper);
		wrapper.setMessage(WebResponseStatus.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		return wrapper;
	}

	public ResponseWrapper<AuthData> validateCustomer(String idnetity, String mOtp) {
		if (mOtp == null) {
			return validateCustomer(idnetity);
		}
		sessionService.getGuestSession().initStep(AuthStep.MOTPVFY);
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());
		ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(idnetity, mOtp, null);

		CustomerModel model = response.getResult();

		sessionService.authorize(model, false);
		sessionService.getGuestSession().getState().setValidMotp(true);

		if (model.getEmail() != null) {
			sessionService.getGuestSession().getState().setPresentEmail(true);
		} else {
			ApiResponse<QuestModelDTO> response2 = jaxClient.setDefaults().getUserclient()
					.getDataVerificationQuestions();
			QuestModelDTO ques = response2.getResult();
			wrapper.getData().setQues(ques);
		}

		wrapper.setMessage(WebResponseStatus.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		sessionService.getGuestSession().endStep(AuthStep.MOTPVFY);
		wrapper.getData().setState(sessionService.getGuestSession().getState());

		return wrapper;
	}

	public ResponseWrapper<AuthData> validateCustomer(String idnetity, String mOtp, SecurityQuestionModel answer) {
		sessionService.getGuestSession().initStep(AuthStep.DATA_VERIFY);
		if (answer == null) {
			return validateCustomer(idnetity, mOtp);
		}
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());
		List<SecurityQuestionModel> answers = new ArrayList<SecurityQuestionModel>();
		answers.add(answer);
		jaxClient.setDefaults().getUserclient().validateDataVerificationQuestions(answers).getResult();

		// update Session/State
		sessionService.getGuestSession().getState().setValidDataVer(true);
		wrapper.setMessage(WebResponseStatus.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		sessionService.getGuestSession().endStep(AuthStep.DATA_VERIFY);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	private void initActivation(ResponseWrapper<UserUpdateData> wrapper) {
		List<QuestModelDTO> questModel = jaxClient.setDefaults().getMetaClient().getSequrityQuestion().getResults();
		wrapper.getData().setSecQuesMeta(questModel);
		wrapper.getData().setSecQuesAns(userSessionInfo.getCustomerModel().getSecurityquestions());
	}

	public ResponseWrapper<UserUpdateData> getSecQues() {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		initActivation(wrapper);
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updateSecQues(List<SecurityQuestionModel> securityquestions, String mOtp,
			String eOtp) {
		sessionService.getGuestSession().initStep(AuthStep.SECQ_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		CustomerModel customerModel = jaxClient.setDefaults().getUserclient()
				.saveSecurityQuestions(securityquestions, mOtp, eOtp).getResult();

		wrapper.getData().setSecQuesAns(customerModel.getSecurityquestions());
		wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "Question Answer Saved Scfuly");
		sessionService.getGuestSession().endStep(AuthStep.SECQ_SET);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updatePhising(String imageUrl, String caption, String mOtp, String eOtp) {
		sessionService.getGuestSession().initStep(AuthStep.CAPTION_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults().getUserclient().savePhishiingImage(caption, imageUrl, mOtp, eOtp).getResult();

		wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "Phishing Image Updated");
		sessionService.getGuestSession().endStep(AuthStep.CAPTION_SET);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> saveLoginIdAndPassword(String loginId, String password, String mOtp,
			String eOtp, String email) {
		sessionService.getGuestSession().initStep(AuthStep.CREDS_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults().getUserclient().saveLoginIdAndPassword(loginId, password, mOtp, eOtp, email)
				.getResult();
		wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "LoginId and Password updated");
		sessionService.getGuestSession().endStep(AuthStep.CREDS_SET);
		wrapper.getData().setState(sessionService.getGuestSession().getState());

		return wrapper;
	}

}
