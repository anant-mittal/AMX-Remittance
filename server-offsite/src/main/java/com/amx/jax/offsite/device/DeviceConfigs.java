package com.amx.jax.offsite.device;

import java.io.Serializable;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amx.jax.api.FileSubmitRequestModel;
import com.amx.jax.cache.CacheBox;
import com.amx.jax.constants.DeviceState;
import com.amx.jax.device.CardData;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

@Configuration
public class DeviceConfigs {

	public static class DeviceData implements Serializable {
		private static final long serialVersionUID = 2981932845270868040L;
		private String terminalId;
		private long updatestamp;

		public long getUpdatestamp() {
			return updatestamp;
		}

		public void setUpdatestamp(long updatestamp) {
			this.updatestamp = updatestamp;
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

	public static class TerminalData implements Serializable {
		private static final long serialVersionUID = 5690691652113911181L;
		String state;
		String status;
		long livestamp;
		long changestamp;
		long updatestamp;

		public long getUpdatestamp() {
			return updatestamp;
		}

		public void setUpdatestamp(long updatestamp) {
			this.updatestamp = updatestamp;
		}

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
			this.state = ArgUtil.parseAsString(state, Constants.BLANK).toUpperCase();
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = ArgUtil.parseAsString(status, Constants.BLANK).toUpperCase();
		}
	}

	public static class SignPadData implements Serializable {
		private static final long serialVersionUID = -6489044552822849830L;
		FileSubmitRequestModel signature;
		DeviceStatusInfoDto stateData;
		DeviceState deviceState;
		long updatestamp;

		public long getUpdatestamp() {
			return updatestamp;
		}

		public void setUpdatestamp(long updatestamp) {
			this.updatestamp = updatestamp;
		}

		public DeviceState getDeviceState() {
			return deviceState;
		}

		public void setDeviceState(DeviceState deviceState) {
			this.deviceState = deviceState;
		}

		public DeviceStatusInfoDto getStateData() {
			return stateData;
		}

		public void setStateData(DeviceStatusInfoDto stateData) {
			this.deviceState = stateData.getDeviceState();
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

		@Override
		public DeviceData getDefault() {
			return new DeviceData();
		}

		/**
		 * 
		 * This method is requried to called whenever there is change in status/state of
		 * device
		 * 
		 * @param deviceRegid
		 */
		public void updateStamp(Object deviceRegid) {
			String deviceRegidStr = ArgUtil.parseAsString(deviceRegid);
			DeviceData deviceData = this.getOrDefault(deviceRegidStr);
			deviceData.setUpdatestamp(System.currentTimeMillis());
			this.fastPut(deviceRegidStr, deviceData);
		}
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

		/**
		 * This method is requried to called whenever there is change in status/state of
		 * terminal
		 * 
		 * @param terminalId
		 */
		public void updateStamp(Object terminalId) {
			String terminalIdStr = ArgUtil.parseAsString(terminalId);
			TerminalData terminalData = this.getOrDefault(terminalIdStr);
			terminalData.setUpdatestamp(System.currentTimeMillis());
			this.fastPut(terminalIdStr, terminalData);
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
