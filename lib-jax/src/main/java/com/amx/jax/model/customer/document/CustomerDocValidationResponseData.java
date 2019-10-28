package com.amx.jax.model.customer.document;

public class CustomerDocValidationResponseData {

	String documentType;
	String documentCategory;

	
	public CustomerDocValidationResponseData(String documentType, String documentCategory) {
		super();
		this.documentType = documentType;
		this.documentCategory = documentCategory;
	}
	
	

	public CustomerDocValidationResponseData(String documentCategory) {
		super();
		this.documentCategory = documentCategory;
	}



	public CustomerDocValidationResponseData() {
		super();
	}



	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}
}
