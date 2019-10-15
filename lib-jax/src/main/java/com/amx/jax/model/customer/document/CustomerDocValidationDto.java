package com.amx.jax.model.customer.document;

import java.util.List;

public class CustomerDocValidationDto {

	List<CustomerDocValidationResponseData> missingDocuments;

	public List<CustomerDocValidationResponseData> getMissingDocuments() {
		return missingDocuments;
	}

	public void setMissingDocuments(List<CustomerDocValidationResponseData> missingDocuments) {
		this.missingDocuments = missingDocuments;
	}

}
