package com.amx.jax.device;

import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.utils.CryptoUtil;

public class DeviceConstants {

	public static final class Config {
		public static final long REQUEST_TOKEN_VALIDITY = 60;

		@Deprecated
		public static final long REQUEST_TOKEN_VALIDITY_OLD = 30;
		public static final long SESSION_TOKEN_VALIDITY = 3 * 3600;
	}

	public static class Keys {
		public static final String CLIENT_REG_KEY_XKEY = "x-device-reg-id";
		public static final String CLIENT_REG_TOKEN_XKEY = "x-device-reg-token";

		public static final String CLIENT_SESSION_TOKEN_XKEY = "x-device-session-token";

		public static final String DEVICE_REQ_TIME_XKEY = "x-device-req-time";
		public static final String DEVICE_REQ_KEY_XKEY = "x-device-req-key";
		public static final String CLIENT_REQ_TOKEN_XKEY = "x-device-req-token";
	}

	public static class Path {
		public static final String DEVICE_TERMINALS = "/pub/device/terminal/list";
		public static final String DEVICE_PAIR = "/pub/device/pair";
		public static final String DEVICE_ACTIVATE = "/pub/device/activate";
		public static final String DEVICE_DEACTIVATE = "/pub/device/deactivate";
		public static final String DEVICE_DELETE = "/pub/device/delete";
		public static final String DEVICE_SESSION = "/pub/device/session";
		public static final String SESSION_PAIR = "/pub/device/session/pair";
		public static final String SESSION_TERMINAL = "/pub/device/session/terminal";
		public static final String DEVICE_STATUS_CARD = "/pub/device/status/card";
		public static final String DEVICE_STATUS_NOTIPY = "/pub/notipy/status";
		public static final String DEVICE_SEND_NOTIPY = "/pub/notipy/send";
		public static final String DEVICE_VERIFY_NOTIPY = "/pub/notipy/verify";

		@Deprecated
		public static final String DEVICE_STATUS_ACTIVITY = "/pub/device/status/activity";
	}

	public static class Params {
		public static final String PARAM_CLIENT_TYPE = "clientType";
		@Deprecated
		public static final String PARAM_DEVICE_TYPE = "deviceType";
		public static final String PARAM_CLIENT_ID = "clientId";
		public static final String PARAM_SYSTEM_ID = "systemid";
		public static final String PARAM_DEVICE_REG_ID = "deviceRegId";
	}

	public static String generateDeviceReqToken(String deviceReqKey, String deviceRegId) {
		return CryptoUtil.generateHMAC(DeviceConstants.Config.REQUEST_TOKEN_VALIDITY, deviceReqKey, deviceRegId);
	}

	public static String generateDeviceReqToken(
			SessionPairingCreds sessionPairingCreds,
			DevicePairingCreds devicePairingCreds) {
		return generateDeviceReqToken(sessionPairingCreds.getDeviceRequestKey(), devicePairingCreds.getDeviceRegId());
	}

	public static boolean validateDeviceReqToken(String deviceReqKey, String deviceRegId, String deviceReqToken) {
		return CryptoUtil.validateHMAC(
				DeviceConstants.Config.REQUEST_TOKEN_VALIDITY, deviceReqKey, deviceRegId,
				deviceReqToken)
				|| CryptoUtil.validateHMAC(
						DeviceConstants.Config.REQUEST_TOKEN_VALIDITY_OLD, deviceReqKey, deviceRegId,
						deviceReqToken);
	}

	public static String generateSessionPairingTokenX(String deviceRegToken, String sessionPairingToken) {
		return CryptoUtil.generateHMAC(
				DeviceConstants.Config.SESSION_TOKEN_VALIDITY, deviceRegToken,
				sessionPairingToken);
	}

	public static boolean validateSessionPairingTokenX(
			String deviceRegKey, String sessionPairingToken,
			String sessionPairingTokenX) {
		return CryptoUtil.validateHMAC(
				DeviceConstants.Config.SESSION_TOKEN_VALIDITY, deviceRegKey, sessionPairingToken,
				sessionPairingTokenX);
	}

}
