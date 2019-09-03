package com.amx.jax.repository.remittance;

/**
 * @author rabil
 */
import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RemittanceTransactionSplitting;

public interface RemittanceTrnxSplitRepository extends CrudRepository<RemittanceTransactionSplitting, Serializable> {

}
