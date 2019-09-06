package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.AuthenticationLimitCheckView;

public interface AuthenticationLimitCheckDAO extends JpaRepository<AuthenticationLimitCheckView, Serializable> {

	@Query(value = "select cts from AuthenticationLimitCheckView cts where authorizationType in ('27','35','36')")
	public List<AuthenticationLimitCheckView> getContactUsTime();

	@Query("select cts from AuthenticationLimitCheckView cts where authorizationType='28'")
	public List<AuthenticationLimitCheckView> getContactUsPhoneNo();

	@Query("select cts from AuthenticationLimitCheckView cts where authorizationType in ('10','11','12')")
	public List<AuthenticationLimitCheckView> getAllNumberOfTxnLimits();

	@Query("select cts from AuthenticationLimitCheckView cts where authorizationType in ('100')")
	public AuthenticationLimitCheckView getTop1OnlineTxnLimit();
	
	@Query("select cts from AuthenticationLimitCheckView cts where authorizationType='13'")
	public AuthenticationLimitCheckView getPerBeneTxnLimit();
	
	public AuthenticationLimitCheckView findByAuthorizationType(String authType);
	
	@Query("select cts from AuthenticationLimitCheckView cts where authorizationType in ('101')")
	public AuthenticationLimitCheckView getFxOrderTxnLimit();
	
	@Query("select cts from AuthenticationLimitCheckView cts where authorizationType in ('45')")
	public AuthenticationLimitCheckView getHomeSendTimerLimit();
	
}
