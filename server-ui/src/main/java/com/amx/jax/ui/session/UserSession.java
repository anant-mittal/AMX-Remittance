package com.amx.jax.ui.session;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.CustomerModel;
import com.bootloaderjs.Random;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable {

	private static final long serialVersionUID = -6354887590466374869L;
	private boolean valid = false;
	private String otpPrefix = null;

	public void setOtpPrefix() {
		this.otpPrefix = Random.randomAlpha(3);
	}

	public String getOtpPrefix() {
		return otpPrefix;
	}

	private String userid = null;

	private CustomerModel customerModel = null;

	private CurrencyMasterDTO localCurrency = null;

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
		if (this.userid != null && this.userid.equals(userid)) {
			this.valid = true;
		}
		return this.valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}