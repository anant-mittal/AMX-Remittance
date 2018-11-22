package com.amx.jax.rbaac.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.config.RbaacConfig;
import com.amx.jax.constant.DeviceState;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.model.response.IDeviceStateData;
import com.amx.jax.rbaac.RbaacConstants;
import com.amx.jax.rbaac.dao.DeviceDao;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.service.BranchSystemDetailService;
import com.amx.jax.rbaac.service.DeviceService;
import com.amx.jax.util.CryptoUtil;
import com.amx.utils.ArgUtil;
import com.amx.utils.Random;

/**
 * @author Prashant
 *
 */
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component

public class DeviceManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	DeviceDao deviceDao;
	@Autowired
	CryptoUtil cryptoUtil;
	@Autowired
	BranchSystemDetailService branchDetailService;
	@Autowired
	RbaacConfig rbaacConfig;

	/**
	 * generates pairing opt and save in db
	 * 
	 * @param device
	 * @return otp
	 * 
	 */
	public DevicePairOtpResponse generateOtp(Device device) {
		String otp = Random.randomNumeric(6);
		logger.debug("generated otp for device {} otp {}", device.getRegistrationId(), otp);
		String otpHash = cryptoUtil.generateHash(device.getRegistrationId().toString(), otp);
		device.setOtpToken(otpHash);
		String sessionPairToken = generateSessionPairToken(device);
		device.setState(DeviceState.SESSION_CREATED);
		device.setSessionToken(sessionPairToken);
		device.setOtpTokenCreatedDate(Calendar.getInstance().getTime());
		deviceDao.saveDevice(device);
		DevicePairOtpResponse resp = generateDevicePaireOtpResponse(device); 
		resp.setSessionPairToken(sessionPairToken);
		resp.setOtp(otp);
		return resp;
	}

	public String generateSessionPairToken(Device device) {
		String hmacToken = com.amx.utils.CryptoUtil.generateHMAC(getDeviceSessionTimeout(), "DEVICE_SESSION_SALT",
				device.getRegistrationId().toString());
		return hmacToken;
	}

	public boolean isLoggedIn(Device device) {
		String sessionToken = device.getSessionToken();
		if (sessionToken == null) {
			return false;
		}
		String hmacToken = com.amx.utils.CryptoUtil.generateHMAC(getDeviceSessionTimeout(), "DEVICE_SESSION_SALT",
				device.getRegistrationId().toString());
		if (sessionToken.equals(hmacToken)) {
			return true;
		}
		return false;
	}

	public long getDeviceSessionTimeout() {
		Long sessionTimeout = rbaacConfig.getDeviceSessionTimeout();
		if (sessionTimeout != null) {
			return sessionTimeout.longValue() * 60;
		}
		return DeviceService.DEVICE_SESSION_TIMEOUT;
	}

	public void validateLogIn(Device device) {
		if (!isLoggedIn(device)) {
			throw new AuthServiceException("Device not logged in", RbaacServiceError.CLIENT_NOT_LOGGGED_IN);
		}
	}

	public void validateDeviceActivationRequest(Integer countryBranchSystemInventoryId, ClientType deviceType) {
		Device device = deviceDao.findDevice(new BigDecimal(countryBranchSystemInventoryId), deviceType);
		if (device != null) {
			throw new AuthServiceException("Device already active", RbaacServiceError.CLIENT_ALREADY_ACTIVE);
		}
	}

	public void validateSessionToken(String sessionToken, Integer registrationId) {
		DeviceStateInfo deviceStateInfo = deviceDao.findBySessionToken(sessionToken, registrationId);
		if (deviceStateInfo == null) {
			throw new AuthServiceException("Invalid session token", RbaacServiceError.CLIENT_INVALID_SESSION_TOKEN);
		}
		Device device = deviceDao.findDevice(new BigDecimal(registrationId));
		String sessionTokenGen = generateSessionPairToken(device);
		if (!sessionToken.equals(sessionTokenGen)) {
			throw new AuthServiceException("Session token is expired", RbaacServiceError.CLIENT_EXPIRED_SESSION_TOKEN);
		}
	}

	public void deactivateDevice(Device device) {
		device.setStatus(RbaacConstants.NO);
		deviceDao.saveDevice(device);
	}

	public DevicePairOtpResponse generateDevicePaireOtpResponse(Device device) {
		DevicePairOtpResponse resp = new DevicePairOtpResponse();
		resp.setDeviceRegId(device.getRegistrationId());
		resp.setTermialId(ArgUtil.parseAsString(device.getBranchSystemInventoryId()));
		resp.setDeviceState(device.getState());
		return resp;
	}
}
