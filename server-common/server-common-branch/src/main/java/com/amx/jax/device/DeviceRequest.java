package com.amx.jax.device;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerError;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.sso.SSOUserSessions;
import com.amx.utils.ArgUtil;
import com.amx.utils.Random;

@Component
public class DeviceRequest {

	private static final Logger LOGGER = LoggerService.getLogger(DeviceRequest.class);

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired
	DeviceBox deviceBox;

	@Autowired(required = false)
	private HttpServletResponse response;

	@Autowired
	SSOUserSessions ssoUserSessions;

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

	public String getDeviceClientVersion() {
		return commonHttpRequest.get(AppConstants.APP_VERSION_XKEY);
	}

	public long getDeviceRequestTime() {
		return ArgUtil.parseAsLong(commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REQ_TIME_XKEY), 0L);
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
		AppContextUtil.getUserClient().setClientVersion(getDeviceClientVersion());
		if (ArgUtil.isEmpty(deviceRegId) || ArgUtil.isEmpty(deviceRegToken)) {
			throw new OffsiteServerError(OffsiteServerCodes.CLIENT_CREDS_MISSING)
					.put(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, deviceRegId);
		}
		return DeviceRestModels.getDevicePairingCreds(deviceRegId, deviceRegToken);
	}

	public boolean isDeviceAuthorized() {
		String deviceRegId = getDeviceRegId();
		String deviceRegToken = getDeviceRegToken();
		if (!ArgUtil.isEmpty(deviceRegId) && !ArgUtil.isEmpty(deviceRegToken)) {
			DeviceData deviceData = deviceBox.get(deviceRegId);
			if (deviceData != null && ArgUtil.isEmpty(deviceData.getDeviceReqKey())) {
				return false;
			}
		}
		return true;
	}

	public void unDeviceAuthorized(BigDecimal deviceRegId) {
		if (!ArgUtil.isEmpty(deviceRegId)) {
			String deviceRegIdStr = ArgUtil.parseAsString(deviceRegId);
			DeviceData deviceData = deviceBox.get(deviceRegIdStr);
			if (deviceData != null) {
				deviceData.setDeviceReqKey(null);
				ssoUserSessions.invalidateTerminal(deviceData.getTerminalId());
				deviceBox.put(deviceRegIdStr, deviceData);
			}
		}
	}

	public DeviceData validateSession() {
		DevicePairingCreds devicePairingCreds = validateDevice();

		String sessionPairToken = getDeviceSessionToken();

		if (ArgUtil.isEmpty(sessionPairToken)) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_CLIENT_SESSION, "Missing SessionPairingToken")
					.put(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, devicePairingCreds.getDeviceRegId());
		}

		LOGGER.debug("validateSession for RID:{} C:{}", devicePairingCreds.getDeviceRegId(),
				DeviceData.class.getName());

		DeviceData deviceData = deviceBox.get(devicePairingCreds.getDeviceRegId());

		if (deviceData == null) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_CLIENT_SESSION, "Invalid Device")
					.put(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, devicePairingCreds.getDeviceRegId());
		}

		if (!DeviceConstants.validateSessionPairingTokenX(devicePairingCreds.getDeviceRegId(), sessionPairToken,
				deviceData.getSessionPairingTokenX())) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_CLIENT_SESSION, "Invalid SessionPairingToken")
					.put(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, devicePairingCreds.getDeviceRegId());
		}
		return deviceData;
	}

	public DeviceData validateRequest() {
		DeviceData deviceData = validateSession();
		// Same logic on client side
		if (!DeviceConstants.validateDeviceReqToken(deviceData.getDeviceReqKey(), getDeviceRegId(),
				getDeviceRequestToken())) {
			throw new OffsiteServerError(OffsiteServerCodes.INVALID_CLIENT_REQUEST,
					String.format("Time Difference %s", System.currentTimeMillis() - getDeviceRequestTime()))
							.put(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, getDeviceRegId());
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
			String empId, ClientType clientType) {

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
		deviceData.setRegId(deviceRegKey);
		deviceData.setClientType(clientType);
		LOGGER.debug("createSession RID:{} C:{}", deviceRegKey, DeviceData.class.getName());
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
