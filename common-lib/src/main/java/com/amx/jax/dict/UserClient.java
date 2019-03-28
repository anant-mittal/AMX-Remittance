package com.amx.jax.dict;

import java.io.Serializable;

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
		ONLINE, KIOSK, MOBILE, BRANCH, THIRD_PARTY, SYSTEM, UNKNOWN;
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
	}

	public enum ClientType {
		// Auth Apps
		NOTP_APP(DeviceType.MOBILE),

		// branch cleints
		BRANCH_WEB_OLD(DeviceType.COMPUTER), BRANCH_WEB(DeviceType.COMPUTER), SIGNATURE_PAD(DeviceType.TABLET),
		BRANCH_ADAPTER(DeviceType.COMPUTER),

		// Other Channels
		OFFSITE_PAD(DeviceType.TABLET), KIOSK(DeviceType.COMPUTER), DELIVERY_APP(DeviceType.MOBILE),

		// Customer Facing interfaces
		ONLINE_WEB(DeviceType.COMPUTER), ONLINE_AND(DeviceType.MOBILE), ONLINE_IOS(DeviceType.MOBILE),

		// Unknown
		SYSTEM, UNKNOWN;

		DeviceType deviceType;

		ClientType(DeviceType deviceType) {
			this.deviceType = deviceType;
		}

		ClientType() {
			this.deviceType = DeviceType.COMPUTER;
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
			return this;
		}
	}

}
