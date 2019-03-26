package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CollectDetailModel;

public interface ICollectionDetailRepository extends CrudRepository<CollectDetailModel, Serializable>{

	public List<CollectDetailModel> findByDocumentNoAndDocumentCodeAndDocumentFinanceYear(BigDecimal documentNo,BigDecimal documentCode,BigDecimal documentFinanceYear);
}
