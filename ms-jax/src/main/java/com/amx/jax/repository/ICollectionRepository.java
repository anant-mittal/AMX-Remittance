package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CollectionMdlv1;

public interface ICollectionRepository extends CrudRepository<CollectionMdlv1, Serializable>{
	
	public List<CollectionMdlv1> findByDocumentNoAndDocumentFinanceYear(BigDecimal collectDocNo,BigDecimal collectDocYear);
	
	@Query("select c from CollectionMdlv1 c where c.documentNo=?1 and c.documentFinanceYear=?2 and c.documentCode =?3")
	public CollectionMdlv1 getCollectionDetails(BigDecimal collectDocNo,BigDecimal collectDocYear, BigDecimal colldocCode);

}
