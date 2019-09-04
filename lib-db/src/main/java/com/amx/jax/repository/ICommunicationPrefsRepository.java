package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CommunicationPrefsModel;

public interface ICommunicationPrefsRepository extends CrudRepository<CommunicationPrefsModel, Serializable> {

	@Override
	public List<CommunicationPrefsModel> findAll();
}
