package com.amx.jax.device;

import javax.validation.constraints.NotNull;

import com.amx.jax.dict.UserClient.ClientType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.swagger.annotations.ApiModelProperty;

public class DeviceRestModels {

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface DevicePairingRequest {
		@ApiModelProperty(example = "192.168.14.162")
		String getDeivceTerminalId();

		void setDeivceTerminalId(String deivceTerminalId);

		@ApiModelProperty(example = "SIGNATURE_PAD")
		ClientType getDeivceClientType();

		void setDeivceClientType(ClientType deivceClientType);

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface DevicePairingResponse {

		void setDeviceRegToken(String devicePairingToken);

		String getDeviceRegToken();

		String getDeviceRegKey();

		void setDeviceRegKey(String deviceRegId);

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface SessionPairingRequest extends DevicePairingResponse {

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	public interface SessionPairingResponse {

		void setDeviceSessionToken(String sessionPairingToken);

		String getDeviceSessionToken();

		@ApiModelProperty(example = "654321")
		String getSessionOTP();

		void setSessionOTP(String sessionOTP);

		void setDeviceRequestKey(String deviceRequestKey);

		String getDeviceRequestKey();

	}

	public static class DeviceRestModel
			implements DevicePairingRequest, DevicePairingResponse, SessionPairingRequest, SessionPairingResponse {

		@NotNull
		String deivceTerminalId;
		ClientType deivceClientType;

		String deviceRegToken;
		String deviceRegKey;

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
		public String getDeviceRegKey() {
			return deviceRegKey;
		}

		@Override
		public void setDeviceRegKey(String deviceRegId) {
			this.deviceRegKey = deviceRegId;
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

}
