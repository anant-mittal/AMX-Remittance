package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.UserRoleMapping;

/**
 * The Interface IUserRoleMappingRepository.
 */
public interface IUserRoleMappingRepository extends JpaRepository<UserRoleMapping, Serializable> {

	/**
	 * Gives User Role Mappings for the EmployeeIds.
	 *
	 * @param employeeIdList the employee id list
	 * @return List of {@link UserRoleMapping}
	 */
	List<UserRoleMapping> findByEmployeeIdIn(List<BigDecimal> employeeIdList);

	/**
	 * Find by employee id.
	 *
	 * @param employeeId the employee id
	 * @return the user role mapping
	 */
	UserRoleMapping findByEmployeeId(BigDecimal employeeId);

	/**
	 * Gives User Role Mapping for the Id.
	 *
	 * @param id the id
	 * @return {@link UserRoleMapping}
	 */
	UserRoleMapping findById(BigDecimal id);

	/**
	 * Gives User Role Mappings for the Ids.
	 *
	 * @param ids the ids
	 * @return the list
	 */
	List<UserRoleMapping> findByIdIn(List<BigDecimal> ids);
	

}
