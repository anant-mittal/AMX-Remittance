package com.amx.jax.signpad;

import java.io.Serializable;

import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

public class TerminalData implements Serializable {
	private static final long serialVersionUID = 5690691652113911181L;
	String state;
	String status;
	long livestamp;
	long changestamp;
	long updatestamp;
	long pagestamp;
	long startStamp;
	String terminalId;

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public long getStartStamp() {
		return startStamp;
	}

	public void setStartStamp(long startStamp) {
		this.startStamp = startStamp;
	}

	public long getPagestamp() {
		return pagestamp;
	}

	public void setPagestamp(long pagestamp) {
		this.pagestamp = pagestamp;
	}

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