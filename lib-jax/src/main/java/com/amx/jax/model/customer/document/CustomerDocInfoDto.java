package com.amx.jax.model.customer.document;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerDocInfoDto {

	@ApiMockModelProperty(example = "INCOME_PROOF")
	String documentCategory;

	@ApiMockModelProperty(example = "LOAN_STATEMENT")
	String documentType;

	public CustomerDocInfoDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerDocInfoDto(String documentCategory, String documentType) {
		super();
		this.documentCategory = documentCategory;
		this.documentType = documentType;
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}


}
