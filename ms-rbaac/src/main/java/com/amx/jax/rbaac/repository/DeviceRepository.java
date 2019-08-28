/**
 * 
 */
package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Device;
import com.amx.jax.dict.UserClient.ClientType;

/**
 * @author Prashant
 *
 */
public interface DeviceRepository extends CrudRepository<Device, Serializable> {

	public List<Device> findByBranchSystemInventoryIdAndDeviceTypeAndStatus(BigDecimal brachSystemInvId,
			ClientType deviceType, String status);

	public List<Device> findByEmployeeIdAndDeviceTypeAndStatus(BigDecimal employeeId, ClientType deviceType,
			String status);
}
