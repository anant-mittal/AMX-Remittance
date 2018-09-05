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
	 * @return List of {@link UserRoleMapping}
	 */
	List<UserRoleMapping> findByEmployeeIdIn(List<BigDecimal> employeeIdList);

	/**
	 * Gives User Role Mapping for the Id
	 * 
	 * @param id
	 * @return {@link UserRoleMapping}
	 */
	UserRoleMapping findById(BigDecimal id);

	/**
	 * Gives User Role Mappings for the Ids
	 * 
	 * @param ids
	 * @return
	 */
	List<UserRoleMapping> findByIdIn(List<BigDecimal> ids);

}
