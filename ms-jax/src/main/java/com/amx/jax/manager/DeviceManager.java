package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.DeviceState;
import com.amx.jax.dao.DeviceDao;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.device.SignaturePadRemittanceManager;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.IDeviceStateData;
import com.amx.jax.service.BranchDetailService;
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
	@Autowired
	SignaturePadRemittanceManager signaturePadRemittanceManager;
	@Autowired
	BranchDetailService branchDetailService;

	/**
	 * activates device
	 * 
	 * @param countryBranchSystemInventoryId
	 * @param deviceType
	 * 
	 */
	@Transactional
	public void activateDevice(Device device) {
		device.setStatus(ConstantDocument.Yes);
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		deviceStateInfo.setState(DeviceState.REGISTERED);
		List<Device> devices = deviceDao.findAllActiveDevices(device.getBranchSystemInventoryId(), device.getDeviceType());
		if (!CollectionUtils.isEmpty(devices)) {
			for (Device d : devices) {
				if (!d.equals(device)) {
					d.setStatus(ConstantDocument.No);
				}
			}
			deviceDao.saveDevices(devices);
		}
		deviceDao.saveDeviceInfo(deviceStateInfo);
		deviceDao.saveDevice(device);
	}

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
		DeviceStateInfo deviceInfo = deviceDao.getDeviceStateInfo(device);
		String otpHash = cryptoUtil.generateHash(device.getRegistrationId().toString(), otp);
		deviceInfo.setOtpToken(otpHash);
		String sessionPairToken = generateSessionPairToken(device);
		deviceInfo.setState(DeviceState.SESSION_CREATED);
		deviceInfo.setSessionToken(sessionPairToken);
		deviceInfo.setOtpTokenCreatedDate(Calendar.getInstance().getTime());
		deviceDao.saveDeviceInfo(deviceInfo);
		DevicePairOtpResponse resp = new DevicePairOtpResponse();
		resp.setOtp(otp);
		resp.setTermialId(getTerminalId(device));
		resp.setSessionPairToken(sessionPairToken);
		return resp;
	}

	private String getTerminalId(Device device) {
		return device.getBranchSystemInventoryId().toString();
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
			throw new GlobalException("Device not logged in", JaxError.CLIENT_NOT_LOGGGED_IN);
		}
	}

	public IDeviceStateData getRemittanceData(BigDecimal remittanceTransactionId) {
		return signaturePadRemittanceManager.getRemittanceReceiptData(remittanceTransactionId);
	}

	public String generateSessionPairToken(Device device) {
		String hmacToken = com.amx.utils.CryptoUtil.generateHMAC(getDeviceSessionTimeout(), "DEVICE_SESSION_SALT",
				device.getRegistrationId().toString());
		return hmacToken;
	}

	public void validateDeviceActivationRequest(Integer countryBranchSystemInventoryId, ClientType deviceType) {
		Device device = deviceDao.findDevice(new BigDecimal(countryBranchSystemInventoryId), deviceType);
		if (device != null) {
			throw new GlobalException("Device already active", JaxError.CLIENT_ALREADY_ACTIVE);
		}
	}

	public void validateSessionToken(String sessionToken, Integer registrationId) {
		DeviceStateInfo deviceStateInfo = deviceDao.findBySessionToken(sessionToken, registrationId);
		if (deviceStateInfo == null) {
			throw new GlobalException("Invalid session token", JaxError.CLIENT_INVALID_SESSION_TOKEN);
		}
		Device device = deviceDao.findDevice(new BigDecimal(registrationId));
		String sessionTokenGen = generateSessionPairToken(device);
		if (!sessionToken.equals(sessionTokenGen)) {
			throw new GlobalException("Session token is expired", JaxError.CLIENT_EXPIRED_SESSION_TOKEN);
		}
	}

	public void validateOtpValidationTimeLimit(BigDecimal deviceRegId) {
		Device device = deviceDao.findDevice(deviceRegId);
		DeviceStateInfo deviceInfo = deviceDao.getDeviceStateInfo(device);
		String configValueStr = jaxConfigService.getConfigValue("DEVICE_OTP_VALIDITY_MINS", "15");
		int configValue = Integer.parseInt(configValueStr);
		Date otpTokenCreationDate = deviceInfo.getOtpTokenCreatedDate();
		if (otpTokenCreationDate != null && !DeviceState.SESSION_PAIRED.equals(deviceInfo.getState())) {
			Date now = Calendar.getInstance().getTime();
			long timeDiff = (now.getTime() - otpTokenCreationDate.getTime());
			if ((timeDiff / 60000) > configValue) {
				throw new GlobalException("Session token otp is not yet validated for " + configValue + " min",
						JaxError.CLIENT_EXPIRED_VALIDATE_OTP_TIME);
			}
		}
	}

	public void deactivateDevice(Device device) {
		device.setStatus(ConstantDocument.No);
		deviceDao.saveDevice(device);
	}

}
