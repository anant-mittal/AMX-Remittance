package com.amx.jax.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.DeviceDao;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dbmodel.DeviceStateInfo;
import com.amx.jax.error.JaxError;
import com.amx.jax.util.CryptoUtil;

@Component
public class DeviceValidation {

	@Autowired
	DeviceDao deviceDao;
	@Autowired
	CryptoUtil cryptoUtil;

	public void validateDevice(Device device) {

		if (device == null) {
			throw new GlobalException("No device found", JaxError.DEVICE_NOT_FOUND);
		}
		if (!device.getStatus().equals(ConstantDocument.Yes)) {
			throw new GlobalException("Device is not active", JaxError.DEVICE_NOT_ACTIVE);
		}
	}

	public void validateDeviceToken(Device device, String otp) {

		DeviceStateInfo deviceStateInfo = deviceDao.getDeviceStateInfo(device);
		String pairTokendb = deviceStateInfo.getPairToken();
		String pairToken = cryptoUtil.getHash(device.getRegistrationId().toString(), otp);
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

}
