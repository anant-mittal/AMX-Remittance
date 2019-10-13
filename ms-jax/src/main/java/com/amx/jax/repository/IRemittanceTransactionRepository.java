package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.remittance.RemittanceTransaction;

public interface IRemittanceTransactionRepository extends CrudRepository<RemittanceTransaction, Serializable> {

	public List<RemittanceTransaction> findByCollectionDocIdAndCollectionDocFinanceYearAndCollectionDocumentNo(BigDecimal collectionDocId,BigDecimal collectionDocFinanceYear,BigDecimal collectionDocumentNo);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update RemittanceTransaction remit set remit.deliveryInd =:deliveryInd,remit.remarks =:remarks,remit.bankReference =:bankReference where remit.remittanceTransactionId=:remittanceTransactionId")
	public void updateDeliveryIndRemarksBySP(@Param("deliveryInd") String deliveryInd,@Param("remarks") String remarks,@Param("remittanceTransactionId") BigDecimal remittanceTransactionId,@Param("bankReference") String bankReference);
	
}
