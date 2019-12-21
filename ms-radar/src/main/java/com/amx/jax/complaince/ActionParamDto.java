package com.amx.jax.complaince;

import com.amx.jax.model.ResourceDTO;

public class ActionParamDto extends ResourceDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String recordId;;

	private String actionCode;

	private String actionDesc;
	
	private ResourceDTO resourceDto;

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public ResourceDTO getResourceDto() {
		return resourceDto;
	}

	public void setResourceDto(ResourceDTO resourceDto) {
		this.resourceDto = resourceDto;
	}
	
	@Override
	public String getResourceName() {
		return this.actionDesc;
	}
	@Override
	public String getResourceCode() {
		return this.actionCode;
	}
}
