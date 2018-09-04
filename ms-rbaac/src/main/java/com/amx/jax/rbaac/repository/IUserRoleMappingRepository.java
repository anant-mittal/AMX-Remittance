package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.UserRoleMapping;

public interface IUserRoleMappingRepository extends JpaRepository<UserRoleMapping, Serializable> {

	/**
	 * Gives User Role Mappings for the EmployeeIds
	 * 
	 * @param employeeIdList
	 * @return List of user role mappings
	 */
	List<UserRoleMapping> findByEmployeeIdIn(List<BigDecimal> employeeIdList);

}
