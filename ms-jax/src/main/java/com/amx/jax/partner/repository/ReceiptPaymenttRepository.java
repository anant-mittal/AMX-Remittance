package com.amx.jax.partner.repository;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import com.amx.jax.dbmodel.ReceiptPayment;



public interface ReceiptPaymenttRepository extends CrudRepository<ReceiptPayment, BigDecimal>{
	
	static final Logger LOGGER = Logger.getLogger(ReceiptPaymenttRepository.class);
	
	@Query("select distinct colDocNo,colDocFyr from ReceiptPayment where deliveryDetSeqId=?1")
	List<Object[]> findByDeliveryDetSeqId(BigDecimal deliveryDetSeqId);
	
	
	@Query("select colDocNo from ReceiptPayment where deliveryDetSeqId=?1 GROUP BY colDocNo")
    BigDecimal findcolDocNo(BigDecimal deliveryDetSeqId);
	
	
	@Query(value="select * from EX_RECEIPT_PAYMENT where DELIVERY_DET_SEQ_ID=?1", nativeQuery = true)
	List<ReceiptPayment> findDeliveryDetSeqId(BigDecimal deliveryDetSeqId);
	



}
