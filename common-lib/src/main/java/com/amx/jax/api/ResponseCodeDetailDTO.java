package com.amx.jax.api;

import java.io.Serializable;

import com.amx.jax.dict.PayGCodes.CodeCategory;

public class ResponseCodeDetailDTO implements Serializable {

	private static final long serialVersionUID = -5279804951579408228L;

	private String responseCode; // client
	private String responseDesc; // client
	private String almullaErrorCode;
	private CodeCategory category;
	private String type;
	private String status;
	
	// Type ?
	// Status

	// Map <String, String> clientResp

	
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}

	public String getAlmullaErrorCode() {
		return almullaErrorCode;
	}

	public void setAlmullaErrorCode(String almullaErrorCode) {
		this.almullaErrorCode = almullaErrorCode;
	}

	public CodeCategory getCategory() {
		return category;
	}

	public void setCategory(CodeCategory category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
