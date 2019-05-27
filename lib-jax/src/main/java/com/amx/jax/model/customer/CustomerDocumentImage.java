package com.amx.jax.model.customer;

import java.time.LocalDateTime;

public class CustomerDocumentImage {

	DocumentImageRenderType documentImageRenderType;
	String imageUrl;
	String imageString;
	LocalDateTime uploadedDate;
	String documentType;

	public DocumentImageRenderType getDocumentImageRenderType() {
		return documentImageRenderType;
	}

	public void setDocumentImageRenderType(DocumentImageRenderType documentImageRenderType) {
		this.documentImageRenderType = documentImageRenderType;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageString() {
		return imageString;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

	public LocalDateTime getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(LocalDateTime uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

}
