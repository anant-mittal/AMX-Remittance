package com.amx.jax.ui.session;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dict.UserClient.DeviceType;
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
public class UserDeviceBean extends UserDevice {

	private static final long serialVersionUID = -6869375666742059912L;

	private transient Logger logger = LoggerService.getLogger(getClass());

	@Autowired
	private transient CommonHttpRequest httpService;

	/**
	 * Resolve.
	 *
	 * @return the user device
	 */
	public UserDevice resolve() {

		UserDevice userDevice = httpService.getUserDevice();

		this.setAppType(userDevice.getAppType());
		this.setType(userDevice.getType());
		this.setPlatform(userDevice.getPlatform());
		this.setUserAgent(userDevice.getUserAgent());

		if (this.id == null) {
			String idn = ArgUtil.parseAsString(userDevice.getUserAgent().getId());
			this.id = httpService.setBrowserId(ArgUtil.parseAsString(idn));
		}

		return this;
	}

	public boolean isAuthorized() {
		if (this.id == null || this.fingerprint == null || httpService == null) {
			return true;
		}
		String ip = ArgUtil.parseAsString(httpService.getIPAddress(), Constants.BLANK);
		String fingerprint = ArgUtil.parseAsString(httpService.getDeviceId(), Constants.BLANK);
		UserAgent userAgent = httpService.getUserAgent();
		String id = ArgUtil.parseAsString(userAgent.getId(), Constants.BLANK);
		if (!id.equals(this.id)
				// || !fingerprint.equals(this.fingerprint)
				|| !ip.equals(this.ip) || !(this.getUserAgent() == null || this.getUserAgent().equals(userAgent))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getFingerprint()
	 */
	@Override
	public String getFingerprint() {
		if (type == null) {
			this.resolve();
		}
		return fingerprint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getId()
	 */
	@Override
	public String getId() {
		if (type == null) {
			this.resolve();
		}
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getAppVersion()
	 */
	@Override
	public String getAppVersion() {
		if (type == null) {
			this.resolve();
		}
		return appVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getIp()
	 */
	@Override
	public String getIp() {
		if (type == null) {
			this.resolve();
		}
		return ip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getType()
	 */
	@Override
	public DeviceType getType() {
		if (type == null) {
			this.resolve();
		}
		return type;
	}

	/**
	 * To map.
	 *
	 * @return the map
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", getId());
		map.put("fingerprint", fingerprint);
		map.put("platform", platform);
		map.put("type", type);
		map.put("agent", this.getUserAgent());
		map.put("ip", ip);
		map.put("appVersion", appVersion);
		map.put("appType", appType);
		return map;
	}

	/**
	 * To user device.
	 *
	 * @return the user device
	 */
	public UserDevice toUserDevice() {
		UserDeviceBean device = new UserDeviceBean();
		device.setId(getId());
		device.setFingerprint(getFingerprint());
		device.setPlatform(getPlatform());
		device.setType(getType());
		device.setIp(getIp());
		device.setAppVersion(getAppVersion());
		device.setAppType(getAppType());
		device.setUserAgent(getUserAgent());
		return device;
	}

}
