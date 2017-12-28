/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.payment.model.db.Customer;
import com.amx.jax.payment.model.db.Demo;
import com.amx.jax.payment.model.db.OnlineConfiguration;

/**
 * @author Viki Sangani
 * 15-Dec-2017
 * Demo.java
 */
@Transactional
public interface OnlineConfigurationRepository extends CrudRepository<OnlineConfiguration, BigDecimal> {

	@Query("select c from OnlineConfiguration c where onlineConfigId=?1")
	public List<OnlineConfiguration> getPGConfiguration(BigDecimal id);
}
