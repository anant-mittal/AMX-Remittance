package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CollectDetailModel;

public interface ICollectionDetailRepository extends CrudRepository<CollectDetailModel, Serializable>{

}
