package com.amx.jax.manager;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constants.DeviceState;
import com.amx.jax.exception.UpdateDeviceStatusRequest;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DeviceStateManager {

	public void updateDeviceStatus(UpdateDeviceStatusRequest request) {

	}
}
