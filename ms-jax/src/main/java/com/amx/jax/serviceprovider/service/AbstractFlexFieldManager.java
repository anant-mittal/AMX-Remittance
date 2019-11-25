package com.amx.jax.serviceprovider.service;

import java.util.List;
import java.util.Map;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.remittance.AdditionalDataDisplayView;
import com.amx.jax.dbmodel.remittance.ViewParameterDetails;
import com.amx.jax.model.response.jaxfield.JaxConditionalFieldDto;
import com.amx.jax.model.response.remittance.FlexFieldDto;

public abstract class AbstractFlexFieldManager {

	public static final String FLEX_FIELD_MANAGER_BEAN_SUFFIX = "_FLEX_FIELD";

	public Map<String, Object> managePreFlexFields(List<AdditionalDataDisplayView> additionalDataRequired,
			Map<String, FlexFieldDto> requestFlexFields, ViewParameterDetails cashSetUp, BenificiaryListView beneficaryDetails,
			List<JaxConditionalFieldDto> requiredFlexFields) {
		return null;
	}

	public abstract void validatePreFlexField(Map<String, FlexFieldDto> requestFlexFields, Map<String, Object> preFlexValidationVariables,
			Map<String, Object> validationResults);
}
