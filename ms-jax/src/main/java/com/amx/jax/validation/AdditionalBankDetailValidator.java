package com.amx.jax.validation;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.serviceprovider.service.ServiceProviderApiManager;
import com.amx.jax.services.BankService;

@Component
public class AdditionalBankDetailValidator {

	@Autowired
	BankService bankService;
	@Autowired
	ApplicationContext appContext;

	public void validateAdditionalBankFields(RemittanceAdditionalBeneFieldModel remittanceAdditionalBeneFieldModel,
			Map<String, Object> remitApplParametersMap) {

		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		String bankCode = bankService.getBankById(routingBankId).getBankCode();
		// TODO find if routing bank addl flex field validation is needed or not based
		try {
			ServiceProviderApiManager serviceProviderApiManager = (ServiceProviderApiManager) appContext.getBean(bankCode);
			serviceProviderApiManager.validateApiInput(remittanceAdditionalBeneFieldModel, remitApplParametersMap);
		} catch (NoSuchBeanDefinitionException ex) {
			
		}

	}
}
