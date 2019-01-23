package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;

public interface IShoppingCartDetailsRepository extends CrudRepository<ShoppingCartDetails, BigDecimal> {
	
	public List<ShoppingCartDetails> findByCustomerId(BigDecimal customerId);

}
