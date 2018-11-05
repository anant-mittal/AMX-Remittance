package com.amx.jax.tpc.models;

import io.swagger.annotations.ApiModelProperty;

public class RequestCustomerAuth {

	@ApiModelProperty(example = "https://clientsite.com/amx/callback")
	public String callbackUrl;
}
