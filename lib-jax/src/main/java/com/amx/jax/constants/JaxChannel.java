package com.amx.jax.constants;

import com.amx.jax.dict.UserClient;
import com.amx.jax.dict.UserClient.Channel;

public enum JaxChannel {
	ONLINE {
		@Override
		public Channel getClientChannel() {
			return UserClient.Channel.ONLINE;
		}
	},
	MOBILE {
		@Override
		public Channel getClientChannel() {
			return UserClient.Channel.MOBILE;
		}
	},
	KIOSK {
		@Override
		public Channel getClientChannel() {
			return UserClient.Channel.KIOSK;
		}
	},
	BRANCH {
		@Override
		public Channel getClientChannel() {
			return UserClient.Channel.BRANCH;
		}
	},
	SYSTEM {
		@Override
		public Channel getClientChannel() {
			return UserClient.Channel.SYSTEM;
		}
	};

	public abstract UserClient.Channel getClientChannel();
}
