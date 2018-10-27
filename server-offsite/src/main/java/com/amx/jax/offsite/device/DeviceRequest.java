package com.amx.jax.offsite.device;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceRestModels;
import com.amx.jax.device.DeviceRestModels.SessionPairingResponse;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.offsite.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.offsite.OffsiteStatus.OffsiteServerError;
import com.amx.jax.offsite.device.DeviceConfigs.DeviceBox;
import com.amx.jax.offsite.device.DeviceConfigs.DeviceData;
import com.amx.utils.ArgUtil;
import com.amx.utils.Random;

@Component
public class DeviceRequest {

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	DeviceBox deviceBox;

	@Autowired(required = false)
	private HttpServletRequest request;

	@Autowired(required = false)
	private HttpServletResponse response;

	public String getDeviceRegKey() {
		return commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REG_KEY_XKEY);
	}

	public String getDeviceRegToken() {
		return commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REG_TOKEN_XKEY);
	}

	public String getDeviceSessionToken() {
		return commonHttpRequest.get(DeviceConstants.Keys.DEVICE_SESSION_TOKEN_XKEY);
	}

	public String setDeviceRequestToken() {
		return commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REQ_TOKEN_XKEY);
	}

	public boolean isRequired(String requestURI) {
		String deviceRegKey = commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REG_KEY_XKEY);
		return (!ArgUtil.isEmpty(deviceRegKey) && !requestURI.startsWith(DeviceConstants.Path.SESSION_PAIR));
	}

	public boolean isValid() {

		String sessionPairToken = commonHttpRequest.get(DeviceConstants.Keys.DEVICE_SESSION_TOKEN_XKEY);

		if (ArgUtil.isEmpty(sessionPairToken)) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_DEVICE_SESSION, "Missing SessionPairingToken");
		}

		String deviceRegKey = commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REG_KEY_XKEY);
		DeviceData deviceData = deviceBox.get(deviceRegKey);
		if (deviceData == null) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_DEVICE_SESSION, "Invalid Device");
		}

		if (!DeviceConstants.validateSessionPairingTokenX(deviceRegKey, sessionPairToken,
				deviceData.getSessionPairingTokenX())) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_DEVICE_SESSION, "Invalid SessionPairingToken");
		}

		String deviceRegToken = commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REG_TOKEN_XKEY);
		String deviceReqToken = commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REQ_TOKEN_XKEY);

		// Same logic on client side
		if (!DeviceConstants.validateDeviceReqToken(deviceData.getDeviceReqKey(), deviceRegToken, deviceReqToken)) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_DEVICE_REQUEST);
		}
		return true;
	}

	public void validateRequest() {
		if (!isValid()) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_DEVICE_REQUEST);
		}
	}

	public SessionPairingResponse createSession(String sessionPairToken, String sessionOtp) {

		String deviceRegKey = commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REG_KEY_XKEY);
		DeviceData deviceData = new DeviceData();

		// Session Request Key
		deviceData.setDeviceReqKey(Random.randomAlphaNumeric(10));

		// Generate and Save Encrypted version of SessionPairing Key
		deviceData
				.setSessionPairingTokenX(DeviceConstants.generateSessionPairingTokenX(deviceRegKey, sessionPairToken));

		deviceBox.put(deviceRegKey, deviceData);
		response.setHeader(DeviceConstants.Keys.DEVICE_REQ_KEY_XKEY, deviceData.getDeviceReqKey());

		// Prepare Response
		SessionPairingResponse creds = DeviceRestModels.get();
		creds.setSessionOTP(sessionOtp);
		creds.setDeviceSessionToken(sessionPairToken);
		creds.setDeviceRequestKey(deviceData.getDeviceReqKey());

		return creds;
	}

}
