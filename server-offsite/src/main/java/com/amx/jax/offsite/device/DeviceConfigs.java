package com.amx.jax.offsite.device;

import java.io.Serializable;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.jax.device.CardData;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.swagger.MockParamBuilder;
import com.amx.jax.swagger.MockParamBuilder.MockParam;

@Configuration
public class DeviceConfigs {

	public static class DeviceData implements Serializable {
		private static final long serialVersionUID = 2981932845270868040L;
		private String terminalId;

		public String getTerminalId() {
			return terminalId;
		}

		public void setTerminalId(String terminalId) {
			this.terminalId = terminalId;
		}

		private String deviceReqKey;
		private String sessionPairingTokenX;

		public String getSessionPairingTokenX() {
			return sessionPairingTokenX;
		}

		public void setSessionPairingTokenX(String sessionPairingTokenX) {
			this.sessionPairingTokenX = sessionPairingTokenX;
		}

		public String getDeviceReqKey() {
			return deviceReqKey;
		}

		public void setDeviceReqKey(String deviceReqKey) {
			this.deviceReqKey = deviceReqKey;
		}

	}

	@Component
	public class DeviceBox extends CacheBox<DeviceData> {

	}

	@Component
	public class CardBox extends CacheBox<CardData> {

	}

	// @Bean
	public MockParam deviceRegKey() {
		return new MockParamBuilder().name(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY).description("Device Reg Key")
				.parameterType(MockParamBuilder.MockParamType.HEADER).required(false).build();

	}

	// @Bean
	public MockParam deviceRegToken() {
		return new MockParamBuilder().name(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY).description("Device Reg Token")
				.parameterType(MockParamBuilder.MockParamType.HEADER).required(false).build();

	}
}
