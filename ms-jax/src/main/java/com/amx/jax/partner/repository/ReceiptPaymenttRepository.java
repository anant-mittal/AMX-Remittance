package com.amx.jax.partner.repository;

import java.math.BigDecimal;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.dbmodel.ReceiptPayment;

public interface ReceiptPaymenttRepository extends CrudRepository<ReceiptPayment, BigDecimal>{
	
	public ReceiptPayment findByDeliveryDetSeqId(BigDecimal deliveryDetSeqId);


}
