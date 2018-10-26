package com.amx.jax.offsite.device;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.amx.jax.device.DeviceConstants;
import com.amx.jax.swagger.ApiMockParam;
import com.amx.jax.swagger.ApiMockParams;
import com.amx.jax.swagger.MockParamBuilder.MockParamType;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ApiMockParams({
		@ApiMockParam(name = DeviceConstants.Keys.DEVICE_REG_KEY_XKEY, value = "Device Registration Id", paramType = MockParamType.HEADER),
		@ApiMockParam(name = DeviceConstants.Keys.DEVICE_REG_TOKEN_XKEY, value = "Device Registration Token", paramType = MockParamType.HEADER),
		@ApiMockParam(name = DeviceConstants.Keys.DEVICE_SESSION_TOKEN_XKEY, value = "Device Session Token", paramType = MockParamType.HEADER),
		@ApiMockParam(name = DeviceConstants.Keys.DEVICE_REQ_TOKEN_XKEY, value = "Device Request Token", paramType = MockParamType.HEADER) })
public @interface ApiDeviceHeaders {

}
