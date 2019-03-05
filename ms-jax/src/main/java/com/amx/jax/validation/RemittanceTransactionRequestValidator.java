package com.amx.jax.validation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.JaxFieldEntity;
import com.amx.amxlib.exception.AdditionalFlexRequiredException;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.JaxFieldDto;
import com.amx.amxlib.model.JaxFieldValueDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.FlexFieldBehaviour;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsViewx;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.AbstractRemittanceApplicationRequestModel;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.repository.IAdditionalBankDetailsDao;
import com.amx.jax.repository.IAdditionalBankRuleMapDao;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.services.JaxFieldService;
import com.amx.jax.util.DateUtil;

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
	IAdditionalBankDetailsDao additionalBankDetailsDao;
	@Autowired
	JaxFieldService jaxFieldService ;
	@Autowired
	DateUtil dateUtil;
	@Autowired
	MetaData metaData;
	
	public void validateExchangeRate(AbstractRemittanceApplicationRequestModel request,
			RemittanceTransactionResponsetModel response) {

		ExchangeRateBreakup oldExchangeRate = request.getExchangeRateBreakup();
		ExchangeRateBreakup newExchangeRate = response.getExRateBreakup();
		oldExchangeRate.setRate(oldExchangeRate.getRate().setScale(newExchangeRate.getRate().scale(), RoundingMode.HALF_UP));
		if (oldExchangeRate.compareTo(newExchangeRate) != 0) {
			throw new GlobalException(JaxError.EXCHANGE_RATE_CHANGED, "Exchange rate has been changed");
		}
	}

	public void validateFlexFields(RemittanceAdditionalBeneFieldModel request,
			Map<String, Object> remitApplParametersMap) {
		request.populateFlexFieldDtoMap();
		List<FlexFiledView> allFlexFields = remittanceApplicationDao.getFlexFields();
		Map<String, FlexFieldDto> requestFlexFields = request.getFlexFieldDtoMap();
		if (requestFlexFields == null) {
			requestFlexFields = new HashMap<>();
			request.setFlexFieldDtoMap(requestFlexFields);
		} else {
			validateFlexFieldValues(requestFlexFields);
		}
		requestFlexFields.put("INDIC1",new FlexFieldDto(request.getAdditionalBankRuleFiledId(), request.getSrlId(), null));
		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");

		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());
		// remove indic1 validation from branch and other channels
		if (!JaxChannel.ONLINE.equals(metaData.getChannel())) {
			flexiFieldIn.remove(ConstantDocument.INDIC1);
		}

		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicability(applicationCountryId, routingCountryId, foreignCurrencyId,remittanceModeId, deliveryModeId, flexiFieldIn.toArray(new String[flexiFieldIn.size()]));
		List<JaxConditionalFieldDto> requiredFlexFields = new ArrayList<>();
		for (AdditionalDataDisplayView flexField : additionalDataRequired) {
			FlexFieldDto flexFieldValueInRequest = requestFlexFields.get(flexField.getFlexField());

			String fieldBehaviour = flexField.getFieldBehaviour();
			List<AdditionalBankRuleMap> addtionalBankRules = additionalBankRuleMapDao.getDynamicLevelMatch(routingCountryId, flexField.getFlexField());
			// bank rule for this flex field
			AdditionalBankRuleMap bankRule = addtionalBankRules.get(0);
			JaxConditionalFieldDto dto = new JaxConditionalFieldDto();
			dto.setEntityName(JaxFieldEntity.FLEX_FIELD);
			JaxFieldDto field = new JaxFieldDto();
			field.setName(bankRule.getFlexField());
			field.setLabel(bankRule.getFieldName());
			field.setRequired(ConstantDocument.Yes.equals(flexField.getIsRequired()));
			field.setMinLength(BigDecimal.ONE);
			field.setMaxLength(new BigDecimal(100));
			field.setDtoPath("flexFields." + bankRule.getFlexField());
			dto.setId(bankRule.getAdditionalBankRuleId());
			if (FlexFieldBehaviour.PRE_DEFINED.toString().equals(fieldBehaviour)) {
				field.setType(FlexFieldBehaviour.PRE_DEFINED.getFieldType().toString());
				List<JaxFieldValueDto> amiecValues = getAmiecValues(bankRule.getFlexField(), routingCountryId,deliveryModeId, remittanceModeId, routingBankId, foreignCurrencyId,bankRule.getAdditionalBankRuleId());
				field.setPossibleValues(amiecValues);
			} else {
				field.setType(FlexFieldBehaviour.USER_ENTERABLE.getFieldType().toString());
			}
			dto.setField(field);
			if (flexFieldValueInRequest == null) {
				requiredFlexFields.add(dto);
			} else {
				if (field.getPossibleValues() != null  && hasFieldValueChanged(field, flexFieldValueInRequest)) {
					requiredFlexFields.add(dto);
				}
			}
		}
		// update jaxfield defination from db
		List<JaxFieldDto> jaxFieldDtos = requiredFlexFields.stream().map(i -> i.getField()).collect(Collectors.toList());
		jaxFieldService.updateDtoFromDb(jaxFieldDtos);
		updateAdditionalValidations(jaxFieldDtos);
		
		if (!requiredFlexFields.isEmpty()) {
			LOGGER.error(requiredFlexFields.toString());
			AdditionalFlexRequiredException exp = new AdditionalFlexRequiredException("Addtional flex fields are required", JaxError.ADDTIONAL_FLEX_FIELD_REQUIRED);
			processFlexFields(requiredFlexFields);
			exp.setMeta(requiredFlexFields);
			throw exp;
		}

	}

	/**
	 * process flex fields for further modification
	 */
	private void processFlexFields(List<JaxConditionalFieldDto> requiredFlexFields) {
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
					fromDate = dateUtil.validateDate(entry.getValue().getAmieceDescription(),
							ConstantDocument.MM_DD_YYYY_DATE_FORMAT);
					if (fromDate == null) {
						throw new GlobalException("Invalid from date");
					}
					if (fromDate.isAfter(today)) {
						throw new GlobalException("From date must be less than current date");
					}

				}
				if ("INDIC5".equals(entry.getKey())) {
					// to date
					toDate = dateUtil.validateDate(entry.getValue().getAmieceDescription(),
							ConstantDocument.MM_DD_YYYY_DATE_FORMAT);
					if (toDate == null) {
						throw new GlobalException("Invalid to date");
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
				String format = "MM/DD/YYYY";
				Map<String, Object> additionalValidations = i.getAdditionalValidations();
				additionalValidations.put("lteq", dateUtil.format(LocalDate.now(), "MM/d/YYYY"));
				additionalValidations.put("format", format);
				i.setAdditionalValidations(additionalValidations);
			}

			if ("PAYMENT PERIOD EXPIRY DATE".equalsIgnoreCase(i.getName())
					|| "TO DATE MM/DD/YYYY".equalsIgnoreCase(i.getName())) {
				Map<String, Object> additionalValidations = i.getAdditionalValidations();
				additionalValidations.put("gt", dateUtil.format(LocalDate.now(), "MM/d/YYYY"));
				additionalValidations.put("format", "MM/DD/YYYY");
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

	private List<JaxFieldValueDto> getAmiecValues(String flexiField, BigDecimal countryId, BigDecimal deleveryModeId,
			BigDecimal remittanceModeId, BigDecimal bankId, BigDecimal currencyId,
			BigDecimal additionalBankRuleFiledId) {
		List<AdditionalBankDetailsViewx> addtionalBankDetails = additionalBankDetailsDao
				.getAdditionalBankDetails(currencyId, bankId, remittanceModeId, deleveryModeId, countryId, flexiField);
		return addtionalBankDetails.stream().map(x -> {
			FlexFieldDto ffDto = new FlexFieldDto(additionalBankRuleFiledId, x.getSrlId(), x.getAmieceDescription());
			JaxFieldValueDto dto = new JaxFieldValueDto();
			dto.setId(ffDto.getSrlId());
			dto.setOptLable(ffDto.getAmieceDescription());
			dto.setValue(ffDto);
			return dto;
		}).collect(Collectors.toList());
	}
}
