package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.DeviceState;
import com.amx.jax.dao.DeviceDao;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.manager.DeviceManager;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.services.AbstractService;
import com.amx.jax.validation.DeviceValidation;
import com.amx.utils.CryptoUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeviceService extends AbstractService {

	Logger logger = LoggerFactory.getLogger(DeviceService.class);

	@Autowired
	DeviceDao deviceDao;
	@Autowired
	DeviceManager deviceManager;
	@Autowired
	DeviceValidation deviceValidation;
	@Autowired
	JaxConfigService jaxConfigService;

	public static final long DEVICE_SESSION_TIMEOUT = 8 * 60 * 60; // in seconds

	public DeviceDto registerNewDevice(DeviceRegistrationRequest request) {
		logger.info("In register device with request: {}", request);
		DeviceDto newDevice = deviceDao.saveDevice(request);
		deviceDao.saveDeviceState(newDevice, DeviceState.REGISTERED);
		logger.info("device registered with id: {}", newDevice.getRegistrationId());
		return newDevice;
	}

	public AmxApiResponse<DeviceDto, Object> updateDeviceState(DeviceStateInfoChangeRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public BooleanResponse activateDevice(Integer countryBranchSystemInventoryId, String deviceType) {
		logger.info("In activateDevice with countryBranchSystemInventoryId: {}", countryBranchSystemInventoryId);
		deviceManager.activateDevice(countryBranchSystemInventoryId, deviceType);
		return new BooleanResponse(Boolean.TRUE);
	}

	public DevicePairOtpResponse sendOtpForPairing(Integer deviceRegId) {
		Device device = deviceDao.findDevice(deviceRegId);
		deviceValidation.validateDevice(device);
		DevicePairOtpResponse response = new DevicePairOtpResponse();
		String otp = deviceManager.generateOtp(device);
		response.setOtp(otp);
		return response;
	}

	public BooleanResponse validateOtpForPairing(Integer countryBranchSystemInventoryId, String otp) {
		Device device = deviceDao.findDevice(countryBranchSystemInventoryId);
		deviceValidation.validateDevice(device);
		deviceValidation.validateDeviceToken(device, otp);
		// device login success
		createSession(device);
		return new BooleanResponse(Boolean.TRUE);
	}

	private void createSession(Device device) {

		DeviceStateInfo deviceInfo = deviceDao.getDeviceStateInfo(device);
		String hmacToken = CryptoUtil.generateHMAC(getDeviceSessionTimeout(), "DEVICE_SESSION_SALT",
				device.getRegistrationId().toString());
		deviceInfo.setSessionToken(hmacToken);
		deviceDao.saveDeviceInfo(deviceInfo);
	}

	public long getDeviceSessionTimeout() {
		JaxConfig jaxConf = jaxConfigService.getConfig("DEVICE_SESISON_TIMEOUT");
		if (jaxConf != null) {
			return Long.parseLong(jaxConf.getValue());
		} else {
			return DEVICE_SESSION_TIMEOUT;
		}
	}
}
