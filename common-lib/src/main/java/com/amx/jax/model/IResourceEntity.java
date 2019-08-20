package com.amx.jax.model;

import java.math.BigDecimal;

public interface IResourceEntity {

	public BigDecimal resourceId();

	public String resourceName();

	public String resourceCode();

	public default Object resourceValue() {
		return null;
	}

}