package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.DeviceStateInfo;

public interface DeviceStateRepository extends CrudRepository<DeviceStateInfo, Serializable> {

	}
