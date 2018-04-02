package com.amx.amxlib.model.request;

import com.amx.amxlib.constant.JaxFieldEntity;
import com.amx.amxlib.model.JaxCondition;

public class GetJaxFieldRequest {

	JaxCondition condition;

	JaxFieldEntity entity;

	@Override
	public String toString() {
		return "GetJaxFieldRequest [condition=" + condition + ", entity=" + entity + "]";
	}

	public JaxCondition getCondition() {
		return condition;
	}

	public void setCondition(JaxCondition condition) {
		this.condition = condition;
	}

	public JaxFieldEntity getEntity() {
		return entity;
	}

	public void setEntity(JaxFieldEntity entity) {
		this.entity = entity;
	}

}
