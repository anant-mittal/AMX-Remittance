package com.amx.jax.constant;

import com.amx.jax.model.response.jaxfield.JaxFieldType;

public enum FlexFieldBehaviour {
	PRE_DEFINED(JaxFieldType.SELECT), USER_ENTERABLE(JaxFieldType.TEXT), SELECT_DATE(
			JaxFieldType.DATE), SELECT_DATETIME(JaxFieldType.DATETIME), DATE(JaxFieldType.DATE),
	/* represents date range field */
	START_MONTH(JaxFieldType.DATE_RANGE_START_MONTH);

	JaxFieldType fieldType;

	private FlexFieldBehaviour(JaxFieldType fieldType) {
		this.fieldType = fieldType;
	}

	public JaxFieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(JaxFieldType fieldType) {
		this.fieldType = fieldType;
	}

}
