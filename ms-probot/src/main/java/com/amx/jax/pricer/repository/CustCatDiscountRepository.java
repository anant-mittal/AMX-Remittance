package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;

@Transactional
public interface CustCatDiscountRepository extends CrudRepository<CustomerCategoryDiscount, BigDecimal> {

	CustomerCategoryDiscount findByCustomerCategory(CUSTOMER_CATEGORY customerCategory);
	
	CustomerCategoryDiscount findById(BigDecimal Id);

}
