package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterModel;

public interface BankMasterRepository extends CrudRepository<BankMasterModel, BigDecimal> {

	public List<BankMasterModel> findBybankCountryId(BigDecimal bankCountryId);
	
}
