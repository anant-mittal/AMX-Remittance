/**
 * 
 */
package com.amx.jax.repository.routing;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.routing.RoutingHeader;

/**
 * @author Prashant
 *
 */
public interface RoutingHeaderRepository extends CrudRepository<RoutingHeader, Serializable> {

}
