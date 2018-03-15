package com.amx.jax.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
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

	@Autowired
	private PostManService postManService;

	public ResponseWrapper<AuthData> verifyId(String civilid) {

		/**
		 * Clearing old session before proceeding
		 */
		sessionService.clear();
		sessionService.getGuestSession().getState().setFlow(AuthState.AuthFlow.ACTIVATION);

		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());

		CivilIdOtpModel model = jaxClient.setDefaults().getUserclient().initRegistration(civilid).getResult();
		// Check if response was successful
		if (model.getIsActiveCustomer()) {
			wrapper.setMessage(ResponseStatus.ALREADY_ACTIVE, ResponseMessage.USER_ALREADY_ACTIVE);
		} else {
			sessionService.getGuestSession().getState().setValidId(true);
			sessionService.getGuestSession().moveNextState();

			wrapper.getData().setmOtpPrefix((model.getmOtpPrefix()));
			wrapper.getData().seteOtpPrefix((model.geteOtpPrefix()));
			wrapper.setMessage(ResponseStatus.OTP_SENT);
			wrapper.getData().setState(sessionService.getGuestSession().getState());
		}
		userSessionInfo.setUserid(civilid);

		return wrapper;
	}

	@Deprecated
	public ResponseWrapper<UserUpdateData> loginWithOtp(String idnetity, String mOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(idnetity, mOtp, null);
		CustomerModel model = response.getResult();
		sessionService.authorize(model, true);
		initActivation(wrapper);
		wrapper.setMessage(ResponseStatus.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		return wrapper;
	}

	public ResponseWrapper<AuthData> validateCustomer(String idnetity, String mOtp) {
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());
		ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(idnetity, mOtp, null);
		CustomerModel model = response.getResult();
		if (model.getEmail() != null) {
			sessionService.authorize(model, false);
		} else {
			ApiResponse<QuestModelDTO> response2 = jaxClient.setDefaults().getUserclient()
					.getDataVerificationQuestions();
			QuestModelDTO ques = response2.getResult();

			// update Session/State
			sessionService.getGuestSession().getState().setValidMotp(true);
			sessionService.getGuestSession().moveNextState();

			// update Response
			wrapper.getData().setQues(ques);
			wrapper.getData().setState(sessionService.getGuestSession().getState());
		}
		wrapper.setMessage(ResponseStatus.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		return wrapper;
	}

	public ResponseWrapper<AuthData> validateCustomer(String idnetity, String mOtp, SecurityQuestionModel answer) {
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());
		List<SecurityQuestionModel> answers = new ArrayList<SecurityQuestionModel>();
		answers.add(answer);
		QuestModelDTO response = jaxClient.setDefaults().getUserclient().validateDataVerificationQuestions(answers)
				.getResult();

		// update Session/State
		sessionService.getGuestSession().getState().setValidDataVer(true);
		sessionService.getGuestSession().moveNextState();

		// update Response
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

		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		CustomerModel customerModel = jaxClient.setDefaults().getUserclient()
				.saveSecurityQuestions(securityquestions, mOtp, eOtp).getResult();

		wrapper.getData().setSecQuesAns(customerModel.getSecurityquestions());
		wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "Question Answer Saved Scfuly");

		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updatePhising(String imageUrl, String caption, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults().getUserclient().savePhishiingImage(caption, imageUrl, mOtp, eOtp).getResult();

		wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "Phishing Image Updated");

		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> saveLoginIdAndPassword(String loginId, String password, String mOtp,
			String eOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		try {
			jaxClient.setDefaults().getUserclient().saveLoginIdAndPassword(loginId, password, mOtp, eOtp).getResult();
			wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "LoginId and Password updated");

			String emailId = userSessionInfo.getCustomerModel().getEmail();

			if (emailId != null && !UIConstants.EMPTY.equals(emailId)) {
				PersonInfo personInfo = userSessionInfo.getCustomerModel().getPersoninfo();
				Email email = new Email();
				email.setSubject(UIConstants.REG_SUC);
				email.addTo(emailId);
				email.setTemplate(Templates.REG_SUC);
				email.setHtml(true);
				email.getModel().put(UIConstants.RESP_DATA_KEY, personInfo);

				try {
					postManService.sendEmail(email);
				} catch (Exception e) {
					LOG.error("Error while sending OTP Email to" + emailId, e);
				}
			}

		} catch (AlreadyExistsException e) {
			wrapper.setMessage(ResponseStatus.USER_UPDATE_FAILED, e);
			e.getStackTrace();
		}

		return wrapper;
	}

}
