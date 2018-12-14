package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.Permission;

/**
 * The Interface PermissionRepository.
 */
public interface PermissionRepository extends JpaRepository<Permission, Serializable> {

	/**
	 * Find by context.
	 *
	 * @param context
	 *            the context
	 * @return the list
	 */
	public List<Permission> findByContext(String context);

}
