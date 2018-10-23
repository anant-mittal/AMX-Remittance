package com.amx.jax.offsite.device;

import com.amx.jax.dict.UserClient.ClientType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DevicePairingModels {

	@JsonDeserialize(as = DeviceReqResp.class)
	public interface PairingRequest {

	}

	@JsonDeserialize(as = DeviceReqResp.class)
	public interface PairingResponse {

	}

	public static class DeviceReqResp implements PairingRequest, PairingResponse {

		ClientType deivceClientType;
		String devicePairingToken;
		String deviceRegId;
		String sessionPairingToken;
		String sessionOTP;
		String sessionClientKey;
		String requestToken;

		public ClientType getDeivceClientType() {
			return deivceClientType;
		}

		public void setDeivceClientType(ClientType deivceClientType) {
			this.deivceClientType = deivceClientType;
		}

		public String getDevicePairingToken() {
			return devicePairingToken;
		}

		public void setDevicePairingToken(String devicePairingToken) {
			this.devicePairingToken = devicePairingToken;
		}

		public String getDeviceRegId() {
			return deviceRegId;
		}

		public void setDeviceRegId(String deviceRegId) {
			this.deviceRegId = deviceRegId;
		}

		public String getSessionPairingToken() {
			return sessionPairingToken;
		}

		public void setSessionPairingToken(String sessionPairingToken) {
			this.sessionPairingToken = sessionPairingToken;
		}

		public String getSessionOTP() {
			return sessionOTP;
		}

		public void setSessionOTP(String sessionOTP) {
			this.sessionOTP = sessionOTP;
		}

		public String getSessionClientKey() {
			return sessionClientKey;
		}

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

}
