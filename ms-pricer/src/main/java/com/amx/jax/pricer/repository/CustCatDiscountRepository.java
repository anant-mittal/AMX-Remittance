package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CustomerCategoryDiscount;

@Transactional
public interface CustCatDiscountRepository extends CrudRepository<CustomerCategoryDiscount, BigDecimal> {

	CustomerCategoryDiscount findByCustomerCategory(String customerCategory);

}
