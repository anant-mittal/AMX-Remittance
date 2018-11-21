package com.amx.jax.client;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.logger.LoggerService;
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

	private static final Logger LOGGER = LoggerService.getLogger(DeviceClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request) {
		try {
			LOGGER.debug("in registerNewDevice");
			String url = appConfig.getJaxURL() + Path.DEVICE_REG;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<DeviceDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in registerNewDevice : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(
			Integer registrationId, String paireToken,
			String sessionToken) {
		try {
			LOGGER.debug("in getStatus");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATUS_GET;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.DEVICE_REG_ID, registrationId.toString())
					.queryParam(Params.PAIRE_TOKEN, paireToken).queryParam(Params.SESSION_TOKEN, sessionToken).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<DeviceStatusInfoDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getStatus : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> sendOtpForPairing(Integer deviceRegId, String paireToken) {
		try {
			LOGGER.debug("in sendOtpForPairing");
			String url = appConfig.getJaxURL() + Path.DEVICE_SEND_PAIR_OTP;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.DEVICE_REG_ID, deviceRegId.toString())
					.queryParam(Params.PAIRE_TOKEN, paireToken)
					.get().as(new ParameterizedTypeReference<AmxApiResponse<DevicePairOtpResponse, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in sendOtpForPairing : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateDeviceState(
			DeviceStateInfoChangeRequest request,
			Integer registrationId, String paireToken, String sessionToken) {
		try {
			LOGGER.debug("in updateDeviceState");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATUS_GET;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.DEVICE_REG_ID, registrationId.toString())
					.queryParam(Params.PAIRE_TOKEN, paireToken).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in updateDeviceState : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> activateDevice(
			Integer deviceRegId,
			String mOtp) {
		try {
			LOGGER.debug("in activateDevice");
			String url = appConfig.getJaxURL() + Path.DEVICE_ACTIVATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.field(Params.MOTP, mOtp)
					.field(Params.DEVICE_REG_ID, deviceRegId).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in activateDevice : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadRemittanceInfo signaturePadRemittanceInfo,
			BigDecimal employeeId) {
		try {
			LOGGER.debug("in updateRemittanceState");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATE_REMITTANCE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.TERMINAL_ID, countryBranchSystemInventoryId)
					.queryParam(Params.DEVICE_TYPE, deviceType).queryParam(Params.EMPLOYEE_ID, employeeId)
					.post(signaturePadRemittanceInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in updateRemittanceState : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcPurchase(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadFCPurchaseSaleInfo signaturePadPurchseInfo,
			BigDecimal employeeId) {
		try {
			LOGGER.debug("in getFcPurchase");
			String url = appConfig.getJaxURL() + Path.DEVICE_FC_PURCHASE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.TERMINAL_ID, countryBranchSystemInventoryId)
					.queryParam(Params.DEVICE_TYPE, deviceType).queryParam(Params.EMPLOYEE_ID, employeeId)
					.post(signaturePadPurchseInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getFcPurchase : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcSale(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadFCPurchaseSaleInfo signaturePadSaleInfo,
			BigDecimal employeeId) {
		try {
			LOGGER.debug("in getFcSale");
			String url = appConfig.getJaxURL() + Path.DEVICE_FC_SALE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.TERMINAL_ID, countryBranchSystemInventoryId)
					.queryParam(Params.DEVICE_TYPE, deviceType).queryParam(Params.EMPLOYEE_ID, employeeId)
					.post(signaturePadSaleInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in getFcSale : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadCustomerRegStateMetaInfo metaInfo,
			BigDecimal employeeId) {
		try {
			LOGGER.debug("in updateCustomerRegStateData");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATE_CUSTOMER_REG_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.TERMINAL_ID, countryBranchSystemInventoryId)
					.queryParam(Params.DEVICE_TYPE, deviceType).queryParam(Params.EMPLOYEE_ID, employeeId)
					.post(metaInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in updateCustomerRegStateData : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(
			Integer deviceRegId,
			String signatureImageClob) {
		try {
			LOGGER.debug("in updateSignatureStateData");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATE_SIGNATURE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo()).field(Params.DEVICE_REG_ID, deviceRegId)
					.field("signatureImageClob", signatureImageClob).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in updateSignatureStateData : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> validateOtpForPairing(ClientType deviceType,
			Integer countryBranchSystemInventoryId, String otp) {
		try {
			LOGGER.debug("in validateOtpForPairing");
			String url = appConfig.getJaxURL() + Path.DEVICE_VALIDATE_PAIR_OTP;
			return restService.ajax(url).field(Params.DEVICE_TYPE, deviceType)
					.field(Params.TERMINAL_ID, countryBranchSystemInventoryId).field(Params.OTP, otp).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<DevicePairOtpResponse, BoolRespModel>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in validateOtpForPairing : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> deactivateDevice(Integer deviceRegId) {
		try {
			LOGGER.debug("in deactivateDevice");
			String url = appConfig.getJaxURL() + Path.DEVICE_DEACTIVATE;
			return restService.ajax(url).field(Params.DEVICE_REG_ID, deviceRegId).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in deactivateDevice : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> clearDeviceState(Integer deviceRegId, String paireToken,
			String sessionToken) {
		try {
			LOGGER.debug("in clearDeviceState {}", deviceRegId);
			String url = appConfig.getJaxURL() + Path.DEVICE_STATE_CLEAR;
			return restService.ajax(url).meta(new JaxMetaInfo()).field(Params.DEVICE_REG_ID, deviceRegId)
					.field(Params.PAIRE_TOKEN, paireToken).field(Params.SESSION_TOKEN, sessionToken).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in clearDeviceState : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

}
