package com.amx.jax.model;

import java.beans.Transient;
import java.math.BigDecimal;

public interface IResourceEntity {

	@Transient
	public BigDecimal getResourceId();

	@Transient
	public String getResourceName();

	@Transient
	public String getResourceCode();
}
