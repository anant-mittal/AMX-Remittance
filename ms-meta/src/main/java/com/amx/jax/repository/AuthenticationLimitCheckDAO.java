package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.AuthenticationLimitCheckView;

public interface AuthenticationLimitCheckDAO extends JpaRepository<AuthenticationLimitCheckView, Serializable>{
	

	@Query(value="select cts from AuthenticationLimitCheckView cts where authorizationType='27'")
	public List<AuthenticationLimitCheckView> getContactUsTime();
	
	@Query("select cts from AuthenticationLimitCheckView cts where authorizationType='28'")
	public List<AuthenticationLimitCheckView> getContactUsPhoneNo();
	
	@Query("select cts from AuthenticationLimitCheckView cts")
	public List<AuthenticationLimitCheckView> getAllAuthLimits();
}
