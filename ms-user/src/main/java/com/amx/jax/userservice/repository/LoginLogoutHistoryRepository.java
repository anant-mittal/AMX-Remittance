package com.amx.jax.userservice.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.LoginLogoutHistory;

@Transactional
public interface LoginLogoutHistoryRepository extends CrudRepository<LoginLogoutHistory, BigDecimal> {

	LoginLogoutHistory findFirst1ByuserName(String userName, Sort sort);
	
    @Query(value = "select * from LoginLogoutHistory where loginLogoutId=(select max(loginLogoutId)-1 from LoginLogoutHistory where userName = ?1)")
    public LoginLogoutHistory getLastLoginByUserName(String userName);
}
