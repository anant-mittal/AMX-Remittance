package com.amx.jax.placeorder.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.PlaceOrder;

public interface IPlaceoderAlertRate extends JpaRepository<PlaceOrder, Serializable> {
	
	@Query("select p from   PlaceOrder p  where p.countryId =:countryId and  p.currencyId =:currencyId and p.bankId =:bankId  and p.targetExchangeRate <=:targetExchangeRate  ")
	public List<PlaceOrder> getPlaceOrderAlertRate(@Param("countryId") BigDecimal countryId,@Param("currencyId") BigDecimal currencyId, @Param("bankId") BigDecimal bankId,@Param("targetExchangeRate") BigDecimal targetExchangeRate);
}


