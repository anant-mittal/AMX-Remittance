package com.amx.jax.proto.tpc.models;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class RemittenceModels {

	public static class RemitVerifyRequest {

		@ApiModelProperty(example = "67783643403", value = "Application Id recieved in RemitConfirm Response",
			required = true)
		public BigDecimal applicationId;

		@ApiModelProperty(example = "345678", value = "OTP recieved by Customer on his mobile", required = true)
		public BigDecimal mOtp;
	}

	public static class RemitVerifyResponse {
		@ApiModelProperty(example = "67783643403", value = "Transaction Id", required = true)
		public BigDecimal transactionId;
	}

	public static class RemitConfirmResponse extends RemitInquiryResponse {

		@ApiModelProperty(value = "This is confirmation of rate customer will get and will have validity of 10 min",
			example = "67783643403", required = true, position = 0)
		public BigDecimal applicationId;

		@ApiModelProperty(example = "ZXY", value = "Prefix for OTP recieved by customer on his mobile", required = true)
		public String mOtpPrefix;

	}

	public static class RemitInquiryRequest {

		@ApiModelProperty(example = "1234", required = true)
		public BigDecimal beneId;

		@ApiModelProperty(example = "100", required = true)
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

	public static class RemitInquiryResponse {

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
}
