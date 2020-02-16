package com.amx.jax.complaince;

import com.amx.jax.model.ResourceDTO;

public class IndicatorParamDto extends ResourceDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String recordId;;

	private String indicatorCode;

	private String indicatorDesc;
	
	private ResourceDTO resourceDto;

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getIndicatorCode() {
		return indicatorCode;
	}

	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}

	public String getIndicatorDesc() {
		return indicatorDesc;
	}

	public void setIndicatorDesc(String indicatorDesc) {
		this.indicatorDesc = indicatorDesc;
	}

	public ResourceDTO getResourceDto() {
		return resourceDto;
	}

	public void setResourceDto(ResourceDTO resourceDto) {
		this.resourceDto = resourceDto;
	}
	
	@Override
	public String getResourceName() {
		return this.indicatorDesc;
	}
	@Override
	public String getResourceCode() {
		return this.indicatorCode;
	}
}
