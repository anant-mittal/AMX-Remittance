package com.amx.jax.userservice.manager;

import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.util.AmxDBConstants.Status;

@Component
public class UserContactVerificationManager {

	public void setContactVerified(Customer customer, String mOtp, String eOtp, String wOtp) {
		if (mOtp != null) {
			customer.setMobileVerified(Status.Y);
		}

		if (eOtp != null) {
			customer.setEmailVerified(Status.Y);
		}

		if (wOtp != null) {
			customer.setWhatsAppVerified(Status.Y);
		}

	}

}
