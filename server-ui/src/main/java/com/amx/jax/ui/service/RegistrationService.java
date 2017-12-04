package com.amx.jax.ui.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.AlreadyExistsException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.UserClient;
import com.amx.jax.ui.EnumUtil;
import com.amx.jax.ui.EnumUtil.StatusCode;
import com.amx.jax.ui.model.UserSession;
import com.amx.jax.ui.response.RegistrationdData;
import com.amx.jax.ui.response.ResponseWrapper;

@Service
public class RegistrationService {

	@Autowired
	private UserClient userclient;

	@Autowired
	private UserSession userSessionInfo;

	@Autowired
	SessionService sessionService;

	@Autowired
	private MetaClient metaClient;

	@Autowired
	private JaxService jaxClient;

	public ResponseWrapper<RegistrationdData> verifyId(String civilid) {

		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());
		jaxClient.setDefaults();
		CivilIdOtpModel model;
		try {
			model = userclient.sendOtpForCivilId(civilid).getResult();
			// Check if response was successful
			if (model.getIsActiveCustomer()) {
				wrapper.setMessage(EnumUtil.StatusCode.ALREADY_ACTIVE, "User is already registered for online");
			} else {
				wrapper.setMessage(EnumUtil.StatusCode.OTP_SENT, "OTP generated and sent");
				// append info in response data
				wrapper.getData().setOtp(model.getOtp());
				wrapper.getData().setOtpsent(true);
			}
			userSessionInfo.setUserid(civilid);
			userSessionInfo.setOtp(model.getOtp());
		} catch (InvalidInputException e) {
			wrapper.setMessage(EnumUtil.StatusCode.INVALID_ID, e.getMessage());
		}
		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> loginWithOtp(String civilid, String otp, HttpServletRequest request) {
		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		if (userSessionInfo.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
		} else {

			try {
				ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(civilid, otp);
				CustomerModel model = response.getResult();

				// Check if otp is valid
				if (model != null && userSessionInfo.isValid(civilid, otp)) {
					sessionService.authorize(model);
					wrapper.setMessage(StatusCode.VERIFY_SUCCESS, "Authentication successful");
				} else { // Use is cannot be validated
					wrapper.setMessage(StatusCode.VERIFY_FAILED, "Verification Failed");
				}
			} catch (IncorrectInputException e) {
				wrapper.setMessage(StatusCode.VERIFY_FAILED, e.getMessage());
			}
		}
		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> getSecQues() {

		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		List<QuestModelDTO> questModel = metaClient.getSequrityQuestion(JaxService.DEFAULT_LANGUAGE_ID).getResults();

		wrapper.getData().setSecQuesMeta(questModel);
		wrapper.getData().setSecQuesAns(userSessionInfo.getCustomerModel().getSecurityquestions());

		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> updateSecQues(List<SecurityQuestionModel> securityquestions) {

		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		jaxClient.setDefaults();

		CustomerModel customerModel = userclient.saveSecurityQuestions(securityquestions).getResult();

		wrapper.getData().setSecQuesAns(customerModel.getSecurityquestions());
		wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_SUCCESS, "Question Answer Saved Scfuly");

		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> updatePhising(String imageUrl, String caption) {
		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		jaxClient.setDefaults().getUserclient().savePhishiingImage(caption, imageUrl).getResult();

		wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_SUCCESS, "Phishing Image Updated");

		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> saveLoginIdAndPassword(String loginId, String password) {
		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		try {
			jaxClient.setDefaults().getUserclient().saveLoginIdAndPassword(loginId, password).getResult();
			wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_SUCCESS, "LoginId and Password updated");
		} catch (AlreadyExistsException e) {
			wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_FAILED, "LoginId already exists");
		}

		return wrapper;
	}

}
