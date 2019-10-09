package com.amx.jax.model.customer.document;

import java.util.List;

public class CustomerDocCatTypeDto {

	CustomerDocumentCategoryDto documentCategoryDto;
	List<CustomerDocumentTypeDto> documentTypesDto;
	
	public CustomerDocCatTypeDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerDocCatTypeDto(CustomerDocumentCategoryDto documentCategoryDto, List<CustomerDocumentTypeDto> documentTypesDto) {
		super();
		this.documentCategoryDto = documentCategoryDto;
		this.documentTypesDto = documentTypesDto;
	}
	public CustomerDocumentCategoryDto getDocumentCategoryDto() {
		return documentCategoryDto;
	}
	public void setDocumentCategoryDto(CustomerDocumentCategoryDto documentCategoryDto) {
		this.documentCategoryDto = documentCategoryDto;
	}
	public List<CustomerDocumentTypeDto> getDocumentTypesDto() {
		return documentTypesDto;
	}
	public void setDocumentTypesDto(List<CustomerDocumentTypeDto> documentTypesDto) {
		this.documentTypesDto = documentTypesDto;
	}

	
}
