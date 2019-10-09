package com.amx.jax.model.customer.document;

public class CustomerDocumentCategoryDto {

	String documentCategory;
	String documentCategoryDesc;
	

	public CustomerDocumentCategoryDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerDocumentCategoryDto(String documentCategory) {
		super();
		this.documentCategory = documentCategory;
		this.documentCategoryDesc = CustomerDocUtil.getDescription(documentCategory);
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

	public String getDocumentCategoryDesc() {
		return documentCategoryDesc;
	}

	public void setDocumentCategoryDesc(String documentCategoryDesc) {
		this.documentCategoryDesc = documentCategoryDesc;
	}
	
}
