package com.amx.jax.client;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DeviceStatusInfoDto;

public interface IDeviceStateService extends IJaxService {

	public static class Path {

		public static final String PREFIX = "/meta/device";

		public static final String DEVICE_STATUS_GET = PREFIX + "/getdevicestatus";
		public static final String DEVICE_STATE_SIGNATURE_UPDATE = PREFIX + "/updatesignature";

		public static final String DEVICE_STATE_REMITTANCE_UPDATE = PREFIX + "/updatestateremittance";
		public static final String DEVICE_STATE_CUSTOMER_REG_UPDATE = PREFIX + "/updatestatecustreg";
		public static final String DEVICE_FC_SALE_UPDATE = PREFIX + "/fcsale";
		public static final String DEVICE_FC_PURCHASE_UPDATE = PREFIX + "/fcpurchase";
		public static final String DEVICE_STATE_CLEAR = PREFIX + "/clearstate";

	}

	public static class Params {

		public static final String TERMINAL_ID = "countryBranchSystemInventoryId";
		public static final String EMPLOYEE_ID = "employeeId";
		public static final String DEVICE_TYPE = "deviceType";
		public static final String DEVICE_REG_ID = "deviceRegId";
		public static final String SESSION_TOKEN = "sessionToken";
		public static final String PAIRE_TOKEN = "paireToken";

	}

	@ApiJaxStatus({ JaxError.JAX_FIELD_VALIDATION_FAILURE, JaxError.CLIENT_INVALID_PAIR_TOKEN,
			JaxError.CLIENT_INVALID_SESSION_TOKEN })
	AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(
			Integer registrationId, String paireToken,
			String sessionToken);

	@ApiJaxStatus({ JaxError.CLIENT_NOT_FOUND, JaxError.CLIENT_NOT_ACTIVE, JaxError.CLIENT_NOT_LOGGGED_IN,
			JaxError.JAX_FIELD_VALIDATION_FAILURE })
	AmxApiResponse<BoolRespModel, Object> updateRemittanceState(ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadRemittanceInfo signaturePadRemittanceInfo,
			BigDecimal employeeId);

	@ApiJaxStatus({ JaxError.CLIENT_NOT_FOUND, JaxError.CLIENT_NOT_ACTIVE, JaxError.CLIENT_NOT_LOGGGED_IN })
	AmxApiResponse<BoolRespModel, Object> updateFcPurchase(
			ClientType deviceType, Integer countryBranchSystemInventoryId,
			SignaturePadFCPurchaseSaleInfo signaturePadPurchseInfo, BigDecimal employeeId);

	@ApiJaxStatus({ JaxError.CLIENT_NOT_FOUND, JaxError.CLIENT_NOT_ACTIVE, JaxError.CLIENT_NOT_LOGGGED_IN })
	AmxApiResponse<BoolRespModel, Object> updateFcSale(ClientType deviceType, Integer countryBranchSystemInventoryId,
			SignaturePadFCPurchaseSaleInfo signaturePadSaleInfo, BigDecimal employeeId);

	@ApiJaxStatus({ JaxError.CLIENT_NOT_LOGGGED_IN, JaxError.CLIENT_NOT_FOUND, JaxError.CLIENT_NOT_ACTIVE })
	AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadCustomerRegStateMetaInfo metaInfo,
			BigDecimal employeeId);

	AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(Integer deviceRegId, String imageUrl);

	@ApiJaxStatus({ JaxError.CLIENT_NOT_FOUND, JaxError.CLIENT_NOT_ACTIVE })
	AmxApiResponse<BoolRespModel, Object> clearDeviceState(Integer registrationId, String paireToken,
			String sessionToken);
}
