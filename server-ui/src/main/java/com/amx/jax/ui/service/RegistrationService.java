package com.amx.jax.ui.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.AbstractException;
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
import com.amx.jax.ui.Constants;
import com.amx.jax.ui.model.LoginData;
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
	private UserService userService;

	@Autowired
	private PostManService postManService;

	public ResponseWrapper<LoginData> verifyId(String civilid) {

		/**
		 * Clearing old session before proceeding
		 */
		sessionService.clear();

		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());
		try {

			CivilIdOtpModel model = jaxClient.setDefaults().getUserclient().sendOtpForCivilId(civilid).getResult();
			// Check if response was successful
			if (model.getIsActiveCustomer()) {
				wrapper.setMessage(ResponseStatus.ALREADY_ACTIVE, ResponseMessage.USER_ALREADY_ACTIVE);
			} else {
				userSessionInfo.setOtpPrefix();
				model.setOtpPrefix(userSessionInfo.getOtpPrefix());

				userService.notifyResetOTP(model);
				wrapper.setMessage(ResponseStatus.OTP_SENT);
				// append info in response data
				wrapper.getData().setOtp(model.getmOtp());

				wrapper.getData().setOtpPrefix(userSessionInfo.getOtpPrefix());
			}
			userSessionInfo.setUserid(civilid);

		} catch (AbstractException e) {
			wrapper.setMessage(ResponseStatus.INVALID_ID, e);
		} catch (Exception e) {
			wrapper.setMessage(ResponseStatus.ERROR, e.getMessage());
		}

		return wrapper;
	}

	public ResponseWrapper<LoginData> loginWithOtp(String idnetity, String otp) {
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());

		if (userSessionInfo.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(ResponseStatus.ACTIVE_SESSION, ResponseMessage.USER_ALREADY_LOGGIN);
		} else {

			try {
				ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(idnetity,
						otp);
				CustomerModel model = response.getResult();
				// Check if otp is valid
				if (model != null && userSessionInfo.isValid(idnetity, otp)) {
					sessionService.authorize(model, true);
					wrapper.setMessage(ResponseStatus.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
				} else { // Use is cannot be validated
					wrapper.setMessage(ResponseStatus.VERIFY_FAILED, ResponseMessage.AUTH_FAILED);
				}
			} catch (AbstractException e) {
				wrapper.setMessage(ResponseStatus.VERIFY_FAILED, e);
			}
		}
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> getSecQues() {

		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		List<QuestModelDTO> questModel = jaxClient.setDefaults().getMetaClient().getSequrityQuestion().getResults();

		wrapper.getData().setSecQuesMeta(questModel);
		wrapper.getData().setSecQuesAns(userSessionInfo.getCustomerModel().getSecurityquestions());

		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updateSecQues(List<SecurityQuestionModel> securityquestions, String otp) {

		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		CustomerModel customerModel = jaxClient.setDefaults().getUserclient()
				.saveSecurityQuestions(securityquestions, otp).getResult();

		wrapper.getData().setSecQuesAns(customerModel.getSecurityquestions());
		wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "Question Answer Saved Scfuly");

		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updatePhising(String imageUrl, String caption, String otp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults().getUserclient().savePhishiingImage(caption, imageUrl, otp).getResult();

		wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "Phishing Image Updated");

		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> saveLoginIdAndPassword(String loginId, String password, String otp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		try {
			jaxClient.setDefaults().getUserclient().saveLoginIdAndPassword(loginId, password, otp).getResult();
			wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "LoginId and Password updated");

			String emailId = userSessionInfo.getCustomerModel().getEmail();

			if (emailId != null && !Constants.EMPTY.equals(emailId)) {
				PersonInfo personInfo = userSessionInfo.getCustomerModel().getPersoninfo();
				Email email = new Email();
				email.setSubject(Constants.REG_SUC);
				email.addTo(emailId);
				email.setTemplate(Templates.REG_SUC);
				email.setHtml(true);
				email.getModel().put(Constants.RESP_DATA_KEY, personInfo);

				try {
					postManService.sendEmail(email);
				} catch (Exception e) {
					LOG.error("Error while sending OTP Email to" + emailId, e);
				}
			}

		} catch (AlreadyExistsException e) {
			wrapper.setMessage(ResponseStatus.USER_UPDATE_FAILED, e);
		}

		return wrapper;
	}

}
