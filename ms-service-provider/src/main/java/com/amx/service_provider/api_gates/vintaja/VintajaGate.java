package com.amx.service_provider.api_gates.vintaja;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.Cancellation_Call_Response;
import com.amx.jax.model.response.serviceprovider.Get_Rmittance_Details_Call_Response;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.model.response.serviceprovider.Status_Call_Response;
import com.amx.jax.model.response.serviceprovider.Validate_Remittance_Inputs_Call_Response;
import com.amx.service_provider.api_gates.common.Common_API_Utils;
import com.amx.service_provider.api_gates.common.TimeOutResponse;
import com.amx.service_provider.dbmodel.webservice.ExOwsLoginCredentials;
import com.amx.service_provider.repository.webservice.OwsParamRespcodeRepository;
import com.amx.service_provider.repository.webservice.OwsTransferLogRepository;

import io.netty.handler.timeout.TimeoutException;

public class VintajaGate
{
	private final String SEND_TXN_METHOD_IND = new String("2"), VALIDATE_SEND_TXN_INPUTS_METHOD_IND = new String("13"),
			GET_REMITTANCE_DETAILS_METHOD_IND = new String("12"), STATUS_INQ_METHOD_IND = new String("3"),
			CANCEL_TXN_METHOD_IND = new String("7");
	Logger logger = Logger.getLogger("VintajaGate.class");

	// Direct DB object
	private ExOwsLoginCredentials owsLoginCredentialsObject;

	// Repo
	private OwsParamRespcodeRepository owsParamRespcodeRepository;
	private OwsTransferLogRepository owsTransferLogRep;

	public VintajaGate(ExOwsLoginCredentials owsLoginCredentialsObject,
			OwsParamRespcodeRepository owsParamRespcodeRepository, OwsTransferLogRepository owsTransferLogRep)
	{
		this.owsLoginCredentialsObject = owsLoginCredentialsObject;
		this.owsParamRespcodeRepository = owsParamRespcodeRepository;
		this.owsTransferLogRep = owsTransferLogRep;

		if (owsLoginCredentialsObject.getTruststore_path() != null)
		{
			System.setProperty("javax.net.ssl.trustStore", owsLoginCredentialsObject.getTruststore_path());
			System.setProperty("javax.net.ssl.trustStorePassword", owsLoginCredentialsObject.getTrusttore_pwd()); // changeit
		}

		// No need for key store setup at this level
	}

	public ServiceProviderResponse send_api_call(TransactionData txn_data, Customer customer_data,
			Benificiary bene_data, String ws_call_type)
	{
		ServiceProviderResponse response = null;
		ExecutorService executor = null;

		// Define the call type as a first step
		try
		{
			if (ws_call_type == null)
				throw new Exception("Web-Service method call is mandatory");

			if (ws_call_type.equals(SEND_TXN_METHOD_IND))
			{
				response = new Remittance_Call_Response();
				((Remittance_Call_Response) response)
						.setOut_going_transaction_reference(txn_data.getOut_going_transaction_reference());
			}
			else if (ws_call_type.equals(STATUS_INQ_METHOD_IND))
			{
				response = new Status_Call_Response();
				((Status_Call_Response) response)
						.setOut_going_transaction_reference(txn_data.getOut_going_transaction_reference());
			}
			else if (ws_call_type.equals(GET_REMITTANCE_DETAILS_METHOD_IND))
			{
				response = new Get_Rmittance_Details_Call_Response();
				((Get_Rmittance_Details_Call_Response) response).getTransactionDto()
						.setOut_going_transaction_reference(txn_data.getOut_going_transaction_reference());
			}
			else if (ws_call_type.equals(VALIDATE_SEND_TXN_INPUTS_METHOD_IND))
			{
				response = new Validate_Remittance_Inputs_Call_Response();
			}
			else if (ws_call_type.equals(CANCEL_TXN_METHOD_IND))
			{
				response = new Cancellation_Call_Response();
			}
			else
				throw new Exception("Given Web-Service method call is undefined. " + ws_call_type);
		}
		catch (Exception e)
		{
			// Undefined method call
			response = new ServiceProviderResponse();

			response.setAction_ind("T");
			response.setResponse_code("TECH-ERROR");
			response.setResponse_description(e.getMessage());

			return response;
		}

		try
		{
			if (ws_call_type.equals(VALIDATE_SEND_TXN_INPUTS_METHOD_IND) || ws_call_type.equals(SEND_TXN_METHOD_IND))
			{
				GovermantPaymentServices target_payment_service =
						VintajaUtils.get_goverment_payment_service(txn_data.getRemittance_mode(),
								txn_data.getDelivery_mode());

				if (
					target_payment_service == GovermantPaymentServices.SSS_CONTRIBUTION_EXISTING_PRN ||
							target_payment_service == GovermantPaymentServices.RESERVATION_PAYMENT
				)
				{
					Get_Rmittance_Details_Call_Response get_txn_detalis_response =
							(Get_Rmittance_Details_Call_Response) send_api_call(txn_data,
									customer_data,
									bene_data,
									GET_REMITTANCE_DETAILS_METHOD_IND);

					// Apply special validation for the service Contribution Existing PRN Code 154
					// This line will throw an exception if the validation fails with proper
					// response details
					VintajaUtils.special_service_validation(get_txn_detalis_response,
							response,
							txn_data,
							customer_data,
							bene_data);

					if (response.getAction_ind() != null && !response.getAction_ind().equals("I"))
					{
						// Do not continue if validation process cause a rejection
						return response;
					}
				}
			}

			// Form the input object as per service provider requirements. pass the three
			// object
			String api_input =
					VintajaUtils.form_api_call_input(txn_data,
							customer_data,
							bene_data,
							owsLoginCredentialsObject,
							ws_call_type);

			// XML tag is added so the string can be stored as XML data in DB
			response.setRequest_XML("<Request>" + api_input + "</Request>");

			try
			{
				String signed_payload = Common_API_Utils.sign_and_encode_payload(api_input, owsLoginCredentialsObject);
				
				// new functionality - timer logic
				String response_string = null;
				Boolean timerStatus = Boolean.FALSE;
				String timerException = null;
				try {
					executor = Executors.newSingleThreadExecutor();
					HashMap<String, Object> mapRequest = new HashMap<String, Object>();
					mapRequest.put("api_input", api_input);
					mapRequest.put("signed_payload", signed_payload);
					mapRequest.put("ws_call_type", ws_call_type);
					mapRequest.put("owsLoginCredentialsObject", owsLoginCredentialsObject);
					mapRequest.put("RoutingBankCode", txn_data.getRoutting_bank_code());
					Object responseObj = executor.submit(new TimeOutResponse(mapRequest)).get(60, TimeUnit.SECONDS);
					if(responseObj != null){
						response_string = (String)responseObj;
					}
				}catch (TimeoutException exception) {
					logger.info("response session time exception" + exception.getMessage());
					timerStatus = Boolean.TRUE;
					timerException = exception.getMessage();
				}catch (Exception exception) {
					logger.info("response session time exception" + exception.getMessage());
					timerStatus = Boolean.TRUE;
					timerException = exception.getMessage();
				}

				// Call external API
				// Passing response object to the call_send_remittance_api where it will get
				// updated there
				// old functionality
				//String response_string = VintajaUtils.call_vintaja_api(api_input, signed_payload, owsLoginCredentialsObject, ws_call_type);

				// TODO
				if(timerStatus) {
					System.out.println(timerException);

					response.setAction_ind("C");
					response.setResponse_code("CONERR");// CONERR -- connection error
					response.setResponse_description("Issue with connection. " + timerException);

					if (timerException != null && timerException.contains("Connection timed out") || timerException.contains("timeout") || timerException.contains("Bad Gateway"))
					{
						response.setResponse_code("TIMOUT");
						response.setResponse_description("Connection Timed Out. " + timerException);
					}

					throw new Exception(response.getResponse_description());
				}else {
					if(response_string != null){
						System.out.println(
								"--------------------------------- JSON Output -----------------------------------------");
						System.out.println(Common_API_Utils.get_formatted_jason_string(response_string)); // Print it with
						// specified
						// indentation
						System.out.println(
								"--------------------------------- JSON Output -----------------------------------------");

						// Setting response string
						response.setResponse_XML("<Response>" + response_string + "</Response>");

						VintajaUtils.parse_api_response(response_string, response, ws_call_type);
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(Common_API_Utils.getStackTrace(e));

				response.setAction_ind("C");
				response.setResponse_code("CONERR");// CONERR -- connection error
				response.setResponse_description("Issue with connection. " + e.getMessage());

				if (
					e.getMessage().contains("Connection timed out") ||
							e.getMessage().contains("timeout") ||
							e.getMessage().contains("Bad Gateway")
				)
				{
					response.setResponse_code("TIMOUT");
					response.setResponse_description("Connection Timed Out. " + e.getMessage());
				}

				throw new Exception(response.getResponse_description());
			}

			if (response.getResponse_code() == null || response.getResponse_code().length() == 0)
			{
				response.setAction_ind("U");
				response.setResponse_code("UNRECG");
				response.setResponse_description("No ErrorCode ");

				throw new Exception(response.getResponse_description());
			}

			response.setAction_ind(Common_API_Utils.getActionInd(txn_data.getRoutting_bank_code(),
					response.getResponse_code().trim(),
					ws_call_type,
					owsParamRespcodeRepository));

			if (response.getAction_ind() != null && response.getAction_ind().length() > 0)
			{
				if (ws_call_type.equals(SEND_TXN_METHOD_IND))
				{
					// If the transaction already available, we need to get its actual status
					// This is can be achieved by calling
					if (response.getAction_ind().equals("D"))
					{
						Status_Call_Response status_inquiry_response =
								(Status_Call_Response) send_api_call(txn_data,
										customer_data,
										bene_data,
										STATUS_INQ_METHOD_IND);

						response.setResponse_code(status_inquiry_response.getResponse_code());
						response.setResponse_description(status_inquiry_response.getResponse_description());

						((Remittance_Call_Response) response).setPartner_transaction_reference(
								status_inquiry_response.getPartner_transaction_reference());

						((Remittance_Call_Response) response).setBene_bank_remittance_reference(
								status_inquiry_response.getBene_bank_remittance_reference());

						response.setResponse_XML(status_inquiry_response.getResponse_XML());
					}
				}

				if (response.getResponse_description() == null) // If Partner API did not provide a description
				{
					response.setResponse_description(
							Common_API_Utils.getResponseDescription(txn_data.getRoutting_bank_code(),
									response.getResponse_code().trim(),
									ws_call_type,
									owsParamRespcodeRepository));
				}

				if (ws_call_type.equals(SEND_TXN_METHOD_IND))
				{
					// Inserts DB log only in case of transaction sending
					// Logs already taking care by UI client. But this is just in case
					Common_API_Utils.insert_api_call_log(txn_data, response, owsTransferLogRep);
				}
			}
			else
			{
				response.setAction_ind("U");
				response.setResponse_description("Action indicator not found for the given response_code: " +
						response.getResponse_code() +
						"\n Response Desc: " +
						response.getResponse_description());

				throw new Exception(response.getResponse_description());
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception occured while calling Web-Service (" +
					ws_call_type +
					"): " +
					Common_API_Utils.getStackTrace(e));

			if (response.getAction_ind() == null)
			{
				response.setAction_ind("U");
				response.setResponse_code("UNRECG");
				response.setResponse_description("Exception occured: " + e.getMessage());
			}
		}

		System.out.println(response.toString());
		return response;
	}

	public static void main(String[] args)
	{
	}
}