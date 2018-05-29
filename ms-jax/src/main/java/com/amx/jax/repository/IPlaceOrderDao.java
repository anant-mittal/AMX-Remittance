package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.PlaceOrder;

/**
 * @author Subodh Bhoir
 *
 */
public interface IPlaceOrderDao extends JpaRepository<PlaceOrder, Serializable>{

	@Query("select p from PlaceOrder p where p.customerId=:customerId and isActive='Y' and trunc(sysdate) <= trunc(validToDate)")
	public List<PlaceOrder> getPlaceOrder(@Param("customerId") BigDecimal customerId);
	

}
