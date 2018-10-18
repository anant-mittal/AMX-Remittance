package com.amx.jax.manager;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.DeviceDao;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.services.DeviceService;
import com.amx.jax.services.JaxConfigService;
import com.amx.jax.util.CryptoUtil;
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
	JaxConfigService jaxConfigService;

	/**
	 * activates device
	 * 
	 * @param countryBranchSystemInventoryId
	 * @param deviceType
	 * 
	 */
	public void activateDevice(Integer countryBranchSystemInventoryId, String deviceType) {
		Device device = deviceDao.findDevice(new BigDecimal(countryBranchSystemInventoryId), deviceType);
		if (device == null) {
			throw new GlobalException("No device found");
		}
		device.setStatus(ConstantDocument.Yes);
		deviceDao.saveDevice(device);
	}

	/**
	 * generates pairing opt and save in db
	 * 
	 * @param device
	 * @return otp
	 * 
	 */
	public String generateOtp(Device device) {
		String otp = Random.randomNumeric(6);
		logger.debug("generated otp for device {} otp {}", device.getRegistrationId(), otp);
		DeviceStateInfo deviceInfo = deviceDao.getDeviceStateInfo(device);
		String otpHash = cryptoUtil.getHash(device.getRegistrationId().toString(), otp);
		deviceInfo.setPairToken(otpHash);
		deviceDao.saveDeviceInfo(deviceInfo);
		return otp;
	}

	public boolean isLoggedIn(Device device) {
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		String sessionToken = deviceStateInfo.getSessionToken();
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
		JaxConfig jaxConf = jaxConfigService.getConfig("DEVICE_SESISON_TIMEOUT");
		if (jaxConf != null) {
			return Long.parseLong(jaxConf.getValue());
		} else {
			return DeviceService.DEVICE_SESSION_TIMEOUT;
		}
	}

	public void validateLogIn(Device device) {
		if (!isLoggedIn(device)) {
			throw new GlobalException("Device not logged in", JaxError.DEVICE_NOT_LOGGGED_IN);
		}
	}
}
