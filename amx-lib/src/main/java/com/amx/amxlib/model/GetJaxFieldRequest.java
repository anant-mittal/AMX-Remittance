package com.amx.amxlib.model;

import com.amx.jax.model.response.jaxfield.JaxFieldEntity;

public class GetJaxFieldRequest {

	JaxCondition condition;

	JaxFieldEntity entity;

	public GetJaxFieldRequest(JaxCondition condition, JaxFieldEntity entity) {
		super();
		this.condition = condition;
		this.entity = entity;
	}

	public GetJaxFieldRequest(JaxFieldEntity jaxFieldEntity) {
		super();
		this.entity = jaxFieldEntity;
	}

	public GetJaxFieldRequest() {
		super();
	}

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
