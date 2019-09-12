package com.amx.jax.repository.annualtransaction;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.annualtransaction.AnnualTransactionFactorModel;

public interface AnnualTransactionFactorRepository extends CrudRepository<AnnualTransactionFactorModel, Serializable>{
	
}
