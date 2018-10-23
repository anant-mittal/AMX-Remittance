package com.amx.jax.client;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DeviceStatusInfoDto;

public interface IDeviceService extends IJaxService {

	public static String DEVICE_REG = "/register";
	public static String DEVICE_STATE = "/state";
	public static final String DEVICE_ACTIVATE = "/activate";
	public static final String DEVICE_SEND_PAIR_OTP = "/sendpairotp";
	public static final String DEVICE_VALIDATE_PAIR_OTP = "/validatepairotp";
	public static final String DEVICE_STATE_UPDATE = "/updatedevicestate";
	public static final String DEVICE_STATUS_GET = "/getdevicestatus";

	public static final String DEVICE_STATE_REMITTANCE_UPDATE = "/updatestateremittance";
	public static final String DEVICE_STATE_CUSTOMER_REG_UPDATE = "/updatestatecustreg";
	public static final String DEVICE_STATE_FC_SALE_UPDATE = "/updatestatefcsale";

	AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request);

	AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(Integer registrationId, String paireToken,
			String sessionToken);

}
