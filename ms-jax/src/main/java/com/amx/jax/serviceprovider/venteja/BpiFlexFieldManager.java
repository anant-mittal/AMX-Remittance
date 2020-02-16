package com.amx.jax.serviceprovider.venteja;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.BankConstants;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.ViewParameterDetails;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.jaxfield.JaxFieldValueDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.repository.remittance.IViewParameterDetailsRespository;
import com.amx.jax.serviceprovider.service.AbstractFlexFieldManager;

@Component(BankConstants.BPI_BANK_CODE + AbstractFlexFieldManager.FLEX_FIELD_MANAGER_BEAN_SUFFIX)
public class BpiFlexFieldManager extends AbstractFlexFieldManager {

	@Autowired
	VentajaFlexFieldManager ventajaFlexFieldManager;
	@Autowired
	IViewParameterDetailsRespository viewParameterDetailsRespository;

	@Override
	public Map<String, Object> managePreFlexFields(List<AdditionalDataDisplayView> additionalDataRequired,
			Map<String, FlexFieldDto> requestFlexFields, ViewParameterDetails cashSetUp, BenificiaryListView beneficaryDetails,
			List<JaxConditionalFieldDto> requiredFlexFields) {
		Map<String, Object> output = ventajaFlexFieldManager.managePreFlexFields(additionalDataRequired, requestFlexFields, cashSetUp,
				beneficaryDetails, requiredFlexFields);
		removeGiftPackages(requiredFlexFields, beneficaryDetails);
		return output;
	}

	private void removeDefaultValueFromPackageFlexFiels(List<JaxConditionalFieldDto> requiredFlexFields) {
		requiredFlexFields.stream().filter(i -> "INDIC6".equalsIgnoreCase((i.getField().getName()))).forEach(i -> {
			i.getField().setDefaultValue(null);
		});

	}

	private void removeGiftPackages(List<JaxConditionalFieldDto> requiredFlexFields, BenificiaryListView beneficaryDetails) {
		List<ViewParameterDetails> vwParamDetailsList = viewParameterDetailsRespository.findByRecordIdAndCharField2AndNumericField1(
				ConstantDocument.BPI_GIFT, beneficaryDetails.getBankCode(), beneficaryDetails.getBranchCode());
		requiredFlexFields.stream().filter(i -> "INDIC6".equals(i.getField().getName())).forEach(i -> {
			List<JaxFieldValueDto> possibleValues = i.getField().getPossibleValues();
			Iterator<JaxFieldValueDto> itr = possibleValues.iterator();
			List<String> giftParamDefCodes = vwParamDetailsList.stream().map(p -> p.getParamCodeDef()).collect(Collectors.toList());
			while (itr.hasNext()) {
				JaxFieldValueDto jaxFieldValueDto = itr.next();
				FlexFieldDto flexFieldDto = (FlexFieldDto) jaxFieldValueDto.getValue();
				if (giftParamDefCodes.contains(flexFieldDto.getAmieceCode())) {
					itr.remove();
				}
			}

		});
	}

	@Override
	public void validatePreFlexField(Map<String, FlexFieldDto> requestFlexFields, Map<String, Object> preFlexValidationVariables,
			Map<String, Object> validationResults) {
		ventajaFlexFieldManager.validatePreFlexField(requestFlexFields, preFlexValidationVariables, validationResults);
		List<JaxConditionalFieldDto> requiredFlexFields = (List<JaxConditionalFieldDto>) validationResults.get("requiredFlexFields");
		BigDecimal packageFcAmount = (BigDecimal) validationResults.get(ValidationResultKey.PACKAGE_FC_AMOUNT.getName());
		if (packageFcAmount != null) {
			validationResults.put(ValidationResultKey.PREFLEXCALL_COMPLETE.getName(), Boolean.TRUE);
		}
	}

}
