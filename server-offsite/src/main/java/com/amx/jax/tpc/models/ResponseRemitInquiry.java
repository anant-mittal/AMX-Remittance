package com.amx.jax.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class ResponseRemitInquiry {

	@ApiModelProperty(example = "1234", required = true)
	public BigDecimal beneId;

	@ApiModelProperty(example = "100", required = true)
	public Float domAmount;

	@ApiModelProperty(example = "23500", required = true)
	public Float forAmount;

	@ApiModelProperty(example = "INR", required = true)
	public String currency;

	@ApiModelProperty(example = "SALARY", required = true)
	public String source;

	@ApiModelProperty(example = "INVEST_LOANS", required = true)
	public String purose;

	@ApiModelProperty(example = "1234", required = true)
	public BigDecimal totalLoyalityPoints;

}
