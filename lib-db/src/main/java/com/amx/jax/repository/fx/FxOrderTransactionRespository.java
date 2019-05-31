package com.amx.jax.repository.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.fx.FxOrderTransactionModel;
import com.amx.jax.repository.DaoRepository;

public interface FxOrderTransactionRespository extends DaoRepository<FxOrderTransactionModel, Serializable>{
	
	@Query("select t from FxOrderTransactionModel t where t.customerId=:customerId")
	public List<FxOrderTransactionModel> getFxOrderTrnxList(@Param("customerId") BigDecimal customerId);
	
	@Query("select t from FxOrderTransactionModel t where t.customerId=:customerId and  t.collectionDocumentNo=:collectionDocumentNo and t.collectionDocumentCode=2 and t.collectionDocumentFinYear=:collectionDocumentFinYear")
	public List<FxOrderTransactionModel> getFxOrderTrnxListByCollectionDocNumber(@Param("customerId") BigDecimal customerId,@Param("collectionDocumentNo") BigDecimal collectionDocumentNo,@Param("collectionDocumentFinYear") BigDecimal collectionDocumentFinYear);
	
	
	@Query("select t from FxOrderTransactionModel t where t.customerId=:customerId and t.pagDetSeqId=:pagDetSeqId")
	public List<FxOrderTransactionModel> getFxOrderTrnxDetailsByPagSeqId(@Param("customerId") BigDecimal customerId,@Param("pagDetSeqId") BigDecimal pagDetSeqId);
	
	@Query(value = "select sd from FxOrderTransactionModel sd where sd.transactionReferenceNo= ?1 ")
	public List<FxOrderTransactionModel> searchTransactionRefNo(String transactionReferenceNo);
	
	
}

