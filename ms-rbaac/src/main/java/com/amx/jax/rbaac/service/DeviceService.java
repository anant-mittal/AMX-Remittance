package com.amx.jax.rbaac.service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
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
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.rbaac.constants.RbaacServiceConstants;
import com.amx.jax.rbaac.dao.DeviceDao;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.manager.DeviceManager;
import com.amx.jax.rbaac.validation.DeviceValidation;
import com.amx.utils.CryptoUtil;
import com.amx.utils.Random;

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
		boolean deviceRegisteredAndActivated = deviceValidation.validateDeviceRegRequest(request, true);

		boolean registerDeviceByDefault = ClientType.BRANCH_ADAPTER.equals(request.getDeviceType())
				&& !deviceRegisteredAndActivated;

		Device newDevice = deviceDao.saveDevice(request, registerDeviceByDefault);
		DeviceState deviceState;
		if (RbaacServiceConstants.YES.equals(newDevice.getStatus())) {
			deviceState = DeviceState.REGISTERED;
		} else {
			deviceState = DeviceState.REGISTERED_NOT_ACTIVE;
		}
		newDevice.setState(deviceState);
		String devicePairToken = Random.randomAlpha(13);

		newDevice.setPairToken(this.getDevicePairTokenHash(devicePairToken, newDevice.getRegistrationId()));
		deviceDao.saveDevice(newDevice);

		logger.info("device registered with id: {}", newDevice.getRegistrationId());

		DeviceDto dto = new DeviceDto();
		try {
			BeanUtils.copyProperties(dto, newDevice);
		} catch (Exception e) {
		}
		dto.setPairToken(devicePairToken);
		return dto;
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
		deviceValidation.validateDeviceOtpToken(device, otp);
		// session pair success
		device.setState(DeviceState.SESSION_PAIRED);
		deviceDao.saveDevice(device);
		DevicePairOtpResponse resp = deviceManager.generateDevicePaireOtpResponse(device);
		return resp;
	}

	public DevicePairOtpResponse validateDevicePairToken(BigDecimal deviceRegId, String devicePairToken) {
		Device device = deviceDao.findDevice(deviceRegId);
		deviceValidation.validateDevice(device);
		String derivedPairToken = null;
		try {
			derivedPairToken = CryptoUtil.getSHA2Hash(devicePairToken + deviceRegId.toString());
		} catch (NoSuchAlgorithmException e) {
		}
		if (!device.getPairToken().equals(derivedPairToken)) {
			throw new AuthServiceException("Invalid paire token", RbaacServiceError.CLIENT_INVALID_PAIR_TOKEN);
		}
		return deviceManager.generateDevicePaireOtpResponse(device);
	}

	public DevicePairOtpResponse validateDeviceSessionPairToken(BigDecimal deviceRegId, String deviceSessionToken) {
		Device device = deviceDao.findDevice(deviceRegId);
		deviceValidation.validateDevice(device);
		String sessionTokenGen = deviceManager.generateSessionPairToken(device);
		if (!deviceSessionToken.equals(sessionTokenGen)) {
			throw new AuthServiceException("Session token is expired", RbaacServiceError.CLIENT_EXPIRED_SESSION_TOKEN);
		}
		deviceValidation.validateOtpValidationTimeLimit(deviceRegId);
		return deviceManager.generateDevicePaireOtpResponse(device);
	}

	public BigDecimal getDeviceRegIdByBranchInventoryId(ClientType deviceClientType,
			BigDecimal countryBranchSystemInventoryId) {
		List<Device> devices = deviceDao.findAllActiveDevices(countryBranchSystemInventoryId, deviceClientType);
		if (CollectionUtils.isEmpty(devices)) {
			throw new AuthServiceException("No device found", RbaacServiceError.CLIENT_NOT_FOUND);
		}
		return devices.get(0).getRegistrationId();
	}

	/**
	 * @param deviceClientType
	 * @param employeeId
	 * @return device object for given employee and device type
	 */
	public Device getDeviceByEmployeeAndDeviceType(ClientType deviceClientType, BigDecimal employeeId) {
		return deviceDao.findDeviceByEmployee(employeeId, deviceClientType);
	}

	public boolean validateEmployeeDeviceMapping(BigDecimal employeeId, String deviceId, BigDecimal deviceRegId,
			String deviceRegToken) {

		if (RbaacServiceConstants.OFFSITE_DEVICE_REG_ID.equals(deviceRegId)) {

			Device device = deviceDao.findDeviceByEmployee(employeeId, ClientType.OFFSITE_PAD);

			if (device == null || employeeId.longValue() != device.getEmployeeId().longValue()
					|| !deviceId.equalsIgnoreCase(device.getDeviceId())) {

				throw new AuthServiceException("Invalid Device Client : Not Paired or Not Mapped",
						RbaacServiceError.DEVICE_CLIENT_INVALID);

			}

		} else {

			Device device = deviceDao.findDevice(deviceRegId);

			String pairTokenHash = this.getDevicePairTokenHash(deviceRegToken, deviceRegId);

			if (device == null || employeeId.longValue() != device.getEmployeeId().longValue()
					|| !deviceId.equalsIgnoreCase(device.getDeviceId())
					|| !deviceRegToken.equalsIgnoreCase(pairTokenHash)) {

				throw new AuthServiceException("Invalid Device Client : Not Paired or Not Mapped",
						RbaacServiceError.DEVICE_CLIENT_INVALID);

			} else if (!RbaacServiceConstants.YES.equalsIgnoreCase(device.getStatus())) {

				logger.info("====== WARNING : Inactive Device Client : Contact Support ======");

				// throw new AuthServiceException("Inactive Device Client : Contact Support",
				// RbaacServiceError.CLIENT_NOT_ACTIVE);
			}

		}

		return Boolean.TRUE;
	}

	private String getDevicePairTokenHash(String pairToken, BigDecimal deviceRegId) {

		try {
			String devicePairTokenStr = pairToken + Long.toString(deviceRegId.longValue());
			return CryptoUtil.getSHA2Hash(devicePairTokenStr);

		} catch (Exception e) {
		}

		return null;
	}

}
