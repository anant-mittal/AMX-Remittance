package com.amx.jax.offsite.device;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceRestModels;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
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

	public String getDeviceRequestToken() {
		return commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REQ_TOKEN_XKEY);
	}

	public DevicePairingCreds validateDevice() {
		String deviceRegKey = getDeviceRegKey();
		String deviceRegToken = getDeviceRegToken();
		if (ArgUtil.isEmpty(deviceRegKey) || ArgUtil.isEmpty(deviceRegToken)) {
			throw new OffsiteServerError(OffsiteServerCodes.DEVICE_CREDS_MISSING);
		}
		return DeviceRestModels.getDevicePairingCreds(deviceRegKey, deviceRegToken);
	}

	public DeviceData validateSession() {
		DevicePairingCreds devicePairingCreds = validateDevice();

		String sessionPairToken = getDeviceSessionToken();

		if (ArgUtil.isEmpty(sessionPairToken)) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_DEVICE_SESSION, "Missing SessionPairingToken");
		}

		DeviceData deviceData = deviceBox.get(devicePairingCreds.getDeviceRegKey());
		if (deviceData == null) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_DEVICE_SESSION, "Invalid Device");
		}

		if (!DeviceConstants.validateSessionPairingTokenX(devicePairingCreds.getDeviceRegKey(), sessionPairToken,
				deviceData.getSessionPairingTokenX())) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_DEVICE_SESSION, "Invalid SessionPairingToken");
		}
		return deviceData;
	}

	public DeviceData validateRequest() {
		DeviceData deviceData = validateSession();
		// Same logic on client side
		if (!DeviceConstants.validateDeviceReqToken(deviceData.getDeviceReqKey(), getDeviceRegToken(),
				getDeviceRequestToken())) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_DEVICE_REQUEST);
		}
		return deviceData;
	}

	public SessionPairingCreds createSession(String sessionPairToken, String sessionOtp, String terminalId) {

		String deviceRegKey = getDeviceRegKey();
		DeviceData deviceData = new DeviceData();

		deviceData.setTerminalId(terminalId);
		// Session Request Key
		deviceData.setDeviceReqKey(Random.randomAlphaNumeric(10));

		// Generate and Save Encrypted version of SessionPairing Key
		deviceData
				.setSessionPairingTokenX(DeviceConstants.generateSessionPairingTokenX(deviceRegKey, sessionPairToken));
		deviceBox.put(deviceRegKey, deviceData);
		response.setHeader(DeviceConstants.Keys.DEVICE_REQ_KEY_XKEY, deviceData.getDeviceReqKey());

		// Prepare Response
		SessionPairingCreds creds = DeviceRestModels.get();
		creds.setSessionOTP(sessionOtp);
		creds.setDeviceSessionToken(sessionPairToken);
		creds.setDeviceRequestKey(deviceData.getDeviceReqKey());

		return creds;
	}

}
