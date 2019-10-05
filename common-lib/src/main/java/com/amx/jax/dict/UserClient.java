package com.amx.jax.dict;

import java.io.Serializable;

import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserClient {

	/**
	 * Business Channel
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum Channel {
		ONLINE, KIOSK, MOBILE, BRANCH, THIRD_PARTY, SYSTEM, UNKNOWN, OFFSITE;
	}

	/**
	 * Physical Device type
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum DeviceType {
		COMPUTER, MOBILE, TABLET(MOBILE), IPAD(MOBILE), UNKNOWN(COMPUTER);

		DeviceType parent;

		DeviceType() {
			this.parent = this;
		}

		DeviceType(DeviceType parent) {
			this.parent = parent.getParent();
		}

		public DeviceType getParent() {
			return parent;
		}

		public boolean isParentOf(DeviceType check) {
			return (this == check || check.getParent() == this);
		}

		public boolean hasParent(DeviceType check) {
			return check.isParentOf(this);
		}

		public boolean isRelated(DeviceType check) {
			return check.isParentOf(this.parent);
		}

		public boolean isMobile() {
			return this.hasParent(MOBILE);
		}
	}

	/**
	 * More of generic OS
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum DevicePlatform {
		IOS, ANDROID, WINDOWS, MAC, LINUX, UNKNOWN;
	}

	/**
	 * Client Type, tech or language
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum AppType {
		WEB, IOS(Channel.MOBILE), ANDROID(Channel.MOBILE), UNKNOWN;

		Channel channel;

		AppType(Channel channel) {
			this.channel = channel;
		}

		AppType() {
			this(Channel.UNKNOWN);
		}

		public Channel getChannel() {
			return channel;
		}

		public boolean isMobile() {
			return this.channel == Channel.MOBILE;
		}
	}

	public enum AuthSystem {
		TERMINAL, DEVICE;

		public static AuthSystem byDeviceType(DeviceType deviceType) {
			if (DeviceType.COMPUTER.isParentOf(deviceType)) {
				return TERMINAL;
			} else if (DeviceType.MOBILE.isParentOf(deviceType)) {
				return DEVICE;
			}
			return null;
		}
	}

	public enum ClientType {
		// Auth Apps
		NOTP_APP(DeviceType.MOBILE, Channel.MOBILE),

		// branch cleints
		BRANCH_WEB_OLD(DeviceType.COMPUTER, Channel.BRANCH), BRANCH_WEB(DeviceType.COMPUTER, Channel.BRANCH),
		SIGNATURE_PAD(DeviceType.TABLET,
				Channel.BRANCH),
		BRANCH_ADAPTER(DeviceType.COMPUTER, Channel.BRANCH),

		// Other Channels
		OFFSITE_PAD(DeviceType.TABLET, Channel.BRANCH), KIOSK(DeviceType.COMPUTER, Channel.KIOSK),
		DELIVERY_APP(DeviceType.MOBILE, Channel.BRANCH),
		OFFSITE_WEB(DeviceType.COMPUTER, Channel.OFFSITE, AuthSystem.DEVICE),

		// Customer Facing interfaces
		ONLINE_WEB(DeviceType.COMPUTER, Channel.ONLINE), ONLINE_AND(DeviceType.MOBILE, Channel.MOBILE),
		ONLINE_IOS(DeviceType.MOBILE, Channel.MOBILE),

		// Unknown
		SYSTEM, UNKNOWN;

		DeviceType deviceType;

		Channel channel = Channel.ONLINE;
		AuthSystem authSystem = AuthSystem.TERMINAL;

		ClientType(DeviceType deviceType, Channel channel, AuthSystem authSystem) {
			this.deviceType = deviceType;
			this.channel = channel;
			this.authSystem = authSystem;
			if (ArgUtil.isEmpty(this.authSystem)) {
				this.authSystem = AuthSystem.byDeviceType(this.deviceType);
			}
		}

		ClientType(DeviceType deviceType, Channel channel) {
			this(deviceType, channel, null);
		}

		ClientType(DeviceType deviceType) {
			this(deviceType, null, null);
		}

		ClientType() {
			this(DeviceType.COMPUTER, null, null);
		}

		public AuthSystem getAuthSystem() {
			return authSystem;
		}

		/**
		 * Allowed Device for This client
		 * 
		 * @return
		 */
		public DeviceType getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(DeviceType deviceType) {
			this.deviceType = deviceType;
		}

		public Channel getChannel() {
			return channel;
		}

		public void setChannel(Channel channel) {
			this.channel = channel;
		}

	}

	@JsonInclude(Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserDeviceClient implements Serializable {

		private static final long serialVersionUID = -3209167233695023422L;

		private String ip;

		@JsonProperty("fp")
		private String fingerprint;

		@JsonProperty("cn")
		private Channel channel;

		@JsonProperty("dt")
		private DeviceType deviceType;

		@JsonProperty("at")
		private AppType appType;

		@JsonProperty("ct")
		private ClientType clientType;

		@JsonProperty("cv")
		private String clientVersion;

		@JsonProperty("lang")
		private Language lang;

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getFingerprint() {
			return fingerprint;
		}

		public void setFingerprint(String fingerprint) {
			this.fingerprint = fingerprint;
		}

		public Channel getChannel() {
			return channel;
		}

		public void setChannel(Channel channel) {
			this.channel = channel;
		}

		public DeviceType getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(DeviceType deviceType) {
			this.deviceType = deviceType;
		}

		public AppType getAppType() {
			return appType;
		}

		public void setAppType(AppType appType) {
			this.appType = appType;
		}

		public ClientType getClientType() {
			return clientType;
		}

		public void setClientType(ClientType clientType) {
			this.clientType = clientType;
		}

		public UserDeviceClient importFrom(UserDeviceClient userDevice) {
			this.setDeviceType(userDevice.getDeviceType());
			this.setAppType(userDevice.getAppType());
			this.setIp(userDevice.getIp());
			this.setFingerprint(userDevice.getFingerprint());
			this.setClientType(userDevice.getClientType());
			this.setClientVersion(userDevice.getClientVersion());
			return this;
		}

		public String getClientVersion() {
			return clientVersion;
		}

		public void setClientVersion(String clientVersion) {
			this.clientVersion = clientVersion;
		}

		public Language getLang() {
			return lang;
		}

		public void setLang(Language lang) {
			this.lang = lang;
		}
	}

	public static boolean isAuthSystem(ClientType clientType, AuthSystem authSystem) {
		if (ArgUtil.isEmpty(clientType) || ArgUtil.isEmpty(authSystem)) {
			return false;
		}
		if (ArgUtil.is(clientType.getAuthSystem())) {
			return clientType.getAuthSystem().equals(authSystem);
		} else {
			return authSystem.equals(AuthSystem.byDeviceType(clientType.getDeviceType()));
		}
	}

}
