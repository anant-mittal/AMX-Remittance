package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.partner.RemitApplSrvProv;

public interface IRemitApplSrvProvRepository extends CrudRepository<RemitApplSrvProv, Serializable>{
	
	public RemitApplSrvProv findByRemittanceApplicationId(BigDecimal remittanceApplicationId);

}
