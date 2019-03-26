/**
 * 
 */
package com.amx.jax.repository.employee;

import java.io.Serializable;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.employee.UserSession;

/**
 * @author Prashant
 *
 */
public interface UserSessionRepository extends CrudRepository<UserSession, Serializable> {

	public UserSession findFirstByIpAddress(String ipAddress, Sort sort);
}
