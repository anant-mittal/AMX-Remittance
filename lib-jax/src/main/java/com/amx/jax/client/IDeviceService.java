package com.amx.jax.client;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;

public interface IDeviceService extends IJaxService {

	public static String REG_DEVICE = "/register";

	AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request);

}
