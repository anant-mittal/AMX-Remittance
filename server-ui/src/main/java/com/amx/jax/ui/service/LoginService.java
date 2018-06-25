package com.amx.jax.ui.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.logger.AuditService;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.amx.jax.ui.auth.CAuthEvent;
import com.amx.jax.ui.config.HttpUnauthorizedException;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.session.UserSession;
import com.amx.utils.ListManager;

/**
 * The Class LoginService.
 */
@Service
public class LoginService {

	/** The log. */
	private Logger log = LoggerFactory.getLogger(getClass());

	/** The user session. */
	@Autowired
	private UserSession userSession;

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/** The audit service. */
	@Autowired
	private AuditService auditService;

	/**
	 * Gets the random security question.
	 *
	 * @param customerModel
	 *            the customer model
	 * @return the random security question
	 */
	private AuthData getRandomSecurityQuestion(CustomerModel customerModel) {
		AuthData loginData = new AuthData();
		ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
				customerModel.getSecurityquestions());

		SecurityQuestionModel answer = listmgr.pickRandom();
		sessionService.getGuestSession().setQuesIndex(listmgr.getIndex());

		List<QuestModelDTO> questModel = jaxService.getMetaClient().getSequrityQuestion().getResults();

		for (QuestModelDTO questModelDTO : questModel) {
			if (questModelDTO.getQuestNumber().equals(answer.getQuestionSrNo())) {
				loginData.setQuestion(questModelDTO.getDescription()); // TODO:- TO be removed
				loginData.setQues(questModelDTO);
			}
		}

		loginData.setImageId(customerModel.getImageUrl());
		loginData.setImageCaption(customerModel.getCaption());
		// loginData.setAnswer(answer);
		return loginData;
	}

	/**
	 * Login.
	 *
	 * @param identity
	 *            the identity
	 * @param password
	 *            the password
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthResponse> login(String identity, String password) {
		ResponseWrapper<AuthResponse> wrapper = new ResponseWrapper<AuthResponse>(null);
		sessionService.clear();
		sessionService.getGuestSession().initFlow(AuthState.AuthFlow.LOGIN);
		CustomerModel customerModel;
		sessionService.getGuestSession().setIdentity(identity);
		customerModel = jaxService.setDefaults().getUserclient().login(identity, password).getResult();
		if (customerModel == null) {
			throw new JaxSystemError();
		}
		sessionService.getGuestSession().setCustomerModel(customerModel);
		wrapper.setData(getRandomSecurityQuestion(customerModel));
		wrapper.setMessage(WebResponseStatus.AUTH_OK, "Password is Correct");
		sessionService.getGuestSession().endStep(AuthStep.USERPASS);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * Login sec ques.
	 *
	 * @param guestanswer
	 *            the guestanswer
	 * @param mOtp
	 *            the m otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthResponse> loginSecQues(SecurityQuestionModel guestanswer, String mOtp) {
		sessionService.getGuestSession().initStep(AuthStep.SECQUES);
		ResponseWrapper<AuthResponse> wrapper = new ResponseWrapper<AuthResponse>(new AuthData());
		CustomerModel customerModel;
		try {

			if (mOtp == null) {
				List<SecurityQuestionModel> guestanswers = new ArrayList<SecurityQuestionModel>();
				guestanswers.add(guestanswer);
				customerModel = jaxService.setDefaults().getUserclient().validateSecurityQuestions(guestanswers)
						.getResult();
			} else {
				customerModel = jaxService.setDefaults().getUserclient().validateOtp(null, mOtp, null).getResult();
			}

			if (customerModel == null) {
				throw new JaxSystemError();
			}

			/*
			 * TODO:- need to evaluate this condition it has some backward compatibility
			 * consideration
			 */
			sessionService.authorize(customerModel,
					sessionService.getGuestSession().getState().isFlow(AuthState.AuthFlow.LOGIN));

			if (sessionService.getGuestSession().getState().isFlow(AuthState.AuthFlow.LOGIN)) {
				jaxService.getUserclient().customerLoggedIn(sessionService.getAppDevice().toUserDevice());

				wrapper.setRedirectUrl(sessionService.getGuestSession().getReturnUrl());
				sessionService.getGuestSession().setReturnUrl(null);
			}

			wrapper.setMessage(WebResponseStatus.AUTH_DONE, ResponseMessage.AUTH_SUCCESS);
			sessionService.getGuestSession().endStep(AuthStep.SECQUES);
			wrapper.getData().setState(sessionService.getGuestSession().getState());

		} catch (IncorrectInputException e) {
			customerModel = sessionService.getGuestSession().getCustomerModel();

			ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
					customerModel.getSecurityquestions());

			SecurityQuestionModel answer = listmgr.pickNext(sessionService.getGuestSession().getQuesIndex());
			sessionService.getGuestSession().nextQuesIndex();

			List<QuestModelDTO> questModel = jaxService.getMetaClient().getSequrityQuestion().getResults();

			for (QuestModelDTO questModelDTO : questModel) {
				if (questModelDTO.getQuestNumber().equals(answer.getQuestionSrNo())) {
					wrapper.getData().setQuestion(questModelDTO.getDescription()); // TODO:- TO be removed
					wrapper.getData().setQues(questModelDTO);
				}
			}
			// wrapper.getData().setAnswer(answer);
			wrapper.setMessage(WebResponseStatus.AUTH_FAILED, e);
			auditService.log(
					new CAuthEvent(sessionService.getGuestSession().getState(), CAuthEvent.Result.FAIL, e.getError()));
		}
		return wrapper;
	}

	/**
	 * Send OTP.
	 *
	 * @param identity
	 *            the identity
	 * @param motp
	 *            the motp
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthResponse> sendOTP(String identity, String motp) {
		ResponseWrapper<AuthResponse> wrapper = new ResponseWrapper<AuthResponse>(new AuthData());
		CivilIdOtpModel model;
		if (identity == null) {
			model = jaxService.setDefaults().getUserclient().sendOtpForCivilId().getResult();
		} else {
			model = jaxService.setDefaults().getUserclient().sendOtpForCivilId(identity).getResult();
		}
		// Check if response was successful
		// append info in response data
		wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
		wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
		wrapper.setMessage(WebResponseStatus.OTP_SENT, "OTP generated and sent");
		return wrapper;
	}

	/**
	 * Inits the reset password.
	 *
	 * @param identity
	 *            the identity
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthResponse> initResetPassword(String identity) {
		sessionService.clear();
		sessionService.invalidate();

		sessionService.getGuestSession().initFlow(AuthState.AuthFlow.RESET_PASS);
		sessionService.getGuestSession().setIdentity(identity);
		ResponseWrapper<AuthResponse> wrapper = new ResponseWrapper<AuthResponse>(new AuthData());
		CivilIdOtpModel model = jaxService.setDefaults().getUserclient().sendResetOtpForCivilId(identity).getResult();
		wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
		wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
		wrapper.setMessage(WebResponseStatus.OTP_SENT, "OTP generated and sent");
		sessionService.getGuestSession().endStep(AuthStep.IDVALID);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * Verify reset password.
	 *
	 * @param identity
	 *            the identity
	 * @param motp
	 *            the motp
	 * @param eotp
	 *            the eotp
	 * @return the response wrapper
	 */
	public ResponseWrapper<AuthResponse> verifyResetPassword(String identity, String motp, String eotp) {
		sessionService.getGuestSession().initStep(AuthStep.MOTPVFY);
		ResponseWrapper<AuthResponse> wrapper = new ResponseWrapper<AuthResponse>(null);
		CustomerModel model = jaxService.setDefaults().getUserclient().validateOtp(identity, motp, eotp).getResult();
		// Check if otp is valid
		if (model == null) {
			throw new JaxSystemError();
		}
		sessionService.getGuestSession().setCustomerModel(model);
		wrapper.setData(getRandomSecurityQuestion(model));

		wrapper.setMessage(WebResponseStatus.VERIFY_SUCCESS, ResponseMessage.AUTH_SUCCESS);
		sessionService.getGuestSession().endStep(AuthStep.MOTPVFY);
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

	/**
	 * Updatepwd.
	 *
	 * @param password
	 *            the password
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	public ResponseWrapper<UserUpdateData> updatepwd(String password, String mOtp, String eOtp) {
		sessionService.getGuestSession().initStep(AuthStep.CREDS_SET);
		ResponseWrapper<UserUpdateData> wrapper = new ResponseWrapper<UserUpdateData>(new UserUpdateData());
		if (sessionService.getGuestSession().getState().isFlow(AuthState.AuthFlow.RESET_PASS)
				&& !sessionService.getGuestSession().getState().isStep(AuthState.AuthStep.SECQUES)) {
			throw new HttpUnauthorizedException(HttpUnauthorizedException.UN_SEQUENCE);
		}
		if (!sessionService.getUserSession().isValid()) {
			// throw new HttpUnauthorizedException(HttpUnauthorizedException.UN_AUTHORIZED);
		}
		BooleanResponse model = jaxService.setDefaults().getUserclient().updatePassword(password, mOtp, eOtp)
				.getResult();
		if (model.isSuccess()) {
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS, "Password Updated Succesfully");
			sessionService.getGuestSession().endStep(AuthStep.CREDS_SET);
			wrapper.getData().setState(sessionService.getGuestSession().getState());
		}
		return wrapper;
	}

}
