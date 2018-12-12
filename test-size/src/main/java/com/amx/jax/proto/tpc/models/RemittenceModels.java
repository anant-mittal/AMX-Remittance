package com.amx.jax.proto.tpc.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class RemittenceModels {

	public static class RemitVerifyRequest {

		@ApiMockModelProperty(example = "67783643403", value = "Application Id recieved in RemitConfirm Response",
				required = true)
		public BigDecimal applicationId;

		@ApiMockModelProperty(example = "345678", value = "OTP recieved by Customer on his mobile", required = true)
		private BigDecimal mOtp;
	}

	public static class RemitVerifyResponse {
		@ApiMockModelProperty(example = "67783643403", value = "Transaction Id", required = true)
		public BigDecimal transactionId;
	}

	public static class RemitConfirmResponse extends RemitInquiryResponse {

		@ApiMockModelProperty(value = "This is confirmation of rate customer will get and will have validity of 10 min",
				example = "67783643403", required = true, position = 0)
		public BigDecimal applicationId;

		@ApiMockModelProperty(example = "ZXY", value = "Prefix for OTP recieved by customer on his mobile",
				required = true)
		private String mOtpPrefix;

	}

	public static class RemitInquiryRequest {

		@ApiMockModelProperty(example = "1234", required = true)
		public BigDecimal beneId;

		@ApiMockModelProperty(example = "100", required = true)
		public Float domAmount;

		@ApiMockModelProperty(example = "23500", required = true)
		public Float forAmount;

		@ApiMockModelProperty(example = "SALARY", required = true)
		public String source;

		@ApiMockModelProperty(example = "INVEST_LOAN", required = true)
		public String purose;

		@ApiMockModelProperty(example = "false", required = false)
		public boolean useLoyalityPoints;

	}

	public static class RemitInquiryResponse {

		@ApiMockModelProperty(example = "1234", required = true)
		public BigDecimal beneId;

		@ApiMockModelProperty(example = "100", required = true)
		public Float domAmount;

		@ApiMockModelProperty(example = "23500", required = true)
		public Float forAmount;

		@ApiMockModelProperty(example = "INR", required = true)
		public String currency;

		@ApiMockModelProperty(example = "SALARY", required = true)
		public String source;

		@ApiMockModelProperty(example = "INVEST_LOANS", required = true)
		public String purose;

		@ApiMockModelProperty(example = "1234", required = true)
		public BigDecimal totalLoyalityPoints;

	}
}
