package com.amx.jax.userservice.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.LoginLogoutHistory;

@Transactional
public interface LoginLogoutHistoryRepository extends CrudRepository<LoginLogoutHistory, BigDecimal> {

	LoginLogoutHistory findFirst1ByuserName(String userName, Sort sort);
}
