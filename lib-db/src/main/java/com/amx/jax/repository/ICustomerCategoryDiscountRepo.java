package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerCategoryDiscountModel;

public interface ICustomerCategoryDiscountRepo extends CrudRepository<CustomerCategoryDiscountModel, Serializable>{
	
	public CustomerCategoryDiscountModel findByIdAndIsActive(BigDecimal id,String isactive);

}
