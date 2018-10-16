package com.amx.jax.dao;

import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.DeviceState;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.repository.DeviceRepository;
import com.amx.jax.repository.DeviceStateRepository;
import com.amx.jax.service.BranchDetailService;

@Component
public class DeviceDao {

	@Autowired
	BranchDetailService branchDetailService;
	@Autowired
	DeviceRepository deviceRepository;
	@Autowired
	DeviceStateRepository deviceStateRepository;

	@Transactional
	public DeviceDto saveDevice(DeviceRegistrationRequest request) {

		Device device = new Device();
		BranchSystemDetail branchSystem = branchDetailService.findBranchSystemByIp(request.getBranchSystemIp());
		device.setBranchSystemInventoryId(branchSystem.getCountryBranchSystemInventoryId());
		device.setCreatedBy("JOMAX_ONLINE");
		device.setCreatedDate(new Date());
		device.setDeviceId(request.getDeviceId());
		device.setDeviceType(request.getDeviceType());
		device.setStatus(ConstantDocument.Yes);
		DeviceStateInfo deviceState = new DeviceStateInfo();
		deviceState.setCreatedBy(device.getCreatedBy());
		deviceState.setCreatedDate(device.getCreatedDate());
		deviceState.setDeviceRegId(device.getRegistrationId());
		deviceState.setState(DeviceState.REGISTERED);
		
		deviceStateRepository.save(deviceState);
		deviceRepository.save(device);
		DeviceDto dto = new DeviceDto();
		try {
			BeanUtils.copyProperties(dto, device);
		} catch (Exception e) {
		}
		return dto;
	}
}
