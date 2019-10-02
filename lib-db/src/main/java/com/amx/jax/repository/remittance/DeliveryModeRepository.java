package com.amx.jax.repository.remittance;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.DeliveryMode;

public interface DeliveryModeRepository extends CrudRepository<DeliveryMode, Serializable> {

}
