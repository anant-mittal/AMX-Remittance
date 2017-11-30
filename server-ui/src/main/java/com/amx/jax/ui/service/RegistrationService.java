package com.amx.jax.ui.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.UserClient;
import com.amx.jax.ui.EnumUtil;
import com.amx.jax.ui.EnumUtil.StatusCode;
import com.amx.jax.ui.config.CustomerAuthProvider;
import com.amx.jax.ui.model.UserSessionInfo;
import com.amx.jax.ui.response.RegistrationdData;
import com.amx.jax.ui.response.ResponseWrapper;

@Service
public class RegistrationService {

	@Autowired
	private UserClient userclient;

	@Autowired
	private UserSessionInfo userSessionInfo;

	@Autowired
	private CustomerAuthProvider customerAuthProvider;

	@Autowired
	private MetaClient metaClient;

	public ResponseWrapper<RegistrationdData> verifyId(String civilid) {

		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());
		CivilIdOtpModel model = userclient.sendOtpForCivilId(civilid).getResult();

		if (model != null) {

			// Check if response was successful
			if (!model.getIsActiveCustomer()) {
				wrapper.setError(EnumUtil.StatusCode.ALREADY_ACTIVE, "User is already registered for online");
			} else if (model.getOtp() == null) {
				wrapper.setError(EnumUtil.StatusCode.INVALID_ID, "Not able to generate OTP for givin cil ID");
			} else {
				wrapper.setStatus(EnumUtil.StatusCode.OTP_SENT, "Not able to generate OTP for givin cil ID");
				// append info in response data
				wrapper.getData().setOtp(model.getOtp());
				wrapper.getData().setOtpsent(true);
			}

			// Save information in user session
			userSessionInfo.setUserid(civilid);
			userSessionInfo.setOtp(model.getOtp());
		}
		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> loginWithOtp(String civilid, String otp, HttpServletRequest request) {
		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		if (userSessionInfo.isValid()) {
			// Check if use is already logged in;
			wrapper.setStatus(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
		} else {
			UsernamePasswordAuthenticationToken token = null;
			try {

				ApiResponse<CustomerModel> response = userclient.validateOtp(civilid, otp);
				CustomerModel model = response.getResult();

				// Check if otp is valid
				if (model != null && userSessionInfo.isValid(civilid, otp)) {
					token = new UsernamePasswordAuthenticationToken(civilid, otp);
					token.setDetails(new WebAuthenticationDetails(request));
					Authentication authentication = this.customerAuthProvider.authenticate(token);
					wrapper.setStatus(StatusCode.VERIFY_SUCCESS, "Authing");
					userSessionInfo.setCustomerModel(model);
					userSessionInfo.setValid(true);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else { // Use is cannot be validated

					wrapper.setStatus(StatusCode.VERIFY_FAILED, "NoAuthing");
				}

			} catch (Exception e) { // user cannot be validated
				token = null;
				wrapper.setStatus(StatusCode.VERIFY_FAILED, "NoAuthing");
			}
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> getSecQues() {

		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());
		List<QuestModelDTO> questModel = metaClient
				.getSequrityQuestion(UserSessionInfo.LANGUAGE_ID, UserSessionInfo.COUNTRY_ID).getResults();

		wrapper.getData().setSecQuesMeta(questModel);
		wrapper.getData().setSecQuesAns(userSessionInfo.getCustomerModel().getSecurityquestions());

		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> updateSecQues(List<SecurityQuestionModel> securityquestions) {

		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());
		CustomerModel customerModel = userclient.saveSecurityQuestions(securityquestions).getResult();
		userSessionInfo.setCustomerModel(customerModel);
		wrapper.getData().setSecQuesAns(customerModel.getSecurityquestions());
		wrapper.setStatus(EnumUtil.StatusCode.QA_UPDATED, "Question Answer Saved Scfuly");
		return wrapper;
	}

}
