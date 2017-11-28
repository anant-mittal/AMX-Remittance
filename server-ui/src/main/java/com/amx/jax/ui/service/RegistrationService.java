package com.amx.jax.ui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.amx.amxlib.model.CivilIdOtpModel;
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
		UIResponse<VerifyIdData> uiresponse = new UIResponse<VerifyIdData>();

		ApiResponse response = userclient.sendOtpForCivilId(civilid);
		VerifyIdData data = new VerifyIdData();
		if (!CollectionUtils.isEmpty(response.getData().getValues())) {
			CivilIdOtpModel model = (CivilIdOtpModel) response.getData().getValues().get(0);

			if (!model.getIsActiveCustomer()) {
				uiresponse.setStatusKey(EnumUtil.StatusCode.ALREADY_ACTIVE);
			}

			if (model.getOtp() == null) {
				uiresponse.setStatusKey(EnumUtil.StatusCode.INVALID_ID);
			}

			userSessionInfo.setOtp(model.getOtp());
			userSessionInfo.setUserid(civilid);

			data.setOtpdata(model);
		}

		uiresponse.setData(data);
		return uiresponse;
	}

	public LoginData loginWithOtp(String civilid, String otp) {
		// ApiResponse response = userclient.validateOtp(civilid, otp);
		LoginData data = new LoginData();

		return data;
	}

}
