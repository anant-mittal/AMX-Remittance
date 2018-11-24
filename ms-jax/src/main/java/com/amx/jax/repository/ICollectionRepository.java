package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CollectionModel;

public interface ICollectionRepository extends CrudRepository<CollectionModel, Serializable>{

}
