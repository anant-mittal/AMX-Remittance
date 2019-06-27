package com.amx.jax.model.customer;

import java.util.Date;

public class CustomerDocumentInfo {

	DocumentImageRenderType documentRenderType;
	String documentUrl;
	String documentString;
	Date uploadedDate;
	String documentType;
	String documentFormat;
	String documentCategory;

	public DocumentImageRenderType getDocumentRenderType() {
		return documentRenderType;
	}

	public void setDocumentRenderType(DocumentImageRenderType documentRenderType) {
		this.documentRenderType = documentRenderType;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public String getDocumentString() {
		return documentString;
	}

	public void setDocumentString(String documentString) {
		this.documentString = documentString;
	}

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentFormat() {
		return documentFormat;
	}

	public void setDocumentFormat(String documentFormat) {
		this.documentFormat = documentFormat;
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

}
