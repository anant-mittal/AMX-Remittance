package com.amx.jax.repository.compliance;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.compliance.HighValueAMLAuthViewFC;

public interface HighValueAMLAuthViewFCRepo extends CrudRepository<HighValueAMLAuthViewFC, Serializable> {

}
