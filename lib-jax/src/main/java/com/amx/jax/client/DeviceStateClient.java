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
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.rest.RestService;

@Component
public class DeviceStateClient implements IDeviceStateService {

	private static final Logger LOGGER = LoggerService.getLogger(DeviceStateClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(
			Integer registrationId, String paireToken,
			String sessionToken) {
		
			LOGGER.debug("in getStatus");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATUS_GET;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.DEVICE_REG_ID, registrationId.toString())
					.queryParam(Params.PAIRE_TOKEN, paireToken).queryParam(Params.SESSION_TOKEN, sessionToken).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<DeviceStatusInfoDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadRemittanceInfo signaturePadRemittanceInfo,
			BigDecimal employeeId) {
	
			LOGGER.debug("in updateRemittanceState");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATE_REMITTANCE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.TERMINAL_ID, countryBranchSystemInventoryId)
					.queryParam(Params.DEVICE_TYPE, deviceType).queryParam(Params.EMPLOYEE_ID, employeeId)
					.post(signaturePadRemittanceInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcPurchase(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadFCPurchaseSaleInfo signaturePadPurchseInfo,
			BigDecimal employeeId) {
		
			LOGGER.debug("in getFcPurchase");
			String url = appConfig.getJaxURL() + Path.DEVICE_FC_PURCHASE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.TERMINAL_ID, countryBranchSystemInventoryId)
					.queryParam(Params.DEVICE_TYPE, deviceType).queryParam(Params.EMPLOYEE_ID, employeeId)
					.post(signaturePadPurchseInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcSale(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadFCPurchaseSaleInfo signaturePadSaleInfo,
			BigDecimal employeeId) {
		
			LOGGER.debug("in getFcSale");
			String url = appConfig.getJaxURL() + Path.DEVICE_FC_SALE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.TERMINAL_ID, countryBranchSystemInventoryId)
					.queryParam(Params.DEVICE_TYPE, deviceType).queryParam(Params.EMPLOYEE_ID, employeeId)
					.post(signaturePadSaleInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(
			ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadCustomerRegStateMetaInfo metaInfo,
			BigDecimal employeeId) {
		
			LOGGER.debug("in updateCustomerRegStateData");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATE_CUSTOMER_REG_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.TERMINAL_ID, countryBranchSystemInventoryId)
					.queryParam(Params.DEVICE_TYPE, deviceType).queryParam(Params.EMPLOYEE_ID, employeeId)
					.post(metaInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomerProfileStateData(ClientType deviceType,
			Integer countryBranchSystemInventoryId, SignaturePadCustomerRegStateMetaInfo metaInfo,
			BigDecimal employeeId) {
		
			LOGGER.debug("in updateCustomerProfileStateData");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATE_CUSTOMER_PROFILE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo())
					.queryParam(Params.TERMINAL_ID, countryBranchSystemInventoryId)
					.queryParam(Params.DEVICE_TYPE, deviceType).queryParam(Params.EMPLOYEE_ID, employeeId)
					.post(metaInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(
			Integer deviceRegId,
			String signatureImageClob) {
		
			LOGGER.debug("in updateSignatureStateData");
			String url = appConfig.getJaxURL() + Path.DEVICE_STATE_SIGNATURE_UPDATE;
			return restService.ajax(url).meta(new JaxMetaInfo()).field(Params.DEVICE_REG_ID, deviceRegId)
					.field("signatureImageClob", signatureImageClob).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> clearDeviceState(Integer deviceRegId, String paireToken,
			String sessionToken) {
		
			LOGGER.debug("in clearDeviceState {}", deviceRegId);
			String url = appConfig.getJaxURL() + Path.DEVICE_STATE_CLEAR;
			return restService.ajax(url).meta(new JaxMetaInfo()).field(Params.DEVICE_REG_ID, deviceRegId)
					.field(Params.PAIRE_TOKEN, paireToken).field(Params.SESSION_TOKEN, sessionToken).postForm()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

}
