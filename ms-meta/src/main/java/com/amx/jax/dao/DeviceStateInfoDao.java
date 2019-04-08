package com.amx.jax.dao;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.config.JaxProperties;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.repository.DeviceStateRepository;
import com.amx.jax.util.CryptoUtil;

@Component
public class DeviceStateInfoDao {

	@Autowired
	DeviceStateRepository deviceStateRepository;
	@Autowired
	CryptoUtil cryptoUtil;
	@Autowired
	JaxProperties jaxProperties;

	Logger logger = LoggerFactory.getLogger(getClass());

	public void saveDeviceInfo(DeviceStateInfo deviceInfo) {
		deviceStateRepository.save(deviceInfo);

	}

	public DeviceStateInfo getDeviceStateInfo(BigDecimal registrationId) {
		DeviceStateInfo deviceStateInfo = deviceStateRepository.findOne(registrationId);
		if(deviceStateInfo==null) {
			throw new GlobalException("DeviceStateInfo is not found for the given DeviceRegId");
		}
		return deviceStateInfo;
	}


	public DeviceStateInfo getWithSaveDeviceStateInfo(BigDecimal registrationId) {
		DeviceStateInfo deviceStateInfo = deviceStateRepository.findOne(registrationId);
		if (deviceStateInfo == null) {
			logger.debug("init device state info D id {} ", registrationId);
			deviceStateInfo = new DeviceStateInfo(registrationId);
			saveDeviceInfo(deviceStateInfo);
		}
		return deviceStateInfo;
	}
}
