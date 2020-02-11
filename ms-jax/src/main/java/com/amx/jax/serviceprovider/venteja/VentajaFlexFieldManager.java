package com.amx.jax.serviceprovider.venteja;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.BankConstants;
import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.ViewParameterDetails;
import com.amx.jax.manager.remittance.AdditionalBankDetailManager;
import com.amx.jax.model.request.remittance.BenePackageRequest;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldValueDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;
import com.amx.jax.repository.remittance.DeliveryModeRepository;
import com.amx.jax.repository.remittance.RemittanceModeMasterRepository;
import com.amx.jax.serviceprovider.service.AbstractFlexFieldManager;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.validation.RemittanceTransactionRequestValidator;

@Component(BankConstants.VINTJA_BANK_CODE + AbstractFlexFieldManager.FLEX_FIELD_MANAGER_BEAN_SUFFIX)
public class VentajaFlexFieldManager extends AbstractFlexFieldManager {

	private static final Logger log = LoggerFactory.getLogger(VentajaFlexFieldManager.class);

	@Autowired
	RemittanceModeMasterRepository remittanceModeMasterRepository;
	@Autowired
	DeliveryModeRepository deliveryModeRepository;
	@Autowired
	RemittanceTransactionRequestValidator remittanceTransactionRequestValidator;
	@Autowired
	BankService bankService;
	@Autowired
	AdditionalBankDetailManager additionalBankDetailManager;
	@Autowired
	BeneficiaryService beneficiaryService;

	public static final String FC_AMOUNT_FLEX_FIELD_NAME = "PACKAGE_FCAMOUNT";
	public static final String INDIC_16_VOLUNTEERCONTRIBUTION_SYMBOL = ">";
	public static final List<String> GIFT_PACKAGE_INDICATORS = Arrays.asList("INDIC6");

	public Map<String, Object> managePreFlexFields(List<AdditionalDataDisplayView> additionalDataRequired,
			Map<String, FlexFieldDto> requestFlexFields, ViewParameterDetails cashSetUp, BenificiaryListView beneficaryDetails,
			List<JaxConditionalFieldDto> requiredFlexFields) {
		Map<String, Object> outputVariables = new HashMap<>();

		ParameterDetailsDto volunteerParameter = null;
		JaxConditionalFieldDto volunteerJaxConditionalFieldDto = null;
		String remittanceModeCode = cashSetUp.getCharField3();
		String deliveryModeCode = cashSetUp.getCharField4();
		BigDecimal remittanceModeId = remittanceModeMasterRepository.findByRemittance(remittanceModeCode).getRemittanceModeId();
		BigDecimal deliveryModeId = deliveryModeRepository.findByDeliveryMode(deliveryModeCode).getDeliveryModeId();
		BigDecimal applicationCountryId = beneficaryDetails.getApplicationCountryId();
		BigDecimal foreignCurrencyId = beneficaryDetails.getCurrencyId();
		BigDecimal routingBankId = beneficaryDetails.getServiceProvider();
		BankMasterMdlv1 routingBank = bankService.getBankById(routingBankId);
		BigDecimal routingCountryId = routingBank.getBankCountryId();
		boolean volunteerContributionIndic = false;
		List<ParameterDetailsDto> parameterSetUp = additionalBankDetailManager.fetchServiceProviderFcAmount(beneficaryDetails.getIdNo());

		for (AdditionalDataDisplayView flexField : additionalDataRequired) {
			FlexFieldDto flexFieldValueInRequest = requestFlexFields.get(flexField.getFlexField());
			JaxConditionalFieldDto jaxConditionalFieldDto = remittanceTransactionRequestValidator.getConditionalFieldDto(flexField, requestFlexFields,
					routingCountryId, remittanceModeId, deliveryModeId, foreignCurrencyId, routingBankId, null, true);
			sortPossibleValues(jaxConditionalFieldDto);
			if ("INDIC16".equals(flexField.getFlexField()) && flexFieldValueInRequest != null
					&& flexFieldValueInRequest.getAmieceCode().contains(INDIC_16_VOLUNTEERCONTRIBUTION_SYMBOL)) {
				volunteerContributionIndic = true;
				volunteerParameter = findVolunteerParam(parameterSetUp,
						flexFieldValueInRequest.getAmieceCode().replaceAll(INDIC_16_VOLUNTEERCONTRIBUTION_SYMBOL, ""));
			}

			if ("INDIC17".equals(flexField.getFlexField())) {
				volunteerJaxConditionalFieldDto = jaxConditionalFieldDto;
			}
			if (jaxConditionalFieldDto != null) {
				requiredFlexFields.add(jaxConditionalFieldDto);
			}
		}
		addMinMaxValueForVoluteerContri(volunteerJaxConditionalFieldDto, volunteerParameter);
		outputVariables.put("volunteerContributionIndic", volunteerContributionIndic);
		if (volunteerContributionIndic) {
			log.debug("volunteer Contribution Selected");
		} else {
			// remove volunteer Indic 17
			Iterator<JaxConditionalFieldDto> itr = requiredFlexFields.iterator();
			while (itr.hasNext()) {
				JaxConditionalFieldDto flexField = itr.next();
				if ("INDIC17".equals(flexField.getField().getName())) {
					itr.remove();
				}
			}
		}
		return outputVariables;
	}

	private void sortPossibleValues(JaxConditionalFieldDto jaxConditionalFieldDto) {
		List<JaxFieldValueDto> possibleValues = jaxConditionalFieldDto.getField().getPossibleValues();
		String fieldName = jaxConditionalFieldDto.getField().getName();
		if ("INDIC16".equals(fieldName) || "INDIC18".equals(fieldName)) {
			if (possibleValues != null) {
				// sorting possible values acc. to amiec code
				Collections.sort(possibleValues, (o1, o2) -> {
					FlexFieldDto dto1 = (FlexFieldDto) o1.getValue();
					FlexFieldDto dto2 = (FlexFieldDto) o2.getValue();
					log.info("dto1.getAmieceCode() : {}", dto1.getAmieceCode());
					log.info("dto2.getAmieceCode() : {}", dto2.getAmieceCode());
					BigDecimal amount1 = new BigDecimal(dto1.getAmieceCode().replace(INDIC_16_VOLUNTEERCONTRIBUTION_SYMBOL, ""));
					BigDecimal amount2 = new BigDecimal(dto2.getAmieceCode().replace(INDIC_16_VOLUNTEERCONTRIBUTION_SYMBOL, ""));
					return amount1.compareTo(amount2);
				});
			}
		}
	}

	private void addMinMaxValueForVoluteerContri(JaxConditionalFieldDto jaxConditionalFieldDto, ParameterDetailsDto volunteerParameter) {
		if (jaxConditionalFieldDto != null && volunteerParameter != null) {
			jaxConditionalFieldDto.getField().setMinValue(volunteerParameter.getNumericUdf3());
			jaxConditionalFieldDto.getField().setMaxValue(volunteerParameter.getNumericUdf4());
		}
	}

	private ParameterDetailsDto findVolunteerParam(List<ParameterDetailsDto> parameterSetUp, String amount) {
		List<ParameterDetailsDto> list = parameterSetUp.stream().filter(i -> {
			if (i.getAmount() != null && i.getMinAmount() != null && i.getMinAmount().doubleValue() > 0 && i.getMaxAmount() != null
					&& i.getMaxAmount().doubleValue() > 0) {
				if (i.getAmount().toString().equals(amount)) {
					return true;
				}
			}
			return false;
		}).collect(Collectors.toList());
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public void validatePreFlexField(Map<String, FlexFieldDto> requestFlexFields, Map<String, Object> preFlexValidationVariables,
			Map<String, Object> validationResults) {
		BenePackageRequest benePackageRequest = (BenePackageRequest) preFlexValidationVariables.get("benePackageRequest");
		Integer noOfMonth = null;
		BenificiaryListView beneListView = beneficiaryService.getBeneByIdNo(benePackageRequest.getBeneId());
		// phil health does not have end date so end date is same start date
		if ("PHHTH".equals(beneListView.getBankCode())) {
			noOfMonth = 1;
		}
		BigDecimal packageFcAmount = null;
		BigDecimal monthlyContribution = null;
		BigDecimal volunteerContribution = null;
		String packageSelectedAmiecCode = null;
		boolean dateRangePresent = false;
		BigDecimal fcAmountSelected = null;
		List<JaxConditionalFieldDto> requiredFlexFields = (List<JaxConditionalFieldDto>) validationResults.get("requiredFlexFields");
		if (requestFlexFields != null) {
			for (Entry<String, FlexFieldDto> element : requestFlexFields.entrySet()) {
				String k = element.getKey();
				FlexFieldDto v = element.getValue();
				if ("INDIC16".equals(k)) {
					monthlyContribution = new BigDecimal(v.getAmieceCode().replaceAll(">", ""));
				}
				if ("INDIC17".equals(k)) {
					volunteerContribution = new BigDecimal(v.getAmieceDescription().replaceAll(">", ""));
				}
				if ("INDIC18".equals(k)) {
					noOfMonth = Integer.parseInt(v.getAmieceCode().replaceAll(">", ""));
				}
				if ("INDIC12".equals(k) && v != null) {
					dateRangePresent = true;
				}
				if(FC_AMOUNT_FLEX_FIELD_NAME.equals(k)) {
					fcAmountSelected = new BigDecimal(v.getAmieceDescription());
				}
				// bpi
				if (GIFT_PACKAGE_INDICATORS.contains(k)) {
					packageSelectedAmiecCode = v.getAmieceCode();
					packageFcAmount = new BigDecimal(v.getAmieceCode().replaceAll(">", ""));
				}
			}
		}
		// in case of pagbig saving
		if (noOfMonth != null && fcAmountSelected != null) {
			monthlyContribution = fcAmountSelected;
		}
		Object volunteerContributionIndicVal = preFlexValidationVariables.get("volunteerContributionIndic");
		Boolean volunteerContributionIndic = volunteerContributionIndicVal != null ? (boolean) volunteerContributionIndicVal : null;

		if (monthlyContribution != null && !Boolean.TRUE.equals(volunteerContributionIndic) && noOfMonth != null) {
			packageFcAmount = monthlyContribution.multiply(BigDecimal.valueOf(noOfMonth));
		}
		if (volunteerContribution != null && noOfMonth != null) {
			packageFcAmount = monthlyContribution.add(volunteerContribution).multiply(BigDecimal.valueOf(noOfMonth));
		}
		addDateRangeParameters(noOfMonth, requiredFlexFields);
		if (noOfMonth == null) {
			// remove Date range flex field if no of months are not selected
			requiredFlexFields.removeIf(i -> {
				if ("INDIC12".equalsIgnoreCase(i.getField().getName())) {
					return true;
				}
				return false;
			});
		}
		if (monthlyContribution != null) {
			if (packageFcAmount != null && dateRangePresent) {
				validationResults.put(ValidationResultKey.PREFLEXCALL_COMPLETE.getName(), Boolean.TRUE);
			} else {
				validationResults.put(ValidationResultKey.PREFLEXCALL_COMPLETE.getName(), Boolean.FALSE);
			}
		}
		if (packageFcAmount != null) {
			validationResults.put(ValidationResultKey.PACKAGE_FC_AMOUNT.getName(), packageFcAmount);
		}
		if (packageSelectedAmiecCode != null) {
			validationResults.put(ValidationResultKey.PACKAGE_SELECTED_AMIECCODE.getName(), packageSelectedAmiecCode);
		}
	}

	private void addDateRangeParameters(Integer noOfMonth, List<JaxConditionalFieldDto> requiredFlexFields) {
		requiredFlexFields.stream().forEach(i -> {
			if ("INDIC12".equals(i.getField().getName()) && noOfMonth != null) {
				Map<String, Object> additionalValidations = i.getField().getAdditionalValidations();
				additionalValidations.put("range", noOfMonth);
			}
		});

	}
}
