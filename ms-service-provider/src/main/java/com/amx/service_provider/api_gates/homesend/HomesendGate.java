package com.amx.service_provider.api_gates.homesend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.axis.types.UnsignedInt;
import org.apache.log4j.Logger;

import com.amg.vcHomeSend.ows.AdviceOfChargeRequest;
import com.amg.vcHomeSend.ows.AdviceOfChargeResponse;
import com.amg.vcHomeSend.ows.AmountInformation;
import com.amg.vcHomeSend.ows.HWSBinding_2_3Stub;
import com.amg.vcHomeSend.ows.HomeSendServiceException;
import com.amg.vcHomeSend.ows.HomeSend_HWS_2_3Locator;
import com.amg.vcHomeSend.ows.Payer;
import com.amg.vcHomeSend.ows.RemittanceCbsType;
import com.amg.vcHomeSend.ows.RemittanceRequest;
import com.amg.vcHomeSend.ows.RemittanceResponse;
import com.amg.vcHomeSend.ows.ReverseQuote;
import com.amg.vcHomeSend.ows.Security;
import com.amg.vcHomeSend.ows.StatusRequest;
import com.amg.vcHomeSend.ows.StatusResponse;
import com.amg.vcHomeSend.ows.VendorSpecificField;
import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.Quotation_Call_Response;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.model.response.serviceprovider.Status_Call_Response;
import com.amx.service_provider.api_gates.common.Common_API_Utils;
import com.amx.service_provider.dbmodel.webservice.OwsParamRespcode;
import com.amx.service_provider.dbmodel.webservice.OwsParamRespcodeKey;
import com.amx.service_provider.homesend.HomeSendDTO;
import com.amx.service_provider.repository.webservice.OwsParamRespcodeRepository;

/*
 * This class is created consume the API services exposed by HomeSend Main
 * services are: 1 - getQutation to get the rate, charges and commission for a
 * given input data 2 - send_remittance to send a remittance request. Required a
 * valid getQutation call as a prerequisite 3 - get_remittance_status to get the
 * remittance status after successful send_remittance call. Currently this
 * service is being called on scheduler basis 4 - cancellation request. But it
 * is not active. It has been tested and it is fine but there is no front end
 * screen to support it and we need to use status inquire call to support it.
 * Basically we need to manage how single transaction will be handled in all
 * those calls 5 - There is scheduler rate request is being fired from the
 * backed Notes on logs: 1 - Real time calls from front end application is for
 * the calls (getQutation and send_remmitance) are handled by front end team.
 * There are using same log table as WU 2 - Back end status call logs are stored
 * in the main remittance log table OWS_TRANSFER_LOG 3 - Rate logs are being
 * stored in the table EX_OWS_SRV_PROV_LOG This application is created by Ahmad
 * Salman 3rd April 2018
 */

public class HomesendGate
{
	private OwsParamRespcodeRepository owsParamRespcodeRepository;

	// initialize BindingStub
	HWSBinding_2_3Stub HomeSend_BindingStub = null;

	final String SERVICE_PROVIDER = new String("HOME");
	final String SERVICE_PROVIDER_DESC = new String("HOME_SEND");
	private final String GET_QUOTATION_METHOD_IND = new String("6"), SEND_TXN_METHOD_IND = new String("2"),
			REMITTANCE_STATUS_INQ_IND = new String("3");
	// SEND_CANCEL_REQUEST_IND = new String("7");
	// CANCEL_STATUS_INQ_IND = new String("8");
	private String API_LOGIN = null, API_PASSWORD = null;
	private final String PROPERTY_FILE_PATH = "com.amx.service_provider.config.Homesend_fileds_code_mapping";
	private final String CUSTOMER_ID_PREFIX = "ewallet:";

	private final String KEY_STORE_LOCATION =
			"D:\\Exchange\\OWS\\Projects\\HomeSend\\Docs\\UAT\\SSL_Config\\KeyStore\\home_send_uat_keystore.jks";
	private final String KEY_STORE_PASSWORD = "changeit";

	private final String TRUST_STORE_LOCATION =
			"D:\\Exchange\\OWS\\Projects\\HomeSend\\Docs\\UAT\\SSL_Config\\TrustStore\\truststore.jks";
	private final String TRUST_STORE_PASSWORD = "changeit";

	// private final String KEY_STORE_LOCATION =
	// "D:\\Salman\\SSL_Config\\KeyStore\\home_send_uat_keystore.jks";
	// private final String KEY_STORE_PASSWORD = "changeit";
	//
	// private final String TRUST_STORE_LOCATION =
	// "D:\\Salman\\SSL_Config\\TrustStore\\truststore.jks";
	// private final String TRUST_STORE_PASSWORD = "changeit";

	Logger logger = Logger.getLogger("WService.class");

	public HomesendGate(HomeSendDTO homeSendDTO)
	{
		try
		{
			// new Log4jPropConfig().log4jconfig();

			HomeSend_HWS_2_3Locator HomeSend_RemitLocator = new HomeSend_HWS_2_3Locator();

			HomeSend_RemitLocator.setHWSPort_2_3EndpointAddress(homeSendDTO.getApi_url());
			HomeSend_BindingStub = (HWSBinding_2_3Stub) HomeSend_RemitLocator.getHWSPort_2_3();

			API_LOGIN = homeSendDTO.getApi_login();
			API_PASSWORD = homeSendDTO.getApi_password();
			new UnsignedInt(homeSendDTO.getVendor_id());
			this.owsParamRespcodeRepository = homeSendDTO.getOwsParamRespcodeRepository();

			System.setProperty("javax.net.ssl.trustStore", TRUST_STORE_LOCATION);
			System.setProperty("javax.net.ssl.trustStorePassword", TRUST_STORE_PASSWORD); // changeit

			System.setProperty("javax.net.ssl.keyStore", KEY_STORE_LOCATION);
			System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);

			System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
		}
		catch (Exception e)
		{
			logger.info("Error: WService:: ");
			logger.info(Common_API_Utils.getStackTrace(e));

			throw new RuntimeException(e.getMessage());
		}
	}

	public Quotation_Call_Response getQuotation(BigDecimal settlement_amount, String settlement_currency,
			BigDecimal destination_amount, String destination_currency, String destination_country_2_digit_ISO,
			String destination_country_3_digit_ISO, String origin_country_3_digit_ISO, String customer_reference,
			String customer_type, String beneficary_reference, String beneficary_type, String beneficary_name,
			String beneficiary_account, int bic_indicator, String beneficiary_bank_code, String beneficiary_branch_code,
			String beneficiary_bank_branch_swift_code, String wallet_service_provider, String request_sequence_id,
			String remittance_mode, String delivery_mode)
	{
		/*
		 * This is service will be used to get the following: Rate Commission and final
		 * mount to be charged and credited in settlement currency and destination
		 * currency Bene Account validation � HomeSend Transaction ID We need to pass as
		 * in put the following: Amount and currency. See the question section. Bene and
		 * customer identification � Routing tag which is a list of possible option
		 * shared by partner. See the question section. This service will be used as
		 * first call in the transaction completion cycle and it need to be called
		 * before each transaction in real time. Keep in mind that this integration
		 * needs to be implanted in real time system and not offline mode. However, we
		 * can use the service smartly to get more data not related to any specific
		 * transaction. We can call this service to get the rate for any
		 * country/currency (see question section).
		 */

		boolean is_iban_number = HomesendUtils.is_iban_number(destination_country_2_digit_ISO, beneficiary_account);

		// Just printing the input details
		logger.info(HomesendUtils.log_quotation_input_details(settlement_amount,
				settlement_currency,
				destination_amount,
				destination_currency,
				destination_country_2_digit_ISO,
				destination_country_3_digit_ISO,
				origin_country_3_digit_ISO,
				customer_reference,
				customer_type,
				beneficary_reference,
				beneficary_type,
				beneficary_name,
				beneficiary_account,
				is_iban_number,
				bic_indicator,
				beneficiary_bank_code,
				beneficiary_branch_code,
				beneficiary_bank_branch_swift_code,
				wallet_service_provider,
				request_sequence_id,
				remittance_mode,
				delivery_mode));

		Quotation_Call_Response local_api_response = new Quotation_Call_Response();
		local_api_response.setOut_going_transaction_reference(request_sequence_id); // return back local generated
																					// reference in the response for
																					// informative purpose

		String cbs_type = null, routting_tag = null, service_type = null;

		AdviceOfChargeRequest request = new AdviceOfChargeRequest();
		AdviceOfChargeResponse response;
		Security security_object = new Security();
		AmountInformation amountInformation = new AmountInformation();
		ReverseQuote reverseQuote = new ReverseQuote();
		// Derived Data, all of them depend on the remittance mode.
		// 1 - CBS Type
		// 2 - Prefix
		// 3 - Routing Tag
		try
		{
			String validation_result =
					HomesendUtils.validate_quotation_request_input(settlement_amount,
							settlement_currency,
							destination_amount,
							destination_currency,
							destination_country_3_digit_ISO,
							origin_country_3_digit_ISO,
							customer_reference,
							customer_type,
							beneficary_reference,
							beneficary_type,
							beneficary_name,
							beneficiary_account,
							is_iban_number,
							request_sequence_id,
							remittance_mode,
							delivery_mode);

			if (validation_result != null && validation_result.length() != 0) // Validation Error
			{
				local_api_response.setResponse_code("JAX_INTERNAL_VALIDATION_ERROR");
				local_api_response.setResponse_description(validation_result);

				logger.info("Validation Result for transaction ( request_sequence_id= " +
						request_sequence_id +
						" / customer_reference= " +
						customer_reference +
						" / beneficary_reference= " +
						beneficary_reference +
						" ): /n " +
						validation_result);
			}
			else
			{
				customer_reference = CUSTOMER_ID_PREFIX + customer_reference;

				beneficiary_account =
						HomesendUtils.form_bank_account(beneficiary_account,
								is_iban_number,
								bic_indicator,
								beneficiary_bank_branch_swift_code,
								beneficiary_bank_code,
								beneficiary_branch_code,
								wallet_service_provider,
								beneficary_reference,
								remittance_mode);

				routting_tag =
						HomesendUtils.form_routting_tag(destination_country_3_digit_ISO,
								remittance_mode,
								beneficiary_bank_branch_swift_code);

				service_type = HomesendUtils.form_service_type(customer_type, beneficary_type);
				cbs_type = service_type;

				// Authentication Details
				security_object.setLogin(API_LOGIN);
				security_object.setPassword(API_PASSWORD);
				request.setSecurity(security_object);

				// Transaction details
				if (settlement_amount == null)
				{
					amountInformation.setAmount(destination_amount);
					amountInformation.setCurrency(destination_currency);

					reverseQuote.setSendingCurrency(settlement_currency);
					request.setReverseQuote(reverseQuote);
				}
				else
				{
					amountInformation.setAmount(settlement_amount);
					amountInformation.setCurrency(settlement_currency);

					// No need to specify the destination currency here.
					// HomeSend will be able to get it by using the destinationURI filed.
				}

				request.setAmountInformation(amountInformation);
				request.setRoutingTag(routting_tag);
				request.setCbsType(new RemittanceCbsType(cbs_type));
				request.setTransactionId(request_sequence_id);

				List<VendorSpecificField> vendor_specific_fields_list =
						HomesendUtils.form_account_of_charge_vsf_list(origin_country_3_digit_ISO,
								service_type,
								destination_currency,
								beneficiary_bank_branch_swift_code,
								beneficiary_bank_code,
								beneficiary_branch_code);

				request.setVendorSpecificFields(vendor_specific_fields_list
						.toArray(new VendorSpecificField[vendor_specific_fields_list.size()]));

				// Sender Details
				request.setSourceUri(customer_reference);

				// Beneficiary Details
				request.setDestinationUri(beneficiary_account);

				// Call time calculation
				long startTime = System.currentTimeMillis();
				long endTime = 0;

				// Call HomeSend service
				try
				{
					logger.info("Start calling Fee Inquiry API for transaction ( request_sequence_id= " +
							request_sequence_id +
							" / customer_reference= " +
							customer_reference +
							" / beneficary_reference= " +
							beneficary_reference +
							" ) ...");

					response = HomeSend_BindingStub.adviceOfCharge(request);

					endTime = System.currentTimeMillis();

					logger.info("Fee Inquiry API for transaction completed( request_sequence_id= " +
							request_sequence_id +
							" / customer_reference= " +
							customer_reference +
							" / beneficary_reference= " +
							beneficary_reference +
							" ) took " +
							((endTime - startTime) / 1000.0) +
							" Seconds");

					// validate destination currency if the you are using only the settlement
					// currency to send
					// remittance
					if (
						!destination_currency
								.equalsIgnoreCase(response.getSenderPayerProposal().getCreditedAmount().getCurrency())
					)
					{
						local_api_response.setResponse_code("JAX_INVALID_DESTINATION_CURRENCY");
						local_api_response.setResponse_description("Destination currency received from partner (" +
								response.getSenderPayerProposal().getCreditedAmount().getCurrency() +
								") is not matching with Al-Mulla destination currency: " +
								destination_currency);
						local_api_response.setAction_ind("R"); // Rejected

						throw new Exception(local_api_response.getResponse_description());
					}

					local_api_response.setPartner_transaction_reference(response.getHsTransactionId());
					local_api_response.setOffer_expiration_date(response.getExpirationDate());

					local_api_response.setSettlement_currency(settlement_currency);
					local_api_response.setDestination_currency(destination_currency);

					local_api_response.setCredited_amount_in_destination_currency(
							response.getSenderPayerProposal().getCreditedAmount().getAmount());

					local_api_response.setInitial_amount_in_settlement_currency(
							response.getSenderPayerProposal().getInitialAmount().getAmount()); // 16.37

					// 18.37 -16.37 = 2
					local_api_response.setFix_charged_amount_in_settlement_currency(
							response.getSenderPayerProposal().getChargedAmount().getAmount()
									.subtract(response.getSenderPayerProposal().getInitialAmount().getAmount()));

					// 2.03- 2 = 0.03
					local_api_response.setVariable_charged_amount_in_settlement_currency(
							response.getSenderPayerProposal().getCommissionAmount().getAmount()
									.subtract(local_api_response.getFix_charged_amount_in_settlement_currency()));

					// 18.37 + 0.03 = 18.4
					local_api_response.setTotal_charged_amount_in_settlement_currency(
							response.getSenderPayerProposal().getChargedAmount().getAmount()
									.add(local_api_response.getVariable_charged_amount_in_settlement_currency()));

					local_api_response.setWhole_sale_fx_rate(new BigDecimal(HomesendUtils.get_filed_value(
							response.getVendorSpecificFields(),
							new UnsignedInt(
									ResourceBundle.getBundle(PROPERTY_FILE_PATH).getString("WHOLESALE_FX_RATE")))));

					local_api_response.setAction_ind("I"); // InProcess
				}
				catch (HomeSendServiceException e)
				{
					logger.info(Common_API_Utils.getStackTrace(e));

					try
					{
						logger.info("Fee Inquiry API for transaction completed with expection( request_sequence_id= " +
								request_sequence_id +
								" / customer_reference= " +
								customer_reference +
								" / beneficary_reference= " +
								beneficary_reference +
								" ) took " +
								((endTime - startTime) / 1000.0) +
								" Seconds");

						OwsParamRespcode temp_ows_param_respcode_record =
								owsParamRespcodeRepository.findByOwsParamRespcodeKey(new OwsParamRespcodeKey(
										SERVICE_PROVIDER, e.getCode(), GET_QUOTATION_METHOD_IND));

						local_api_response.setResponse_code(e.getCode());
						local_api_response.setResponse_description(e.getFaultString());
						local_api_response.setAction_ind(
								temp_ows_param_respcode_record != null ? temp_ows_param_respcode_record.getAction_ind()
										: null);
						local_api_response.setPartner_transaction_reference(e.getHsTransactionId());
						local_api_response.setTechnical_details(Common_API_Utils.getStackTrace(e));

						// Adding default value for action ind if it is null.
						// Since we are in the exception. Then the default value should be (R)
						if (
							local_api_response.getAction_ind() == null ||
									local_api_response.getAction_ind().length() == 0
						)
						{
							local_api_response.setAction_ind("R");// Rejected
						}
					}
					catch (Exception ex)
					{
						logger.info(Common_API_Utils.getStackTrace(ex));

						local_api_response.setResponse_code("JAX_UNEXPECTED_ERROR");
						local_api_response.setResponse_description(
								"Unknown error occurred while calling or processing partner response");
						local_api_response.setAction_ind("R"); // Rejected
					}
				}

				if (
					local_api_response.getResponse_description() == null ||
							local_api_response.getResponse_description().length() == 0
				)
				{
					local_api_response.setResponse_description(local_api_response.getResponse_code());
				}
			}
		}
		catch (Exception e)
		{
			logger.info(Common_API_Utils.getStackTrace(e));

			if (local_api_response.getResponse_code() == null)
			{
				local_api_response.setResponse_code("JAX_UNEXPECTED_ERROR");
				local_api_response.setResponse_description("Unknown error occurred while processing");
				local_api_response.setTechnical_details(Common_API_Utils.getStackTrace(e));

				if (
					e != null &&
							e.getMessage() != null &&
							(e.getMessage().toUpperCase().contains("CONNECTION") ||
									e.getMessage().toUpperCase().contains("TIMED OUT"))
				)
				{
					local_api_response.setAction_ind("C"); // Connection Issue
				}
				else
				{
					local_api_response.setAction_ind("R"); // Rejected
				}
			}
		}
		finally
		{
			if (HomeSend_BindingStub != null)
			{
				local_api_response.setRequest_XML(HomeSend_BindingStub.getRequestString());
				local_api_response.setResponse_XML(HomeSend_BindingStub.getResponseString());
			}
		}

		return local_api_response;
	}

	public Remittance_Call_Response send_remittance(TransactionData txn_data, Customer customer_data,
			Benificiary bene_data)
	{
		Remittance_Call_Response local_api_response = new Remittance_Call_Response();
		RemittanceResponse response;
		RemittanceRequest request = new RemittanceRequest();
		Security security_object = new Security();
		AmountInformation amountInformation = new AmountInformation();
		// SenderInformation senderInformation = new SenderInformation();
		String routting_tag, service_type, cbs_type;

		try
		{
			// Assign the same out-going reference to the response object
			local_api_response.setOut_going_transaction_reference(txn_data.getOut_going_transaction_reference());
			
			logger.info(HomesendUtils.log_send_remittance_input_details(txn_data, customer_data, bene_data));
			String validation_result =
					HomesendUtils.validate_remittance_request_input(txn_data, customer_data, bene_data);

			if (validation_result != null && validation_result.length() != 0)
			{
				local_api_response.setResponse_code("JAX_INTERNAL_VALIDATION_ERROR");
				local_api_response.setAction_ind("R"); // Rejected
				local_api_response.setResponse_description(validation_result);

				logger.info("Validation Error: " + validation_result);
			}
			else
			{
				local_api_response.setPartner_transaction_reference(txn_data.getPartner_transaction_reference());
				local_api_response.setOut_going_transaction_reference(txn_data.getOut_going_transaction_reference());

				customer_data.setCustomer_reference(CUSTOMER_ID_PREFIX + customer_data.getCustomer_reference());

				bene_data.setBeneficiary_account_number(
						HomesendUtils.form_bank_account(bene_data.getBeneficiary_account_number(),
								bene_data.isIs_iban_number_holder(),
								bene_data.getBic_indicator(),
								bene_data.getBeneficiary_bank_branch_swift_code(),
								bene_data.getBeneficiary_bank_code(),
								bene_data.getBeneficiary_branch_code(),
								bene_data.getWallet_service_provider(),
								bene_data.getBeneficiary_id_number(),
								txn_data.getRemittance_mode()));

				routting_tag =
						HomesendUtils.form_routting_tag(txn_data.getDestination_country_3_digit_ISO(),
								txn_data.getRemittance_mode(),
								bene_data.getBeneficiary_bank_branch_swift_code());

				service_type =
						HomesendUtils.form_service_type(customer_data.getCustomer_type(),
								bene_data.getBeneficiary_type());
				cbs_type = service_type;

				// Authentication Details
				security_object.setLogin(API_LOGIN);
				security_object.setPassword(API_PASSWORD);
				request.setSecurity(security_object);

				// Transaction details
				request.setHsTransactionId(txn_data.getPartner_transaction_reference());
				request.setTransactionId(txn_data.getRequest_sequence_id());
				request.setRoutingTag(routting_tag);
				request.setCbsType(new RemittanceCbsType(cbs_type));
				request.setPayer(new Payer(ResourceBundle.getBundle(PROPERTY_FILE_PATH).getString("PAYER")));
				request.setReservationRequired(false);
				request.setDescription(txn_data.getFurther_instruction());

				amountInformation.setAmount(txn_data.getSettlement_amount().setScale(3, BigDecimal.ROUND_HALF_EVEN));
				amountInformation.setCurrency(txn_data.getSettlement_currency());
				request.setAmountInformation(amountInformation);

				ArrayList<VendorSpecificField> vendor_specific_fields_list =
						HomesendUtils.form_remittance_vsf_list(txn_data, customer_data, bene_data, API_LOGIN);

				request.setVendorSpecificFields(vendor_specific_fields_list
						.toArray(new VendorSpecificField[vendor_specific_fields_list.size()]));

				// Sender Details
				request.setSourceUri(customer_data.getCustomer_reference());

				// Beneficiary Details
				request.setDestinationUri(bene_data.getBeneficiary_account_number());

				// Call time calculation
				long startTime = System.currentTimeMillis();
				long endTime = 0;

				// Call HomeSend service
				try
				{
					logger.info("Start calling Send Remittance API for transaction ( HomeSend ID= " +
							txn_data.getPartner_transaction_reference() +
							" / customer_reference= " +
							customer_data.getCustomer_reference() +
							" / beneficary_reference= " +
							bene_data.getBeneficiary_reference() +
							" ) ...");

					response = HomeSend_BindingStub.remittance(request);

					endTime = System.currentTimeMillis();

					logger.info("Send Remittance API completed for transaction ( HomeSend ID= " +
							txn_data.getPartner_transaction_reference() +
							" / customer_reference= " +
							customer_data.getCustomer_reference() +
							" / beneficary_reference= " +
							bene_data.getBeneficiary_reference() +
							"  took " +
							((endTime - startTime) / 1000.0) +
							" Seconds");

					// Empty Response Validation
					if (response == null)
					{
						local_api_response.setResponse_code("JAX_INVALID_PARTNER_RESPONSE");
						local_api_response.setAction_ind("C"); // Connection Issue
						local_api_response.setResponse_description("Empty details received from partner API");

						throw new Exception(local_api_response.getResponse_description());
					}

					if (response.getProvisionalResponse() == null) // Synchronous Response (i.e. Immediate Payment)
					{
						local_api_response.setResponse_code("Completed");
						local_api_response.setResponse_description("Remittance Completed");
						local_api_response.setAction_ind("P"); // PAID
					}
					else
					{
						local_api_response.setResponse_code(response.getProvisionalResponse().getState());
						local_api_response.setResponse_description(response.getProvisionalResponse().getLabel());
						local_api_response.setAction_ind("I"); // InProccess
						local_api_response
								.setExpected_completion_date(response.getProvisionalResponse().getMaxCompletionDate());
						local_api_response.setBene_bank_remittance_reference(
								HomesendUtils.get_filed_value(response.getVendorSpecificFields(),
										new UnsignedInt(Common_API_Utils.getProperties("TRANSACTION_SECRET_CODE",
												PROPERTY_FILE_PATH))));
					}
				}
				catch (HomeSendServiceException e)
				{
					logger.info(Common_API_Utils.getStackTrace(e));

					try
					{
						endTime = System.currentTimeMillis();
						logger.info("Send Remittance API completed for transaction ( HomeSend ID= " +
								txn_data.getPartner_transaction_reference() +
								" / customer_reference= " +
								customer_data.getCustomer_reference() +
								" / beneficary_reference= " +
								bene_data.getBeneficiary_reference() +
								"  took " +
								((endTime - startTime) / 1000.0) +
								" Seconds");

						OwsParamRespcode temp_ows_param_respcode_record =
								owsParamRespcodeRepository.findByOwsParamRespcodeKey(
										new OwsParamRespcodeKey(SERVICE_PROVIDER, e.getCode(), SEND_TXN_METHOD_IND));

						local_api_response.setResponse_code(e.getCode());
						local_api_response.setResponse_description(e.getFaultString());
						local_api_response.setAction_ind(
								temp_ows_param_respcode_record != null ? temp_ows_param_respcode_record.getAction_ind()
										: null);
						local_api_response.setTechnical_details(Common_API_Utils.getStackTrace(e));

						// Adding default value for action ind if it is null.
						// Since we are in the exception. Then the default value should be (R)
						if (
							local_api_response.getAction_ind() == null ||
									local_api_response.getAction_ind().length() == 0
						)
						{
							local_api_response.setAction_ind("R");// Rejected
						}
					}
					catch (Exception ex)
					{
						logger.info(Common_API_Utils.getStackTrace(ex));

						local_api_response.setResponse_code("JAX_UNEXPECTED_ERROR");
						local_api_response.setResponse_description(
								"Unknown error occurred while calling or processing partner response");
						local_api_response.setAction_ind("R"); // Rejected
						local_api_response.setTechnical_details(Common_API_Utils.getStackTrace(e));
					}
				}

				if (
					local_api_response.getResponse_description() == null ||
							local_api_response.getResponse_description().length() == 0
				)
				{
					local_api_response.setResponse_description(local_api_response.getResponse_code());
				}
			}
		}
		catch (Exception e)
		{
			logger.info(Common_API_Utils.getStackTrace(e));

			if (local_api_response.getResponse_code() == null)
			{
				local_api_response.setResponse_code("JAX_UNEXPECTED_ERROR");
				local_api_response
						.setResponse_description("Unknown error occurred while calling or processing partner response");
				local_api_response.setTechnical_details(Common_API_Utils.getStackTrace(e));

				if (
					e != null &&
							e.getMessage() != null &&
							(e.getMessage().toUpperCase().contains("CONNECTION") ||
									e.getMessage().toUpperCase().contains("TIMED OUT"))
				)
				{
					local_api_response.setAction_ind("C"); // Connection Issue
				}
				else
				{
					local_api_response.setAction_ind("R"); // Rejected
				}
			}
		}
		finally
		{
			if (HomeSend_BindingStub != null)
			{
				local_api_response.setRequest_XML(HomeSend_BindingStub.getRequestString());
				local_api_response.setResponse_XML(HomeSend_BindingStub.getResponseString());
			}
		}

		return local_api_response;
	}

	public Status_Call_Response get_remittance_status(String request_sequence_id, String homeSend_transaction_id)
	{
		Status_Call_Response local_api_response = new Status_Call_Response();
		StatusResponse response;
		StatusRequest request = new StatusRequest();
		Security security_object = new Security();

		try
		{
			String validation_result =
					HomesendUtils.validate_status_request_input(request_sequence_id, homeSend_transaction_id);

			if (validation_result != null && validation_result.length() != 0)
			{
				local_api_response.setResponse_code("JAX_INTERNAL_VALIDATION_ERROR");
				local_api_response.setResponse_description(validation_result);
				local_api_response.setAction_ind("R"); // Rejected
			}
			else
			{
				// Authentication Details
				security_object.setLogin(API_LOGIN);
				security_object.setPassword(API_PASSWORD);
				request.setSecurity(security_object);

				// Transaction details
				request.setHsTransactionId(homeSend_transaction_id);
				request.setTransactionId(request_sequence_id);

				// Call HomeSend service
				try
				{
					response = HomeSend_BindingStub.status(request);

					// Empty Response Validation
					if (response == null || response.getStatus() == null)
					{
						local_api_response.setResponse_code("JAX_INVALID_PARTNER_RESPONSE");
						local_api_response.setResponse_description("Empty details received from partner API");
						local_api_response.setAction_ind("C"); // Connection Issue or Invalid response

						throw new Exception(local_api_response.getResponse_description());
					}

					if (response.getStatus().getComplete() != null)
					{
						local_api_response.setResponse_code("Completed");
						local_api_response.setResponse_description("Remittance Completed");
						local_api_response.setAction_ind("P"); // Paid
					}
					else if (response.getStatus().getPending() != null)
					{
						local_api_response.setResponse_code(response.getStatus().getPending().getState());
						local_api_response.setResponse_description(response.getStatus().getPending().getLabel());
						local_api_response.setAction_ind("I"); // InProcess
						local_api_response
								.setExpected_completion_date(response.getStatus().getPending().getMaxCompletionDate());
					}
					else if (response.getStatus().getRejected() != null)
					{
						OwsParamRespcode temp_ows_param_respcode_record =
								owsParamRespcodeRepository.findByOwsParamRespcodeKey(new OwsParamRespcodeKey(
										SERVICE_PROVIDER, response.getStatus().getRejected().getCode(),
										REMITTANCE_STATUS_INQ_IND));

						local_api_response.setResponse_code(response.getStatus().getRejected().getCode());
						local_api_response.setResponse_description(response.getStatus().getRejected().getMessage());
						local_api_response.setAction_ind(
								temp_ows_param_respcode_record != null ? temp_ows_param_respcode_record.getAction_ind()
										: null); // Rejected

						// In case action indicator not found for the received response code, mark
						// action indicator as (R)
						if (local_api_response.getAction_ind() == null)
							local_api_response.setAction_ind("R"); // Rejected
					}

					if (response.getVendorSpecificFields() != null) // Get third party bank reference if any. Mostly for
																	// cash
					{
						local_api_response.setBene_bank_remittance_reference(
								HomesendUtils.get_filed_value(response.getVendorSpecificFields(),
										new UnsignedInt(Common_API_Utils.getProperties("TRANSACTION_SECRET_CODE",
												PROPERTY_FILE_PATH))));
					}
				}
				catch (HomeSendServiceException e)
				{
					logger.info(Common_API_Utils.getStackTrace(e));

					try
					{
						OwsParamRespcode temp_ows_param_respcode_record =
								owsParamRespcodeRepository.findByOwsParamRespcodeKey(new OwsParamRespcodeKey(
										SERVICE_PROVIDER, e.getCode(), REMITTANCE_STATUS_INQ_IND));
						local_api_response.setResponse_code(e.getCode());
						local_api_response.setResponse_description(e.getFaultString());
						local_api_response.setAction_ind(
								temp_ows_param_respcode_record != null ? temp_ows_param_respcode_record.getAction_ind()
										: null);

						if (
							local_api_response.getAction_ind() == null ||
									local_api_response.getAction_ind().length() == 0
						)
							local_api_response.setAction_ind("F"); // Follow up

						local_api_response.setTechnical_details(Common_API_Utils.getStackTrace(e));
					}
					catch (Exception ex)
					{
						logger.info(Common_API_Utils.getStackTrace(ex));

						local_api_response.setResponse_code("JAX_UNEXPECTED_ERROR");
						local_api_response.setResponse_description(
								"Unknown error occurred while calling or processing partner response");
						local_api_response.setAction_ind("T"); // Technical Error
						local_api_response.setTechnical_details(Common_API_Utils.getStackTrace(e));
					}
				}

				local_api_response.setRequest_XML(HomeSend_BindingStub.getRequestString());
				local_api_response.setResponse_XML(HomeSend_BindingStub.getResponseString());

				if (
					local_api_response.getResponse_description() == null ||
							local_api_response.getResponse_description().length() == 0
				)
				{
					local_api_response.setResponse_description(local_api_response.getResponse_code());
				}
			}
		}
		catch (Exception e)
		{
			logger.info(Common_API_Utils.getStackTrace(e));

			if (local_api_response.getResponse_code() == null)
			{
				local_api_response.setResponse_code("JAX_UNEXPECTED_ERROR");
				local_api_response
						.setResponse_description("Unknown error occurred while calling or processing partner response");
				local_api_response.setAction_ind("T"); // Technical Error
				local_api_response.setTechnical_details(Common_API_Utils.getStackTrace(e));

				if (
					e != null &&
							e.getMessage() != null &&
							(e.getMessage().toUpperCase().contains("CONNECTION") ||
									e.getMessage().toUpperCase().contains("TIMED OUT"))
				)
				{
					local_api_response.setAction_ind("C"); // Connection Issue
				}
				else
				{
					local_api_response.setAction_ind("T"); // Technical Error
				}
			}
		}

		return local_api_response;
	}

	public void cancel_remittance(String request_sequence_id, String homeSend_transaction_id, String cancel_reason_code,
			String cancel_reason_desc)
	{
		// Response local_api_response = new Response();
		// CancellationResponse response;
		// CancellationRequest request = new CancellationRequest();
		// CancellationReason cancellation_reason = new CancellationReason();
		// Security security_object = new Security();
		//
		// try
		// {
		// String validation_result =
		// WServiceUtils.validate_cancellation_request_input(request_sequence_id,
		// homeSend_transaction_id,
		// cancel_reason_code);
		//
		// if (validation_result != null && validation_result.length() != 0)
		// {
		// local_api_response.setResponse_code("JAX_INTERNAL_VALIDATION_ERROR");
		// local_api_response.setResponse_description(validation_result);
		// local_api_response.setAction_ind("R"); // Rejected
		// }
		// else
		// {
		// // Authentication Details
		// security_object.setLogin(API_LOGIN);
		// security_object.setPassword(API_PASSWORD);
		// request.setSecurity(security_object);
		//
		// // Transaction details
		// cancellation_reason.setCode(new Integer(cancel_reason_code.trim()));
		// cancellation_reason.setMessage(cancel_reason_desc);
		//
		// request.setHsTransactionId(homeSend_transaction_id);
		// request.setTransactionId(request_sequence_id);
		// request.setReason(cancellation_reason);
		//
		// // Call HomeSend service
		// try
		// {
		// response = HomeSend_BindingStub.cancellation(request);
		//
		// // Empty Response Validation
		// if (response == null)
		// {
		// local_api_response.setResponse_code("JAX_INVALID_PARTNER_RESPONSE");
		// local_api_response.setResponse_description("Empty details received from
		// partner API");
		// local_api_response.setAction_ind("R"); // Rejected
		//
		// throw new Exception(local_api_response.getResponse_description());
		// }
		//
		// // : At this point of execution, we assume that the cancellation has been
		// accepted by HomeSend (initially). Then on
		// // scheduler bases the transactions need to be sent in status queue
		//
		// // : Should find a way to push this record in the status queue.
		//
		// local_api_response.setResponse_code("Initial Success");
		// local_api_response
		// .setResponse_description("HomeSend accepts cancellation request initially. "
		// +
		// "But the request will be under process and final confirmation will be
		// received later."
		// + "This response does not grantee that the cancellation request is completed
		// successfully.");
		//
		// local_api_response.setAction_ind("I"); // InProcess
		// }
		// catch (HomeSendServiceException e)
		// {
		// try
		// {
		// local_api_response.setResponse_code(e.getCode());
		// local_api_response.setResponse_description(e.getFaultString());
		// local_api_response.setAction_ind(WServiceUtils.getActionInd(SERVICE_PROVIDER,
		// e.getCode(),
		// SEND_CANCEL_REQUEST_IND));
		// local_api_response.setTechnical_details(WServiceUtils.getStackTrace(e));
		// }
		// catch (Exception ex)
		// {
		// logger.info(WServiceUtils.getStackTrace(ex));
		//
		// local_api_response.setResponse_code("JAX_UNEXPECTED_ERROR");
		// local_api_response.setResponse_description("Unknown error occurred while
		// calling or processing partner response");
		// local_api_response.setAction_ind("R"); // Rejected
		// local_api_response.setTechnical_details(WServiceUtils.getStackTrace(e));
		// }
		// }
		//
		// local_api_response.setRequest_XML(HomeSend_BindingStub.getRequestString());
		// local_api_response.setResponse_XML(HomeSend_BindingStub.getResponseString());
		// }
		// }
		// catch (Exception e)
		// {
		// if (local_api_response.getResponse_code() == null)
		// {
		// logger.info(WServiceUtils.getStackTrace(e));
		//
		// local_api_response.setResponse_code("JAX_UNEXPECTED_ERROR");
		// local_api_response.setResponse_description("Unknown error occurred while
		// calling or processing partner response");
		// local_api_response.setTechnical_details(WServiceUtils.getStackTrace(e));
		//
		// if (e != null && e.getMessage() != null
		// && (e.getMessage().toUpperCase().contains("CONNECTION") ||
		// e.getMessage().toUpperCase().contains("TIMED OUT")))
		// {
		// local_api_response.setAction_ind("C"); // Connection Issue
		// }
		// else
		// {
		// local_api_response.setAction_ind("R"); // Rejected
		// }
		// }
		// }
		//
		// return local_api_response;
	}

	public void ammend_remittance()
	{

	}

	public void get_service_availability()
	{

	}

	public static void main(String[] args)
	{
	}
}
