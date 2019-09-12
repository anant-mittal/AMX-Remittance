package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CustomerDiscountsView;

public interface CustomerDiscountRepository extends CrudRepository<CustomerDiscountsView, BigDecimal> {
	
	public CustomerDiscountsView getByCustomerIdAndDiscountTypeAndGroupId(BigDecimal customerId,String discountType,BigDecimal groupId);
	
	public List<CustomerDiscountsView> getByCustomerIdAndGroupId(BigDecimal customerId,BigDecimal groupId);

}
