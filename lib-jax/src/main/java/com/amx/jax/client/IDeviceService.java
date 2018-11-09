package com.amx.jax.client;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.error.JaxError;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.DeviceStatusInfoDto;

public interface IDeviceService extends IJaxService {

	public static class Path {

		public static final String PREFIX = "/meta/device";

		public static final String DEVICE_ACTIVATE = "/activate";
		public static final String DEVICE_SEND_PAIR_OTP = "/sendpairotp";
		public static final String DEVICE_VALIDATE_PAIR_OTP = "/validatepairotp";
		public static final String DEVICE_STATE_UPDATE = "/updatedevicestate";
		public static final String DEVICE_STATUS_GET = "/getdevicestatus";
		public static final String DEVICE_STATE_REMITTANCE_UPDATE = "/updatestateremittance";
		public static final String DEVICE_STATE_CUSTOMER_REG_UPDATE = "/updatestatecustreg";
		public static final String DEVICE_STATE_FC_SALE_UPDATE = "/updatestatefcsale";
		public static final String DEVICE_STATE_SIGNATURE_UPDATE = "/updatesignature";
		public static final String DEVICE_FC_PURCHASE = "/fcpurchase";
		public static final String DEVICE_FC_SALE = "/fcsale";
		public static final String DEVICE_STATE = "/state";
		public static final String DEVICE_REG = "/register";
	}

	public static class Params {

		public static final String TERMINAL_ID = "countryBranchSystemInventoryId";
		public static final String EMPLOYEE_ID = "employeeId";
		public static final String DEVICE_TYPE = "deviceType";
		public static final String DEVICE_REG_ID = "deviceRegId";
		public static final String SESSION_TOKEN = "sessionToken";
		public static final String PAIRE_TOKEN = "paireToken";

	}

	@ApiJaxStatus({ JaxError.CLIENT_ALREADY_REGISTERED })
	AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request);

	@ApiJaxStatus(
		{ JaxError.JAX_FIELD_VALIDATION_FAILURE, JaxError.CLIENT_INVALID_PAIR_TOKEN,
				JaxError.CLIENT_INVALID_SESSION_TOKEN }
	)
	AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(
			Integer registrationId, String paireToken,
			String sessionToken
	);

	@ApiJaxStatus({ JaxError.CLIENT_INVALID_PAIR_TOKEN, JaxError.CLIENT_NOT_FOUND, JaxError.CLIENT_NOT_ACTIVE })
	AmxApiResponse<DevicePairOtpResponse, Object> sendOtpForPairing(Integer deviceRegId, String paireToken);

	@ApiJaxStatus(
		{ JaxError.JAX_FIELD_VALIDATION_FAILURE, JaxError.CLIENT_INVALID_PAIR_TOKEN,
				JaxError.CLIENT_INVALID_SESSION_TOKEN }
	)
	AmxApiResponse<BoolRespModel, Object> updateDeviceState(
			DeviceStateInfoChangeRequest request,
			Integer registrationId, String paireToken, String sessionToken
	);

	AmxApiResponse<BoolRespModel, Object> activateDevice(Integer countryBranchSystemInventoryId, ClientType deviceType);

	AmxApiResponse<BoolRespModel, Object> updateRemittanceState(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadRemittanceInfo signaturePadRemittanceInfo,
			BigDecimal employeeId
	);

	AmxApiResponse<BoolRespModel, Object> updateFcPurchase(
			ClientType deviceType, Integer countryBranchSystemInventoryId,
			SignaturePadFCPurchaseSaleInfo signaturePadPurchseInfo, BigDecimal employeeId
	);

	AmxApiResponse<BoolRespModel, Object> updateFcSale(
			ClientType deviceType, Integer countryBranchSystemInventoryId,
			SignaturePadFCPurchaseSaleInfo signaturePadSaleInfo, BigDecimal employeeId
	);

	AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadCustomerRegStateMetaInfo metaInfo, BigDecimal employeeId
	);

	AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(Integer deviceRegId, String imageUrl);

}
