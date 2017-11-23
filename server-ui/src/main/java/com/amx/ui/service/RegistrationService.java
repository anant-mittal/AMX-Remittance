package com.amx.ui.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.UserClient;
import com.amx.ui.response.VerifyIdData;

@Service
public class RegistrationService {

	@Autowired
	private UserClient userclient;

	public VerifyIdData verifyId(String civilid) {
		ApiResponse response = userclient.sendOtpForCivilId(civilid);
		VerifyIdData data = new VerifyIdData();
		if (!CollectionUtils.isEmpty(response.getData().getValues())) {
			CivilIdOtpModel model = (CivilIdOtpModel) response.getData().getValues().get(0);
			data.setOtpdata(model);
		}
		return data;
	}

	public VerifyIdData verifyCustomer(String civilid) {
		return null;
	}

}
