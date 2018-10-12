package com.amx.jax.auditlogs;

import com.amx.jax.model.request.CustomerPersonalDetail;

public class SendOtpEmailMobileAuditEvent extends JaxAuditEvent{	

	CustomerPersonalDetail customerPersonalDetails;

	public CustomerPersonalDetail getCustomerPersonalDetails() {
		return customerPersonalDetails;
	}

	public void setCustomerPersonalDetails(CustomerPersonalDetail customerPersonalDetails) {
		this.customerPersonalDetails = customerPersonalDetails;
	}
	
	public SendOtpEmailMobileAuditEvent(CustomerPersonalDetail customerPersonalDetails) {
		super(Type.MOBILE_EMAIL_OTP);
		this.customerPersonalDetails= customerPersonalDetails;
		
	}
	

}
