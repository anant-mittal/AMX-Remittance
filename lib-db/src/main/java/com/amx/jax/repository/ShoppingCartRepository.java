package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.FxShoppingCartDetails;



public interface ShoppingCartRepository extends CrudRepository<FxShoppingCartDetails, Serializable>{
	
	public List<FxShoppingCartDetails> findByApplicationCountryIdAndCompanyIdAndCustomerId(BigDecimal applicationCountryId,BigDecimal companyId,BigDecimal customerId);

	
	@Query("select d from FxShoppingCartDetails d where  d.applicationCountryId=:applicationCountryId and d.companyId=:companyId and d.customerId=:customerId")
	public List<FxShoppingCartDetails> fetchFcSaleApplicationDetails(@Param("applicationCountryId") BigDecimal applicationCountryId,@Param("companyId") BigDecimal companyId,@Param("customerId") BigDecimal customerId);

}
