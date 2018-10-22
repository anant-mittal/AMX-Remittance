package com.amx.jax.services;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constants.DeviceState;
import com.amx.jax.constants.DeviceStateDataType;
import com.amx.jax.dao.DeviceDao;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.device.SignaturePadRemittanceMetaInfo;
import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.DeviceManager;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.model.response.IDeviceStateData;
import com.amx.jax.services.AbstractService;
import com.amx.jax.validation.DeviceValidation;
import com.amx.utils.CryptoUtil;
import com.amx.utils.JsonUtil;

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
		deviceValidation.validateDeviceRegRequest(request);
		DeviceDto newDevice = deviceDao.saveDevice(request);
		deviceDao.saveDeviceState(newDevice, DeviceState.REGISTERED);
		logger.info("device registered with id: {}", newDevice.getRegistrationId());
		return newDevice;
	}

	public AmxApiResponse<DeviceDto, Object> updateDeviceState(DeviceStateInfoChangeRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public BoolRespModel activateDevice(Integer countryBranchSystemInventoryId, DeviceType deviceType) {
		logger.info("In activateDevice with countryBranchSystemInventoryId: {}", countryBranchSystemInventoryId);
		deviceManager.activateDevice(countryBranchSystemInventoryId, deviceType);
		return new BoolRespModel(Boolean.TRUE);
	}

	public DevicePairOtpResponse sendOtpForPairing(Integer deviceRegId) {
		Device device = deviceDao.findDevice(new BigDecimal(deviceRegId));
		deviceValidation.validateDevice(device);
		DevicePairOtpResponse response = new DevicePairOtpResponse();
		String otp = deviceManager.generateOtp(device);
		response.setOtp(otp);
		return response;
	}

	public BoolRespModel validateOtpForPairing(DeviceType deviceType, Integer countryBranchSystemInventoryId,
			String otp) {
		deviceValidation.validateOtp(otp);
		Device device = deviceDao.findDevice(new BigDecimal(countryBranchSystemInventoryId), deviceType);
		deviceValidation.validateDevice(device);
		deviceValidation.validateDeviceToken(device, otp);
		// device login success
		createSession(device);
		return new BoolRespModel(Boolean.TRUE);
	}

	private void createSession(Device device) {

		DeviceStateInfo deviceInfo = deviceDao.getDeviceStateInfo(device);
		String hmacToken = CryptoUtil.generateHMAC(deviceManager.getDeviceSessionTimeout(), "DEVICE_SESSION_SALT",
				device.getRegistrationId().toString());
		deviceInfo.setSessionToken(hmacToken);
		deviceInfo.setState(DeviceState.PAIRED);
		deviceDao.saveDeviceInfo(deviceInfo);
	}

	/**
	 * @param registrationId
	 * @return device's status like loggedIn, signing etc
	 * 
	 */
	public DeviceStatusInfoDto getStatus(Integer registrationId) {

		if (registrationId == null) {
			throw new GlobalException("Device registration id can not be blank");
		}
		Device device = deviceDao.findDevice(new BigDecimal(registrationId));
		deviceValidation.validateDevice(device);
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		DeviceStatusInfoDto dto = new DeviceStatusInfoDto();
		dto.setStateDataType(deviceStateInfo.getStateDataType());
		dto.setDeviceState(deviceStateInfo.getState());
		if (!deviceManager.isLoggedIn(device)) {
			dto.setDeviceState(DeviceState.REGISTERED);
		}
		if (deviceStateInfo.getStateDataType() != null) {
			switch (deviceStateInfo.getStateDataType()) {
			case REMITTANCE:
				SignaturePadRemittanceMetaInfo stateData = JsonUtil.fromJson(deviceStateInfo.getStateData(),
						SignaturePadRemittanceMetaInfo.class);
				dto.setStateData(deviceManager.getRemittanceData(stateData.getRemittanceTransactionId()));
				break;
			default:
				break;
			}
		}
		return dto;
	}

	public BoolRespModel updateDeviceState(DeviceType deviceType, Integer countryBranchSystemInventoryId,
			IDeviceStateData deviceStateData, DeviceStateDataType type) {
		Device device = deviceDao.findDevice(new BigDecimal(countryBranchSystemInventoryId), deviceType);
		deviceValidation.validateDevice(device);
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		deviceManager.validateLogIn(device);
		logger.debug("updating device state D id {} ", device.getRegistrationId());
		String deviceStateDataStr = JsonUtil.toJson(deviceStateData);
		deviceStateInfo.setStateData(deviceStateDataStr);
		deviceStateInfo.setStateDataType(type);
		deviceDao.saveDeviceInfo(deviceStateInfo);
		return new BoolRespModel(Boolean.TRUE);

	}
}
