package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CollectionModel;

public interface ICollectionRepository extends CrudRepository<CollectionModel, Serializable>{
	
	public List<CollectionModel> findByDocumentNoAndDocumentFinanceYear(BigDecimal collectDocNo,BigDecimal collectDocYear);
	
	@Query("select c from CollectionModel c where c.documentNo=?1 and c.documentFinanceYear=?2 and c.documentCode =?3")
	public CollectionModel getCollectionDetails(BigDecimal collectDocNo,BigDecimal collectDocYear, BigDecimal colldocCode);

}
