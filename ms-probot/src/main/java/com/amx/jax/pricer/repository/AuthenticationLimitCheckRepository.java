package com.amx.jax.pricer.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.pricer.dbmodel.AuthenticationLimitCheckView;

public interface AuthenticationLimitCheckRepository extends JpaRepository<AuthenticationLimitCheckView, Serializable> {
	
	@Query("select cts from AuthenticationLimitCheckView cts where authorizationType in ('45')")
	public AuthenticationLimitCheckView getHomeSendTimerLimit();
	
}
