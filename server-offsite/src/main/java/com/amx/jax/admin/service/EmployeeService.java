package com.amx.jax.admin.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.model.AuthData;
import com.amx.jax.response.ResponseWrapper;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.service.SessionService;

public class EmployeeService {

	@Autowired
	private SessionService sessionService;
	
	public ResponseWrapper<AuthData> sendOTP(OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		sessionService.getGuestSession().initStep(AuthStep.SAVE_HOME);
		ResponseWrapper<AuthData> wrapper = new ResponseWrapper<AuthData>(new AuthData());

		BooleanResponse resp = jaxClient.setDefaults().getCustRegClient().saveHomeAddress(customerHomeAddress)
				.getResult();
		if (resp.isSuccess()) {
			// update Session/State
			sessionService.getGuestSession().getState().setValidMotp(true);
			sessionService.getGuestSession().getState().setValidEotp(true);
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_SUCCESS);
			sessionService.getGuestSession().endStep(AuthStep.SAVE_HOME);
		} else {
			wrapper.setMessage(WebResponseStatus.USER_UPDATE_FAILED);
		}

		wrapper.getData().setState(sessionService.getGuestSession().getState());
		return wrapper;
	}

}
