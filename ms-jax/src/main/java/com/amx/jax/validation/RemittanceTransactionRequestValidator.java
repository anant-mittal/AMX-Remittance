package com.amx.jax.validation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.AdditionalFlexRequiredException;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.branchremittance.manager.BranchRemittanceManager;
import com.amx.jax.constant.BankConstants;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.FlexFieldBehaviour;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.PurposeTrnxAmicDesc;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsViewx;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.remittance.AdditionalBankDetailManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.AbstractRemittanceApplicationRequestModel;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldEntity;
import com.amx.jax.model.response.jaxfield.JaxFieldType;
import com.amx.jax.model.response.jaxfield.JaxFieldValueDto;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.repository.IAdditionalBankDetailsDao;
import com.amx.jax.repository.IAdditionalBankRuleMapDao;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.repository.IPurposeTrnxAmicDescRepository;
import com.amx.jax.services.BankService;
import com.amx.jax.services.JaxFieldService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;

@Component
public class RemittanceTransactionRequestValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemittanceTransactionRequestValidator.class);

	@Autowired
	IAdditionalDataDisplayDao additionalDataDisplayDao;
	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;
	@Autowired
	IAdditionalBankRuleMapDao additionalBankRuleMapDao;
	@Autowired
	JaxFieldService jaxFieldService;
	@Autowired
	DateUtil dateUtil;
	@Autowired
	MetaData metaData;
	@Autowired
	BranchRemittanceManager branchRemitManager;
	@Autowired
	AdditionalBankDetailManager additionalBankDetailManager;
	@Autowired
	BankService bankService;

	public void validateExchangeRate(AbstractRemittanceApplicationRequestModel request, RemittanceTransactionResponsetModel response) {

		ExchangeRateBreakup oldExchangeRate = request.getExchangeRateBreakup();
		ExchangeRateBreakup newExchangeRate = response.getExRateBreakup();
		oldExchangeRate.setRate(oldExchangeRate.getRate().setScale(newExchangeRate.getRate().scale(), RoundingMode.HALF_UP));
		if (oldExchangeRate.compareTo(newExchangeRate) != 0) {
			throw new GlobalException(JaxError.EXCHANGE_RATE_CHANGED, "Exchange rate has been changed");
		}
	}
	
	public void validatePurposeOfTransaction(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap) {
		List<String> flexiFields = new ArrayList<>();
		flexiFields.add(ConstantDocument.INDIC1);
		validateFlexFields(request, remitApplParametersMap, flexiFields);
	}

	public void validateFlexFields(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap) {
		List<FlexFiledView> allFlexFields = remittanceApplicationDao.getFlexFields();
		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());
		// remove indic1 validation from branch and other channels
		if (!JaxChannel.ONLINE.equals(metaData.getChannel())) {
			flexiFieldIn.remove(ConstantDocument.INDIC1);
		}
		validateFlexFields(request, remitApplParametersMap, flexiFieldIn);
	}

	public void validateFlexFields(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap, List<String> flexiFields) {
		request.populateFlexFieldDtoMap();
		request.populateAdditionalFieldsDtoMap();
		Map<String, FlexFieldDto> requestFlexFields = request.getFlexFieldDtoMap();
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		String routingBankCode = bankService.getBankById(routingBankId).getBankCode();
		if (requestFlexFields == null) {
			requestFlexFields = new HashMap<>();
			request.setFlexFieldDtoMap(requestFlexFields);
		} else {
			if (!BankConstants.VINTJA_BANK_CODE.equals(routingBankCode)) {
				validateFlexFieldValues(requestFlexFields);
			}
		}
		// if request is having srl id and addlbankruleid then it is by default INDIC1
		// values
		if (request.getAdditionalBankRuleFiledId() != null && request.getSrlId() != null) {
			requestFlexFields.put("INDIC1", new FlexFieldDto(request.getAdditionalBankRuleFiledId(), request.getSrlId(), null, null));
		}
		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");

		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicabilityForBank(
				applicationCountryId, routingCountryId, foreignCurrencyId, remittanceModeId, deliveryModeId,
				flexiFields.toArray(new String[flexiFields.size()]), routingBankId, ConstantDocument.No);
		if (CollectionUtils.isEmpty(additionalDataRequired)) {
			additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicability(applicationCountryId, routingCountryId,
					foreignCurrencyId, remittanceModeId, deliveryModeId, flexiFields.toArray(new String[flexiFields.size()]),ConstantDocument.No);
		}
		FlexFieldDto servicePackage = request.getServicePackage();
		List<JaxConditionalFieldDto> requiredFlexFields = new ArrayList<>();
		for (AdditionalDataDisplayView flexField : additionalDataRequired) {
			JaxConditionalFieldDto jaxConditionalFieldDto = getConditionalFieldDto(flexField, requestFlexFields, routingCountryId, remittanceModeId,
					deliveryModeId, foreignCurrencyId, routingBankId, servicePackage, false);
			if (jaxConditionalFieldDto != null) {
				requiredFlexFields.add(jaxConditionalFieldDto);
			}
		}
		// update jaxfield defination from db
		List<JaxFieldDto> jaxFieldDtos = requiredFlexFields.stream().map(i -> i.getField()).collect(Collectors.toList());
		jaxFieldService.updateDtoFromDb(jaxFieldDtos);
		updateAdditionalValidations(jaxFieldDtos);

		if (!JaxChannel.ONLINE.equals(metaData.getChannel()) && !JaxUtil.isNullZeroBigDecimalCheck(request.getPurposeOfTrnxId())) {
			JaxConditionalFieldDto dto = new JaxConditionalFieldDto();
			dto.setEntityName(JaxFieldEntity.PURPOSE_OF_TRNX);
			JaxFieldDto field = new JaxFieldDto();
			field.setName(ConstantDocument.INDIC1);
			field.setType(FlexFieldBehaviour.PRE_DEFINED.getFieldType().toString());
			field.setRequired(true);
			field.setMinLength(BigDecimal.ONE);
			field.setMaxLength(new BigDecimal(100));
			List<JaxFieldValueDto> purposeTrnxValue = getPurposeOfTrnx(request.getBeneId(), routingCountryId);
			if (purposeTrnxValue != null && !purposeTrnxValue.isEmpty()) {
				field.setPossibleValues(purposeTrnxValue);
			}
			dto.setField(field);
			requiredFlexFields.add(dto);
		}

		if (!requiredFlexFields.isEmpty()) {
			AdditionalFlexRequiredException exp = new AdditionalFlexRequiredException("Addtional flex fields are required",
					JaxError.ADDTIONAL_FLEX_FIELD_REQUIRED);
			processFlexFields(requiredFlexFields);
			additionalBankDetailManager.processMissingFlexFields(request, remitApplParametersMap, jaxFieldDtos);
			additionalBankDetailManager.setDefaultValues(requiredFlexFields, request, remitApplParametersMap);
			exp.setMeta(requiredFlexFields);
			throw exp;
		}
	}

	public JaxConditionalFieldDto getConditionalFieldDto(AdditionalDataDisplayView flexField, Map<String, FlexFieldDto> requestFlexFields,
			BigDecimal routingCountryId, BigDecimal remittanceModeId, BigDecimal deliveryModeId, BigDecimal foreignCurrencyId,
			BigDecimal routingBankId, FlexFieldDto servicePackage, boolean forceIncludeFlexField) {

		FlexFieldDto flexFieldValueInRequest = requestFlexFields.get(flexField.getFlexField());
		String routingBankCode = bankService.getBankById(routingBankId).getBankCode();
		String fieldBehaviour = flexField.getFieldBehaviour();
		LOGGER.info("Flex field value is " + flexField.getFlexField());
		LOGGER.info("Routing country value is " + routingCountryId);
		
		List<AdditionalBankRuleMap> addtionalBankRules = additionalBankRuleMapDao.getDynamicLevelMatch(routingCountryId, flexField.getFlexField());
		// bank rule for this flex field
		AdditionalBankRuleMap bankRule = addtionalBankRules.get(0);
		JaxConditionalFieldDto dto = new JaxConditionalFieldDto();
		dto.setEntityName(JaxFieldEntity.FLEX_FIELD);
		JaxFieldDto field = new JaxFieldDto();
		field.setName(bankRule.getFlexField());
		field.setLabel(bankRule.getFieldName());
		field.setRequired(ConstantDocument.Yes.equals(flexField.getIsRequired()));
		if (flexField.getMinLength() != null && flexField.getMinLength().compareTo(BigDecimal.ZERO) != 0 && flexField.getMaxLength() != null
				&& flexField.getMaxLength().compareTo(BigDecimal.ZERO) != 0) {
			field.setMinLength(flexField.getMinLength());
			field.setMaxLength(flexField.getMaxLength());
		} else {
			field.setMinLength(BigDecimal.ONE);
			field.setMaxLength(new BigDecimal(100));
		}
		field.setDtoPath("flexFields." + bankRule.getFlexField());
		dto.setId(bankRule.getAdditionalBankRuleId());
		FlexFieldBehaviour flexFieldBehaviourEnum = FlexFieldBehaviour.valueOf(fieldBehaviour);
		field.setType(getFieldType(flexFieldBehaviourEnum, flexField.getFieldType()));
		if (flexField.getFieldFormat() != null) {
			field.getAdditionalValidations().put("format", flexField.getFieldFormat());
		}
		switch (flexFieldBehaviourEnum) {
		case PRE_DEFINED:
			List<JaxFieldValueDto> amiecValues = additionalBankDetailManager.getAmiecValues(bankRule.getFlexField(), routingCountryId, deliveryModeId, remittanceModeId,
					routingBankId, foreignCurrencyId, bankRule.getAdditionalBankRuleId());
			// To set default value for bpi gift service provider
			if (servicePackage != null && servicePackage.getIndic() != null && servicePackage.getIndic().equalsIgnoreCase(field.getName())) {
				amiecValues = getDefaultForServicePackage(servicePackage, amiecValues);
				if (amiecValues != null && !amiecValues.isEmpty())
					field.setDefaultValue(amiecValues.get(0).getId().toString());
			}
			field.setPossibleValues(amiecValues);
			break;
		case DATE:
			String dateFormat = ConstantDocument.MM_DD_YYYY_DATE_FORMAT.toUpperCase();
			if (BankConstants.BPI_BANK_CODE.equals(routingBankCode)) {
				dateFormat = ConstantDocument.DD_MM_YYYY_DATE_FORMAT;
			}
			field.getAdditionalValidations().put("format", dateFormat);
			break;
		default:
			break;
		}
		dto.setField(field);
		if (flexFieldValueInRequest == null) {
			return dto;
		} else {
			switch (flexFieldBehaviourEnum) {
			case PRE_DEFINED:
				dto.getField().setDefaultValue(flexFieldValueInRequest.getResourceId().toString());
				break;
			default:
				dto.getField().setDefaultValue(flexFieldValueInRequest.getAmieceDescription());
			}

			if (field.getPossibleValues() != null && hasFieldValueChanged(field, flexFieldValueInRequest)) {
				return dto;
			}
		}
		if (forceIncludeFlexField) {
			return dto;
		} else {
			return null;
		}
	}

	/**
	 * process flex fields for further modification
	 */
	public void processFlexFields(List<JaxConditionalFieldDto> requiredFlexFields) {
		// make type field to be upper case
		requiredFlexFields.forEach(i -> {
			i.getField().setType(i.getField().getType().toUpperCase());
		});
	}

	private void validateFlexFieldValues(Map<String, FlexFieldDto> requestFlexFields) {
		if (requestFlexFields != null) {
			LocalDate today = LocalDate.now();
			LocalDate fromDate = null;
			LocalDate toDate = null;
			for (Map.Entry<String, FlexFieldDto> entry : requestFlexFields.entrySet()) {
				if ("INDIC4".equals(entry.getKey())) {
					// from date
					fromDate = dateUtil.validateDate(entry.getValue().getAmieceDescription(), ConstantDocument.MM_DD_YYYY_DATE_FORMAT);
					if (fromDate == null) {
						throw new GlobalException("Invalid from date");
					}
					if (fromDate.isAfter(today)) {
						throw new GlobalException("From date must be less than current date");
					}

				}
				if ("INDIC5".equals(entry.getKey())) {
					// to date
					toDate = dateUtil.validateDate(entry.getValue().getAmieceDescription(), ConstantDocument.MM_DD_YYYY_DATE_FORMAT);
					if (toDate == null) {
						throw new GlobalException("Invalid to date");
					}
				}

				if ("INDIC14".equals(entry.getKey())) {
					// to date
					LocalDate indic14toDate = dateUtil.validateDate(entry.getValue().getAmieceDescription(), ConstantDocument.DD_MM_YYYY_DATE_FORMAT);
					if (indic14toDate == null) {
						throw new GlobalException("Invalid date format .It must be "+ ConstantDocument.DD_MM_YYYY_DATE_FORMAT);
					}
					if (indic14toDate != null && indic14toDate.compareTo(today) <= 0) {
						throw new GlobalException("The delivery date must be greater than today date.");
					}
				}

			}
			if (toDate != null && fromDate == null) {
				throw new GlobalException("From date is not present");
			}
			if (toDate != null && toDate.isBefore(fromDate)) {
				throw new GlobalException("To date must be greater than from date");
			}
		}
	}

	private void updateAdditionalValidations(List<JaxFieldDto> jaxFieldDtos) {
		jaxFieldDtos.forEach(i -> {
			if ("PAYMENT PERIOD FROM DATE".equals(i.getName())) {
				Map<String, Object> additionalValidations = i.getAdditionalValidations();
				additionalValidations.put("lteq", dateUtil.format(LocalDate.now(), "MM/d/YYYY"));
				additionalValidations.put("format", ConstantDocument.MM_DD_YYYY_DATE_FORMAT);
				i.setAdditionalValidations(additionalValidations);
			}

			if ("PAYMENT PERIOD EXPIRY DATE".equalsIgnoreCase(i.getName()) || "TO DATE MM/DD/YYYY".equalsIgnoreCase(i.getName())) {
				Map<String, Object> additionalValidations = i.getAdditionalValidations();
				additionalValidations.put("gt", dateUtil.format(LocalDate.now(), "MM/d/YYYY"));
				additionalValidations.put("format", ConstantDocument.MM_DD_YYYY_DATE_FORMAT);
				i.setAdditionalValidations(additionalValidations);
			}
		});

	}

	private boolean hasFieldValueChanged(JaxFieldDto field, FlexFieldDto flexFieldValue) {
		boolean changedValue = true;
		for (Object value : field.getPossibleValues()) {
			JaxFieldValueDto jaxFieldValueDto = (JaxFieldValueDto) value;
			if (jaxFieldValueDto.getValue().equals(flexFieldValue)) {
				changedValue = false;
			}
		}
		return changedValue;
	}


	private List<JaxFieldValueDto> getPurposeOfTrnx(BigDecimal beneRelaId, BigDecimal routingCountryId) {
		List<AdditionalExchAmiecDto> purposeOfTrnxList = branchRemitManager.getPurposeOfTrnx(beneRelaId, routingCountryId);
		if (purposeOfTrnxList != null && !purposeOfTrnxList.isEmpty()) {
			return purposeOfTrnxList.stream().map(x -> {
				FlexFieldDto ffDto = new FlexFieldDto(x.getAdditionalBankFieldId(), x.getResourceId(), x.getResourceName(), x.getLocalName());

				JaxFieldValueDto dto = new JaxFieldValueDto();
				dto.setId(ffDto.getSrlId());
				dto.setOptLable(ffDto.getAmieceDescription());
				dto.setValue(ffDto);
				dto.setResourceName(ffDto.getAmieceDescription());
				return dto;
			}).collect(Collectors.toList());
		}
		return null;
	}

	private List<JaxFieldValueDto> getDefaultForServicePackage(FlexFieldDto servicePackage, List<JaxFieldValueDto> amiecValues) {
		List<JaxFieldValueDto> amiecValue = new ArrayList<>();
		for (JaxFieldValueDto flexDto : amiecValues) {
			FlexFieldDto flex = (FlexFieldDto) flexDto.getValue();
			if (flex != null && flex.getAmieceCode() != null && flex.getAmieceCode().equals(servicePackage.getAmieceCode())) {
				amiecValue.add(flexDto);
			}
		}
		return amiecValue;
	}

	public void saveFlexFields(RemittanceAdditionalBeneFieldModel requestApplModel, Map<String, Object> remitApplParametersMap) {
		additionalBankDetailManager.saveFlexFields(requestApplModel, remitApplParametersMap);
	}

	private String getFieldType(FlexFieldBehaviour flexFieldBehaviour, String fieldType) {
		String flexFieldType = flexFieldBehaviour.getFieldType().toString();
		if (FlexFieldBehaviour.USER_ENTERABLE.equals(flexFieldBehaviour)) {
			if (StringUtils.isNotBlank(fieldType)) {
				switch (fieldType) {
				case "N":
					flexFieldType = JaxFieldType.NUMBER.toString();
					break;
				case "A":
				case "B":
					flexFieldType = JaxFieldType.TEXT.toString();
					break;
				}
			}
		}
		return flexFieldType;
	}
}
