package com.amx.jax.validation;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.DeviceDao;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.service.BranchDetailService;
import com.amx.jax.util.CryptoUtil;

@Component
public class DeviceValidation {

	@Autowired
	DeviceDao deviceDao;
	@Autowired
	CryptoUtil cryptoUtil;
	@Autowired
	BranchDetailService branchDetailService;

	public void validateDevice(Device device) {

		if (device == null) {
			throw new GlobalException("No device found", JaxError.CLIENT_NOT_FOUND);
		}
		if (!device.getStatus().equals(ConstantDocument.Yes)) {
			throw new GlobalException("Device is not active", JaxError.CLIENT_NOT_ACTIVE);
		}
	}

	public void validateDeviceToken(Device device, String otp) {

		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		String pairTokendb = deviceStateInfo.getOtpToken();
		if (pairTokendb == null) {
			throw new GlobalException("Opt not generated");
		}
		String pairToken = cryptoUtil.generateHash(device.getRegistrationId().toString(), otp);
		if (!pairToken.equals(pairTokendb)) {
			throw new GlobalException("Invalid pair otp");
		}
	}

	public void validateOtp(String otp) {
		if (StringUtils.isBlank(otp)) {
			throw new GlobalException("otp can not be empty");
		}
		if (otp.length() != 6) {
			throw new GlobalException("otp lenght should be 6");
		}
	}

	public void validateDeviceRegRequest(DeviceRegistrationRequest request) {
		BranchSystemDetail branchSystem = branchDetailService.findBranchSystemByIp(request.getBranchSystemIp());
		Device existing = deviceDao.findDevice(branchSystem.getCountryBranchSystemInventoryId(),
				request.getDeviceType());
		if (existing != null) {
			throw new GlobalException("Device already registered", JaxError.CLIENT_ALREADY_REGISTERED);
		}
	}

	public void validatePaireToken(String paireToken, Integer registrationId) {
		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfoByPaireToken(paireToken, registrationId);
		if (deviceStateInfo == null) {
			throw new GlobalException("Invalid paire token", JaxError.CLIENT_INVALID_PAIR_TOKEN);
		}
	}

	public void validateSessionToken(String sessionToken, Integer registrationId) {
		DeviceStateInfo deviceStateInfo = deviceDao.findBySessionToken(sessionToken, registrationId);
		if (deviceStateInfo == null) {
			throw new GlobalException("Invalid session token", JaxError.CLIENT_INVALID_SESSION_TOKEN);
		}

	}

	public void validateDevice(Integer deviceRegId) {

		Device device = deviceDao.findDevice(new BigDecimal(deviceRegId));
		if (device == null) {
			throw new GlobalException("device not found with given reg id", JaxError.CLIENT_NOT_FOUND);
		}
		if (!ConstantDocument.Yes.equals(device.getStatus())) {
			throw new GlobalException("device not active", JaxError.CLIENT_NOT_ACTIVE);
		}
	}

	public URL validateImageUrl(String imageUrl) {

		try {
			URL url = new URL(imageUrl);
			return url;
		} catch (MalformedURLException e) {
			throw new GlobalException("image url is not valid");
		}
	}

	public void validateDeviceForActivation(Device device) {
		if (device == null) {
			throw new GlobalException("No device found", JaxError.CLIENT_NOT_FOUND);
		}
	}

}
