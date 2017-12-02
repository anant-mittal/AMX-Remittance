package com.amx.jax.ui.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.client.UserClient;
import com.amx.jax.ui.EnumUtil;
import com.amx.jax.ui.EnumUtil.StatusCode;
import com.amx.jax.ui.config.CustomerAuthProvider;
import com.amx.jax.ui.model.GuestSession;
import com.amx.jax.ui.model.UserSession;
import com.amx.jax.ui.response.LoginData;
import com.amx.jax.ui.response.RegistrationdData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.bootloaderjs.ListManager;

@Service
public class LoginService {

	@Autowired
	private UserClient userclient;

	@Autowired
	private GuestSession guestSession;

	@Autowired
	private UserSession userSession;

	@Autowired
	private CustomerAuthProvider customerAuthProvider;

	@Autowired
	private JaxService jaxService;

	public ResponseWrapper<LoginData> login(String identity, String password) {

		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());

		if (userSession.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
		} else {
			CustomerModel customerModel;
			try {
				customerModel = jaxService.setDefaults().getUserclient().login(identity, password).getResult();

				guestSession.setCustomerModel(customerModel);

				ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
						customerModel.getSecurityquestions());

				SecurityQuestionModel answer = listmgr.pickRandom();
				guestSession.setQuesIndex(listmgr.getIndex());

				List<QuestModelDTO> questModel = jaxService.getMetaClient()
						.getSequrityQuestion(JaxService.DEFAULT_LANGUAGE_ID, JaxService.DEFAULT_COUNTRY_ID)
						.getResults();

				for (QuestModelDTO questModelDTO : questModel) {
					if (questModelDTO.getQuestNumber() == answer.getQuestionSrNo()) {
						wrapper.getData().setQuestion(questModelDTO.getDescription());
					}
				}

				wrapper.getData().setAnswer(answer);
				wrapper.getData().setImageId(customerModel.getImageUrl());
				wrapper.getData().setImageCaption(customerModel.getCaption());

				wrapper.setMessage(StatusCode.AUTH_OK, "Password is Correct");

			} catch (IncorrectInputException e) {
				wrapper.setMessage(StatusCode.AUTH_FAILED, "Wrong Credentials");
			}
		}

		return wrapper;
	}

	public ResponseWrapper<LoginData> loginSecQues(LoginData loginData) {
		ResponseWrapper<LoginData> wrapper = new ResponseWrapper<LoginData>(new LoginData());
		if (userSession.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
		} else {
			CustomerModel customerModel = guestSession.getCustomerModel();

			ListManager<SecurityQuestionModel> listmgr = new ListManager<SecurityQuestionModel>(
					customerModel.getSecurityquestions());

			SecurityQuestionModel answer = listmgr.pickNext(guestSession.getQuesIndex());

			List<QuestModelDTO> questModel = jaxService.getMetaClient()
					.getSequrityQuestion(JaxService.DEFAULT_LANGUAGE_ID, JaxService.DEFAULT_COUNTRY_ID).getResults();

			for (QuestModelDTO questModelDTO : questModel) {
				if (questModelDTO.getQuestNumber() == answer.getQuestionSrNo()) {
					wrapper.getData().setQuestion(questModelDTO.getDescription());
				}
			}
			wrapper.getData().setAnswer(answer);
		}
		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> checkSecurity(String identity, String password,
			HttpServletRequest request) {

		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		if (userSession.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
		} else {
			UsernamePasswordAuthenticationToken token = null;
			try {

				CustomerModel customerModel = jaxService.setDefaults().getUserclient().login(identity, password)
						.getResult();
				// Check if otp is valid
				if (customerModel != null) {
					token = new UsernamePasswordAuthenticationToken(identity, password);
					token.setDetails(new WebAuthenticationDetails(request));
					Authentication authentication = this.customerAuthProvider.authenticate(token);
					wrapper.setMessage(StatusCode.VERIFY_SUCCESS, "Authing");
					userSession.setCustomerModel(customerModel);
					userSession.setValid(true);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else { // Use is cannot be validated

					wrapper.setMessage(StatusCode.VERIFY_FAILED, "NoAuthing");
				}

			} catch (Exception e) { // user cannot be validated
				token = null;
				wrapper.setMessage(StatusCode.VERIFY_FAILED, "NoAuthing");
			}
			SecurityContextHolder.getContext().setAuthentication(token);
		}

		return wrapper;
	}

}
