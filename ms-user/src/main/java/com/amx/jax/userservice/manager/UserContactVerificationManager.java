package com.amx.jax.userservice.manager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.util.AmxDBConstants.Status;

@Component
public class UserContactVerificationManager {
	
	@Autowired
	CustomerRepository customerRepo;

	public void setContactVerified(Customer customer, String mOtp, String eOtp, String wOtp) {
		if(mOtp != null && eOtp != null && !mOtp.equals(eOtp)) {
			if (mOtp != null && !StringUtils.isBlank(mOtp)) {
				customer.setMobileVerified(Status.Y);
			}

			if (eOtp != null && !StringUtils.isBlank(eOtp)) {
				customer.setEmailVerified(Status.Y);
			}
		}
		
		if(mOtp != null && eOtp == null) {
			customer.setMobileVerified(Status.Y);
		}
		
		if(eOtp != null && mOtp == null) {
			customer.setEmailVerified(Status.Y);
		}
		
		if (wOtp != null) {
			customer.setWhatsAppVerified(Status.Y);
		}
		customerRepo.save(customer);
	}
}
