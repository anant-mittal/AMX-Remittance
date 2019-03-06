package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

public interface IRemittanceTransactionRepository extends CrudRepository<RemittanceTransaction, Serializable> {

}
