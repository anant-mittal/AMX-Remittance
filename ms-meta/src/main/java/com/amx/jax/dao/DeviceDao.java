package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.DeviceState;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.repository.DeviceRepository;
import com.amx.jax.repository.DeviceStateRepository;
import com.amx.jax.service.BranchDetailService;
import com.amx.jax.util.CryptoUtil;

@Component
public class DeviceDao {

	@Autowired
	BranchDetailService branchDetailService;
	@Autowired
	DeviceRepository deviceRepository;
	@Autowired
	DeviceStateRepository deviceStateRepository;
	@Autowired
	CryptoUtil cryptoUtil;

	public DeviceDto saveDevice(DeviceRegistrationRequest request) {

		Device device = new Device();
		BranchSystemDetail branchSystem = branchDetailService
				.findBranchSystemByIp(new BigDecimal(request.getCountryBranchId()), request.getBranchSystemIp());
		device.setBranchSystemInventoryId(branchSystem.getCountryBranchSystemInventoryId());
		device.setCreatedBy("JOMAX_ONLINE");
		device.setCreatedDate(new Date());
		device.setDeviceId(request.getDeviceId());
		device.setDeviceType(request.getDeviceType());
		device.setStatus(ConstantDocument.No);
		deviceRepository.save(device);
		DeviceDto dto = new DeviceDto();
		try {
			BeanUtils.copyProperties(dto, device);
		} catch (Exception e) {
		}
		return dto;
	}

	public void saveDeviceState(DeviceDto newDevice, DeviceState state) {
		DeviceStateInfo deviceState = new DeviceStateInfo();
		deviceState.setCreatedBy("JOMAX_ONLINE");
		deviceState.setCreatedDate(new Date());
		deviceState.setDeviceRegId(newDevice.getRegistrationId());
		deviceState.setState(state);
		deviceState.setPairToken(cryptoUtil.generateHash("DEVICE_PAIR", newDevice.getRegistrationId().toString()));
		deviceStateRepository.save(deviceState);
		newDevice.setPairToken(deviceState.getPairToken());
	}

	public Device findDevice(BigDecimal branchSystemInvId, ClientType deviceType) {
		return deviceRepository.findByBranchSystemInventoryIdAndDeviceTypeAndStatus(branchSystemInvId, deviceType,
				ConstantDocument.Yes);
	}

	public Device findLatestDevice(BigDecimal branchSystemInvId, ClientType deviceType) {
		return deviceRepository.findFirst1ByBranchSystemInventoryIdAndDeviceType(branchSystemInvId, deviceType,
				new Sort("deviceId"));
	}

	public void saveDevice(Device device) {
		deviceRepository.save(device);
	}

	public DeviceStateInfo getDeviceStateInfo(Device device) {
		return deviceStateRepository.findOne(device.getRegistrationId());
	}

	public Device findDevice(BigDecimal deviceRegId) {
		return deviceRepository.findOne(deviceRegId);
	}

	public void saveDeviceInfo(DeviceStateInfo deviceInfo) {
		deviceStateRepository.save(deviceInfo);

	}

	public DeviceStateInfo getDeviceStateInfoByPaireToken(String pairToken, Integer registrationId) {
		return deviceStateRepository.findByPairTokenAndDeviceRegId(pairToken, new BigDecimal(registrationId));
	}

	public DeviceStateInfo findBySessionToken(String sessionToken, Integer registrationId) {
		return deviceStateRepository.findBySessionTokenAndDeviceRegId(sessionToken, new BigDecimal(registrationId));
	}
}
