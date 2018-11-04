package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.exception.AbstractJaxException;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.rest.RestService;

@Component
public class DeviceClient implements IDeviceService {

	private static final String END_POINT_JAX_DEVICE = "/meta/device";

	private static final Logger LOGGER = Logger.getLogger(DeviceClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request) {
		try {
			LOGGER.info("in registerNewDevice");

			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_REG;
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(request, getHeader());
			return restService.ajax(url).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<DeviceDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in registerNewDevice : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(Integer registrationId, String paireToken,
			String sessionToken) {
		try {
			LOGGER.info("in getStatus");

			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_STATUS_GET;
			return restService.ajax(url).header("registrationId", registrationId.toString())
					.header("paireToken", paireToken).header("sessionToken", sessionToken).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<DeviceStatusInfoDto, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getStatus : ", e);
			throw new JaxSystemError();
		}
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> sendOtpForPairing(Integer deviceRegId, String paireToken) {
		try {
			LOGGER.info("in sendOtpForPairing");

			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_SEND_PAIR_OTP;
			return restService.ajax(url).header("deviceRegId", deviceRegId.toString()).header("paireToken", paireToken)
					.get().as(new ParameterizedTypeReference<AmxApiResponse<DevicePairOtpResponse, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in sendOtpForPairing : ", e);
			throw new JaxSystemError();
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateDeviceState(DeviceStateInfoChangeRequest request,
			Integer registrationId, String paireToken, String sessionToken) {
		try {
			LOGGER.info("in updateDeviceState");

			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_STATUS_GET;
			return restService.ajax(url).header("deviceRegId", registrationId.toString())
					.header("paireToken", paireToken).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in updateDeviceState : ", e);
			throw new JaxSystemError();
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> activateDevice(Integer countryBranchSystemInventoryId,
			ClientType deviceType) {
		try {
			LOGGER.info("in activateDevice");

			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_ACTIVATE;
			return restService.ajax(url).queryParam("countryBranchSystemInventoryId", countryBranchSystemInventoryId)
					.queryParam("deviceType", deviceType).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in activateDevice : ", e);
			throw new JaxSystemError();
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadRemittanceInfo signaturePadRemittanceInfo) {
		try {
			LOGGER.info("in updateRemittanceState");

			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_STATE_REMITTANCE_UPDATE;
			return restService.ajax(url).queryParam("countryBranchSystemInventoryId", countryBranchSystemInventoryId)
					.queryParam("deviceType", deviceType).post(signaturePadRemittanceInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in updateRemittanceState : ", e);
			throw new JaxSystemError();
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcPurchase(ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadFCPurchaseSaleInfo signaturePadPurchseInfo) {
		try {
			LOGGER.info("in getFcPurchase");

			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_FC_PURCHASE;
			return restService.ajax(url).queryParam("countryBranchSystemInventoryId", countryBranchSystemInventoryId)
					.queryParam("deviceType", deviceType).post(signaturePadPurchseInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getFcPurchase : ", e);
			throw new JaxSystemError();
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcSale(ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadFCPurchaseSaleInfo signaturePadSaleInfo) {
		try {
			LOGGER.info("in getFcSale");
			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_FC_SALE;
			return restService.ajax(url).queryParam("countryBranchSystemInventoryId", countryBranchSystemInventoryId)
					.queryParam("deviceType", deviceType).post(signaturePadSaleInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in getFcSale : ", e);
			throw new JaxSystemError();
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadCustomerRegStateMetaInfo metaInfo) {
		try {
			LOGGER.info("in updateCustomerRegStateData");
			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_STATE_CUSTOMER_REG_UPDATE;
			return restService.ajax(url).queryParam("countryBranchSystemInventoryId", countryBranchSystemInventoryId)
					.queryParam("deviceType", deviceType).post(metaInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in updateCustomerRegStateData : ", e);
			throw new JaxSystemError();
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(Integer deviceRegId, String imageUrl) {
		// TODO Auto-generated method stub
		return null;
	}

}
