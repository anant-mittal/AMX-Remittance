package com.amx.jax.userservice.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.LoginLogoutHistory;

@Transactional
public interface LoginLogoutHistoryRepository extends CrudRepository<LoginLogoutHistory, BigDecimal> {

	List<LoginLogoutHistory> findFirst2ByuserName(String userName, Sort sort);
	
	LoginLogoutHistory findFirstByuserNameAndLogoutTimeIsNotNull(String userName, Sort sort);
	
}
