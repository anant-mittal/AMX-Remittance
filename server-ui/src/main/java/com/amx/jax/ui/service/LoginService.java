package com.amx.jax.ui.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.ui.ResponseStatus;
import com.amx.jax.ui.model.UserSession;
import com.amx.jax.ui.response.LoginData;
import com.amx.jax.ui.response.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.bootloaderjs.ListManager;

@Service
public class LoginService {

	@Autowired
	private UserSession userSession;

	@Autowired
	private JaxService jaxService;

	@Autowired
	SessionService sessionService;

	public ResponseWrapper<LoginData> login(String identity, String password) {

		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());

		if (userSession.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(ResponseStatus.ALREADY_LOGGED_IN, "User already logged in");
		} else {
			CustomerModel customerModel;
			try {
				customerModel = jaxService.setDefaults().getUserclient().login(identity, password).getResult();

				sessionService.getGuestSession().setCustomerModel(customerModel);

				ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
						customerModel.getSecurityquestions());

				SecurityQuestionModel answer = listmgr.pickRandom();
				sessionService.getGuestSession().setQuesIndex(listmgr.getIndex());

				List<QuestModelDTO> questModel = jaxService.getMetaClient()
						.getSequrityQuestion(JaxService.DEFAULT_LANGUAGE_ID).getResults();

				for (QuestModelDTO questModelDTO : questModel) {
					System.out.println(questModelDTO.getQuestNumber() + "===" + answer.getQuestionSrNo());
					if (questModelDTO.getQuestNumber().equals(answer.getQuestionSrNo())) {
						wrapper.getData().setQuestion(questModelDTO.getDescription());
					}
				}

				wrapper.getData().setAnswer(answer);
				wrapper.getData().setImageId(customerModel.getImageUrl());
				wrapper.getData().setImageCaption(customerModel.getCaption());

				wrapper.setMessage(ResponseStatus.AUTH_OK, "Password is Correct");

			} catch (IncorrectInputException e) {
				wrapper.setMessage(ResponseStatus.AUTH_FAILED, e.getErrorMessage());
			} catch (CustomerValidationException e) {
				wrapper.setMessage(ResponseStatus.AUTH_FAILED, e.getErrorMessage());
			} catch (LimitExeededException e) {
				wrapper.setMessage(ResponseStatus.AUTH_BLOCKED_TEMP, e.getMessage());
			}
		}

		return wrapper;
	}

	public ResponseWrapper<LoginData> loginSecQues(SecurityQuestionModel guestanswer, HttpServletRequest request) {
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());
		if (userSession.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(ResponseStatus.ALREADY_LOGGED_IN, "User already logged in");
		} else {
			CustomerModel customerModel;
			try {
				List<SecurityQuestionModel> guestanswers = new ArrayList<SecurityQuestionModel>();
				guestanswers.add(guestanswer);
				customerModel = jaxService.setDefaults().getUserclient().validateSecurityQuestions(guestanswers)
						.getResult();

				sessionService.authorize(customerModel);

				wrapper.setMessage(ResponseStatus.VERIFY_SUCCESS, "Authing");
				wrapper.setMessage(ResponseStatus.AUTH_DONE, "User authenitcated successfully");

			} catch (IncorrectInputException e) {
				customerModel = sessionService.getGuestSession().getCustomerModel();

				ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
						customerModel.getSecurityquestions());

				SecurityQuestionModel answer = listmgr.pickNext(sessionService.getGuestSession().getQuesIndex());
				sessionService.getGuestSession().nextQuesIndex();

				List<QuestModelDTO> questModel = jaxService.getMetaClient()
						.getSequrityQuestion(JaxService.DEFAULT_LANGUAGE_ID).getResults();

				for (QuestModelDTO questModelDTO : questModel) {
					if (questModelDTO.getQuestNumber().equals(answer.getQuestionSrNo())) {
						wrapper.getData().setQuestion(questModelDTO.getDescription());
					}
				}
				wrapper.getData().setAnswer(answer);
				wrapper.setMessage(ResponseStatus.AUTH_FAILED, e.getMessage());
			} catch (CustomerValidationException e) {
				wrapper.setMessage(ResponseStatus.AUTH_FAILED, e.getErrorMessage());
			} catch (LimitExeededException e) {
				wrapper.setMessage(ResponseStatus.AUTH_BLOCKED_TEMP, e.getMessage());
			}

		}
		return wrapper;
	}

	public ResponseWrapper<LoginData> reset(String identity, String otp) {
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());
		if (otp == null) {
			try {
				CivilIdOtpModel model = jaxService.setDefaults().getUserclient().sendOtpForCivilId(identity)
						.getResult();
				// Check if response was successful
				// append info in response data
				wrapper.getData().setOtp(model.getOtp());
				userSession.setUserid(identity);
				userSession.setOtp(model.getOtp());

				wrapper.setMessage(ResponseStatus.OTP_SENT, "OTP generated and sent");

			} catch (InvalidInputException e) {
				wrapper.setMessage(ResponseStatus.INVALID_ID, e.getMessage());
			}
		} else {
			try {
				CustomerModel model = jaxService.setDefaults().getUserclient().validateOtp(identity, otp).getResult();
				// Check if otp is valid
				if (model != null) {
					sessionService.authorize(model);
					wrapper.setMessage(ResponseStatus.VERIFY_SUCCESS, "Authentication successful");
				} else { // Use is cannot be validated
					wrapper.setMessage(ResponseStatus.VERIFY_FAILED, "Verification Failed");
				}
			} catch (IncorrectInputException e) {
				wrapper.setMessage(ResponseStatus.VERIFY_FAILED, e.getMessage());
			}
		}
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updatepwd(String password) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		try {
			BooleanResponse model = jaxService.setDefaults().getUserclient().updatePassword(password).getResult();
			if (model.isSuccess()) {
				wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "Password Updated Succesfully");
			}
		} catch (IncorrectInputException | CustomerValidationException | LimitExeededException e) {
			wrapper.setMessage(ResponseStatus.USER_UPDATE_FAILED, e.getMessage());
		}

		return wrapper;
	}

}
