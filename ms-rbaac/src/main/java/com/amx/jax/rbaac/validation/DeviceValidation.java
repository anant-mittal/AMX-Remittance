package com.amx.jax.rbaac.validation;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.config.RbaacConfig;
import com.amx.jax.constant.DeviceState;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.rbaac.RbaacConstants;
import com.amx.jax.rbaac.dao.DeviceDao;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.service.BranchSystemDetailService;
import com.amx.jax.util.CryptoUtil;

@Component
public class DeviceValidation {

	@Autowired
	DeviceDao deviceDao;
	@Autowired
	CryptoUtil cryptoUtil;
	@Autowired
	BranchSystemDetailService branchDetailService;
	@Autowired
	RbaacConfig rbaacConfig ; 

	public void validateDevice(Device device) {

		if (device == null) {
			throw new AuthServiceException("No device found", RbaacServiceError.CLIENT_NOT_FOUND);
		}
		if (!device.getStatus().equals(RbaacConstants.YES)) {
			throw new AuthServiceException("Device is not active", RbaacServiceError.CLIENT_NOT_ACTIVE);
		}
	}

	public void validateDeviceToken(Device device, String otp) {

		String pairTokendb = device.getPairToken();
		if (pairTokendb == null) {
			throw new AuthServiceException("Opt not generated");
		}
		String pairToken = cryptoUtil.generateHash(device.getRegistrationId().toString(), otp);
		if (!pairToken.equals(pairTokendb)) {
			throw new AuthServiceException("Invalid pair otp");
		}
	}

	public void validateOtp(String otp) {
		if (StringUtils.isBlank(otp)) {
			throw new AuthServiceException("otp can not be empty");
		}
		if (otp.length() != 6) {
			throw new AuthServiceException("otp lenght should be 6");
		}
	}

	public void validateDeviceRegRequest(DeviceRegistrationRequest request) {
		BranchSystemDetail branchSystem = branchDetailService.findBranchSystemByIp(request.getBranchSystemIp());
		Device existing = deviceDao.findDevice(branchSystem.getCountryBranchSystemInventoryId(),
				request.getDeviceType());
		if (existing != null) {
			throw new AuthServiceException("Device already registered", RbaacServiceError.CLIENT_ALREADY_REGISTERED);
		}
	}

	public void validateSessionToken(String sessionToken, Integer registrationId) {
		DeviceStateInfo deviceStateInfo = deviceDao.findBySessionToken(sessionToken, registrationId);
		if (deviceStateInfo == null) {
			throw new AuthServiceException("Invalid session token", RbaacServiceError.CLIENT_INVALID_SESSION_TOKEN);
		}

	}

	public void validateDevice(Integer deviceRegId) {

		Device device = deviceDao.findDevice(new BigDecimal(deviceRegId));
		if (device == null) {
			throw new AuthServiceException("device not found with given reg id", RbaacServiceError.CLIENT_NOT_FOUND);
		}
		if (!RbaacConstants.YES.equals(device.getStatus())) {
			throw new AuthServiceException("device not active", RbaacServiceError.CLIENT_NOT_ACTIVE);
		}
	}

	public URL validateImageUrl(String imageUrl) {

		try {
			URL url = new URL(imageUrl);
			return url;
		} catch (MalformedURLException e) {
			throw new AuthServiceException("image url is not valid");
		}
	}

	public void validateDeviceForActivation(Device device) {
		if (device == null) {
			throw new AuthServiceException("No device found", RbaacServiceError.CLIENT_NOT_FOUND);
		}
	}

	public void validateSystemInventoryForDuplicateDevice(Device device) {
		Device activeDevice = deviceDao.findDevice(device.getBranchSystemInventoryId(), device.getDeviceType());
		if (activeDevice != null) {
			throw new AuthServiceException("Another device client already active",
					RbaacServiceError.CLIENT_ANOTHER_ALREADY_ACTIVE);
		}
	}
	
	/**
	 * validates the whether session pair otp has been validated within last 15 mins(configurable)
	 * @param deviceRegId
	 * 
	 */
	public void validateOtpValidationTimeLimit(BigDecimal deviceRegId) {
		Device device = deviceDao.findDevice(deviceRegId);
		int configValue = (rbaacConfig.getPaireOtpvalidationTime()) == null ? 15: rbaacConfig.getPaireOtpvalidationTime();
		Date otpTokenCreationDate = device.getOtpTokenCreatedDate();
		if (otpTokenCreationDate != null && !DeviceState.SESSION_PAIRED.equals(device.getState())) {
			Date now = Calendar.getInstance().getTime();
			long timeDiff = (now.getTime() - otpTokenCreationDate.getTime());
			if ((timeDiff / 60000) > configValue) {
				throw new AuthServiceException("Session token otp is not yet validated for " + configValue + " min",
						RbaacServiceError.CLIENT_EXPIRED_VALIDATE_OTP_TIME);
			}
		}
	}
}
