package com.amx.jax.model.response.fx;

import java.util.Date;

import com.amx.jax.model.ResourceDTO;

public class ForexOutLookResponseDTO extends ResourceDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4292890337204886660L;
	private String isActive;
	private Date modifiedDate;
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	

}
