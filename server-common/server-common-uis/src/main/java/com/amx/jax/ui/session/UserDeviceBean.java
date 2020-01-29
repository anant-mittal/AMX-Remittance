package com.amx.jax.ui.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.UserDevice;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * The Class UserDeviceBean.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDeviceBean implements Serializable {

	private static final long serialVersionUID = -6869375666742059912L;

	private transient Logger logger = LoggerService.getLogger(getClass());

	@Autowired
	private transient CommonHttpRequest httpService;

	private UserDevice userDevice;

	/**
	 * Resolve.
	 *
	 * @return the user device
	 */
	public UserDevice resolve() {

		if (userDevice == null) {
			userDevice = httpService.getUserDevice();
		}
		if (this.userDevice.getId() == null) {
			String idn = ArgUtil.parseAsString(userDevice.getUserAgent().getId());
			this.userDevice.setId(httpService.setBrowserId(ArgUtil.parseAsString(idn)));
		}

		return userDevice;
	}

	public boolean isAuthorized() {
		if (this.userDevice == null || this.userDevice.getId() == null || this.userDevice.getFingerprint() == null
				|| httpService == null) {
			return true;
		}
		String ip = ArgUtil.parseAsString(httpService.getIPAddress(), Constants.BLANK);
		String fingerprint = ArgUtil.parseAsString(httpService.getDeviceId(), Constants.BLANK);
		UserAgent userAgent = httpService.getUserAgent();
		String id = ArgUtil.parseAsString(userAgent.getId(), Constants.BLANK);
		if (!id.equals(userDevice.getId())
				// || !fingerprint.equals(this.fingerprint)
				|| !ip.equals(userDevice.getIp())
				|| !(userDevice.getUserAgent() == null || userDevice.getUserAgent().equals(userAgent))) {
			return false;
		}
		return true;
	}

	/**
	 * To map.
	 *
	 * @return the map
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", userDevice.getId());
		map.put("fingerprint", userDevice.getFingerprint());
		map.put("platform", userDevice.getPlatform());
		map.put("type", userDevice.getType());
		map.put("agent", userDevice.getUserAgent());
		map.put("ip", userDevice.getIp());
		map.put("appVersion", userDevice.getAppVersion());
		map.put("appType", userDevice.getAppType());
		return map;
	}

	/**
	 * To user device.
	 *
	 * @return the user device
	 */
	public UserDevice getUserDevice() {
		if (userDevice == null) {
			this.resolve();
		}
		return userDevice;
	}

	public UserAgent getUserAgent() {
		if (userDevice == null) {
			this.resolve();
		}
		if (userDevice == null) {
			return null;
		}
		return userDevice.getUserAgent();
	}
}
