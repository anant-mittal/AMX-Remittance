/**
 * 
 */
package com.amx.jax.repository.employee;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.employee.AmgEmployee;

/**
 * @author Prashant
 *
 */
public interface AmgEmployeeRepository extends CrudRepository<AmgEmployee, Serializable> {

	public AmgEmployee findByCivilIdAndEmployeeInd(String civilId, String empInd);
}
