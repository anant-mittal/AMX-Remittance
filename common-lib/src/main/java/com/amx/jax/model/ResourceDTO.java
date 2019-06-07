package com.amx.jax.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Use this class to return response,
 * 
 * Entity should be create by implmenting IResourceEntity and use
 * {@link ResourceDTO.create(IResourceEntity))} to create its instance
 * 
 * @see {@link com.amx.jax.model.IResourceEntity}
 * @author lalittanwar
 * @author Prashant
 *
 */
public class ResourceDTO implements IResourceEntity, Serializable {

	private static final long serialVersionUID = -5802360864645036772L;

	/**
	 * db identifier of resource
	 */
	@JsonProperty("_id")
	protected BigDecimal resourceId;

	/**
	 * name of resource
	 */
	@JsonProperty("_name")
	protected String resourceName;

	/**
	 * A short name for resource, eg:- ISO3 codes for Countries
	 */
	@JsonProperty("_code")
	protected String resourceCode;

	public ResourceDTO() {

	}

	public ResourceDTO(IResourceEntity resource) {
		this.resourceId = resource.resourceId();
		this.resourceName = resource.resourceName();
		this.resourceCode = resource.resourceCode();
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

	public void importFrom(IResourceEntity entity) {
		this.resourceId = entity.resourceId();
		this.resourceCode = entity.resourceCode();
		this.resourceName = entity.resourceName();
	}

	public static ResourceDTO create(IResourceEntity entity) {
		ResourceDTO dto = new ResourceDTO();
		dto.importFrom(entity);
		return dto;
	}

	public static List<ResourceDTO> create(List<IResourceEntity> entityList) {
		List<ResourceDTO> list = new ArrayList<ResourceDTO>();
		for (IResourceEntity entity : entityList) {
			ResourceDTO dto = create(entity);
			list.add(dto);
		}
		return list;
	}
	
	public static List<ResourceDTO> createList(List<? extends IResourceEntity> entityList) {
		List<ResourceDTO> list = new ArrayList<ResourceDTO>();
		for (IResourceEntity entity : entityList) {
			ResourceDTO dto = create(entity);
			list.add(dto);
		}
		return list;
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
