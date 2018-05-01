package com.amx.jax.trnx;

import java.io.Serializable;

import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.jax.trnx.model.OtpData;

public class CustomerRegistrationTrnxModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CustomerPersonalDetail customerPersonalDetail;

	OtpData otpData;
	
	CustomerHomeAddress customerHomeAddress;

	public CustomerPersonalDetail getCustomerPersonalDetail() {
		return customerPersonalDetail;
	}

	public void setCustomerPersonalDetail(CustomerPersonalDetail customerPersonalDetail) {
		this.customerPersonalDetail = customerPersonalDetail;
	}

	public OtpData getOtpData() {
		return otpData;
	}

	public void setOtpData(OtpData otpData) {
		this.otpData = otpData;
	}

	public CustomerHomeAddress getCustomerHomeAddress() {
		return customerHomeAddress;
	}

	public void setCustomerHomeAddress(CustomerHomeAddress customerHomeAddress) {
		this.customerHomeAddress = customerHomeAddress;
	}

}
