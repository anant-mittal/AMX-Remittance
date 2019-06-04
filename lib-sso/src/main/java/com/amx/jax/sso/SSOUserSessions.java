package com.amx.jax.sso;

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
	public boolean isValidUnique() {
		String userid = ArgUtil.parseAsString(sSOUser.getUserId());
		if (ArgUtil.isEmpty(userid)) {
			return true;
		}
		Long latestUserLoginTime = this.getOrDefault(userid, 0L);
		Long thisUserLoginTime = sSOUser.getLoginTime();
		if (latestUserLoginTime == 0L) {
			this.put(userid, thisUserLoginTime);
			return true;
		}
		if (thisUserLoginTime >= latestUserLoginTime) {
			this.put(userid, thisUserLoginTime);
			return true;
		}
		return false;
	}

}
