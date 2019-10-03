package com.amx.jax.serviceprovider;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApplicationProcedureParam;
import com.amx.jax.AppContextUtil;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.repository.remittance.DeliveryModeRepository;
import com.amx.jax.repository.remittance.RemittanceModeMasterRepository;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.services.BankService;

@Component
public class ServiceProviderTransactionDataManager {

	@Autowired
	CountryService countryService;
	@Autowired
	CurrencyMasterService currencyMasterService;
	@Autowired
	RemittanceModeMasterRepository remittanceModeMasterRepository;
	@Autowired
	DeliveryModeRepository deliveryModeRepository;
	@Autowired
	protected BankService bankService;

	public void setTransactionDtoDbValues(Map<String, Object> remitApplParametersMap, ServiceProviderCallRequestDto serviceProviderCallRequestDto) {
		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		BigDecimal beneCountryId = (BigDecimal) remitApplParametersMap.get("P_BENE_BANK_COUNTRY_ID");
		if (beneCountryId == null) {
			beneCountryId = (BigDecimal) remitApplParametersMap.get("P_BENEFICIARY_COUNTRY_ID");
		}

		String routingBankCode = bankService.getBankById(routingBankId).getBankCode();
		TransactionData transactionDto = serviceProviderCallRequestDto.getTransactionDto();
		String appCountryIsoCode = countryService.getCountryMaster(applicationCountryId).getCountryAlpha3Code();
		String appCountryIsoCode2 = countryService.getCountryMaster(applicationCountryId).getCountryAlpha2Code();
		String beneCountryIsoCode = countryService.getCountryMaster(beneCountryId).getCountryIsoCode();
		String fcCurrencyQuote = currencyMasterService.getCurrencyMasterById(foreignCurrencyId).getQuoteName();
		transactionDto.setRemittance_mode(remittanceModeMasterRepository.findOne(remittanceModeId).getRemittance());
		transactionDto.setDelivery_mode(deliveryModeRepository.findOne(deliveryModeId).getDeliveryMode());
		transactionDto.setApplication_country_3_digit_ISO(appCountryIsoCode);
		transactionDto.setApplication_country_2_digit_ISO(appCountryIsoCode2);
		transactionDto.setDestination_country_3_digit_ISO(beneCountryIsoCode);
		transactionDto.setRoutting_bank_code(routingBankCode);
		transactionDto.setDestination_currency(fcCurrencyQuote);
		transactionDto.setFlexi_field_1("0"); // 0 means not applicable
		transactionDto.setOut_going_transaction_reference(getOutGoingTransactionReference());
		BigDecimal destinationAmount = ApplicationProcedureParam.P_CALCULATED_FC_AMOUNT.getValue(remitApplParametersMap);
		transactionDto.setDestination_amount(destinationAmount);

	}

	private String getOutGoingTransactionReference() {
		return AppContextUtil.getTraceId();
	}

}
