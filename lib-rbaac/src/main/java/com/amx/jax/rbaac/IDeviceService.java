package com.amx.jax.rbaac;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.error.RbaacApiStatusBuilder.RbaacApiStatus;
import com.amx.jax.rbaac.error.RbaacServiceError;

public interface IDeviceService {

	public static class Path {

		public static final String PREFIX = "/meta/device";
		private static final String PUBG = "";

		public static final String DEVICE_ACTIVATE = PREFIX + "/activate";
		public static final String DEVICE_DEACTIVATE = PREFIX + "/deactivate";
		public static final String DEVICE_SEND_PAIR_OTP = PREFIX + "/sendpairotp";
		public static final String DEVICE_VALIDATE_PAIR_OTP = PUBG + PREFIX + "/validatepairotp";
		public static final String DEVICE_VALIDATE_DEVICE_TOKEN = PUBG + PREFIX + "/validatedevicetoken";
		public static final String DEVICE_VALIDATE_SESSION_TOKEN = PUBG + PREFIX + "/validatesessiontoken";
		public static final String DEVICE_STATE_UPDATE = PREFIX + "/updatedevicestate";
		public static final String DEVICE_STATUS_GET = PREFIX + "/getdevicestatus";
		public static final String DEVICE_REG = PREFIX + "/register";
		public static final String DEVICE_GET_DEVICE_REG_ID = PUBG + PREFIX + "/getdeviceregid";
	}

	public static class Params {

		public static final String TERMINAL_ID = "countryBranchSystemInventoryId";
		public static final String EMPLOYEE_ID = "employeeId";
		public static final String DEVICE_TYPE = "deviceType";
		public static final String DEVICE_REG_ID = "deviceRegId";
		public static final String SESSION_TOKEN = "sessionToken";
		public static final String PAIRE_TOKEN = "paireToken";
		public static final String DEVICE_CLIENT_TYPE = "deviceClientType";
		public static final String DEVICE_SYS_INV_ID = "countryBranchSystemInventoryId";
		public static final String OTP = "otp";
		public static final String MOTP = "mOtp";

	}

	@RbaacApiStatus({ RbaacServiceError.CLIENT_ALREADY_REGISTERED })
	AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request);

	@RbaacApiStatus({ RbaacServiceError.CLIENT_NOT_FOUND })
	AmxApiResponse<BoolRespModel, Object> activateDevice(Integer deviceRegId, String mOtp);

	@RbaacApiStatus(RbaacServiceError.CLIENT_NOT_FOUND)
	AmxApiResponse<BoolRespModel, Object> deactivateDevice(Integer deviceRegId);

	AmxApiResponse<DevicePairOtpResponse, Object> sendOtpForPairing(Integer deviceRegId, String paireToken);

	AmxApiResponse<DevicePairOtpResponse, BoolRespModel> validateOtpForPairing(ClientType deviceType,
			Integer countryBranchSystemInventoryId, String otp);

	AmxApiResponse<BoolRespModel, Object> validateDeviceToken(BigDecimal deviceRegId,
			String devicePairToken);

	AmxApiResponse<BoolRespModel, Object> validateDeviceSessionToken(BigDecimal deviceRegId, String deviceSessionToken);

	AmxApiResponse<BigDecimal, Object> getDeviceRegIdByBranchInventoryId(ClientType deviceClientType,
			BigDecimal countryBranchSystemInventoryId);

}
