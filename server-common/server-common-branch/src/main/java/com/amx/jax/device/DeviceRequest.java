package com.amx.jax.device;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConstants;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerError;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.http.CommonHttpRequest;
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

	public String getDeviceRegId() {
		return commonHttpRequest.get(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY);
	}

	public String getDeviceRegToken() {
		return commonHttpRequest.get(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY);
	}

	public String getDeviceSessionToken() {
		return commonHttpRequest.get(DeviceConstants.Keys.CLIENT_SESSION_TOKEN_XKEY);
	}

	public String getDeviceRequestToken() {
		return commonHttpRequest.get(DeviceConstants.Keys.CLIENT_REQ_TOKEN_XKEY);
	}

	public DeviceData getDeviceData() {
		DeviceData deviceData = deviceBox.get(this.getDeviceRegId());
		if (deviceData == null) {
			return deviceBox.put(this.getDeviceRegId(), new DeviceData());
		}
		return deviceData;
	}

	public void save() {
		DeviceData deviceData = deviceBox.get(this.getDeviceRegId());
		deviceBox.put(this.getDeviceRegId(), deviceData);
	}

	public DevicePairingCreds validateDevice() {
		String deviceRegId = getDeviceRegId();
		String deviceRegToken = getDeviceRegToken();
		if (ArgUtil.isEmpty(deviceRegId) || ArgUtil.isEmpty(deviceRegToken)) {
			throw new OffsiteServerError(OffsiteServerCodes.CLIENT_CREDS_MISSING);
		}
		return DeviceRestModels.getDevicePairingCreds(deviceRegId, deviceRegToken);
	}

	public DeviceData validateSession() {
		DevicePairingCreds devicePairingCreds = validateDevice();

		String sessionPairToken = getDeviceSessionToken();

		if (ArgUtil.isEmpty(sessionPairToken)) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_CLIENT_SESSION, "Missing SessionPairingToken");
		}

		DeviceData deviceData = deviceBox.get(devicePairingCreds.getDeviceRegId());
		if (deviceData == null) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_CLIENT_SESSION, "Invalid Device");
		}

		if (!DeviceConstants.validateSessionPairingTokenX(devicePairingCreds.getDeviceRegId(), sessionPairToken,
				deviceData.getSessionPairingTokenX())) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_CLIENT_SESSION, "Invalid SessionPairingToken");
		}
		return deviceData;
	}

	public DeviceData validateRequest() {
		DeviceData deviceData = validateSession();
		// Same logic on client side
		if (!DeviceConstants.validateDeviceReqToken(deviceData.getDeviceReqKey(), getDeviceRegId(),
				getDeviceRequestToken())) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_CLIENT_REQUEST);
		}
		return deviceData;
	}

	public void updateStamp(Object deviceRegId) {
		deviceBox.updateStamp(deviceRegId);
	}

	public void checkStamp(Object deviceRegId) {
		deviceBox.checkStamp(deviceRegId);
	}

	public SessionPairingCreds createSession(String sessionPairToken, String sessionOtp, String terminalId,
			String empId) {

		String deviceRegKey = getDeviceRegId();
		DeviceData deviceData = new DeviceData();

		deviceData.setTerminalId(terminalId);
		deviceData.setEmpId(empId);
		// Session Request Key
		deviceData.setDeviceReqKey(Random.randomAlphaNumeric(10));

		// Generate and Save Encrypted version of SessionPairing Key
		deviceData
				.setSessionPairingTokenX(DeviceConstants.generateSessionPairingTokenX(deviceRegKey, sessionPairToken));
		deviceData.setUpdatestamp(System.currentTimeMillis());
		deviceData.setLocalIp(commonHttpRequest.get(AppConstants.DEVICE_IP_LOCAL_XKEY));
		deviceData.setGlobalIp(commonHttpRequest.getIPAddress());
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
