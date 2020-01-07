package com.amx.jax.serviceprovider.venteja;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.BankConstants;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.ViewParameterDetails;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.serviceprovider.service.AbstractFlexFieldManager;

@Component(BankConstants.BPI_BANK_CODE + AbstractFlexFieldManager.FLEX_FIELD_MANAGER_BEAN_SUFFIX)
public class BpiFlexFieldManager extends AbstractFlexFieldManager {

	@Autowired
	VentajaFlexFieldManager ventajaFlexFieldManager;

	@Override
	public Map<String, Object> managePreFlexFields(List<AdditionalDataDisplayView> additionalDataRequired,
			Map<String, FlexFieldDto> requestFlexFields, ViewParameterDetails cashSetUp, BenificiaryListView beneficaryDetails,
			List<JaxConditionalFieldDto> requiredFlexFields) {
		return ventajaFlexFieldManager.managePreFlexFields(additionalDataRequired, requestFlexFields, cashSetUp, beneficaryDetails,
				requiredFlexFields);
	}

	@Override
	public void validatePreFlexField(Map<String, FlexFieldDto> requestFlexFields, Map<String, Object> preFlexValidationVariables,
			Map<String, Object> validationResults) {
		ventajaFlexFieldManager.validatePreFlexField(requestFlexFields, preFlexValidationVariables, validationResults);
		BigDecimal packageFcAmount = (BigDecimal) validationResults.get(ValidationResultKey.PACKAGE_FC_AMOUNT.getName());
		if (packageFcAmount != null) {
			validationResults.put(ValidationResultKey.PREFLEXCALL_COMPLETE.getName(), Boolean.TRUE);
		}
	}

}
