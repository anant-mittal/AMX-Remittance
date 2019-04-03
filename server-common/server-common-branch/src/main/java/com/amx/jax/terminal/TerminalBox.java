package com.amx.jax.terminal;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.jax.signpad.TerminalData;
import com.amx.utils.ArgUtil;

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
		if (!ArgUtil.isEmpty(terminalId)) {
			String terminalIdStr = ArgUtil.parseAsString(terminalId);
			TerminalData terminalData = this.getOrDefault(terminalIdStr);
			terminalData.setUpdatestamp(System.currentTimeMillis());
			this.fastPut(terminalIdStr, terminalData);
		}
	}

	public Object version() {
		return 1;
	}
}