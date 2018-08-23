package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BizComponentData;

public interface IBizComponentDataRepository extends CrudRepository<BizComponentData, Serializable> {

	public BizComponentData findBycomponentCode(String componentCode);
}
