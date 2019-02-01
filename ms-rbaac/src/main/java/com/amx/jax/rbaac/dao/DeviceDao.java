package com.amx.jax.rbaac.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.constant.DeviceState;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.repository.DeviceRepository;
import com.amx.jax.rbaac.service.BranchSystemDetailService;
import com.amx.jax.util.CryptoUtil;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

@Component
public class DeviceDao {

	@Autowired
	BranchSystemDetailService branchDetailService;
	@Autowired
	DeviceRepository deviceRepository;
	@Autowired
	CryptoUtil cryptoUtil;
	@Autowired
	AppConfig appConfig;
	@Autowired
	RbaacDao rbaacDao;

	/**
	 * 
	 * 
	 * @param request
	 * @param registerDefault - Register device by default
	 * @return
	 */
	public Device saveDevice(DeviceRegistrationRequest request, boolean registerDefault) {

		Device device = new Device();
		if (request.getBranchSystemIp() != null) {
			BranchSystemDetail branchSystem = branchDetailService.findBranchSystemByIp(request.getBranchSystemIp());
			device.setBranchSystemInventoryId(branchSystem.getCountryBranchSystemInventoryId());
		}
		if (request.getIdentityInt() != null) {
			Employee employee = rbaacDao.fetchEmpDetails(request.getIdentityInt());

			if (ArgUtil.isEmpty(employee)) {
				throw new AuthServiceException(RbaacServiceError.INVALID_USER_DETAILS,
						"Cannot find user with provided details");
			}

			device.setEmployeeId(employee.getEmployeeId());
		}
		device.setCreatedBy("JOMAX_ONLINE");
		device.setCreatedDate(new Date());
		device.setDeviceId(request.getDeviceId());
		device.setDeviceType(request.getDeviceType());
		device.setStatus("N");
		device.setState(DeviceState.REGISTERED_NOT_ACTIVE);
		if (registerDefault) {
			device.setStatus(Constants.YES);
			device.setState(DeviceState.REGISTERED);
		}
		deviceRepository.save(device);
		return device;
	}

	/**
	 * @param branchSystemInvId
	 * @param deviceType
	 * @return currently active device
	 * 
	 */
	public Device findDevice(BigDecimal branchSystemInvId, ClientType deviceType) {
		List<Device> devices = deviceRepository.findByBranchSystemInventoryIdAndDeviceTypeAndStatus(branchSystemInvId,
				deviceType, Constants.YES);
		Device device = null;
		if (devices != null && devices.size() > 1) {
			throw new AuthServiceException(RbaacServiceError.CLIENT_TOO_MANY_ACTIVE, "Too many devices activated");
		}
		if (devices != null && devices.size() == 1) {
			device = devices.get(0);
		}
		return device;
	}

	public Device findDeviceByEmployee(BigDecimal employeeId, ClientType deviceType) {
		List<Device> devices = deviceRepository.findByEmployeeIdAndDeviceTypeAndStatus(employeeId, deviceType,
				Constants.YES);
		Device device = null;
		if (devices != null && devices.size() > 1) {
			throw new AuthServiceException(RbaacServiceError.CLIENT_TOO_MANY_ACTIVE, "Too many devices activated");
		}
		if (devices != null && devices.size() == 1) {
			device = devices.get(0);
		}
		return device;
	}

	public List<Device> findAllActiveDevices(BigDecimal branchSystemInvId, ClientType deviceType) {
		List<Device> devices = deviceRepository.findByBranchSystemInventoryIdAndDeviceTypeAndStatus(branchSystemInvId,
				deviceType, Constants.YES);
		return devices;
	}

	public List<Device> findAllActiveDevicesForEmployee(BigDecimal employeeId, ClientType deviceType) {
		List<Device> devices = deviceRepository.findByEmployeeIdAndDeviceTypeAndStatus(employeeId, deviceType,
				Constants.YES);
		return devices;
	}

	public Device findLatestDevice(BigDecimal branchSystemInvId, ClientType deviceType) {
		return deviceRepository.findFirst1ByBranchSystemInventoryIdAndDeviceType(branchSystemInvId, deviceType,
				new Sort("deviceId"));
	}

	public void saveDevice(Device device) {
		deviceRepository.save(device);
	}

	public void saveDevices(List<Device> devices) {
		deviceRepository.save(devices);
	}

	public Device findDevice(BigDecimal deviceRegId) {
		return deviceRepository.findOne(deviceRegId);
	}

	public DeviceStateInfo findBySessionToken(String sessionToken, Integer registrationId) {
		return deviceRepository.findBySessionTokenAndRegistrationId(sessionToken, new BigDecimal(registrationId));
	}

}
