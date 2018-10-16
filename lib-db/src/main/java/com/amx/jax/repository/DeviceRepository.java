/**
 * 
 */
package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Device;

/**
 * @author Prashant
 *
 */
public interface DeviceRepository extends CrudRepository<Device, Serializable> {

}
