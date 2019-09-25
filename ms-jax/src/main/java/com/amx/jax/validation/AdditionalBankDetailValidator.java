package com.amx.jax.validation;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.serviceprovider.ServiceProviderClientWrapper;
import com.amx.jax.manager.remittance.ServiceProviderFlexField;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.serviceprovider.ServiceProviderBeneDataManager;
import com.amx.jax.serviceprovider.ServiceProviderCustomerDataManager;
import com.amx.jax.serviceprovider.ServiceProviderTransactionDataManager;
import com.amx.jax.services.BankService;

@Component
public class AdditionalBankDetailValidator {

	@Autowired
	ServiceProviderClientWrapper serviceProviderClientWrapper;
	@Autowired
	ServiceProviderBeneDataManager serviceProviderBeneDataManager;
	@Autowired
	ServiceProviderCustomerDataManager serviceProviderCustomerDataManager;
	@Autowired
	ServiceProviderTransactionDataManager serviceProviderTransactionDataManager;
	@Autowired
	BankService bankService;

	public void validateAdditionalBankFields(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap) {

		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		String bankCode = bankService.getBankById(routingBankId).getBankCode();
		// TODO find if routing bank addl flex field validation is needed or not based

		if ("VENTAJ".equals(bankCode)) {
			// on bankid
			ServiceProviderCallRequestDto serviceProviderCallRequestDto = new ServiceProviderCallRequestDto();
			ServiceProviderFlexField[] allFlexFields = ServiceProviderFlexField.values();
			for (ServiceProviderFlexField flexField : allFlexFields) {
				flexField.setValue(serviceProviderCallRequestDto, request.getFlexFieldDtoMap());
			}
			serviceProviderBeneDataManager.setBeneficiaryDtoDbValues(request, remitApplParametersMap, serviceProviderCallRequestDto);
			serviceProviderCustomerDataManager.setCustomerDtoDbValues(request, remitApplParametersMap, serviceProviderCallRequestDto);
			serviceProviderTransactionDataManager.setTransactionDtoDbValues(request, remitApplParametersMap, serviceProviderCallRequestDto);
			serviceProviderClientWrapper.validateRemittanceInputs(serviceProviderCallRequestDto);
		}

	}
}
