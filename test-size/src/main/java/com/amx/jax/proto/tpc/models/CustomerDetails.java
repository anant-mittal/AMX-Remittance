package com.amx.jax.proto.tpc.models;

import com.amx.jax.proto.tpc.models.RemittenceModels.RemitInquiryResponse;
import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerDetails {

	@ApiMockModelProperty(example = "Full Name of Customer,to be displayed on screen")
	public String fullName;

	public RemitInquiryResponse remit;

	public CustomerBeneDTO bene;
}
