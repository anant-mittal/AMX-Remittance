package com.amx.jax.ui.model;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.CustomerModel;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GuestSession implements Serializable {

	private static final long serialVersionUID = -8825493107883952226L;

	private CustomerModel customerModel = null;

	public CustomerModel getCustomerModel() {
		return customerModel;
	}

	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
	}

	private boolean valid = false;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Integer quesIndex = 0;

	public Integer getQuesIndex() {
		return quesIndex;
	}

	public void setQuesIndex(Integer quesIndex) {
		this.quesIndex = quesIndex;
	}

	public void nextQuesIndex() {
		this.quesIndex++;
	}

}