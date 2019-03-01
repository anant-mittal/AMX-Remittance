package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.JaxFieldEntity;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.GetJaxFieldRequest;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxDynamicField;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.remittance.RemittanceAdditionalBeneFieldModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.repository.IAdditionalDataDisplayDao;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.JaxFieldService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceAdditionalFieldManager {

	@Autowired
	JaxFieldService jaxFieldService;
	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	BankService bankService;
	@Autowired
	IAdditionalDataDisplayDao additionalDataDisplayDao;

	Logger logger = LoggerFactory.getLogger(getClass());

	public void validateAdditionalFields(RemittanceAdditionalBeneFieldModel model,
			Map<String, Object> remitApplParametersMap) {
		ApiResponse<JaxConditionalFieldDto> apiResponse = jaxFieldService
				.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.REMITTANCE_ONLINE));
		List<JaxConditionalFieldDto> allJaxConditionalFields = apiResponse.getResults();
		Map<String, AdditionalDataDisplayView> flexFieldMap = getAdditionalDataDisplayMap(remitApplParametersMap);
		List<JaxConditionalFieldDto> missingJaxConditionalFields = new ArrayList<>();
		Map<String, Object> additionalFields = model.getAdditionalFields();
		boolean isAdditionalFieldMissing = false;
		addDataFromAdditionalDataDisplay(allJaxConditionalFields, flexFieldMap);

		for (JaxConditionalFieldDto jaxConditionalField : allJaxConditionalFields) {
			if (additionalFields == null || additionalFields.get(jaxConditionalField.getField().getName()) == null) {
				if (!isDynamicFieldRequired(jaxConditionalField, model, flexFieldMap)) {
					continue;
				}
				jaxConditionalField.getField()
						.setDtoPath("additionalFields." + jaxConditionalField.getField().getName());
				missingJaxConditionalFields.add(jaxConditionalField);
				isAdditionalFieldMissing = true;
			} else {
				Object fieldValue = additionalFields.get(jaxConditionalField.getField().getName());
				jaxFieldService.validateJaxFieldRegEx(jaxConditionalField.getField(), (String) fieldValue);
			}
		}
		if (isAdditionalFieldMissing) {
			GlobalException ex = new GlobalException(JaxError.ADDTIONAL_FLEX_FIELD_REQUIRED,
					"Additional fields required");
			ex.setMeta(missingJaxConditionalFields);
			throw ex;
		}
	}

	private void addDataFromAdditionalDataDisplay(List<JaxConditionalFieldDto> allJaxConditionalFields,
			Map<String, AdditionalDataDisplayView> flexFieldMap) {
		for (JaxConditionalFieldDto jaxConditionalField : allJaxConditionalFields) {
			JaxDynamicField jaxDynamicField = JaxDynamicField.valueOf(jaxConditionalField.getField().getName());
			if (jaxDynamicField != null && jaxDynamicField.getFlexField() != null) {
				AdditionalDataDisplayView addlDataDisplay = flexFieldMap.get(jaxDynamicField.getFlexField());
				if (addlDataDisplay != null) {
					if (addlDataDisplay.getIsRequired() != null) {
						jaxConditionalField.getField().setRequired(
								ConstantDocument.Yes.equalsIgnoreCase(addlDataDisplay.getIsRequired()) ? true : false);
					}
				}
			}
		}
	}

	private Map<String, AdditionalDataDisplayView> getAdditionalDataDisplayMap(
			Map<String, Object> remitApplParametersMap) {
		BigDecimal applicationCountryId = (BigDecimal) remitApplParametersMap.get("P_APPLICATION_COUNTRY_ID");
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");

		List<AdditionalDataDisplayView> additionalDataRequired = additionalDataDisplayDao
				.getAdditionalDataFromServiceApplicability(applicationCountryId, routingCountryId, foreignCurrencyId,
						remittanceModeId, deliveryModeId, JaxDynamicField.getAllAdditionalFlexFields());
		return additionalDataRequired.stream().collect(Collectors.toMap(i -> i.getFlexField(), i -> i));
	}

	private boolean isDynamicFieldRequired(JaxConditionalFieldDto jaxConditionalField,
			RemittanceAdditionalBeneFieldModel model, Map<String, AdditionalDataDisplayView> flexFieldMap) {
		JaxDynamicField jaxDynamicField = JaxDynamicField.valueOf(jaxConditionalField.getField().getName());
		if (jaxDynamicField.getFlexField() != null && flexFieldMap.get(jaxDynamicField.getFlexField()) == null) {
			return false;
		}
		switch (jaxDynamicField) {
		case BENE_BANK_IBAN_NUMBER:
			boolean isBeneIbanFieldRequired = isBeneIbanFieldRequired(model.getBeneId());
			return isBeneIbanFieldRequired;
		default:
			return true;
		}
	}

	private boolean isBeneIbanFieldRequired(BigDecimal beneId) {
		BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(beneId);
		String ibanFlag = bankService.getBankById(beneficiaryDetail.getBankId()).getIbanFlag();
		if (!ConstantDocument.Yes.equalsIgnoreCase(ibanFlag)) {
			return false;
		}
		BeneficaryAccount beneficaryAccount = beneficiaryService
				.getBeneAccountByAccountSeqId(beneficiaryDetail.getBeneficiaryAccountSeqId());
		if (StringUtils.isBlank(beneficaryAccount.getIbanNumber())) {
			return true;
		}
		return false;
	}

	public void processAdditionalFields(RemittanceAdditionalBeneFieldModel model) {
		ApiResponse<JaxConditionalFieldDto> apiResponse = jaxFieldService
				.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.REMITTANCE_ONLINE));
		List<JaxConditionalFieldDto> allJaxConditionalFields = apiResponse.getResults();
		Map<String, Object> fieldValues = model.getAdditionalFields();
		if (allJaxConditionalFields != null && fieldValues != null) {
			for (JaxConditionalFieldDto jaxConditionalField : allJaxConditionalFields) {
				Object fieldValue = fieldValues.get(jaxConditionalField.getField().getName());
				if (fieldValue == null || StringUtils.isBlank(fieldValue.toString())) {
					continue;
				}
				if (JaxDynamicField.BENE_BANK_IBAN_NUMBER.name().equals(jaxConditionalField.getField().getName())) {
					// set iban number
					String ibanNumber = (String) fieldValue;
					BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(model.getBeneId());
					BeneficaryAccount beneficaryAccount = beneficiaryService
							.getBeneAccountByAccountSeqId(beneficiaryDetail.getBeneficiaryAccountSeqId());
					beneficaryAccount.setIbanNumber(ibanNumber);
					logger.info("setting iban number for bene bank account seq id {} , IBAN : {} ",
							beneficiaryDetail.getBeneficiaryAccountSeqId(), ibanNumber);
					beneficiaryService.saveBeneAccount(beneficaryAccount);
				}
				if (JaxDynamicField.BENE_FLAT_NO.name().equals(jaxConditionalField.getField().getName())
						&& fieldValue != null) {
					BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(model.getBeneId());
					BeneficaryMaster beneficaryMaster = beneficiaryService
							.getBeneficiaryMasterBybeneficaryMasterSeqId(beneficiaryDetail.getBeneficaryMasterSeqId());
					beneficaryMaster.setFlatNo(fieldValue.toString());
					logger.info("setting flat no number for bene master seq id {} , : {} ",
							beneficiaryDetail.getBeneficaryMasterSeqId(), fieldValue);
					beneficiaryService.saveBeneMaster(beneficaryMaster);
				}
				if (JaxDynamicField.BENE_HOUSE_NO.name().equals(jaxConditionalField.getField().getName())
						&& fieldValue != null) {
					BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(model.getBeneId());
					BeneficaryMaster beneficaryMaster = beneficiaryService
							.getBeneficiaryMasterBybeneficaryMasterSeqId(beneficiaryDetail.getBeneficaryMasterSeqId());
					beneficaryMaster.setBuildingNo(fieldValue.toString());
					logger.info("setting house no number for bene master seq id {} , : {} ",
							beneficiaryDetail.getBeneficaryMasterSeqId(), fieldValue);
					beneficiaryService.saveBeneMaster(beneficaryMaster);
				}
				if (JaxDynamicField.BENE_STREET_NO.name().equals(jaxConditionalField.getField().getName())
						&& fieldValue != null) {
					BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(model.getBeneId());
					BeneficaryMaster beneficaryMaster = beneficiaryService
							.getBeneficiaryMasterBybeneficaryMasterSeqId(beneficiaryDetail.getBeneficaryMasterSeqId());
					beneficaryMaster.setStreetNo(fieldValue.toString());
					logger.info("setting street no number for bene master seq id {} , : {} ",
							beneficiaryDetail.getBeneficaryMasterSeqId(), fieldValue);
					beneficiaryService.saveBeneMaster(beneficaryMaster);
				}
			}
		}
	}
}
