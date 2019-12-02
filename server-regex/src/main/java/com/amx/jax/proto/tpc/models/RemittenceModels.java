package com.amx.jax.proto.tpc.models;

import java.math.BigDecimal;
import java.util.Map;

import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RemittenceModels {

	private static interface TPCPaymentReference {
		@ApiMockModelProperty(example = "CAPTURED", value = "Payment Status", required = true)
		public PaymentStatus getPaymentStatus();

		void setPaymentStatus(PaymentStatus paymentStatus);

		@ApiMockModelProperty(
				value = "TPC Transaction/Payment Reference",
				notes = "This is reference id should be generated by client and should be part of response for record reference",
				example = "ABCF67783643403", required = true, position = 0)
		public String getPaymntReference();

		void setPaymntReference(String paymntReference);
	}

	private static interface RemitApplicationDetails {

		@ApiMockModelProperty(
				value = "Application Id",
				notes = "This is confirmation of rate customer will get and will have validity of 10 min",
				example = "67783643403", required = true, position = 0)
		public String getApplicationId();

		void setApplicationId(String applicationId);
	}

	private static interface RemitTranxStatus extends RemitApplicationDetails {

		@ApiMockModelProperty(value = "Transaction Id",
				notes = "Transaction Id : will be present only if, payment is done and application has been converted into transaction",
				example = "TR232323902323", required = true)
		public String getTransactionId();

		void setTransactionId(String transactionId);

		@ApiMockModelProperty(example = "PAIDC", value = "Transaction Status", required = true)
		public TranxStatus getTranxStatus();

		void setTranxStatus(TranxStatus tranxStatus);

	}

	/**********************
	 * 
	 * Remittance Models
	 * 
	 ***********************/

	public static class RemitInquiryRequest {

		@ApiMockModelProperty(example = "1234", required = true)
		public BigDecimal beneId;

		@ApiMockModelProperty(example = "100", required = true)
		public BigDecimal domAmount;

//		@ApiMockModelProperty(example = "BHD", required = true)
//		public Currency domCur;
//
//		@ApiMockModelProperty(example = "INR", required = true)
//		public Currency forCur;

		@ApiMockModelProperty(example = "23", required = true)
		public BigDecimal source;

	}

	public static class RemitInquiryResponse extends RemitInquiryRequest {

		@ApiMockModelProperty(example = "23500", required = true)
		public BigDecimal forAmount;

		@ApiMockModelProperty(example = "0.0054", required = true)
		public BigDecimal rate;

	}

	public static class RemitInitRequest extends RemitInquiryResponse {

		@ApiMockModelProperty(value = "Flex Fields to be passed")
		public Map<String, Object> flexFields;

		@ApiMockModelProperty(value = "Additional Fields to be passed")
		public Map<String, Object> additionalFields;

		@ApiMockModelProperty(example = "INVEST_LOANS", required = true)
		public String purpose;
	}

	@JsonDeserialize(as = RemittenceDTO.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static interface RemitConfirmRequest extends RemitApplicationDetails, TPCPaymentReference {
	}

	@JsonDeserialize(as = RemittenceDTO.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static interface RemitTranxStatusResponse extends RemitTranxStatus {
	}

	@JsonDeserialize(as = RemittenceDTO.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static interface RemitVerifyRequest extends RemitApplicationDetails {
	}

	@JsonDeserialize(as = RemittenceDTO.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static interface RemitVerifyResponse extends TPCPaymentReference {

	}

	public static enum PaymentStatus {
		CAPTURED, PENDING, CANCELED, VOIDED
	}

	public static enum TranxStatus {
		INITIATED,
		PAYMENT_SUCCESS_APPLICATION_SUCCESS, PAYMENT_SUCCESS_APPLICATION_FAIL, PAYMENT_FAIL, PAYMENT_IN_PROCESS,
		APPLICATION_CREATED,
		PAYMENT_CANCELED_BY_USER
	}

	public static class RemittenceDTO implements RemitVerifyResponse, RemitVerifyRequest, RemitTranxStatusResponse {

		private String applicationId;

		private String transactionId;

		private TranxStatus tranxStatus;

		private PaymentStatus paymentStatus;

		private String paymntReference;

		@Override
		public void setApplicationId(String applicationId) {
			this.applicationId = applicationId;
		}

		@Override
		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}

		@Override
		public void setTranxStatus(TranxStatus tranxStatus) {
			this.tranxStatus = tranxStatus;
		}

		@Override
		public void setPaymentStatus(PaymentStatus paymentStatus) {
			this.paymentStatus = paymentStatus;
		}

		@Override
		public void setPaymntReference(String paymntReference) {
			this.paymntReference = paymntReference;
		}

		@Override
		public String getApplicationId() {
			return applicationId;
		}

		@Override
		public String getTransactionId() {
			return transactionId;
		}

		@Override
		public TranxStatus getTranxStatus() {
			return tranxStatus;
		}

		@Override
		public PaymentStatus getPaymentStatus() {
			return paymentStatus;
		}

		@Override
		public String getPaymntReference() {
			return paymntReference;
		}
	}

}
