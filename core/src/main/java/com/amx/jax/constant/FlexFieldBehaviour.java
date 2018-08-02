package com.amx.jax.constant;

import com.amx.amxlib.constant.JaxFieldType;

public enum FlexFieldBehaviour {
	PRE_DEFINED(JaxFieldType.SELECT), USER_ENTERABLE(JaxFieldType.TEXT);

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
