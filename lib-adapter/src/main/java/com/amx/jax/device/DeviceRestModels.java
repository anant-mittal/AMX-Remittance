package com.amx.jax.device;

import com.amx.jax.dict.UserClient.ClientType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DeviceRestModels {

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface DevicePairingRequest {
		String getDeivceTerminalId();

		void setDeivceTerminalId(String deivceTerminalId);

		ClientType getDeivceClientType();

		void setDeivceClientType(ClientType deivceClientType);

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface DevicePairingResponse {

		void setDevicePairingToken(String devicePairingToken);

		String getDevicePairingToken();

		String getDeviceRegId();

		void setDeviceRegId(String deviceRegId);

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface SessionPairingRequest extends DevicePairingResponse {

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface SessionPairingResponse {

		void setSessionPairingToken(String sessionPairingToken);

		String getSessionPairingToken();

		String getSessionClientKey();

		String getSessionOTP();

		void setSessionClientKey(String sessionClientKey);

		void setSessionOTP(String sessionOTP);

	}

	public static class DeviceRestModel
			implements DevicePairingRequest, DevicePairingResponse, SessionPairingRequest, SessionPairingResponse {

		String deivceTerminalId;
		ClientType deivceClientType;

		String devicePairingToken;
		String deviceRegId;

		String sessionPairingToken;
		String sessionOTP;
		String sessionClientKey;

		String requestToken;

		@Override
		public String getDeivceTerminalId() {
			return deivceTerminalId;
		}

		@Override
		public void setDeivceTerminalId(String deivceTerminalId) {
			this.deivceTerminalId = deivceTerminalId;
		}

		@Override
		public ClientType getDeivceClientType() {
			return deivceClientType;
		}

		@Override
		public void setDeivceClientType(ClientType deivceClientType) {
			this.deivceClientType = deivceClientType;
		}

		@Override
		public String getDevicePairingToken() {
			return devicePairingToken;
		}

		@Override
		public void setDevicePairingToken(String devicePairingToken) {
			this.devicePairingToken = devicePairingToken;
		}

		@Override
		public String getDeviceRegId() {
			return deviceRegId;
		}

		@Override
		public void setDeviceRegId(String deviceRegId) {
			this.deviceRegId = deviceRegId;
		}

		@Override
		public String getSessionPairingToken() {
			return sessionPairingToken;
		}

		@Override
		public void setSessionPairingToken(String sessionPairingToken) {
			this.sessionPairingToken = sessionPairingToken;
		}

		@Override
		public String getSessionOTP() {
			return sessionOTP;
		}

		@Override
		public void setSessionOTP(String sessionOTP) {
			this.sessionOTP = sessionOTP;
		}

		@Override
		public String getSessionClientKey() {
			return sessionClientKey;
		}

		@Override
		public void setSessionClientKey(String sessionClientKey) {
			this.sessionClientKey = sessionClientKey;
		}

		public String getRequestToken() {
			return requestToken;
		}

		public void setRequestToken(String requestToken) {
			this.requestToken = requestToken;
		}
	}

	public static DeviceRestModel get() {
		return new DeviceRestModel();
	}

}
