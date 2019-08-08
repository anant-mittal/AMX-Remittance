package com.amx.amxlib.service;

import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.JaxError;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;

public interface IUserService {

	@ApiJaxStatus({ JaxError.CONTACT_TYPE_REQUIRED, JaxError.MOTP_REQUIRED, JaxError.EOTP_REQUIRED,
		JaxError.DOTP_REQUIRED, JaxError.INVALID_OTP })
	AmxApiResponse<CustomerModel, Object> validateCustomerLoginOtp(String identityInt);
}
