/**
 * 
 */
package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dict.UserClient.ClientType;

/**
 * @author Prashant
 *
 */
public interface DeviceRepository extends CrudRepository<Device, Serializable> {

	public Device findByRegistrationId(BigDecimal registrationId);

	public List<Device> findByBranchSystemInventoryIdAndDeviceTypeAndStatus(BigDecimal brachSystemInvId,
			ClientType deviceType, String status);

	public List<Device> findByEmployeeIdAndDeviceTypeAndStatus(BigDecimal employeeId, ClientType deviceType,
			String status);

	public Device findFirst1ByBranchSystemInventoryIdAndDeviceType(BigDecimal brachSystemInvId, ClientType deviceType,
			Sort sort);

	public List<Device> findByBranchSystemInventoryIdAndDeviceType(BigDecimal brachSystemInvId, ClientType deviceType);

	public DeviceStateInfo findByPairTokenAndRegistrationId(String pairToken, BigDecimal bigDecimal);

	public DeviceStateInfo findBySessionTokenAndRegistrationId(String sessionToken, BigDecimal bigDecimal);
}
