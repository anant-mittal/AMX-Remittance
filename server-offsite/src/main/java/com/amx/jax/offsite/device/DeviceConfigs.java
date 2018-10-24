package com.amx.jax.offsite.device;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConstants;
import com.amx.jax.cache.CacheBox;
import com.amx.jax.def.MockParamBuilder;
import com.amx.jax.def.MockParamBuilder.MockParam;
import com.amx.jax.device.CardData;

@Configuration
public class DeviceConfigs {

	public static class DeviceData {

	}

	@Component
	public class DeviceBox extends CacheBox<DeviceData> {

	}

	@Component
	public class CardBox extends CacheBox<CardData> {

	}

	@Bean
	public MockParam deviceRegKey() {
		return new MockParamBuilder().name(AppConstants.DEVICE_REG_KEY_XKEY).description("Device Reg Key")
				.defaultValue("1234").parameterType(MockParamBuilder.MockParamType.HEADER).required(false).build();

	}

	@Bean
	public MockParam deviceRegToken() {
		return new MockParamBuilder().name(AppConstants.DEVICE_REG_TOKEN_XKEY).description("Device Reg Token")
				.defaultValue("djhk33434nkjj34").parameterType(MockParamBuilder.MockParamType.HEADER).required(false)
				.build();

	}
}
