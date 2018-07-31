package com.amx.jax.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.JaxField;

public interface JaxFieldRepository extends CrudRepository<JaxField, String> {

	public List<JaxField> findByNameIn(List<String> names);
}
