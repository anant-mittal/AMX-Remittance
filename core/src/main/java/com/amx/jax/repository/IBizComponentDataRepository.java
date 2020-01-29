package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BizComponentData;

public interface IBizComponentDataRepository extends CrudRepository<BizComponentData, Serializable> {

	public BizComponentData findBycomponentCode(String componentCode);
	
	/** added by Rabil on 20 Jan 2020*/
	public BizComponentData findByComponentDataId(BigDecimal componentDataId);
}
