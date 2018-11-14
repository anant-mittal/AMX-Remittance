package com.amx.jax.offsite.device;

import java.io.Serializable;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amx.jax.api.FileSubmitRequestModel;
import com.amx.jax.cache.CacheBox;
import com.amx.jax.device.CardData;
import com.amx.jax.model.response.DeviceStatusInfoDto;

@Configuration
public class DeviceConfigs {

	public static class DeviceData implements Serializable {
		private static final long serialVersionUID = 2981932845270868040L;
		private String terminalId;

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

	public static class TerminalData {
		String state;
		String status;
		long livestamp;
		long changestamp;

		public long getChangestamp() {
			return changestamp;
		}

		public void setChangestamp(long changestamp) {
			this.changestamp = changestamp;
		}

		public long getLivestamp() {
			return livestamp;
		}

		public void setLivestamp(long livestamp) {
			this.livestamp = livestamp;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}

	public static class SignPadData {
		FileSubmitRequestModel signature;
		DeviceStatusInfoDto stateData;

		public DeviceStatusInfoDto getStateData() {
			return stateData;
		}

		public void setStateData(DeviceStatusInfoDto stateData) {
			this.stateData = stateData;
		}

		public FileSubmitRequestModel getSignature() {
			return signature;
		}

		public void setSignature(FileSubmitRequestModel signature) {
			this.signature = signature;
		}
	}

	@Component
	public class DeviceBox extends CacheBox<DeviceData> {

	}

	@Component
	public class CardBox extends CacheBox<CardData> {

	}

	@Component
	public class TerminalBox extends CacheBox<TerminalData> {
		@Override
		public TerminalData getDefault() {
			return new TerminalData();
		}
	}

	@Component
	public class SignPadBox extends CacheBox<SignPadData> {

		@Override
		public SignPadData getDefault() {
			return new SignPadData();
		}

	}

}
