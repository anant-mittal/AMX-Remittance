/**
 * 
 */
package com.amx.jax.repository.insurance;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.insurance.CustomerInsurance;

/**
 * @author prashant
 *
 */
public interface CustomerInsuranceRepository extends CrudRepository<CustomerInsurance, Serializable> {

	public CustomerInsurance findByCustomerIdAndIsActive(BigDecimal customerId, String isActive);
}
