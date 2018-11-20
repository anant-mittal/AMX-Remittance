package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.FxDeliveryDetailsModel;

public interface FxDeliveryDetailsRepository extends CrudRepository<FxDeliveryDetailsModel, Serializable>{

}
