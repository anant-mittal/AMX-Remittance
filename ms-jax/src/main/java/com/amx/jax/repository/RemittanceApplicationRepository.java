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
	
	@Query("select appl from RemittanceApplication appl where appl.fsCustomer=:customerId and trunc(sysdate)=trunc(createdDate) "
			+ " and NVL(resultCode,' ') NOT IN('CAPTURED','APPROVED')")
	public List<RemittanceApplication> deActivateNotUsedApplication(@Param("customerId") BigDecimal customerId);

}
