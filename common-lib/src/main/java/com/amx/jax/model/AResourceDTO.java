package com.amx.jax.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amx.utils.ArgUtil;
import com.amx.utils.EntityDtoUtil;
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
public abstract class AResourceDTO<T extends AResourceDTO<T>> implements IResourceEntity, Serializable {

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

	@JsonProperty("_local_name")
	protected String resourceLocalName;

	/**
	 * A short name for resource, eg:- ISO3 codes for Countries
	 */
	@JsonProperty("_code")
	protected String resourceCode;

	@JsonProperty("_value")
	protected Object resourceValue;

	public AResourceDTO() {

	}

	public AResourceDTO(IResourceEntity resource) {
		this.resourceId = resource.resourceId();
		this.resourceName = resource.resourceName();
		this.resourceCode = resource.resourceCode();
		this.resourceLocalName = resource.resourceLocalName();
	}

	public AResourceDTO(BigDecimal resourceId, String resourceName, String resourceCode) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceCode = resourceCode;
	}

	public AResourceDTO(BigDecimal resourceId, String resourceName) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
	}

	@Override
	public BigDecimal resourceId() {
		return resourceId;
	}

	public void setResourceId(BigDecimal resourceId) {
		this.resourceId = resourceId;
	}

	public BigDecimal getResourceId() {
		return resourceId;
	}

	@Override
	public String resourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(Object resourceName) {
		this.resourceName = ArgUtil.parseAsString(resourceName);
	}

	@Override
	public String resourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public void setResourceCode(Object resourceCode) {
		this.resourceCode = ArgUtil.parseAsString(resourceCode);
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public Object getResourceValue() {
		return resourceValue;
	}

	public void setResourceValue(Object resourceValue) {
		this.resourceValue = resourceValue;
	}

	public abstract T newInstance();

	@SuppressWarnings("unchecked")
	public T importFrom(IResourceEntity entity) {
		this.resourceId = entity.resourceId();
		this.resourceCode = entity.resourceCode();
		this.resourceName = entity.resourceName();
		this.resourceLocalName = entity.resourceLocalName();
		EntityDtoUtil.entityToDto(entity, this);
		return (T) this;
	}

	public List<T> importFrom(List<? extends IResourceEntity> entityList) {
		List<T> list = new ArrayList<T>();
		for (IResourceEntity entity : entityList) {
			T dto = this.newInstance().importFrom(entity);
			list.add(dto);
		}
		return list;
	}

	public String getResourceLocalName() {
		return resourceLocalName;
	}

	public void setResourceLocalName(String resourceLocalName) {
		this.resourceLocalName = resourceLocalName;
	}

	@Override
	public String resourceLocalName() {
		return resourceLocalName;
	}

}
