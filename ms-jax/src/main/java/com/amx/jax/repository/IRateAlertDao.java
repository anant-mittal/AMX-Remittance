package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.RateAlert;

public interface IRateAlertDao extends JpaRepository<RateAlert, Serializable> {

	@Query("select r from RateAlert r where r.onlineRateAlertId=:onlineRateAlertId and isActive='Y'")
	public List<RateAlert> getRateAlertDetails(@Param("onlineRateAlertId") BigDecimal onlineRateAlertId);

	@Query("select r from RateAlert r where r.customerId=:customerId and isActive='Y' and trunc(sysdate) <= trunc(toDate)")
	public List<RateAlert> getRateAlertForCustomer(@Param("customerId") BigDecimal customerId);

	@Query("select r from RateAlert r where isActive='Y' and trunc(sysdate) >= trunc(fromDate) and trunc(sysdate) <= trunc(toDate) ")
	public List<RateAlert> getAllRateAlert();

	@Query("select r from RateAlert r where r.customerId=:customerId and isActive='Y'")
	public List<RateAlert> deleteRateAlertForId(@Param("customerId") String customerId);

}
