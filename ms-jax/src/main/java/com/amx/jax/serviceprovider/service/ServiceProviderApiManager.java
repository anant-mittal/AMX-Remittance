package com.amx.jax.serviceprovider.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.ServiceProviderClientWrapper;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.Validate_Remittance_Inputs_Call_Response;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.serviceprovider.ServiceProviderBeneDataManager;
import com.amx.jax.serviceprovider.ServiceProviderCustomerDataManager;
import com.amx.jax.serviceprovider.ServiceProviderTransactionDataManager;
import com.amx.jax.services.BankService;

@Component
public class ServiceProviderApiManager {

	@Autowired
	protected ServiceProviderBeneDataManager serviceProviderBeneDataManager;
	@Autowired
	protected ServiceProviderCustomerDataManager serviceProviderCustomerDataManager;
	@Autowired
	protected ServiceProviderTransactionDataManager serviceProviderTransactionDataManager;
	@Autowired
	protected BankService bankService;
	@Autowired
	protected ServiceProviderClientWrapper serviceProviderClientWrapper;
	@Autowired
	CountryService countryService;
	@Autowired
	CurrencyMasterService currencyMasterService;

	protected ServiceProviderCallRequestDto createValidateInputRequest(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = new ServiceProviderCallRequestDto();

		serviceProviderBeneDataManager.setBeneficiaryDtoDbValues(remittanceAdditionalBeneFieldModel, remitApplParametersMap,
				serviceProviderCallRequestDto);
		serviceProviderCustomerDataManager.setCustomerDtoDbValues(remittanceAdditionalBeneFieldModel, remitApplParametersMap,
				serviceProviderCallRequestDto);
		serviceProviderTransactionDataManager.setTransactionDtoDbValues(remittanceAdditionalBeneFieldModel, remitApplParametersMap,
				serviceProviderCallRequestDto);
		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		BigDecimal beneCountryId = (BigDecimal) remitApplParametersMap.get("P_BENEFICIARY_COUNTRY_ID");

		String routingBankCode = bankService.getBankById(routingBankId).getBankCode();
		TransactionData transactionDto = serviceProviderCallRequestDto.getTransactionDto();
		String appCountryIsoCode = countryService.getCountryMaster(applicationCountryId).getCountryAlpha3Code();
		String appCountryIsoCode2 = countryService.getCountryMaster(applicationCountryId).getCountryAlpha2Code();
		String beneCountryIsoCode = countryService.getCountryMaster(beneCountryId).getCountryIsoCode();
		String fcCurrencyQuote = currencyMasterService.getCurrencyMasterById(foreignCurrencyId).getQuoteName();
		transactionDto.setRemittance_mode(remittanceModeId.toString());
		transactionDto.setDelivery_mode(deliveryModeId.toString());
		transactionDto.setApplication_country_3_digit_ISO(appCountryIsoCode);
		transactionDto.setApplication_country_2_digit_ISO(appCountryIsoCode2);
		transactionDto.setDestination_country_3_digit_ISO(beneCountryIsoCode);
		transactionDto.setRoutting_bank_code(routingBankCode);
		transactionDto.setDestination_currency(fcCurrencyQuote);
		return serviceProviderCallRequestDto;
	}

	public Validate_Remittance_Inputs_Call_Response validateApiInput(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {
		ServiceProviderCallRequestDto serviceProviderCallRequestDto = createValidateInputRequest(remittanceAdditionalBeneFieldModel,
				remitApplParametersMap);
		AmxApiResponse<Validate_Remittance_Inputs_Call_Response, Object> response = serviceProviderClientWrapper
				.validateRemittanceInputs(serviceProviderCallRequestDto);
		return response.getResult();
	}

}
