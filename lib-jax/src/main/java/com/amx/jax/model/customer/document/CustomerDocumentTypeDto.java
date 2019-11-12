package com.amx.jax.model.customer.document;

public class CustomerDocumentTypeDto {

	String documentType;
	String documentTypeDesc;

	public CustomerDocumentTypeDto() {
		super();
	}

	public CustomerDocumentTypeDto(String documentType) {
		super();
		this.documentType = documentType;
		this.documentTypeDesc = CustomerDocUtil.getDescription(documentType);
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentTypeDesc() {
		return documentTypeDesc;
	}

	public void setDocumentTypeDesc(String documentTypeDesc) {
		this.documentTypeDesc = documentTypeDesc;
	}

}
