package com.amx.jax.proto.tpc.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class SourceOfFundDTO {

	@ApiMockModelProperty(example = "1234")
	public BigDecimal id;

	@ApiMockModelProperty(example = "SALARY")
	public String code;

	@ApiMockModelProperty(example = "Salary Income")
	public String description;

}
