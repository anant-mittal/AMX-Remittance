package com.amx.jax.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.VwLoyalityEncash;

@Transactional
public interface VwLoyalityEncashRepository extends CrudRepository<VwLoyalityEncash, BigDecimal> {

	@Query(value = "SELECT SUM(NVL(LOYALTY_POINTS_ENCASHED,0)) "
			+ "            FROM   EX_APPL_TRNX" + "            WHERE  CUSTOMER_ID                =   ?1"
			+ "            AND    APPLICATION_STATUS         IS  NULL"
			+ "            AND    TRANSACTION_FINANCE_YEAR   IS  NULL"
			+ "            AND    TRANSACTION_DOCUMENT_NO    IS  NULL"
			+ "            AND    TRUNC(DOCUMENT_DATE)       =   TRUNC(SYSDATE)"
			+ "            AND    NVL(ISACTIVE,' ')          =   'Y'", nativeQuery = true)
	public BigDecimal getTodaysLoyalityPointsEncashed(BigDecimal customerId);
}
