package com.amx.jax.ui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.UserClient;
import com.amx.jax.ui.EnumUtil;
import com.amx.jax.ui.model.UserSessionInfo;
import com.amx.jax.ui.response.LoginData;
import com.amx.jax.ui.response.UIResponse;
import com.amx.jax.ui.response.VerifyIdData;

@Service
public class RegistrationService {

	@Autowired
	private UserClient userclient;

	@Autowired
	private UserSessionInfo userSessionInfo;

	public UIResponse<VerifyIdData> verifyId(String civilid) {

		ApiResponse<CivilIdOtpModel> response = userclient.sendOtpForCivilId(civilid);
		CivilIdOtpModel result = response.getResult();
		VerifyIdData data = new VerifyIdData();
		UIResponse<VerifyIdData> uiresponse = new UIResponse<VerifyIdData>();
		if (result != null) {
			CivilIdOtpModel model = (CivilIdOtpModel) response.getData().getValues().get(0);
			data.setOtpdata(model);
			if (!model.getIsActiveCustomer()) {
				uiresponse.setStatusKey(EnumUtil.StatusCode.ALREADY_ACTIVE);
			}

			if (model.getOtp() == null) {
				uiresponse.setStatusKey(EnumUtil.StatusCode.INVALID_ID);
			}

			userSessionInfo.setOtp(model.getOtp());
			userSessionInfo.setUserid(civilid);

		}
		return uiresponse;
	}

	public LoginData loginWithOtp(String civilid, String otp) {
		ApiResponse<CustomerModel> response = userclient.validateOtp(civilid, otp);
		LoginData data = new LoginData();
		CustomerModel result = response.getResult();
		if (result != null) {
			CivilIdOtpModel model = (CivilIdOtpModel) response.getData().getValues().get(0);
			data.setOtpdata(model);
		}

		return data;
	}

}
