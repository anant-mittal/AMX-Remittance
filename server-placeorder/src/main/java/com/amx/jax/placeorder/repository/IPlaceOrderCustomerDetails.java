package com.amx.jax.placeorder.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.Customer;

public interface IPlaceOrderCustomerDetails extends JpaRepository<Customer, Serializable> {
	@Query("select c from  Customer c where c.customerId in (:customerId) ")
	public Customer getPlaceOrderCustomerDetails(@Param("customerId") BigDecimal customerId);
}
