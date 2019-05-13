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
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.service_provider.dbmodel.webservice.ExOwsLoginCredentials;
import com.amx.service_provider.homesend.HomesendGate;
import com.amx.service_provider.repository.webservice.ExOwsLoginCredentialsRepository;
import com.amx.service_provider.repository.webservice.OwsParamRespcodeRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
// @EntityScan("com.amx.jax.dbmodel.webservice")
// @EnableJpaRepositories("com.amx.jax.dbmodel.webservice")
// @ComponentScan(basePackages = { "com.amx.jax.dbmodel.webservice" })
public class ServiceProviderManger implements IServiceProvider
{
	// Fetching access details
	@Autowired
	com.amx.service_provider.repository.webservice.ExOwsLoginCredentialsRepository exOwsLoginCredentialsRepository;
	
	@Autowired
	private OwsParamRespcodeRepository owsParamRespcodeRepository;

	public ServiceProviderResponse getQutation(ServiceProviderCallRequestDto quatationRequestDto)
	{
		ServiceProviderResponse response = new ServiceProviderResponse();

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
					// Service Initialization
					HomesendGate homesend_service = new HomesendGate(
							owsLoginCredentialsObject.getWsUserName()/* api_login */,
							owsLoginCredentialsObject.getWsPassword()/* api_password */,
							owsLoginCredentialsObject.getWsAgentId()/* vendor_id */,
							owsLoginCredentialsObject.getFlexiField1()/* api_url */,
							owsParamRespcodeRepository);

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

	public ServiceProviderResponse sendRemittance(ServiceProviderCallRequestDto sendRemittanceRequestDto)
	{
		ServiceProviderResponse response = new ServiceProviderResponse();
		// TODO: Validate the input for before calling the service

		TransactionData txn_data = sendRemittanceRequestDto.getTransactionDto();
		Customer customer_data = sendRemittanceRequestDto.getCustomerDto();
		Benificiary bene_data = sendRemittanceRequestDto.getBeneficiaryDto();

		// Initial validation to validate the key fields require to identify the other
		// validation rules
		HashMap<String, String> validate_inputs_result = validate_initial_inputs(
				txn_data.getApplication_country_3_digit_ISO(),
				txn_data.getDestination_country_3_digit_ISO(),
				txn_data.getRoutting_bank_code(),
				txn_data.getDestination_currency(),
				txn_data.getRemittance_mode(),
				txn_data.getDelivery_mode());

		if (validate_inputs_result.isEmpty() == true) // No validation issues in initial stage
		{
			validate_inputs_result = validate_send_remittance_inputs(txn_data, customer_data, bene_data);

			if (validate_inputs_result.isEmpty() == true) // No validation issues in getQutation inputs
			{
				// TODO: Need to form a response object

				ExOwsLoginCredentials owsLoginCredentialsObject = exOwsLoginCredentialsRepository
						.findByApplicationCountryAndBankCode(txn_data.getApplication_country_3_digit_ISO(),
								txn_data.getRoutting_bank_code());

				// Selecting which service to call based on the routing bank code
				if (txn_data.getRoutting_bank_code().equals("HOME")) // HomeSend
				{
					// Service Initialization
					HomesendGate homesend_service = new HomesendGate(
							owsLoginCredentialsObject.getWsUserName()/* api_login */,
							owsLoginCredentialsObject.getWsPassword()/* api_password */,
							owsLoginCredentialsObject.getWsAgentId()/* vendor_id */,
							owsLoginCredentialsObject.getFlexiField1()/* api_url */,
							owsParamRespcodeRepository);

					// Calling the send remittance service
					response = homesend_service.send_remittance(txn_data, customer_data, bene_data);
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

	private HashMap<String, String> validate_send_remittance_inputs(TransactionData txn_data, Customer customer_data,
			Benificiary bene_data)
	{

		HashMap<String, String> validation_result = new HashMap<String, String>();
		// TODO Auto-generated method stub
		return validation_result;
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
