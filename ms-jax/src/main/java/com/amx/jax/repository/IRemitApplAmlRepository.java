package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;

public interface IRemitApplAmlRepository extends CrudRepository<RemitApplAmlModel, Serializable>{
	
	public RemitApplAmlModel findByExRemittanceAppfromAml(RemittanceApplication remittanceApplicationId);

}
