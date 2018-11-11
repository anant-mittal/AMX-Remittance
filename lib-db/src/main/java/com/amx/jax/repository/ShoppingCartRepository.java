package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ShoppingCartDetails;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCartDetails, Serializable>{
	
	public List<ShoppingCartDetails> findByApplicationCountryIdAndCompanyIdAndCustomerId(BigDecimal applicationCountryId,BigDecimal companyId,BigDecimal customerId);

}
