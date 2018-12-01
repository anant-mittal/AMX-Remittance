package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CollectionModel;

public interface ICollectionRepository extends CrudRepository<CollectionModel, Serializable>{
	
	public List<CollectionModel> findByDocumentNoAndDocumentFinanceYear(BigDecimal collectDocNo,BigDecimal collectDocYear);

}
