package com.amx.jax.ui.service;

import java.util.List;

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
import com.amx.jax.ui.response.LoginData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.UserUpdateData;

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

	public ResponseWrapper<UserUpdateData> verifyId(String civilid) {

		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		jaxClient.setDefaults();
		CivilIdOtpModel model;
		try {
			model = userclient.sendOtpForCivilId(civilid).getResult();
			// Check if response was successful
			if (model.getIsActiveCustomer()) {
				wrapper.setMessage(EnumUtil.StatusCode.ALREADY_ACTIVE, "msg.usr.actv.alrdy");
			} else {
				wrapper.setMessage(EnumUtil.StatusCode.OTP_SENT, "msg.rsp.tp.sent");
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

	public ResponseWrapper<LoginData> loginWithOtp(String idnetity, String otp) {
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());

		if (userSessionInfo.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
		} else {

			try {
				ApiResponse<CustomerModel> response = jaxClient.setDefaults().getUserclient().validateOtp(idnetity,
						otp);
				CustomerModel model = response.getResult();
				// Check if otp is valid
				if (model != null && userSessionInfo.isValid(idnetity, otp)) {
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

	public ResponseWrapper<UserUpdateData> getSecQues() {

		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		List<QuestModelDTO> questModel = metaClient.getSequrityQuestion(JaxService.DEFAULT_LANGUAGE_ID).getResults();

		wrapper.getData().setSecQuesMeta(questModel);
		wrapper.getData().setSecQuesAns(userSessionInfo.getCustomerModel().getSecurityquestions());

		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updateSecQues(List<SecurityQuestionModel> securityquestions) {

		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults();

		CustomerModel customerModel = userclient.saveSecurityQuestions(securityquestions).getResult();

		wrapper.getData().setSecQuesAns(customerModel.getSecurityquestions());
		wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_SUCCESS, "Question Answer Saved Scfuly");

		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updatePhising(String imageUrl, String caption) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		jaxClient.setDefaults().getUserclient().savePhishiingImage(caption, imageUrl).getResult();

		wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_SUCCESS, "Phishing Image Updated");

		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> saveLoginIdAndPassword(String loginId, String password) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());

		try {
			jaxClient.setDefaults().getUserclient().saveLoginIdAndPassword(loginId, password).getResult();
			wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_SUCCESS, "LoginId and Password updated");
		} catch (AlreadyExistsException e) {
			wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_FAILED, "LoginId already exists");
		}

		return wrapper;
	}

}
