package com.amx.service_provider.manger;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.Quotation_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.service_provider.api_gates.common.Common_API_Utils;
import com.amx.service_provider.api_gates.homesend.HomesendGate;
import com.amx.service_provider.api_gates.vintaja.VintajaGate;
import com.amx.service_provider.dbmodel.webservice.ExOwsLoginCredentials;
import com.amx.service_provider.repository.webservice.ExOwsLoginCredentialsRepository;
import com.amx.service_provider.repository.webservice.OwsParamRespcodeRepository;
import com.amx.service_provider.repository.webservice.OwsTransferLogRepository;
import com.amx.service_provider.utils.MSServiceProviderConfig;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServiceProviderManger implements IServiceProvider
{
	// Fetching access details
	@Autowired
	ExOwsLoginCredentialsRepository exOwsLoginCredentialsRepository;

	@Autowired
	private OwsParamRespcodeRepository owsParamRespcodeRepository;

	@Autowired
	private OwsTransferLogRepository owsTransferLogRep;
	
	@Autowired
	MSServiceProviderConfig msServiceProviderConfig;


	private final String SEND_TXN_METHOD_IND = new String("2"), VALIDATE_SEND_TXN_INPUTS_METHOD_IND = new String("13"),
			GET_REMITTANCE_DETAILS_METHOD_IND = new String("12"), STATUS_INQ_METHOD_IND = new String("3");

	public ServiceProviderResponse getQutation(ServiceProviderCallRequestDto quatationRequestDto)
	{
		Quotation_Call_Response response = new Quotation_Call_Response();

		// TODO: Form the input object from the given IDs
		// As advise by the team, no need to set all the data in each one of the three
		// objects. Instead, we can pass all the customer id and bene id to get the
		// remaining details.
		// If that happens, then we need to get the remaining details and form the
		// objects to be used later to call the services

		// Initial validation to validate the key fields require to identify the other
		// validation rules

		TransactionData txn_data = quatationRequestDto.getTransactionDto();
		Customer customer_data = quatationRequestDto.getCustomerDto();
		Benificiary bene_data = quatationRequestDto.getBeneficiaryDto();

		HashMap<String, String> validate_inputs_result = validate_initial_inputs(
				txn_data.getApplication_country_3_digit_ISO(),
				txn_data.getDestination_country_3_digit_ISO(),
				txn_data.getRoutting_bank_code(),
				txn_data.getDestination_currency(),
				txn_data.getRemittance_mode(),
				txn_data.getDelivery_mode());

		if (validate_inputs_result.isEmpty() == true) // No validation issues in initial stage
		{
			// Kept for future use, no validation applied at this time since all validation
			// applied in HomesendService class
			validate_inputs_result = validate_get_qutation_inputs(txn_data, customer_data, bene_data);

			if (validate_inputs_result.isEmpty() == true) // No validation issues in getQutation inputs
			{
				com.amx.service_provider.dbmodel.webservice.ExOwsLoginCredentials owsLoginCredentialsObject = exOwsLoginCredentialsRepository
						.findByApplicationCountryAndBankCode(txn_data.getApplication_country_3_digit_ISO(),
								txn_data.getRoutting_bank_code());

				// Selecting which service to call based on the routing bank code
				if (txn_data.getRoutting_bank_code().equals("HOME")) // HomeSend
				{
					HomesendGate homesend_service = new HomesendGate(owsLoginCredentialsObject, owsParamRespcodeRepository);

					// Calling the quotation service
					response = homesend_service.getQuotation(txn_data.getSettlement_amount(),
							txn_data.getSettlement_currency(),
							txn_data.getDestination_amount(),
							txn_data.getDestination_currency(),
							txn_data.getDestination_country_2_digit_ISO(),
							txn_data.getDestination_country_3_digit_ISO(),
							txn_data.getOrigin_country_3_digit_ISO(),
							customer_data.getCustomer_reference(),
							customer_data.getCustomer_type(),
							bene_data.getBeneficiary_reference(),
							bene_data.getBeneficiary_type(),
							bene_data.getFull_name(),
							bene_data.getBeneficiary_account_number(),
							bene_data.getBic_indicator(),
							bene_data.getBeneficiary_bank_code(),
							bene_data.getBeneficiary_branch_code(),
							bene_data.getBeneficiary_bank_branch_swift_code(),
							bene_data.getWallet_service_provider(),
							txn_data.getRequest_sequence_id(),
							txn_data.getRemittance_mode(),
							txn_data.getDelivery_mode());

				}
				else if (txn_data.getRoutting_bank_code().equals("WU")) // Western Union
				{

				} // TODO: Add the remaining service provider
			}
			else
			{
				// TODO: getQutation inputs validation fails
			}
		}
		else
		{
			// TODO: initial validation fails
			// TODO: Add the null check for the 3 boxes as well
		}

		return response;
	}

	public ServiceProviderResponse validateRemittanceInputs(
			ServiceProviderCallRequestDto validateRemittanceInputsRequestDto)
	{
		ServiceProviderResponse response = new ServiceProviderResponse();

		TransactionData txn_data = validateRemittanceInputsRequestDto.getTransactionDto();
		Customer customer_data = validateRemittanceInputsRequestDto.getCustomerDto();
		Benificiary bene_data = validateRemittanceInputsRequestDto.getBeneficiaryDto();

		// Initial validation to validate the key fields require to identify the other
		// validation rules
		HashMap<String, String> validate_inputs_result =
				Common_API_Utils.validate_initial_inputs(txn_data.getApplication_country_3_digit_ISO(),
						txn_data.getDestination_country_3_digit_ISO(),
						txn_data.getRoutting_bank_code(),
						txn_data.getDestination_currency(),
						txn_data.getRemittance_mode(),
						txn_data.getDelivery_mode());

		if (validate_inputs_result.isEmpty() == true) // No validation issues in initial stage
		{
			validate_inputs_result =
					Common_API_Utils.validate_send_remittance_inputs(txn_data, customer_data, bene_data);

			if (validate_inputs_result.isEmpty() == true) // No validation issues in sendRemittance inputs
			{
				// Selecting which service to call based on the routing bank code
				if (txn_data.getRoutting_bank_code().equals("HOME")) // HomeSend
				{
				}
				else if (txn_data.getRoutting_bank_code().equals("VINTJA"))
				{
					response =
							new VintajaGate(exOwsLoginCredentialsRepository, owsParamRespcodeRepository,
									owsTransferLogRep).send_api_call(txn_data,
											customer_data,
											bene_data,
											VALIDATE_SEND_TXN_INPUTS_METHOD_IND);
				}
				else if (txn_data.getRoutting_bank_code().equals("WU")) // Western Union
				{

				} // TODO: Add the remaining service provider
				else
				{
					// This service is not available for the given service provider
					response.setAction_ind("T");
					response.setResponse_description(
							"This service (sendRemittance) is not available for the given service provider " +
									txn_data.getRoutting_bank_code());
				}
			}
			else
			{
				// sendRemittance inputs validation fails
				response.setAction_ind("R");
				response.setResponse_description("Missing Data: " + validate_inputs_result.toString());
			}
		}
		else
		{
			// Initial validation fails
			response.setAction_ind("R");
			response.setResponse_description("Missing Data: " + validate_inputs_result.toString());
		}

		return response;

	}

	public ServiceProviderResponse sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto)
	{
		ServiceProviderResponse response = new ServiceProviderResponse();
		// TODO: Validate the input for before calling the service

		TransactionData txn_data = sendRemittanceRequestDto.getTransactionDto();
		Customer customer_data = sendRemittanceRequestDto.getCustomerDto();
		Benificiary bene_data = sendRemittanceRequestDto.getBeneficiaryDto();

		// Initial validation to validate the key fields require to identify the other
		// validation rules
		HashMap<String, String> validate_inputs_result =
				Common_API_Utils.validate_initial_inputs(txn_data.getApplication_country_3_digit_ISO(),
						txn_data.getDestination_country_3_digit_ISO(),
						txn_data.getRoutting_bank_code(),
						txn_data.getDestination_currency(),
						txn_data.getRemittance_mode(),
						txn_data.getDelivery_mode());

		if (validate_inputs_result.isEmpty() == true) // No validation issues in initial stage
		{
			validate_inputs_result =
					Common_API_Utils.validate_send_remittance_inputs(txn_data, customer_data, bene_data);

			if (validate_inputs_result.isEmpty() == true) // No validation issues in sendRemittance inputs
			{
				ExOwsLoginCredentials owsLoginCredentialsObject =
						exOwsLoginCredentialsRepository.findByApplicationCountryAndBankCode(
								txn_data.getApplication_country_3_digit_ISO(),
								txn_data.getRoutting_bank_code());

				// Selecting which service to call based on the routing bank code
				if (txn_data.getRoutting_bank_code().equals("HOME")) // HomeSend
				{
					HomesendGate homesend_service = new HomesendGate(owsLoginCredentialsObject, owsParamRespcodeRepository);

					// Calling the quotation service
					response = homesend_service.getQuotation(txn_data.getSettlement_amount(),
							txn_data.getSettlement_currency(),
							txn_data.getDestination_amount(),
							txn_data.getDestination_currency(),
							txn_data.getDestination_country_2_digit_ISO(),
							txn_data.getDestination_country_3_digit_ISO(),
							txn_data.getOrigin_country_3_digit_ISO(),
							customer_data.getCustomer_reference(),
							customer_data.getCustomer_type(),
							bene_data.getBeneficiary_reference(),
							bene_data.getBeneficiary_type(),
							bene_data.getFull_name(),
							bene_data.getBeneficiary_account_number(),
							bene_data.getBic_indicator(),
							bene_data.getBeneficiary_bank_code(),
							bene_data.getBeneficiary_branch_code(),
							bene_data.getBeneficiary_bank_branch_swift_code(),
							bene_data.getWallet_service_provider(),
							txn_data.getRequest_sequence_id(),
							txn_data.getRemittance_mode(),
							txn_data.getDelivery_mode());

				}
				else if (txn_data.getRoutting_bank_code().equals("VINTJA"))
				{
					response =
							new VintajaGate(owsLoginCredentialsObject, owsParamRespcodeRepository,
									owsTransferLogRep)
											.send_api_call(txn_data, customer_data, bene_data, SEND_TXN_METHOD_IND);
				}
				else if (txn_data.getRoutting_bank_code().equals("WU")) // Western Union
				{

				} // TODO: Add the remaining service provider
				else
				{
					// This service is not available for the given service provider
					response.setAction_ind("T");
					response.setResponse_description(
							"This service (sendRemittance) is not available for the given service provider " +
									txn_data.getRoutting_bank_code());
				}
			}
			else
			{
				// sendRemittance inputs validation fails
				response.setAction_ind("R");
				response.setResponse_description("Missing Data: " + validate_inputs_result.toString());
			}
		}
		else
		{
			// Initial validation fails
			response.setAction_ind("R");
			response.setResponse_description("Missing Data: " + validate_inputs_result.toString());
		}

		return response;

	}

	public ServiceProviderResponse getRemittanceDetails(ServiceProviderCallRequestDto getRemittanceDetailsRequestDto)
	{
		ServiceProviderResponse response = new ServiceProviderResponse();

		TransactionData txn_data = getRemittanceDetailsRequestDto.getTransactionDto();
		Customer customer_data = getRemittanceDetailsRequestDto.getCustomerDto();
		Benificiary bene_data = getRemittanceDetailsRequestDto.getBeneficiaryDto();

		// Initial validation to validate the key fields require to identify the other
		// validation rules
		HashMap<String, String> validate_inputs_result =
				Common_API_Utils.validate_initial_inputs(txn_data.getApplication_country_3_digit_ISO(),
						txn_data.getDestination_country_3_digit_ISO(),
						txn_data.getRoutting_bank_code(),
						txn_data.getDestination_currency(),
						txn_data.getRemittance_mode(),
						txn_data.getDelivery_mode());

		if (validate_inputs_result.isEmpty() == true) // No validation issues in initial stage
		{
			validate_inputs_result =
					Common_API_Utils.validate_get_remittance_details_inputs(txn_data, customer_data, bene_data);

			if (validate_inputs_result.isEmpty() == true) // No validation issues in sendRemittance inputs
			{
				// Selecting which service to call based on the routing bank code
				if (txn_data.getRoutting_bank_code().equals("VINTJA"))
				{
					response =
							new VintajaGate(exOwsLoginCredentialsRepository, owsParamRespcodeRepository,
									owsTransferLogRep).send_api_call(txn_data,
											customer_data,
											bene_data,
											GET_REMITTANCE_DETAILS_METHOD_IND);
				}
				else if (txn_data.getRoutting_bank_code().equals("WU")) // Western Union
				{

				} // TODO: Add the remaining service provider
				else
				{
					// This service is not available for the given service provider
					response.setAction_ind("T");
					response.setResponse_description(
							"This service (sendRemittance) is not available for the given service provider " +
									txn_data.getRoutting_bank_code());
				}
			}
			else
			{
				// sendRemittance inputs validation fails
				response.setAction_ind("R");
				response.setResponse_description("Missing Data: " + validate_inputs_result.toString());
			}
		}
		else
		{
			// Initial validation fails
			response.setAction_ind("R");
			response.setResponse_description("Missing Data: " + validate_inputs_result.toString());
		}

		return response;
	}

	public ServiceProviderResponse getRemittanceStatus(ServiceProviderCallRequestDto getRemittanceStatusRequestDto)
	{
		ServiceProviderResponse response = new ServiceProviderResponse();

		TransactionData txn_data = getRemittanceStatusRequestDto.getTransactionDto();
		Customer customer_data = getRemittanceStatusRequestDto.getCustomerDto();
		Benificiary bene_data = getRemittanceStatusRequestDto.getBeneficiaryDto();

		// Initial validation to validate the key fields require to identify the other
		// validation rules
		HashMap<String, String> validate_inputs_result =
				Common_API_Utils.validate_initial_inputs(txn_data.getApplication_country_3_digit_ISO(),
						txn_data.getDestination_country_3_digit_ISO(),
						txn_data.getRoutting_bank_code(),
						txn_data.getDestination_currency(),
						txn_data.getRemittance_mode(),
						txn_data.getDelivery_mode());

		if (validate_inputs_result.isEmpty() == true) // No validation issues in initial stage
		{
			validate_inputs_result =
					Common_API_Utils.validate_get_remittance_status_inputs(txn_data, customer_data, bene_data);

			if (validate_inputs_result.isEmpty() == true) // No validation issues in sendRemittance inputs
			{
				// Selecting which service to call based on the routing bank code
				if (txn_data.getRoutting_bank_code().equals("VINTJA"))
				{
					response =
							new VintajaGate(exOwsLoginCredentialsRepository, owsParamRespcodeRepository,
									owsTransferLogRep)
											.send_api_call(txn_data, customer_data, bene_data, STATUS_INQ_METHOD_IND);
				}
				else if (txn_data.getRoutting_bank_code().equals("WU")) // Western Union
				{

				} // TODO: Add the remaining service provider
				else
				{
					// This service is not available for the given service provider
					response.setAction_ind("T");
					response.setResponse_description(
							"This service (sendRemittance) is not available for the given service provider " +
									txn_data.getRoutting_bank_code());
				}
			}
			else
			{
				// sendRemittance inputs validation fails
				response.setAction_ind("R");
				response.setResponse_description("Missing Data: " + validate_inputs_result.toString());
			}
		}
		else
		{
			// Initial validation fails
			response.setAction_ind("R");
			response.setResponse_description("Missing Data: " + validate_inputs_result.toString());
		}

		return response;
	}
	
	private HashMap<String, String> validate_get_qutation_inputs(TransactionData txn_data, Customer customer_data,
			Benificiary bene_data)
	{

		HashMap<String, String> validation_result = new HashMap<String, String>();
		// TODO Auto-generated method stub
		return validation_result;
	}
	
	private HashMap<String, String> validate_initial_inputs(String application_country_3_digit_ISO,
			String destination_country_3_digit_ISO, String routting_bank_code, String destination_currency,
			String remittance_mode, String delivery_mode)
	{
		HashMap<String, String> validation_result = new HashMap<String, String>();

		if (StringUtils.isEmpty(application_country_3_digit_ISO) == true)
			validation_result.put("application_country_3_digit_ISO", "Application country ISO is empty");

		if (StringUtils.isEmpty(destination_country_3_digit_ISO) == true)
			validation_result.put("destination_country_3_digit_ISO", "Destination country ISO is empty");

		if (StringUtils.isEmpty(routting_bank_code) == true)
			validation_result.put("routting_bank_code", "Routting bank code is empty");

		if (StringUtils.isEmpty(destination_currency) == true)
			validation_result.put("destination_currency", "Destination currency is empty");

		if (StringUtils.isEmpty(remittance_mode) == true)
			validation_result.put("remittance_mode", "remittance_mode is empty");

		if (StringUtils.isEmpty(delivery_mode) == true)
			validation_result.put("delivery_mode", "Delivery mode is empty");

		return validation_result;
	}

}
