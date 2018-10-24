/**
 * 
 */
package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Device;
import com.amx.jax.dict.UserClient.ClientType;

/**
 * @author Prashant
 *
 */
public interface DeviceRepository extends CrudRepository<Device, Serializable> {

	public Device findByBranchSystemInventoryIdAndDeviceTypeAndStatus(BigDecimal brachSystemInvId,
			ClientType deviceType, String status);

	public Device findFirst1ByBranchSystemInventoryIdAndDeviceType(BigDecimal brachSystemInvId, ClientType deviceType,
			Sort sort);
}
