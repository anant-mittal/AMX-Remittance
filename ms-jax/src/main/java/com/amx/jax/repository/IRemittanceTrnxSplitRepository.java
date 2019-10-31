package com.amx.jax.repository;

/**
 * @author rabil
 */
import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RemittanceTransactionSplitting;

public interface IRemittanceTrnxSplitRepository extends CrudRepository<RemittanceTransactionSplitting, Serializable> {

	//List<RemittanceTransactionSplitting> findBy

}

