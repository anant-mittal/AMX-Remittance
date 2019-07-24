package com.amx.jax.model.request.insurance;

import javax.validation.constraints.NotNull;

public class OptInOutRequest {

	@NotNull
	Boolean optIn;

	public OptInOutRequest() {
		super();
	}

	public OptInOutRequest(Boolean optIn) {
		super();
		this.optIn = optIn;
	}

	public Boolean getOptIn() {
		return optIn;
	}

	public void setOptIn(Boolean optIn) {
		this.optIn = optIn;
	}

}
