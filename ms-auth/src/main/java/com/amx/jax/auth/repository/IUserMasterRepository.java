package com.amx.jax.auth.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.auth.dbmodel.UserRoleMaster;

public interface IUserMasterRepository extends JpaRepository<UserRoleMaster, Serializable>{
	
	public UserRoleMaster findByUserRoleId(BigDecimal userRoleId);
	
	public UserRoleMaster findByEmployeeId(BigDecimal employeeId);

}

