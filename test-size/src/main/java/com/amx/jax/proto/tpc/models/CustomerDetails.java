package com.amx.jax.proto.tpc.models;

import com.amx.jax.proto.tpc.models.RemittenceModels.RemitInquiryResponse;

import io.swagger.annotations.ApiModelProperty;

public class CustomerDetails {

	@ApiModelProperty(example = "Full Name of Customer,to be displayed on screen")
	public String fullName;

	public RemitInquiryResponse remit;

	public CustomerBeneDTO bene;
}
