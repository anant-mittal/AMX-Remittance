package com.amx.jax.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.amx.jax.ui.config.HttpUnauthorizedException;
import com.amx.jax.ui.model.LoginData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.session.GuestSession.AuthFlow;
import com.amx.jax.ui.session.GuestSession.AuthFlowStep;
import com.amx.jax.ui.session.UserSession;
import com.bootloaderjs.ListManager;

@Service
public class LoginService {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserSession userSession;

	@Autowired
	private JaxService jaxService;

	@Autowired
	private SessionService sessionService;

	private LoginData getRandomSecurityQuestion(CustomerModel customerModel) {
		LoginData loginData = new LoginData();
		ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
				customerModel.getSecurityquestions());

		SecurityQuestionModel answer = listmgr.pickRandom();
		sessionService.getGuestSession().setQuesIndex(listmgr.getIndex());

		List<QuestModelDTO> questModel = jaxService.getMetaClient().getSequrityQuestion().getResults();

		for (QuestModelDTO questModelDTO : questModel) {
			if (questModelDTO.getQuestNumber().equals(answer.getQuestionSrNo())) {
				loginData.setQuestion(questModelDTO.getDescription());
			}
		}
		loginData.setImageId(customerModel.getImageUrl());
		loginData.setImageCaption(customerModel.getCaption());
		loginData.setAnswer(answer);
		return loginData;
	}

	public ResponseWrapper<LoginData> login(String identity, String password) {

		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(null);
		sessionService.clear();
		sessionService.getGuestSession().setFlow(AuthFlow.LOGIN);
		CustomerModel customerModel;
		try {
			customerModel = jaxService.setDefaults().getUserclient().login(identity, password).getResult();
			if (customerModel == null) {
				wrapper.setMessage(ResponseStatus.AUTH_FAILED, ResponseMessage.AUTH_FAILED);
			} else {
				log.info("Login Started for user : customer id : {}", customerModel.getCustomerId());
				sessionService.getGuestSession().setCustomerModel(customerModel);
				sessionService.getGuestSession().setAuthStep(AuthFlowStep.USERPASS);
				wrapper.setData(getRandomSecurityQuestion(customerModel));
				wrapper.setMessage(ResponseStatus.AUTH_OK, "Password is Correct");
			}
		} catch (LimitExeededException e) {
			wrapper.setMessage(ResponseStatus.AUTH_BLOCKED_TEMP, e);
		}

		return wrapper;
	}

	public ResponseWrapper<LoginData> loginSecQues(SecurityQuestionModel guestanswer) {
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());
		CustomerModel customerModel;
		try {
			List<SecurityQuestionModel> guestanswers = new ArrayList<SecurityQuestionModel>();
			guestanswers.add(guestanswer);
			customerModel = jaxService.setDefaults().getUserclient().validateSecurityQuestions(guestanswers)
					.getResult();

			if (customerModel == null) {
				wrapper.setMessage(ResponseStatus.AUTH_FAILED, ResponseMessage.AUTH_FAILED);
			} else {
				sessionService.getGuestSession().setAuthStep(AuthFlowStep.SECQUES);

				if (sessionService.getGuestSession().isFlow(AuthFlow.LOGIN)) {
					sessionService.authorize(customerModel, true);
				}

				wrapper.setMessage(ResponseStatus.AUTH_DONE, ResponseMessage.AUTH_SUCCESS);
			}

		} catch (IncorrectInputException e) {
			customerModel = sessionService.getGuestSession().getCustomerModel();

			ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
					customerModel.getSecurityquestions());

			SecurityQuestionModel answer = listmgr.pickNext(sessionService.getGuestSession().getQuesIndex());
			sessionService.getGuestSession().nextQuesIndex();

			List<QuestModelDTO> questModel = jaxService.getMetaClient().getSequrityQuestion().getResults();

			for (QuestModelDTO questModelDTO : questModel) {
				if (questModelDTO.getQuestNumber().equals(answer.getQuestionSrNo())) {
					wrapper.getData().setQuestion(questModelDTO.getDescription());
				}
			}
			wrapper.getData().setAnswer(answer);
			wrapper.setMessage(ResponseStatus.AUTH_FAILED, e);
		} catch (CustomerValidationException e) {
			wrapper.setMessage(ResponseStatus.AUTH_FAILED, e);
		} catch (LimitExeededException e) {
			wrapper.setMessage(ResponseStatus.AUTH_BLOCKED_TEMP, e);
		}
		return wrapper;
	}

	public ResponseWrapper<LoginData> sendOTP(String identity) {
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());
		try {
			CivilIdOtpModel model;
			if (identity == null) {
				model = jaxService.setDefaults().getUserclient().sendOtpForCivilId().getResult();
			} else {
				model = jaxService.setDefaults().getUserclient().sendOtpForCivilId(identity).getResult();
				userSession.setUserid(identity);
			}
			// Check if response was successful
			// append info in response data
			wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
			wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
			wrapper.setMessage(ResponseStatus.OTP_SENT, "OTP generated and sent");
		} catch (InvalidInputException | CustomerValidationException | LimitExeededException e) {
			wrapper.setMessage(ResponseStatus.INVALID_ID, e);
		}
		return wrapper;
	}

	public ResponseWrapper<LoginData> initResetPassword(String identity) {
		sessionService.clear();
		sessionService.getGuestSession().setFlow(AuthFlow.RESET_PASS);
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());
		try {
			CivilIdOtpModel model = jaxService.setDefaults().getUserclient().sendResetOtpForCivilId(identity)
					.getResult();
			userSession.setUserid(identity);
			wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
			wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
			wrapper.setMessage(ResponseStatus.OTP_SENT, "OTP generated and sent");
			sessionService.getGuestSession().setAuthStep(AuthFlowStep.IDVALID);
		} catch (InvalidInputException | CustomerValidationException | LimitExeededException e) {
			wrapper.setMessage(ResponseStatus.INVALID_ID, e);
		}
		return wrapper;
	}

	public ResponseWrapper<LoginData> verifyResetPassword(String identity, String motp, String eotp) {
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(null);
		try {
			CustomerModel model = jaxService.setDefaults().getUserclient().validateOtp(identity, motp, eotp)
					.getResult();
			// Check if otp is valid
			if (model != null) {
				sessionService.getGuestSession().setCustomerModel(model);
				sessionService.getGuestSession().setAuthStep(AuthFlowStep.DOTPVFY);
				wrapper.setData(getRandomSecurityQuestion(model));
				wrapper.setMessage(ResponseStatus.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
			} else { // Use is cannot be validated
				wrapper.setMessage(ResponseStatus.VERIFY_FAILED, ResponseMessage.AUTH_FAILED);
			}
		} catch (IncorrectInputException | CustomerValidationException | LimitExeededException e) {
			wrapper.setMessage(ResponseStatus.VERIFY_FAILED, e);
		}
		return wrapper;
	}

	public ResponseWrapper<UserUpdateData> updatepwd(String password, String mOtp, String eOtp) {
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		try {
			if (sessionService.getGuestSession().isFlow(AuthFlow.RESET_PASS)
					&& !sessionService.getGuestSession().isAuthStep(AuthFlowStep.DOTPVFY)) {
				throw new HttpUnauthorizedException(HttpUnauthorizedException.UN_SEQUENCE);
			}
			if (!sessionService.getUserSession().isValid()) {
				throw new HttpUnauthorizedException(HttpUnauthorizedException.UN_AUTHORIZED);
			}
			BooleanResponse model = jaxService.setDefaults().getUserclient().updatePassword(password, mOtp, eOtp)
					.getResult();
			if (model.isSuccess()) {
				wrapper.setMessage(ResponseStatus.USER_UPDATE_SUCCESS, "Password Updated Succesfully");
			}
		} catch (IncorrectInputException | CustomerValidationException | LimitExeededException e) {
			wrapper.setMessage(ResponseStatus.USER_UPDATE_FAILED, e);
		}
		return wrapper;
	}

}
