package com.amx.jax.rbaac.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.constant.DeviceState;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.repository.DeviceRepository;
import com.amx.jax.rbaac.service.BranchSystemDetailService;
import com.amx.jax.util.CryptoUtil;

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

	public Device saveDevice(DeviceRegistrationRequest request) {

		Device device = new Device();
		BranchSystemDetail branchSystem = branchDetailService.findBranchSystemByIp(request.getBranchSystemIp());
		device.setBranchSystemInventoryId(branchSystem.getCountryBranchSystemInventoryId());
		device.setCreatedBy("JOMAX_ONLINE");
		device.setCreatedDate(new Date());
		device.setDeviceId(request.getDeviceId());
		device.setDeviceType(request.getDeviceType());
		device.setStatus("N");
		device.setState(DeviceState.REGISTERED_NOT_ACTIVE);
		if (appConfig.isDebug()) {
			device.setStatus("Y");
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
				deviceType, "Y");
		Device device = null;
		if (devices != null && devices.size() > 1) {
			throw new AuthServiceException("Too many devices activated", RbaacServiceError.CLIENT_TOO_MANY_ACTIVE);
		}
		if (devices != null && devices.size() == 1) {
			device = devices.get(0);
		}
		return device;
	}

	public List<Device> findAllActiveDevices(BigDecimal branchSystemInvId, ClientType deviceType) {
		List<Device> devices = deviceRepository.findByBranchSystemInventoryIdAndDeviceTypeAndStatus(branchSystemInvId,
				deviceType, "Y");
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
