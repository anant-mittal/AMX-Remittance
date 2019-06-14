package com.amx.jax.model.response.remittance;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BsbApiResponse {

	  @JsonProperty("is_valid_account")
	 Boolean is_valid_account;
	  @JsonProperty("is_technical_error_while_calling_3rd_party") 
	 Boolean techError;
	  @JsonProperty("response_code") 
	 String  responseCode;
	  @JsonProperty("response_desc") 
	 String  responseDesc;
	public Boolean getIs_valid_account() {
		return is_valid_account;
	}
	public void setIs_valid_account(Boolean is_valid_account) {
		this.is_valid_account = is_valid_account;
	}
	public Boolean getTechError() {
		return techError;
	}
	public void setTechError(Boolean techError) {
		this.techError = techError;
	}
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
	
}

