package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApplicationProcedureParam;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailData;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.repository.remittance.AdditionalBankDetailDataRepository;
import com.amx.jax.validation.AdditionalBankDetailValidator;

@Component
public class AdditionalBankDetailManager {
	@Autowired
	AdditionalBankDetailValidator additionalBankDetailValidator;
	@Autowired
	AdditionalBankDetailDataRepository additionalBankDetailDataRepository;
	@Autowired
	IAdditionalDataDisplayDao additionalDataDisplayDao;
	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;

	public void setDefaultValues(List<JaxConditionalFieldDto> requiredFlexFields, RemittanceAdditionalBeneFieldModel request,
			Map<String, Object> remitApplParametersMap) {

		BigDecimal beneAccSeqId = ApplicationProcedureParam.P_BENEFICIARY_ACCOUNT_NO.getValue(remitApplParametersMap);
		List<AdditionalBankDetailData> additionalBeneData = additionalBankDetailDataRepository.findByBeneAccSeqId(beneAccSeqId);
		Map<Object, AdditionalBankDetailData> valueMap = additionalBeneData.stream().collect(Collectors.toMap(x -> x.getKey(), x -> x));

		if (valueMap != null && valueMap.size() != 0) {
			requiredFlexFields.forEach(i -> {
				AdditionalBankDetailData data = valueMap.get(i.getField().getName());
				i.getField().setDefaultValue(data.getValue());
			});
		}
	}

	public void validateAdditionalBankFields(RemittanceAdditionalBeneFieldModel request, Map<String, Object> remitApplParametersMap) {
		additionalBankDetailValidator.validateAdditionalBankFields(request, remitApplParametersMap);
	}

	public void saveFlexFields(RemittanceAdditionalBeneFieldModel requestApplModel, Map<String, Object> remitApplParametersMap) {

		List<FlexFiledView> allFlexFields = remittanceApplicationDao.getFlexFields();
		Map<String, FlexFieldDto> additionalbankFields = requestApplModel.getAdditionalDtoMap();
		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		BigDecimal beneAccSeqId = ApplicationProcedureParam.P_BENEFICIARY_ACCOUNT_NO.getValue(remitApplParametersMap);

		List<String> flexiFieldIn = allFlexFields.stream().map(i -> i.getFieldName()).collect(Collectors.toList());
		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao.getAdditionalDataFromServiceApplicabilityForBank(
				applicationCountryId, routingCountryId, foreignCurrencyId, remittanceModeId, deliveryModeId,
				flexiFieldIn.toArray(new String[flexiFieldIn.size()]), routingBankId);
		Map<String, AdditionalDataDisplayView> additionalDataRequiredMap = additionalDataRequired.stream()
				.collect(Collectors.toMap(i -> i.getFlexField(), i -> i));

		List<AdditionalBankDetailData> additionalBeneData = additionalBankDetailDataRepository.findByBeneAccSeqId(beneAccSeqId);
		Map<Object, AdditionalBankDetailData> valueMap = additionalBeneData.stream().collect(Collectors.toMap(x -> x.getKey(), x -> x));

		additionalbankFields.forEach((k, v) -> {
			AdditionalDataDisplayView additionalDataDisplayView = additionalDataRequiredMap.get(k);
			if (ConstantDocument.Yes.equalsIgnoreCase(additionalDataDisplayView.getIsBeneTag())) {
				AdditionalBankDetailData data = valueMap.get(k);
				if (data == null) {
					data = new AdditionalBankDetailData(beneAccSeqId, k, v.getAmieceDescription());
				} else {
					data.setValue(v.getAmieceDescription());
				}
				additionalBankDetailDataRepository.save(data);
			}
		});

	}

}
