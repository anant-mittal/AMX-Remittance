/**
 * 
 */
package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.RoutingHeader;

public interface RoutingHeaderRepository extends CrudRepository<RoutingHeader, Serializable> {

	public List<RoutingHeader> findByCountryIdAndCurrenyIdAndIsActive(BigDecimal countryId, BigDecimal currencyId, String isActive);

}
