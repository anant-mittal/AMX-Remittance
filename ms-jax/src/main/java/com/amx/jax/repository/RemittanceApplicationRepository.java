package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.remittance.RemittanceApplication;

@Transactional
public interface RemittanceApplicationRepository extends CrudRepository<RemittanceApplication, BigDecimal> {

	@Query("select ra from RemittanceApplication ra where ra.paymentId=:paymentId")
	public List<RemittanceApplication> fetchRemitApplTrnxRecordsByPayId(@Param("paymentId") String paymentId);

//	@Query(name = "UPDATE EX_APPL_TRNX set ISACTIVE ='D' , APPLICATION_STATUS=null  "
//			+ "WHERE CUSTOMER_ID=?1 AND trunc(sysdate)=trunc(CREATED_DATE) AND NVL(RESULT_CODE,' ') NOT IN('CAPTURED','APPROVED')", nativeQuery = true)
//	@Modifying
//	public void deActivateApplication(@Param("customerId") BigDecimal customerId);

}
