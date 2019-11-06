package com.amx.jax.repository;
/**
 * @author rabil 
 * @date 10/29/2019
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.RatePlaceOrder;

public interface IRatePlaceOrderRepository  extends CrudRepository<RatePlaceOrder, Serializable>{

	
	@Query("select rv from RatePlaceOrder rv where rv.customerId=?1 and trunc(createdDate)=trunc(sysdate) and rv.isActive='Y'")
	public List<RatePlaceOrder> fetchPlaceOrderForCustomer(BigDecimal customerId);
}
