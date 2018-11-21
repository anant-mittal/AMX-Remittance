package com.amx.jax.device;

import javax.validation.constraints.NotNull;

import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DeviceRestModels {

	public static class NetAddress {
		String mac;
		String localIp;
		String hostName;
		String userName;

		public String getHostName() {
			return hostName;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getMac() {
			return mac;
		}

		public void setMac(String mac) {
			this.mac = mac;
		}

		public String getLocalIp() {
			return localIp;
		}

		public void setLocalIp(String localIp) {
			this.localIp = localIp;
		}
	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface DevicePairingRequest {
		@ApiMockModelProperty(example = "192.168.14.162")
		String getDeivceTerminalId();

		void setDeivceTerminalId(String deivceTerminalId);

		@ApiMockModelProperty(example = "SIGNATURE_PAD")
		ClientType getDeivceClientType();

		void setDeivceClientType(ClientType deivceClientType);

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface DevicePairingCreds extends DevicePairingRequest {

		void setDeviceRegToken(String deviceRegToken);

		String getDeviceRegToken();

		String getDeviceRegId();

		void setDeviceRegId(String deviceRegId);

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface SessionPairingRequest extends DevicePairingCreds {

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface SessionPairingCreds {

		void setDeviceSessionToken(String sessionPairingToken);

		String getDeviceSessionToken();

		@ApiMockModelProperty(example = "654321")
		String getSessionOTP();

		void setSessionOTP(String sessionOTP);

		void setDeviceRequestKey(String deviceRequestKey);

		String getDeviceRequestKey();

	}

	public static class DeviceRestModel
			implements DevicePairingRequest, DevicePairingCreds, SessionPairingRequest, SessionPairingCreds {

		@NotNull
		String deivceTerminalId;
		ClientType deivceClientType;

		String deviceRegToken;
		String deviceRegId;

		String deviceSessionToken;

		String sessionOTP;

		String deviceRequestKey;

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
		public String getDeviceRegToken() {
			return deviceRegToken;
		}

		@Override
		public void setDeviceRegToken(String devicePairingToken) {
			this.deviceRegToken = devicePairingToken;
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
		public String getDeviceSessionToken() {
			return deviceSessionToken;
		}

		@Override
		public void setDeviceSessionToken(String deviceSessionToken) {
			this.deviceSessionToken = deviceSessionToken;
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
		public void setDeviceRequestKey(String deviceRequestKey) {
			this.deviceRequestKey = deviceRequestKey;
		}

		@Override
		public String getDeviceRequestKey() {
			return this.deviceRequestKey;
		}

	}

	public static DeviceRestModel get() {
		return new DeviceRestModel();
	}

	public static DevicePairingCreds getDevicePairingCreds(String deviceRegId, String deviceRegToken) {
		DevicePairingCreds model = new DeviceRestModel();
		model.setDeviceRegId(deviceRegId);
		model.setDeviceRegToken(deviceRegToken);
		return model;
	}

}
