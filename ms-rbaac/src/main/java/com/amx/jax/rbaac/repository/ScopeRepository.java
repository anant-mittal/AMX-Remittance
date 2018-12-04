package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.PermScope;

/**
 * The Interface ScopeRepository.
 */
public interface ScopeRepository extends JpaRepository<PermScope, Serializable> {

	/**
	 * Find by flags.
	 *
	 * @param flags the flags
	 * @return the list
	 */
	public List<PermScope> findByFlags(BigDecimal flags);

}
