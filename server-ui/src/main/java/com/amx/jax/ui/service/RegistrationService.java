package com.amx.jax.ui.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.response.ApiResponse;
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

	public ResponseWrapper<RegistrationdData> verifyId(String civilid) {
		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());

		ApiResponse response = userclient.sendOtpForCivilId(civilid);

		if (!CollectionUtils.isEmpty(response.getData().getValues())) {
			CivilIdOtpModel model = (CivilIdOtpModel) response.getData().getValues().get(0);

			if (!model.getIsActiveCustomer()) {
				wrapper.setStatus(EnumUtil.StatusCode.ALREADY_ACTIVE);
			} else if (model.getOtp() == null) {
				wrapper.setStatus(EnumUtil.StatusCode.INVALID_ID);
			}

			userSessionInfo.setOtp(model.getOtp());
			userSessionInfo.setUserid(civilid);

			wrapper.getData().setOtpdata(model);
		}
		return wrapper;
	}

	public ResponseWrapper<RegistrationdData> loginWithOtp(String civilid, String otp, HttpServletRequest request) {
		ResponseWrapper<RegistrationdData> wrapper = new ResponseWrapper<RegistrationdData>(new RegistrationdData());
		if (userSessionInfo.isValid()) {
			wrapper.setStatus(EnumUtil.StatusCode.ALREADY_LOGGED_IN, "User already logged in");
		} else {
			UsernamePasswordAuthenticationToken token = null;
			try {
				if (userSessionInfo.isValid(civilid, otp)) {
					token = new UsernamePasswordAuthenticationToken(civilid, otp);
					token.setDetails(new WebAuthenticationDetails(request));
					Authentication authentication = this.customerAuthProvider.authenticate(token);
					wrapper.setStatus(StatusCode.VERIFY_SUCCESS, "Authing");
					userSessionInfo.setValid(true);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					wrapper.setStatus(StatusCode.VERIFY_FAILED, "NoAuthing");
				}

			} catch (Exception e) {
				token = null;
				wrapper.setStatus(StatusCode.VERIFY_FAILED, "NoAuthing");
			}
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		return wrapper;
	}

}
