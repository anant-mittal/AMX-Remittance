package com.amx.jax.offsite.device;

import java.io.Serializable;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amx.jax.api.FileSubmitRequestModel;
import com.amx.jax.cache.CacheBox;
import com.amx.jax.device.CardData;

@Configuration
public class DeviceConfigs {

	public static class DeviceData implements Serializable {
		private static final long serialVersionUID = 2981932845270868040L;
		private String terminalId;

		FileSubmitRequestModel signature;

		public FileSubmitRequestModel getSignature() {
			return signature;
		}

		public void setSignature(FileSubmitRequestModel signature) {
			this.signature = signature;
		}

		public String getTerminalId() {
			return terminalId;
		}

		public void setTerminalId(String terminalId) {
			this.terminalId = terminalId;
		}

		private String deviceReqKey;
		private String sessionPairingTokenX;

		public String getSessionPairingTokenX() {
			return sessionPairingTokenX;
		}

		public void setSessionPairingTokenX(String sessionPairingTokenX) {
			this.sessionPairingTokenX = sessionPairingTokenX;
		}

		public String getDeviceReqKey() {
			return deviceReqKey;
		}

		public void setDeviceReqKey(String deviceReqKey) {
			this.deviceReqKey = deviceReqKey;
		}
	}

	public static class SignPadData {

	}

	@Component
	public class DeviceBox extends CacheBox<DeviceData> {

	}

	@Component
	public class CardBox extends CacheBox<CardData> {

	}

	@Component
	public class SignPadBox extends CacheBox<SignPadData> {

	}

}
