package com.amx.jax.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class RequestRemitInquiry {

	@ApiModelProperty(example = "1234", required = false)
	public BigDecimal beneId;

	@ApiModelProperty(example = "100", required = false)
	public Float domAmount;

	@ApiModelProperty(example = "23500", required = true)
	public Float forAmount;

	@ApiModelProperty(example = "SALARY", required = true)
	public String source;

	@ApiModelProperty(example = "INVEST_LOAN", required = true)
	public String purose;

	@ApiModelProperty(example = "false", required = false)
	public boolean useLoyalityPoints;

}
