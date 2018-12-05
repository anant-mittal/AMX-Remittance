package com.amx.jax.proto.tpc.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class SourceOfFundDTO {

	@ApiMockModelProperty(example = "1234")
	public BigDecimal id;

	@ApiMockModelProperty(example = "INVEST_LOAN")
	public String code;

	@ApiMockModelProperty(example = "Invest in Loan")
	public String description;

}
