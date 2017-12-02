package com.amx.jax.ui.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.CustomerValidationException;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.EnumUtil;
import com.amx.jax.ui.EnumUtil.StatusCode;
import com.amx.jax.ui.config.CustomerAuthProvider;
import com.amx.jax.ui.model.UserSession;
import com.amx.jax.ui.response.LoginData;
import com.amx.jax.ui.response.RegistrationdData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.bootloaderjs.ListManager;

@Service
public class LoginService {

	@Autowired
	private UserSession userSession;

	@Autowired
	private CustomerAuthProvider customerAuthProvider;

	@Autowired
	private JaxService jaxService;

	@Autowired
	SessionService sessionService;

	public ResponseWrapper<LoginData> login(String identity, String password) {

		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());

		if (userSession.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
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
						.getSequrityQuestion(JaxService.DEFAULT_LANGUAGE_ID, JaxService.DEFAULT_COUNTRY_ID)
						.getResults();

				for (QuestModelDTO questModelDTO : questModel) {
					System.out.println(questModelDTO.getQuestNumber() + "===" + answer.getQuestionSrNo());
					if (questModelDTO.getQuestNumber().equals(answer.getQuestionSrNo())) {
						wrapper.getData().setQuestion(questModelDTO.getDescription());
					}
				}

				wrapper.getData().setAnswer(answer);
				wrapper.getData().setImageId(customerModel.getImageUrl());
				wrapper.getData().setImageCaption(customerModel.getCaption());

				wrapper.setMessage(StatusCode.AUTH_OK, "Password is Correct");

			} catch (IncorrectInputException e) {
				wrapper.setMessage(StatusCode.AUTH_FAILED, e.getErrorMessage());
			} catch (CustomerValidationException e) {
				wrapper.setMessage(StatusCode.AUTH_FAILED, e.getErrorMessage());
			} catch (LimitExeededException e) {
				wrapper.setMessage(StatusCode.AUTH_BLOCKED_TEMP, e.getMessage());
			}
		}

		return wrapper;
	}

	public ResponseWrapper<LoginData> loginSecQues(SecurityQuestionModel guestanswer, HttpServletRequest request) {
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());
		if (userSession.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
		} else {
			CustomerModel customerModel;
			try {
				List<SecurityQuestionModel> guestanswers = new ArrayList<SecurityQuestionModel>();
				guestanswers.add(guestanswer);
				customerModel = jaxService.setDefaults().getUserclient().validateSecurityQuestions(guestanswers)
						.getResult();

				sessionService.authorize(customerModel);

				wrapper.setMessage(StatusCode.VERIFY_SUCCESS, "Authing");
				wrapper.setMessage(EnumUtil.StatusCode.AUTH_DONE, "User authenitcated successfully");

			} catch (IncorrectInputException e) {
				customerModel = sessionService.getGuestSession().getCustomerModel();

				ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
						customerModel.getSecurityquestions());

				SecurityQuestionModel answer = listmgr.pickNext(sessionService.getGuestSession().getQuesIndex());
				sessionService.getGuestSession().nextQuesIndex();

				List<QuestModelDTO> questModel = jaxService.getMetaClient()
						.getSequrityQuestion(JaxService.DEFAULT_LANGUAGE_ID, JaxService.DEFAULT_COUNTRY_ID)
						.getResults();

				for (QuestModelDTO questModelDTO : questModel) {
					if (questModelDTO.getQuestNumber().equals(answer.getQuestionSrNo())) {
						wrapper.getData().setQuestion(questModelDTO.getDescription());
					}
				}
				wrapper.getData().setAnswer(answer);
				wrapper.setMessage(EnumUtil.StatusCode.AUTH_FAILED, e.getMessage());
			} catch (CustomerValidationException e) {
				wrapper.setMessage(StatusCode.AUTH_FAILED, e.getErrorMessage());
			} catch (LimitExeededException e) {
				wrapper.setMessage(StatusCode.AUTH_BLOCKED_TEMP, e.getMessage());
			}

		}
		return wrapper;
	}

}
