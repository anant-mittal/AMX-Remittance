package com.amx.jax.model.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

public class ImageSubmissionRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<String> image;
	
	@ApiMockModelProperty(example="Y")
	private String politicallyExposed;
	
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
	private Date identityExpiredDate;
	
	private BigDecimal documentUploadReferenceId;
	
	private String uploadType;

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	

	public BigDecimal getDocumentUploadReferenceId() {
		return documentUploadReferenceId;
	}

	public void setDocumentUploadReferenceId(BigDecimal documentUploadReferenceId) {
		this.documentUploadReferenceId = documentUploadReferenceId;
	}

	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}

	public String getPoliticallyExposed() {
		return politicallyExposed;
	}

	public void setPoliticallyExposed(String politicallyExposed) {
		this.politicallyExposed = politicallyExposed;
	}

	public Date getIdentityExpiredDate() {
		return identityExpiredDate;
	}

	public void setIdentityExpiredDate(Date identityExpiredDate) {
		this.identityExpiredDate = identityExpiredDate;
	}

}
