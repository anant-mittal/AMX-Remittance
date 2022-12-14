/**
 * 
 */
package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
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

	public List<Device> findByBranchSystemInventoryIdAndStatus(BigDecimal brachSystemInvId, String status);

	@Query("select d from Device d where (d.branchSystemInventoryId=?1 or d.branchSystemInventoryId=?2) and d.status=?3")
	public List<Device> findByBranchSystemInventoryIdAndStatus(
			BigDecimal brachSystemInvId,
			BigDecimal brachSystemInvId2,
			String status);
	
	@Query("select d from Device d where (d.registrationId=?1) or (d.deviceId=?2 and d.status='Y')")
	public List<Device> findByDeviceRegIdAndActiveDevicesByDeviceId(
			BigDecimal registrationId,
			String deviceId);

	public List<Device> findByEmployeeIdAndDeviceTypeAndStatus(BigDecimal employeeId, ClientType deviceType,
			String status);
}
