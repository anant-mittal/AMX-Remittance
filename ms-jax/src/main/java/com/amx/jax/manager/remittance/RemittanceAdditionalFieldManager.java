package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxDynamicField;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.error.JaxError;
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

	Logger logger = LoggerFactory.getLogger(getClass());

	public void validateAdditionalFields(RemittanceTransactionRequestModel model) {
		ApiResponse<JaxConditionalFieldDto> apiResponse = jaxFieldService
				.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.REMITTANCE_ONLINE));
		List<JaxConditionalFieldDto> allJaxConditionalFields = apiResponse.getResults();
		List<JaxConditionalFieldDto> missingJaxConditionalFields = new ArrayList<>();
		Map<String, Object> additionalFields = model.getAdditionalFields();
		boolean isAdditionalFieldMissing = false;

		for (JaxConditionalFieldDto jaxConditionalField : allJaxConditionalFields) {
			if (additionalFields == null || additionalFields.get(jaxConditionalField.getField().getName()) == null) {
				//iban field
				if (JaxDynamicField.BENE_BANK_IBAN_NUMBER.name().equals(jaxConditionalField.getField().getName())) {
					boolean isBeneIbanFieldRequired = isBeneIbanFieldRequired(model.getBeneId());
					if (!isBeneIbanFieldRequired) {
						continue;
					}
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

	private boolean isBeneIbanFieldRequired(BigDecimal beneId) {
		BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(beneId);
		String ibanFlag = bankService.getBankById(beneficiaryDetail.getBankId()).getIbanFlag();
		if(!ConstantDocument.Yes.equalsIgnoreCase(ibanFlag)) {
			return false;
		}
		BeneficaryAccount beneficaryAccount = beneficiaryService
				.getBeneAccountByAccountSeqId(beneficiaryDetail.getBeneficiaryAccountSeqId());
		if( StringUtils.isBlank(beneficaryAccount.getIbanNumber())) {
			return true;
		}
		return false;
	}

	public void processAdditionalFields(RemittanceTransactionRequestModel model) {
		ApiResponse<JaxConditionalFieldDto> apiResponse = jaxFieldService
				.getJaxFieldsForEntity(new GetJaxFieldRequest(JaxFieldEntity.REMITTANCE_ONLINE));
		List<JaxConditionalFieldDto> allJaxConditionalFields = apiResponse.getResults();
		Map<String, Object> fieldValues = model.getAdditionalFields();
		if (allJaxConditionalFields != null && fieldValues != null) {
			for (JaxConditionalFieldDto jaxConditionalField : allJaxConditionalFields) {
				if (JaxDynamicField.BENE_BANK_IBAN_NUMBER.name().equals(jaxConditionalField.getField().getName())
						&& fieldValues.get(jaxConditionalField.getField().getName()) != null) {
					// set iban number
					String ibanNumber = (String) fieldValues.get(jaxConditionalField.getField().getName());
					BenificiaryListView beneficiaryDetail = beneficiaryService.getBeneByIdNo(model.getBeneId());
					BeneficaryAccount beneficaryAccount = beneficiaryService
							.getBeneAccountByAccountSeqId(beneficiaryDetail.getBeneficiaryAccountSeqId());
					beneficaryAccount.setIbanNumber(ibanNumber);
					logger.info("setting iban number for bene bank account seq id {} , IBAN : {} ",
							beneficiaryDetail.getBeneficiaryAccountSeqId(), ibanNumber);
					beneficiaryService.saveBeneAccount(beneficaryAccount);
				}
			}
		}
	}
}
