package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;

public interface IRemitApplAmlRepository extends CrudRepository<RemitApplAmlModel, Serializable>{
	
	public List<RemitApplAmlModel> findByExRemittanceAppfromAml(RemittanceApplication remittanceApplicationId);

}
