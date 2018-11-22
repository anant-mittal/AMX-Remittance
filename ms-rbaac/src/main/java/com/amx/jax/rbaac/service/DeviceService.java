package com.amx.jax.rbaac.service;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constant.DeviceState;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.error.JaxError;
import com.amx.jax.rbaac.RbaacConstants;
import com.amx.jax.rbaac.dao.DeviceDao;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.manager.DeviceManager;
import com.amx.jax.rbaac.validation.DeviceValidation;
import com.amx.jax.util.CryptoUtil;
import com.amx.utils.ArgUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeviceService extends AbstractService {

	Logger logger = LoggerFactory.getLogger(DeviceService.class);

	@Autowired
	DeviceDao deviceDao;
	@Autowired
	DeviceValidation deviceValidation;
	@Autowired
	DeviceManager deviceManager;
	@Autowired
	CryptoUtil cryptoUtil;

	public static final long DEVICE_SESSION_TIMEOUT = 8 * 60 * 60; // in seconds

	/**
	 * activates device
	 * 
	 * @param countryBranchSystemInventoryId
	 * @param deviceType
	 * 
	 */
	@Transactional
	public void activateDevice(Device device) {
		device.setStatus("Y");
		List<Device> devices = deviceDao.findAllActiveDevices(device.getBranchSystemInventoryId(),
				device.getDeviceType());
		if (!CollectionUtils.isEmpty(devices)) {
			for (Device d : devices) {
				if (!d.equals(device)) {
					d.setStatus("N");
				}
			}
			deviceDao.saveDevices(devices);
		}
		deviceDao.saveDevice(device);
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	public BoolRespModel deactivateDevice(Integer deviceRegId) {
		logger.info("In deactivateDevice with deviceRegId: {}", deviceRegId);
		Device device = deviceDao.findDevice(new BigDecimal(deviceRegId));
		deviceValidation.validateDeviceForActivation(device);
		deactivateDevice(device);
		return new BoolRespModel(Boolean.TRUE);
	}

	public void deactivateDevice(Device device) {
		device.setStatus("N");
		deviceDao.saveDevice(device);
	}

	@Transactional
	public DeviceDto registerNewDevice(DeviceRegistrationRequest request) {
		logger.info("In register device with request: {}", request);
		deviceValidation.validateDeviceRegRequest(request);
		DeviceDto newDevice = deviceDao.saveDevice(request);
		DeviceState deviceState;
		if (RbaacConstants.YES.equals(newDevice.getStatus())) {
			deviceState = DeviceState.REGISTERED;
		} else {
			deviceState = DeviceState.REGISTERED_NOT_ACTIVE;
		}
		Device device = deviceDao.findDevice(newDevice.getRegistrationId());
		device.setState(deviceState);
		device.setPairToken(cryptoUtil.generateHash("DEVICE_PAIR", newDevice.getRegistrationId().toString()));
		deviceDao.saveDevice(device);
		logger.info("device registered with id: {}", newDevice.getRegistrationId());
		return newDevice;
	}

	public BoolRespModel activateDevice(Integer deviceRegId) {
		logger.info("In activateDevice with deviceRegId: {}", deviceRegId);
		Device device = deviceDao.findDevice(new BigDecimal(deviceRegId));
		deviceValidation.validateDeviceForActivation(device);
		activateDevice(device);
		return new BoolRespModel(Boolean.TRUE);
	}

	public DevicePairOtpResponse sendOtpForPairing(Integer deviceRegId, String paireToken) {
		Device device = deviceDao.findDevice(new BigDecimal(deviceRegId));
		validateDevicePairToken(new BigDecimal(deviceRegId), paireToken);
		DevicePairOtpResponse response = deviceManager.generateOtp(device);
		return response;
	}

	public DevicePairOtpResponse validateOtpForPairing(ClientType deviceType, Integer countryBranchSystemInventoryId,
			String otp) {
		deviceValidation.validateOtp(otp);
		Device device = deviceDao.findDevice(new BigDecimal(countryBranchSystemInventoryId), deviceType);
		deviceValidation.validateDevice(device);
		deviceValidation.validateDeviceToken(device, otp);
		// session pair success
		device.setState(DeviceState.SESSION_PAIRED);
		DevicePairOtpResponse resp = new DevicePairOtpResponse();
		resp.setDeviceRegId(device.getRegistrationId());
		resp.setTermialId(ArgUtil.parseAsString(device.getBranchSystemInventoryId()));
		return resp;
	}

	public BoolRespModel validateDevicePairToken(BigDecimal deviceRegId, String devicePairToken) {
		Device device = deviceDao.findDevice(deviceRegId);
		deviceValidation.validateDevice(device);
		if (!device.getPairToken().equals(devicePairToken)) {
			throw new AuthServiceException("Invalid paire token", RbaacServiceError.CLIENT_INVALID_PAIR_TOKEN);
		}
		return new BoolRespModel(true);
	}

	public BoolRespModel validateDeviceSessionPairToken(BigDecimal deviceRegId, String deviceSessionToken) {
		Device device = deviceDao.findDevice(deviceRegId);
		deviceValidation.validateDevice(device);
		String sessionTokenGen = deviceManager.generateSessionPairToken(device);
		if (!deviceSessionToken.equals(sessionTokenGen)) {
			throw new AuthServiceException("Session token is expired", RbaacServiceError.CLIENT_EXPIRED_SESSION_TOKEN);
		}
		deviceValidation.validateOtpValidationTimeLimit(deviceRegId);
		return new BoolRespModel(true);
	}

	public BigDecimal getDeviceRegIdByBranchInventoryId(ClientType deviceClientType,
			BigDecimal countryBranchSystemInventoryId) {
		// TODO Auto-generated method stub
		return null;
	}
}
