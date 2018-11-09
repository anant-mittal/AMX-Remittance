package com.amx.jax.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.ApiEndpoint.MetaApi;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.constants.DeviceStateDataType;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.services.DeviceService;

@RestController
@RequestMapping(MetaApi.PREFIX + "/device")
public class DeviceController implements IDeviceService {

	@Autowired
	DeviceService deviceService;

	@RequestMapping(value = DEVICE_REG, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(@Valid @RequestBody DeviceRegistrationRequest request) {

		DeviceDto newDevice = deviceService.registerNewDevice(request);
		return AmxApiResponse.build(newDevice);
	}

	@RequestMapping(value = DEVICE_STATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateDeviceState(
			@Valid @RequestBody DeviceStateInfoChangeRequest request, @RequestHeader Integer registrationId,
			@RequestHeader String paireToken, @RequestHeader String sessionToken) {
		BoolRespModel response = deviceService.updateDeviceState(request, registrationId, paireToken, sessionToken);
		return AmxApiResponse.build(response);
	}

	@RequestMapping(value = DEVICE_ACTIVATE, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<BoolRespModel, Object> activateDevice(@RequestParam Integer countryBranchSystemInventoryId,
			@RequestParam ClientType deviceType) {
		BoolRespModel response = deviceService.activateDevice(countryBranchSystemInventoryId, deviceType);
		return AmxApiResponse.build(response);
	}

	@RequestMapping(value = DEVICE_SEND_PAIR_OTP, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> sendOtpForPairing(@RequestHeader Integer deviceRegId,
			@RequestHeader String paireToken) {
		DevicePairOtpResponse otpResponse = deviceService.sendOtpForPairing(deviceRegId, paireToken);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = DEVICE_VALIDATE_PAIR_OTP, method = RequestMethod.GET)
	public AmxApiResponse<BoolRespModel, Object> validateOtpForPairing(@RequestParam ClientType deviceType,
			@RequestParam Integer countryBranchSystemInventoryId, @RequestParam String otp) {
		BoolRespModel otpResponse = deviceService.validateOtpForPairing(deviceType, countryBranchSystemInventoryId,
				otp.toString());
		return AmxApiResponse.build(otpResponse);
	}

	@Override
	@RequestMapping(value = DEVICE_STATUS_GET, method = RequestMethod.GET)
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(@RequestHeader Integer registrationId,
			@RequestHeader String paireToken, @RequestHeader String sessionToken) {
		DeviceStatusInfoDto otpResponse = deviceService.getStatus(registrationId, paireToken, sessionToken);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = DEVICE_STATE_REMITTANCE_UPDATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(@RequestParam ClientType deviceType,
			@RequestParam Integer countryBranchSystemInventoryId,
			@Valid @RequestBody SignaturePadRemittanceInfo signaturePadRemittanceInfo,
			@RequestParam BigDecimal employeeId) {
		BoolRespModel otpResponse = deviceService.updateDeviceStateData(deviceType, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, DeviceStateDataType.REMITTANCE, employeeId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = DEVICE_FC_PURCHASE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcPurchase(@RequestParam ClientType deviceType,
			@RequestParam Integer countryBranchSystemInventoryId,
			@Valid @RequestBody SignaturePadFCPurchaseSaleInfo signaturePadPurchseInfo,
			@RequestParam BigDecimal employeeId) {
		BoolRespModel otpResponse = deviceService.updateDeviceStateData(deviceType, countryBranchSystemInventoryId,
				signaturePadPurchseInfo, DeviceStateDataType.FC_PURCHASE, employeeId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = DEVICE_FC_SALE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcSale(@RequestParam ClientType deviceType,
			@RequestParam Integer countryBranchSystemInventoryId,
			@Valid @RequestBody SignaturePadFCPurchaseSaleInfo signaturePadSaleInfo,
			@RequestParam BigDecimal employeeId) {
		BoolRespModel otpResponse = deviceService.updateDeviceStateData(deviceType, countryBranchSystemInventoryId,
				signaturePadSaleInfo, DeviceStateDataType.FC_SALE, employeeId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = DEVICE_STATE_CUSTOMER_REG_UPDATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(@RequestParam ClientType deviceType,
			@RequestParam Integer countryBranchSystemInventoryId,
			@Valid @RequestBody SignaturePadCustomerRegStateMetaInfo metaInfo, @RequestParam BigDecimal employeeId) {
		BoolRespModel otpResponse = deviceService.updateDeviceStateData(deviceType, countryBranchSystemInventoryId,
				metaInfo, DeviceStateDataType.CUSTOMER_REGISTRATION, employeeId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = DEVICE_STATE_SIGNATURE_UPDATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(@RequestParam Integer deviceRegId,
			@RequestParam String signatureImageClob) {
		BoolRespModel otpResponse = deviceService.updateSignatureStateData(deviceRegId, signatureImageClob);
		return AmxApiResponse.build(otpResponse);
	}
}
