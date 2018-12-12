package com.amx.jax.dict;

public class UserClient {

	/**
	 * Business Channel
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum Channel {
		ONLINE, KIOSK, MOBILE, BRANCH, THIRD_PARTY;
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
		IOS, ANDROID, WINDOWS, MAC, LINUX, UNKNOWN
	}

	/**
	 * Client Type, tech or language
	 * 
	 * @author lalittanwar
	 *
	 */
	public enum AppType {
		WEB, ANDROID, IOS, UNKNOWN;
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
		ONLINE_WEB(DeviceType.COMPUTER), ONLINE_ANDROID(DeviceType.MOBILE), ONLINE_IOS(DeviceType.MOBILE);

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

}
