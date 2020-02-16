package com.amx.jax.complaince;

import com.amx.jax.model.ResourceDTO;

public class ReasonParamDto extends ResourceDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String recordId;;

	private String reasonCode;

	private String reasonDesc;
	
	private ResourceDTO resourceDto;

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public ResourceDTO getResourceDto() {
		return resourceDto;
	}

	public void setResourceDto(ResourceDTO resourceDto) {
		this.resourceDto = resourceDto;
	}
	
	@Override
	public String getResourceName() {
		return this.reasonDesc;
	}
	@Override
	public String getResourceCode() {
		return this.reasonCode;
	}

}
