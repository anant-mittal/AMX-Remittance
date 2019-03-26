package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.AccessType;

/**
 * The Interface AccessTypeRepository.
 */
public interface AccessTypeRepository extends JpaRepository<AccessType, Serializable> {

	/**
	 * Find by flags.
	 *
	 * @param flags
	 *            the flags
	 * @return the list
	 */
	public List<AccessType> findByFlags(BigDecimal flags);

}
