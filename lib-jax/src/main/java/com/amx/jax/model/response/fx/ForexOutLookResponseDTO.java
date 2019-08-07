package com.amx.jax.model.response.fx;

import com.amx.jax.model.ResourceDTO;

public class ForexOutLookResponseDTO extends ResourceDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4292890337204886660L;
	
	private String message;
	private String curpairName;
	private String modifiedDate;
	
	
	
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCurpairName() {
		return curpairName;
	}
	public void setCurpairName(String curpairName) {
		this.curpairName = curpairName;
	}
	
	
	
	

}
