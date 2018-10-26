package com.amx.jax.model.request.device;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.IDeviceStateData;

public class SignaturePadCustomerRegStateMetaInfo implements IDeviceStateData {

	@NotNull
	Integer customerId;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
}
