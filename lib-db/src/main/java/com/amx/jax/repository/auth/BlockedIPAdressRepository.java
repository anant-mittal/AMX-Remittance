/**
 * 
 */
package com.amx.jax.repository.auth;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.auth.BlockedIPAdress;

/**
 * @author prashant
 *
 */
public interface BlockedIPAdressRepository extends CrudRepository<BlockedIPAdress, Serializable> {

	List<BlockedIPAdress> findByIpAddressAndIsActive(String ip, String isActive);
}
