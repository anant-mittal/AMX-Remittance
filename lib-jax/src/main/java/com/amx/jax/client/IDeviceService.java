package com.amx.jax.client;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;

public interface IDeviceService extends IJaxService {

	public static String DEVICE_REG = "/register";
	public static String DEVICE_STATE = "/state";
	public static final String DEVICE_ACTIVATE = "/activate";
	public static final String DEVICE_SEND_PAIR_OTP = "/sendpairotp";
	public static final String DEVICE_VALIDATE_PAIR_OTP = "/validatepairotp";
	public static final String DEVICE_STATUS_UPDATE = "/updatedevicestatus";
	public static final String DEVICE_STATUS_GET = "/getdevicestatus";

	AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request);

}
