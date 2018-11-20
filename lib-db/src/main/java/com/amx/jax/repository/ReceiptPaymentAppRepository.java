package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.amx.jax.dbmodel.ReceiptPaymentApp;


@Transactional
public interface ReceiptPaymentAppRepository extends CrudRepository<ReceiptPaymentApp, BigDecimal>{

	
	
	@Query("select appl from ReceiptPaymentApp appl where appl.customerId=:customerId and appl.foreignCurrencyId =:currencyId and trunc(sysdate)=trunc(createdDate) "
			+ " and NVL(applicationStatus,' ') NOT IN('T')")
	public List<ReceiptPaymentApp> deActivateNotUsedApplication(@Param("customerId") BigDecimal customerId,@Param("currencyId") BigDecimal currencyId);


}
