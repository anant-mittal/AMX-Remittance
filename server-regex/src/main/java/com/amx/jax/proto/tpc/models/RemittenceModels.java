package com.amx.jax.proto.tpc.models;

import java.math.BigDecimal;
import java.util.Map;

import com.amx.jax.dict.Currency;
import com.amx.jax.swagger.ApiMockModelProperty;

public class RemittenceModels {

	public static class RemitInquiryRequest {

		@ApiMockModelProperty(example = "1234", required = true)
		public BigDecimal beneId;

		@ApiMockModelProperty(example = "100", required = true)
		public Float domAmount;

		@ApiMockModelProperty(example = "BHD", required = true)
		public Currency domCur;

		@ApiMockModelProperty(example = "INR", required = true)
		public Currency forCur;

		@ApiMockModelProperty(example = "SALARY", required = true)
		public String source;

	}

	public static class RemitInquiryResponse {

		@ApiMockModelProperty(example = "1234", required = true)
		public BigDecimal beneId;

		@ApiMockModelProperty(example = "100", required = true)
		public Float domAmount;

		@ApiMockModelProperty(example = "23500", required = true)
		public Float forAmount;

		@ApiMockModelProperty(example = "BHD", required = true)
		public Currency domCur;

		@ApiMockModelProperty(example = "INR", required = true)
		public Currency forCur;

		@ApiMockModelProperty(example = "SALARY", required = true)
		public String source;

		@ApiMockModelProperty(example = "INVEST_LOANS", required = true)
		public String purpose;

	}

	public static class RemitConfirmPaymentRequest extends RemitInquiryRequest {

		@ApiMockModelProperty(value = "Flex Fields to be passed")
		private Map<String, Object> flexFields;

		@ApiMockModelProperty(value = "Additional Fields to be passed")
		private Map<String, Object> additionalFields;
	}

	public static class RemitConfirmPaymentResponse {

		@ApiMockModelProperty(value = "This is confirmation of rate customer will get and will have validity of 10 min",
				example = "67783643403", required = true, position = 0)
		public BigDecimal applicationId;

		@ApiMockModelProperty(example = "TR232323902323", value = "Transaction Id", required = true)
		public BigDecimal transactionId;

		@ApiMockModelProperty(example = "PAIDC", value = "Transaction Status", required = true)
		public TranxStatus status;
	}

	public static class RemitVerifyRequest {
		@ApiMockModelProperty(example = "APP67783643403", value = "Application Id", required = true)
		public BigDecimal applicationId;
	}

	public static enum TranxStatus {
		INITIATED, CONFIRMED, CANCELLED,
		/**
		 * Rejected by AMX
		 */
		REJECTED,
		/**
		 * Payment is done by customer
		 */
		PAIDC,

		/**
		 * Payment is done to beneficiary
		 */
		PAIDB

	}

	public static class RemitVerifyResponse {
		@ApiMockModelProperty(example = "TR232323902323", value = "Transaction Id", required = true)
		public BigDecimal transactionId;

		@ApiMockModelProperty(example = "PAIDC", value = "Transaction Status", required = true)
		public TranxStatus status;
	}

}
