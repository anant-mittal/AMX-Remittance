package com.amx.jax.repository.compliance;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.compliance.HighValueAMLAuthViewLocal;

public interface HighValueAMLAuthViewLocalRepo extends CrudRepository<HighValueAMLAuthViewLocal, Serializable> {

	List<HighValueAMLAuthViewLocal> findAll();
}
