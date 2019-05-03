package com.amx.jax.device;

import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.swagger.ApiMockModelProperty;
import com.amx.utils.CryptoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DeviceRestModels {

	@JsonDeserialize(as = DeviceRestModel.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public interface DevicePairingRequest {

		@ApiMockModelProperty(example = "284052306594", required = false)
		String getIdentity();

		void setIdentity(String identity);

		@ApiMockModelProperty(example = "192.168.14.162", required = false)
		String getDeivceTerminalId();

		void setDeivceTerminalId(String deivceTerminalId);

		@ApiMockModelProperty(example = "SIGNATURE_PAD")
		ClientType getDeivceClientType();

		void setDeivceClientType(ClientType deivceClientType);

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public interface DeviceModelConfig {
		long getOtpTtl();

		void setOtpTtl(long otpValidity);

		public long getRequestTtl();

		public void setRequestTtl(long requestTtl);

		public String getOtpChars();

		public void setOtpChars(String otpChars);
	}

	@JsonDeserialize(as = DeviceRestModel.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public interface DevicePairingCreds extends DevicePairingRequest, DeviceModelConfig {

		void setDeviceRegToken(String deviceRegToken);

		String getDeviceRegToken();

		String getDeviceRegId();

		void setDeviceRegId(String deviceRegId);

		String getDeviceSecret();

		void setDeviceSecret(String deviceSecret);

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public interface SessionPairingRequest extends DevicePairingCreds {

	}

	@JsonDeserialize(as = DeviceRestModel.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public interface SessionPairingCreds extends DeviceModelConfig {

		void setDeviceSessionToken(String sessionPairingToken);

		String getDeviceSessionToken();

		@ApiMockModelProperty(example = "654321")
		String getSessionOTP();

		void setSessionOTP(String sessionOTP);

		void setDeviceRequestKey(String deviceRequestKey);

		String getDeviceRequestKey();

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DeviceRestModel
			implements DevicePairingRequest, DevicePairingCreds, SessionPairingRequest, SessionPairingCreds {

		String identity;

		String deivceTerminalId;
		ClientType deivceClientType;

		String deviceRegToken;
		String deviceRegId;

		String deviceSessionToken;

		String sessionOTP;

		String deviceRequestKey;

		String deviceSecret;

		private long otpTtl;
		private long requestTtl;
		private String otpChars = CryptoUtil.COMPLEX_CHARS;

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

		@Override
		public String getIdentity() {
			return this.identity;
		}

		@Override
		public void setIdentity(String identity) {
			this.identity = identity;
		}

		@Override
		public String getDeviceSecret() {
			return this.deviceSecret;
		}

		@Override
		public void setDeviceSecret(String deviceSecret) {
			this.deviceSecret = deviceSecret;
		}

		@Override
		public long getOtpTtl() {
			return this.otpTtl;
		}

		@Override
		public void setOtpTtl(long otpTtl) {
			this.otpTtl = otpTtl;
		}

		@Override
		public long getRequestTtl() {
			return requestTtl;
		}

		@Override
		public void setRequestTtl(long requestTtl) {
			this.requestTtl = requestTtl;
		}

		@Override
		public String getOtpChars() {
			return otpChars;
		}

		@Override
		public void setOtpChars(String otpChars) {
			this.otpChars = otpChars;
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
