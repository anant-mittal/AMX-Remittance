package com.amx.jax.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ReceiptPayment;

public interface ReceiptPaymentRespository extends CrudRepository<ReceiptPayment, BigDecimal>{
	
	@Modifying
	@Query("update ReceiptPayment rp set rp.deliveryDetSeqId = ?2 where rp.receiptId = ?1 ")
	public void updateDeliveryDetails(BigDecimal receiptPaymentId,BigDecimal deliveryDetailsId);
	
	@Modifying
	@Query("update ReceiptPayment rp set rp.inventoryId = ?2 where rp.receiptId = ?1 ")
	public void updateInventoryId(BigDecimal receiptPaymentId,String inventoryId);

}
