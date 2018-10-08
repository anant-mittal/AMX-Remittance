package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.PlaceOrder;

/**
 * @author Subodh Bhoir
 *
 */
public interface IPlaceOrderDao extends JpaRepository<PlaceOrder, Serializable> {

	public static final String QUERY_PLACE_ORDER_RATE_ALERT_EFT = "select PO.* from  " + "JAX_ONLINE_PLACE_ORDER PO , "
			+ "EX_ROUTING_HEADER RH , " + "EX_PIPS_MASTER PM " + " where  " + "PO.BANK_ID = PM.BANK_ID "
			+ "and PM.bank_id = RH.ROUTING_BANK_ID " + "and PO.CURRENCY_ID = RH.CURRENCY_ID "
			+ "and PO.CURRENCY_ID = RH.CURRENCY_ID " + "and PM.PIPS_MASTER_ID=:pipsMasterId "
			+ "and PO.RECEIVE_AMOUNT >= PM.FROM_AMOUNT  " + "and PO.RECEIVE_AMOUNT <= PM.TO_AMOUNT "
			+ "and RH.SERVICE_MASTER_ID=101 "
			+ " AND ROUND(( 1 / PM.derived_sell_rate ), (select DECIMAL_NUMBER from EX_CURRENCY_MASTER where CURRENCY_ID= PO.CURRENCY_ID) )"
			+ " >= ROUND(PO.target_exchange_rate , (select DECIMAL_NUMBER from EX_CURRENCY_MASTER where CURRENCY_ID= PO.CURRENCY_ID)) "
			+ "and PO.ISACTIVE='Y' " + "and RH.ISACTIVE='Y' "
			+ "and  (trunc(sysdate)  between trunc(PO.VALID_FROM_DATE) and  trunc(PO.VALID_TO_DATE)) "
			+ "and (trunc( PO.NOTIFICATION_DATE) < trunc(sysdate) or PO.NOTIFICATION_DATE  is null)";

	public static final String QUERY_PLACE_ORDER_RATE_ALERT_TT = "select PO.* from " + "JAX_ONLINE_PLACE_ORDER PO ,"
			+ "EX_ROUTING_HEADER RH ," + "EX_PIPS_MASTER PM" + " where"
			+ " PO.BANK_ID not in (select ROUTING_BANK_ID from EX_ROUTING_HEADER where ROUTING_BANK_ID != PM.BANK_ID  and isactive='Y')"
			+ " and PM.bank_id = RH.ROUTING_BANK_ID" + " and PO.CURRENCY_ID = RH.CURRENCY_ID"
			+ " and PO.CURRENCY_ID = PM.CURRENCY_ID" + " and PM.PIPS_MASTER_ID=:pipsMasterId "
			+ " and PO.RECEIVE_AMOUNT >= PM.FROM_AMOUNT" + " and PO.RECEIVE_AMOUNT <= PM.TO_AMOUNT"
			+ " and RH.SERVICE_MASTER_ID=102"
			+ " AND ROUND(( 1 / PM.derived_sell_rate ), (select DECIMAL_NUMBER from EX_CURRENCY_MASTER where CURRENCY_ID= PO.CURRENCY_ID) )"
			+ " >= ROUND(PO.target_exchange_rate , (select DECIMAL_NUMBER from EX_CURRENCY_MASTER where CURRENCY_ID= PO.CURRENCY_ID))"
			+ " and PO.ISACTIVE='Y' and RH.ISACTIVE='Y'"
			+ " and (trunc(sysdate)  between trunc(PO.VALID_FROM_DATE) and  trunc(PO.VALID_TO_DATE))"
			+ " and (trunc( PO.NOTIFICATION_DATE) < trunc(sysdate) or PO.NOTIFICATION_DATE  is null)";

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

	@Query(nativeQuery = true, value = QUERY_PLACE_ORDER_RATE_ALERT_EFT)
	public Set<PlaceOrder> getPlaceOrderAlertRate1(@Param("pipsMasterId") BigDecimal pipsMasterId);

	@Query(nativeQuery = true, value = QUERY_PLACE_ORDER_RATE_ALERT_TT)
	public Set<PlaceOrder> getPlaceOrderAlertRate2(@Param("pipsMasterId") BigDecimal pipsMasterId);

	@Query("select p from PlaceOrder p where p.remittanceApplicationId=:remittanceApplicationId and isActive='Y' ")
	public List<PlaceOrder> getPlaceOrderForRemittanceApplicationId(@Param("remittanceApplicationId") BigDecimal remittanceApplicationId);
	
	@Query("select p from PlaceOrder p where p.remittanceApplicationId=:remittanceTransactionId and isActive='C' ")
	public PlaceOrder getPlaceOrderForRemittanceTransactionId(@Param("remittanceTransactionId") BigDecimal remittanceTransactionId);

	@Transactional
	@Modifying
	@Query("update PlaceOrder po set po.updatedDate = ?2 , po.notificationDate = ?2 where po.onlinePlaceOrderId in ( ?1 )")
	public int updatePlaceOrdersForRateAlert(List<BigDecimal> placeOrderIds, Date now);
}