/**
 * 
 */
package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Device;
import com.amx.jax.dict.UserClient.DeviceType;

/**
 * @author Prashant
 *
 */
public interface DeviceRepository extends CrudRepository<Device, Serializable> {

	public Device findByBranchSystemInventoryIdAndDeviceTypeAndStatus(BigDecimal brachSystemInvId,
			DeviceType deviceType, String status);

	public Device findFirst1ByBranchSystemInventoryIdAndDeviceType(BigDecimal brachSystemInvId,
			DeviceType deviceType, Sort sort);
}
