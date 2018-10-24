package com.amx.jax.client;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.JaxError;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
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

	@ApiJaxStatus({ JaxError.DEVICE_ALREADY_REGISTERED })
	AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request);

	@ApiJaxStatus({ JaxError.JAX_FIELD_VALIDATION_FAILURE, JaxError.DEVICE_INVALID_PAIR_TOKEN,
			JaxError.DEVICE_INVALID_SESSION_TOKEN })
	AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(Integer registrationId, String paireToken,
			String sessionToken);

	@ApiJaxStatus({ JaxError.DEVICE_INVALID_PAIR_TOKEN, JaxError.DEVICE_NOT_FOUND, JaxError.DEVICE_NOT_ACTIVE })
	AmxApiResponse<DevicePairOtpResponse, Object> sendOtpForPairing(Integer deviceRegId, String paireToken);

}
