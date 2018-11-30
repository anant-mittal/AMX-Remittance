package com.amx.jax.model;

import java.math.BigDecimal;

import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Prashant
 *
 */
public class ResourceDTO implements IResourceEntity {

	/**
	 * db identifier of resource
	 */
	@JsonProperty("_id")
	BigDecimal resourceId;

	/**
	 * name of resource
	 */
	@JsonProperty("_name")
	String resourceName;

	@JsonProperty("_code")
	String resourceCode;

	public ResourceDTO() {

	}

	public ResourceDTO(BigDecimal resourceId, String resourceName, String resourceCode) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceCode = resourceCode;
	}

	public ResourceDTO(BigDecimal resourceId, String resourceName) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
	}

	public BigDecimal resourceId() {
		return resourceId;
	}

	public void setResourceId(BigDecimal resourceId) {
		this.resourceId = resourceId;
	}

	public String resourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setResourceName(Object resourceName) {
		this.resourceName = ArgUtil.parseAsString(resourceName);
	}

	public String resourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public void setResourceCode(Object resourceCode) {
		this.resourceCode = ArgUtil.parseAsString(resourceCode);
	}

	public static ResourceDTO create(IResourceEntity entity) {
		ResourceDTO dto = new ResourceDTO();
		dto.setResourceId(entity.resourceId());
		dto.setResourceName(entity.resourceName());
		dto.setResourceCode(entity.resourceCode());
		return dto;
	}

	public BigDecimal getResourceId() {
		return resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getResourceCode() {
		return resourceCode;
	}
}
