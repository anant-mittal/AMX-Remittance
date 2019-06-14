package com.amx.jax.dbmodel.customer;

public enum CustomerDocumentType {

	CIVIL_ID("CIVIL ID"), PASSPORT("PASSPORT"), GCC_ID("GCC NATIONAL ID"), BEDOON_ID("BEDOUIN");

	String identityType;

	private CustomerDocumentType() {
	}

	private CustomerDocumentType(String identityType) {
		this.identityType = identityType;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public static CustomerDocumentType getCustomerDocTypeByIdentityType(String identityType) {
		CustomerDocumentType ret = null;
		for (CustomerDocumentType type : CustomerDocumentType.values()) {
			if (type.getIdentityType() != null && type.getIdentityType().equals(identityType)) {
				ret = type;
				break;
			}
		}
		return ret;
	}
}
