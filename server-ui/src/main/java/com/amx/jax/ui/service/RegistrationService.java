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
	private CustomerAuthProvider customerAuthProvider;

	@Autowired
	private MetaClient metaClient;

	@Autowired
	private JaxService jaxClient;

	public ResponseWrapper<RegistrationdData> verifyId(String civilid) {

		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());
		jaxClient.setDefaults();
		CivilIdOtpModel model = userclient.sendOtpForCivilId(civilid).getResult();

		if (model != null) {

			// Check if response was successful
			if (model.getIsActiveCustomer()) {
				wrapper.setError(EnumUtil.StatusCode.ALREADY_ACTIVE, "User is already registered for online");
			} else if (model.getOtp() == null) {
				wrapper.setError(EnumUtil.StatusCode.INVALID_ID, "Not able to generate OTP for given civil ID");
			} else {
				wrapper.setMessage(EnumUtil.StatusCode.OTP_SENT, "OTP generated and sent");
				// append info in response data
				wrapper.getData().setOtp(model.getOtp());
				wrapper.getData().setOtpsent(true);
			}

			// Save information in user session
			userSessionInfo.setUserid(civilid);
			userSessionInfo.setOtp(model.getOtp());
			wrapper.getData().setOtp(model.getOtp());
			userSessionInfo.setUserid(civilid);
		}
		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> loginWithOtp(String civilid, String otp, HttpServletRequest request) {
		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		if (userSessionInfo.isValid()) {
			// Check if use is already logged in;
			wrapper.setMessage(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
		} else {
			UsernamePasswordAuthenticationToken token = null;
			try {

				jaxClient.setDefaults();
				ApiResponse<CustomerModel> response = userclient.validateOtp(civilid, otp);
				CustomerModel model = response.getResult();

				// Check if otp is valid
				if (model != null && userSessionInfo.isValid(civilid, otp)) {
					token = new UsernamePasswordAuthenticationToken(civilid, otp);
					token.setDetails(new WebAuthenticationDetails(request));
					Authentication authentication = this.customerAuthProvider.authenticate(token);
					wrapper.setMessage(StatusCode.VERIFY_SUCCESS, "Authing");
					userSessionInfo.setCustomerModel(model);
					userSessionInfo.setValid(true);
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

	public ResponseWrapper<RegistrationdData> getSecQues() {

		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		List<QuestModelDTO> questModel = metaClient
				.getSequrityQuestion(JaxService.DEFAULT_LANGUAGE_ID, JaxService.DEFAULT_COUNTRY_ID).getResults();

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

		jaxClient.setDefaults().getUserclient().savePhishiingImage(imageUrl, caption).getResult();

		wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_SUCCESS, "Phishing Image Updated");

		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> saveLoginIdAndPassword(String loginId, String password) {
		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		jaxClient.setDefaults().getUserclient().saveLoginIdAndPassword(loginId, password).getResult();

		wrapper.setMessage(EnumUtil.StatusCode.USER_UPDATE_SUCCESS, "LoginId and Password updated");

		return wrapper;
	}

}
