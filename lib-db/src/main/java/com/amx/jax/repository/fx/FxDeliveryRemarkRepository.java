package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.FxDeliveryRemark;

public interface FxDeliveryRemarkRepository extends CrudRepository<FxDeliveryRemark, Serializable> {

	public List<FxDeliveryRemark> findByIsActive(String isActive);
}
