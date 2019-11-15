package com.amx.jax.model.response.jaxfield;

import java.util.List;

import com.amx.jax.model.ResourceDTO;

public class JaxFieldValueDto extends ResourceDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object id;
	Object optLable;
	Object value;
	private String localName;
	private String packageDescription;

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getOptLable() {
		return optLable;
	}

	public void setOptLable(Object optLable) {
		this.optLable = optLable;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	
	
	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	@Override
	public String getResourceLocalName() {
		return this.localName;

	}
	
}
