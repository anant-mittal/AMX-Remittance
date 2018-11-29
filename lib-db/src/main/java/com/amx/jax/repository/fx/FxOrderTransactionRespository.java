package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;

public interface FxOrderTransactionRespository extends CrudRepository<FxOrderTransactionModel, Serializable>{
	
	@Query("select t from FxOrderTransactionModel t where t.customerId=:customerId and  trunc(t.documentDate) between trunc(sysdate-6*30) and  trunc(sysdate)")
	public List<FxOrderTransactionModel> getFxOrderTrnxList(@Param("customerId") BigDecimal customerId);
	
	@Query("select t from FxOrderTransactionModel t where t.customerId=:customerId and  t.collectionDocumentNo=:collectionDocumentNo and t.collectionDocumentCode=2 and t.collectionDocumentFinYear=:collectionDocumentFinYear")
	public List<FxOrderTransactionModel> getFxOrderTrnxListByCollectionDocNumber(@Param("customerId") BigDecimal customerId,@Param("collectionDocumentNo") BigDecimal collectionDocumentNo,@Param("collectionDocumentFinYear") BigDecimal collectionDocumentFinYear);
	
	
	@Query("select t from FxOrderTransactionModel t where t.customerId=:customerId and t.pagDetSeqId=:pagDetSeqId")
	public List<FxOrderTransactionModel> getFxOrderTrnxDetailsByPagSeqId(@Param("customerId") BigDecimal customerId,@Param("pagDetSeqId") BigDecimal pagDetSeqId);
	
}

