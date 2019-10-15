package com.amx.jax.sso;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.utils.ArgUtil;

@Component
public class SSOUserSessions extends CacheBox<Long> {

	public SSOUserSessions() {
		super("SSOUserSessions");
	}

	@Autowired
	SSOUser sSOUser;

	/**
	 * To make sure user is not loggedin from other system
	 * 
	 * @return
	 */
	public boolean isUserValidUnique() {
		String userid = ArgUtil.parseAsString(sSOUser.getUserId());
		if (ArgUtil.isEmpty(userid)) {
			return true;
		}
		String userKey = "U#" + userid;
		Long latestUserLoginTime = this.getOrDefault(userKey, 0L);
		Long thisUserLoginTime = sSOUser.getLoginTime();
		if (latestUserLoginTime == 0L) {
			this.put(userKey, thisUserLoginTime);
			return true;
		}
		if (thisUserLoginTime >= latestUserLoginTime) {
			this.put(userKey, thisUserLoginTime);
			return true;
		}
		return false;
	}

	public boolean isTerminalValidUnique() {
		String terminalId = ArgUtil.parseAsString(sSOUser.getTerminalId());
		if (ArgUtil.isEmpty(terminalId)) {
			return true;
		}
		String terminalKey = "T#" + terminalId;
		Long latestTerminalLoginTime = this.getOrDefault(terminalKey, 0L);
		Long thisTerminalLoginTime = sSOUser.getLoginTime();
		if (latestTerminalLoginTime == 0L) {
			this.put(terminalKey, thisTerminalLoginTime);
			return true;
		}
		if (thisTerminalLoginTime >= latestTerminalLoginTime) {
			this.put(terminalKey, thisTerminalLoginTime);
			return true;
		}
		return false;
	}

	public boolean isUserToBeThrownOut() {
		return !(isUserValidUnique() && isTerminalValidUnique());
	}

	public void invalidateTerminal(String terminalId) {
		if (!ArgUtil.isEmpty(terminalId)) {
			String terminalKey = "T#" + terminalId;
			this.put(terminalKey, System.currentTimeMillis());
		}
	}

}
