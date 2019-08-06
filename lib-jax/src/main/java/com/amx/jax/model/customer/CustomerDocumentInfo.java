package com.amx.jax.model.customer;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.model.customer.document.CustomerDocUtil;

public class CustomerDocumentInfo {

	DocumentImageRenderType documentRenderType;
	String documentUrl;
	String documentString;
	Date uploadedDate;
	String documentType;
	String documentTypeDesc;
	String documentFormat;
	String documentCategory;
	String documentCategoryDesc;
	Date expiryDate;
	BigDecimal uploadRefId;

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
		this.documentTypeDesc = CustomerDocUtil.getDescription(documentType);
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
		this.documentCategoryDesc = CustomerDocUtil.getDescription(documentCategory);
	}

	public String getDocumentTypeDesc() {
		return documentTypeDesc;
	}

	public void setDocumentTypeDesc(String documentTypeDesc) {
		this.documentTypeDesc = documentTypeDesc;
	}

	public String getDocumentCategoryDesc() {
		return documentCategoryDesc;
	}

	public void setDocumentCategoryDesc(String documentCategoryDesc) {
		this.documentCategoryDesc = documentCategoryDesc;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public BigDecimal getUploadRefId() {
		return uploadRefId;
	}

	public void setUploadRefId(BigDecimal uploadRefId) {
		this.uploadRefId = uploadRefId;
	}

}
