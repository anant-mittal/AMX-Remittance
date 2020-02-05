package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerCartMaster;

public interface CustomerCartRepository extends CrudRepository<CustomerCartMaster, Serializable> {

	@Query("select c1 from CustomerCartMaster c1 where c1.customerId=?1 and trunc(sysdate) = trunc(c1.createdDate)")
	CustomerCartMaster getCartDataByCustId(BigDecimal customerId);

	@Query(value = "SELECT * FROM JAX_CUSTOMER_CART WHERE TRUNC(SYSDATE)=TRUNC(CREATED_DATE) AND CUSTOMER_ID=?1 AND APPLICATION_IDS LIKE %?2%", nativeQuery = true)
	CustomerCartMaster getCartDataByApplAndCustId(BigDecimal customerId, BigDecimal applIds);

}
