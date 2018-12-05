/**
 * 
 */
package com.amx.jax.repository.fx;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.fx.StatusMaster;

/**
 * @author Prashant
 *
 */
public interface StatusMasterRepository extends CrudRepository<StatusMaster, Serializable> {

	public StatusMaster findByStatusCode(String code);
}
