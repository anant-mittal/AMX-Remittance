package com.amx.jax.validation;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.AdditionalFlexRequiredException;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.FlexFieldDto;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.JaxFieldDto;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.FlexFieldBehaviour;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsViewx;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.repository.IAdditionalBankDetailsDao;
import com.amx.jax.repository.IAdditionalBankRuleMapDao;
import com.amx.jax.repository.IAdditionalDataDisplayDao;

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

	public void validateExchangeRate(RemittanceTransactionRequestModel request,
			RemittanceTransactionResponsetModel response) {

		ExchangeRateBreakup oldExchangeRate = request.getExRateBreakup();
		ExchangeRateBreakup newExchangeRate = response.getExRateBreakup();
		oldExchangeRate
				.setRate(oldExchangeRate.getRate().setScale(newExchangeRate.getRate().scale(), RoundingMode.HALF_UP));
		if (oldExchangeRate.compareTo(newExchangeRate) != 0) {
			throw new GlobalException("Exchange rate has been changed", JaxError.EXCHANGE_RATE_CHANGED);
		}
	}

	public void validateFlexFields(RemittanceTransactionRequestModel request,
			Map<String, Object> remitApplParametersMap) {
		List<FlexFiledView> allFlexFields = remittanceApplicationDao.getFlexFields();
		Map<String, FlexFieldDto> requestFlexFields = request.getFlexFields();
		if (requestFlexFields == null) {
			requestFlexFields = new HashMap<>();
			request.setFlexFields(requestFlexFields);
		}
		requestFlexFields.put("INDIC1",
				new FlexFieldDto(request.getAdditionalBankRuleFiledId(), request.getSrlId(), null));
		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");

		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());

		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao
				.getAdditionalDataFromServiceApplicability(applicationCountryId, routingCountryId, foreignCurrencyId,
						remittanceModeId, deliveryModeId, flexiFieldIn.toArray(new String[flexiFieldIn.size()]));
		List<JaxConditionalFieldDto> requiredFlexFields = new ArrayList<>();
		for (AdditionalDataDisplayView flexField : additionalDataRequired) {
			FlexFieldDto flexFieldValueInRequest = requestFlexFields.get(flexField.getFlexField());

			String fieldBehaviour = flexField.getFieldBehaviour();
			List<AdditionalBankRuleMap> addtionalBankRules = additionalBankRuleMapDao
					.getDynamicLevelMatch(routingCountryId, flexField.getFlexField());
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
			dto.setId(bankRule.getAdditionalBankRuleId());
			if (FlexFieldBehaviour.PRE_DEFINED.toString().equals(fieldBehaviour)) {
				field.setType(FlexFieldBehaviour.PRE_DEFINED.getFieldType().toString());
				Map<Object, Object> amiecValues = getAmiecValues(bankRule.getFlexField(), routingCountryId,
						deliveryModeId, remittanceModeId, routingBankId, foreignCurrencyId,
						bankRule.getAdditionalBankRuleId());
				field.setPossibleValues(amiecValues);
			}
			dto.setField(field);
			if (flexFieldValueInRequest == null) {
				requiredFlexFields.add(dto);
			} else {
				if (hasFieldValueChanged(field, flexFieldValueInRequest)) {
					requiredFlexFields.add(dto);
				}
			}
		}
		if (!requiredFlexFields.isEmpty()) {
			LOGGER.error(requiredFlexFields.toString());
			AdditionalFlexRequiredException exp = new AdditionalFlexRequiredException(
					"Addtional flex fields are required", JaxError.ADDTIONAL_FLEX_FIELD_REQUIRED);
			exp.setMeta(requiredFlexFields);
			throw exp;
		}

	}

	private boolean hasFieldValueChanged(JaxFieldDto field, FlexFieldDto flexFieldValue) {
		boolean changedValue = true;
		for (Map.Entry<Object, Object> entry : field.getPossibleValues().entrySet()) {
			FlexFieldDto ffd = (FlexFieldDto) entry.getValue();
			if (ffd.equals(flexFieldValue)) {
				changedValue = false;
			}
		}
		return changedValue;
	}

	private Map<Object, Object> getAmiecValues(String flexiField, BigDecimal countryId, BigDecimal deleveryModeId,
			BigDecimal remittanceModeId, BigDecimal bankId, BigDecimal currencyId,
			BigDecimal additionalBankRuleFiledId) {
		List<AdditionalBankDetailsViewx> addtionalBankDetails = additionalBankDetailsDao
				.getAdditionalBankDetails(currencyId, bankId, remittanceModeId, deleveryModeId, countryId, flexiField);
		return addtionalBankDetails.stream().collect(Collectors.toMap(AdditionalBankDetailsViewx::getSrlId, x -> {
			FlexFieldDto ffDto = new FlexFieldDto(additionalBankRuleFiledId, x.getSrlId(), x.getAmieceDescription());
			return ffDto;
		}));
	}
}
