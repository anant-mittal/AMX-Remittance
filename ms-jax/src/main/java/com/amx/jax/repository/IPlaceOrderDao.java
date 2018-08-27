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
	public List<PlaceOrder> getPlaceOrderForCustomer(@Param("customerId") BigDecimal customerId);
	
	@Query("select p from PlaceOrder p where isActive='Y' and trunc(sysdate) >= trunc(validFromDate) and trunc(sysdate) <= trunc(validToDate) ")
	public List<PlaceOrder> getAllPlaceOrder();

	@Query("select p from PlaceOrder p where p.onlinePlaceOrderId=:onlinePlaceOrderId and isActive='Y'")
	public List<PlaceOrder> getPlaceOrderDelete(@Param("onlinePlaceOrderId") BigDecimal onlinePlaceOrderId);
	
	@Query("select p from PlaceOrder p where p.onlinePlaceOrderId=:onlinePlaceOrderId and isActive='Y' ")
	public List<PlaceOrder> getPlaceOrderForId(@Param("onlinePlaceOrderId") BigDecimal onlinePlaceOrderId);
	
	@Query("select p from PlaceOrder p where p.onlinePlaceOrderId=:onlinePlaceOrderId and isActive='Y'")
	public List<PlaceOrder> getPlaceOrderUpdate(@Param("onlinePlaceOrderId") BigDecimal onlinePlaceOrderId);
	
	
	@Query("select p from   PlaceOrder p  where p.countryId =:countryId and  p.currencyId =:currencyId and p.bankId =:bankId  and p.targetExchangeRate <=:targetExchangeRate and  (to_date(p.notificationDate,'dd-mm-yy') < to_date(SYSDATE,'dd-mm-YY') or p.notificationDate IS NULL) and isActive='Y' ")
	public List<PlaceOrder> getPlaceOrderAlertRate(@Param("countryId") BigDecimal countryId,@Param("currencyId") BigDecimal currencyId, @Param("bankId") BigDecimal bankId,@Param("targetExchangeRate") BigDecimal targetExchangeRate);

}