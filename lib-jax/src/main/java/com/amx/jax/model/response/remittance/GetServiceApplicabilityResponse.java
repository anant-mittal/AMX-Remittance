package com.amx.jax.model.response.remittance;

public class GetServiceApplicabilityResponse {

	String fieldName;
	String fieldDescription;
	boolean mandatory;

	
	public GetServiceApplicabilityResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GetServiceApplicabilityResponse(String fieldName, String fieldDescription, boolean mandatory) {
		super();
		this.fieldName = fieldName;
		this.fieldDescription = fieldDescription;
		this.mandatory = mandatory;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldDescription() {
		return fieldDescription;
	}

	public void setFieldDescription(String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldDescription == null) ? 0 : fieldDescription.hashCode());
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + (mandatory ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GetServiceApplicabilityResponse other = (GetServiceApplicabilityResponse) obj;
		if (fieldDescription == null) {
			if (other.fieldDescription != null)
				return false;
		} else if (!fieldDescription.equals(other.fieldDescription))
			return false;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (mandatory != other.mandatory)
			return false;
		return true;
	}

}
