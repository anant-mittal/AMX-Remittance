package com.amx.jax.client.task;

public class ComplianceNotificationTaskMessageDto extends AbstractNotificationTaskMessageDto {

	String firstName;
	String documentTypes;
	String lastName;
	String identityInt;
	String transactionNumber;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(String documentTypes) {
		this.documentTypes = documentTypes;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

}
