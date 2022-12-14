package com.amx.jax.signpad;

import java.io.Serializable;

import com.amx.jax.api.FileSubmitRequestModel;
import com.amx.jax.constant.DeviceState;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.model.Indexable;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignPadData implements Serializable, Indexable {
	private static final long serialVersionUID = -6489044552822849830L;
	FileSubmitRequestModel signature;
	DeviceStatusInfoDto stateData;
	DeviceState deviceState;
	long updatestamp;
	long changeStamp;
	private String terminalId;
	private String regId;

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

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
		this.changeStamp = System.currentTimeMillis();
	}

	public DeviceStatusInfoDto getStateData() {
		return stateData;
	}

	public void setStateData(DeviceStatusInfoDto stateData) {
		this.stateData = stateData;
		this.changeStamp = System.currentTimeMillis();
	}

	public FileSubmitRequestModel getSignature() {
		return signature;
	}

	public void setSignature(FileSubmitRequestModel signature) {
		this.signature = signature;
		this.changeStamp = System.currentTimeMillis();
	}

	/**
	 * Timestamp represents when last change was made to any data in SignPad
	 * 
	 * @return
	 */
	public long getChangeStamp() {
		return changeStamp;
	}

	public void setChangeStamp(long changeStamp) {
		this.changeStamp = changeStamp;
	}

	@Override
	public String id() {
		return ArgUtil.parseAsString(this.terminalId);
	}
}