package com.amx.jax.ui.model;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.CustomerModel;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSessionInfo implements Serializable {

	private static final long serialVersionUID = -6354887590466374869L;
	private boolean valid = false;
	private String otp = null;
	private String userid = null;

	private CustomerModel customerModel = null;

	public CustomerModel getCustomerModel() {
		return customerModel;
	}

	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isValid(String userid, String otp) {
		if (this.userid != null && this.userid.equals(userid) && this.otp != null && this.otp.equals(otp)) {
			this.valid = true;
		}
		return this.valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}