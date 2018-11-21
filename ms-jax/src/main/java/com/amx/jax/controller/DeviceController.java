package com.amx.jax.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.constants.DeviceStateDataType;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.services.DeviceService;

@RestController
public class DeviceController implements IDeviceService {

	@Autowired
	DeviceService deviceService;

	Logger logger = LoggerFactory.getLogger(getClass());

/*	@RequestMapping(value = Path.DEVICE_REG, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(@Valid @RequestBody DeviceRegistrationRequest request) {
		DeviceDto newDevice = deviceService.registerNewDevice(request);
		return AmxApiResponse.build(newDevice);
	}*/

	@RequestMapping(value = Path.DEVICE_STATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateDeviceState(
			@Valid @RequestBody DeviceStateInfoChangeRequest request,
			@RequestParam(name = Params.DEVICE_REG_ID) Integer registrationId,
			@RequestParam(name = Params.PAIRE_TOKEN) String paireToken,
			@RequestParam(name = Params.SESSION_TOKEN) String sessionToken) {
		BoolRespModel response = deviceService.updateDeviceState(request, registrationId, paireToken, sessionToken);
		return AmxApiResponse.build(response);
	}

	@RequestMapping(value = Path.DEVICE_ACTIVATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> activateDevice(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer deviceRegId,
			@RequestParam(name = Params.MOTP, required = false) String mOtp) {
		BoolRespModel response = deviceService.activateDevice(deviceRegId);
		return AmxApiResponse.build(response);
	}

	@RequestMapping(value = Path.DEVICE_DEACTIVATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> deactivateDevice(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer deviceRegId) {
		BoolRespModel response = deviceService.deactivateDevice(deviceRegId);
		return AmxApiResponse.build(response);
	}

	@RequestMapping(value = Path.DEVICE_SEND_PAIR_OTP, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> sendOtpForPairing(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer deviceRegId,
			@RequestParam(name = Params.PAIRE_TOKEN) String paireToken) {
		DevicePairOtpResponse otpResponse = deviceService.sendOtpForPairing(deviceRegId, paireToken);
		return AmxApiResponse.build(otpResponse);
	}

	@Override
	@RequestMapping(value = Path.DEVICE_VALIDATE_PAIR_OTP, method = RequestMethod.POST)
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> validateOtpForPairing(
			@RequestParam(name = Params.DEVICE_TYPE) ClientType deviceType,
			@RequestParam(name = Params.TERMINAL_ID) Integer countryBranchSystemInventoryId,
			@RequestParam(name = Params.OTP) String otp) {
		DevicePairOtpResponse otpResponse = deviceService.validateOtpForPairing(deviceType,
				countryBranchSystemInventoryId, otp.toString());
		return AmxApiResponse.build(otpResponse, new BoolRespModel(Boolean.TRUE));
	}

	@Override
	@RequestMapping(value = Path.DEVICE_STATUS_GET, method = RequestMethod.GET)
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer registrationId,
			@RequestParam(name = Params.PAIRE_TOKEN) String paireToken,
			@RequestParam(name = Params.SESSION_TOKEN) String sessionToken) {
		logger.debug("in get Device status api with params reg id:  {} , pairetoken: {} , sessionToken: {}",
				registrationId, paireToken, sessionToken);
		DeviceStatusInfoDto otpResponse = deviceService.getStatus(registrationId, paireToken, sessionToken);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = Path.DEVICE_STATE_REMITTANCE_UPDATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(
			@RequestParam(name = Params.DEVICE_TYPE) ClientType deviceType,
			@RequestParam(name = Params.TERMINAL_ID) Integer countryBranchSystemInventoryId,
			@Valid @RequestBody SignaturePadRemittanceInfo signaturePadRemittanceInfo,
			@RequestParam(name = Params.EMPLOYEE_ID) BigDecimal employeeId) {
		BoolRespModel otpResponse = deviceService.updateDeviceStateData(deviceType, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, DeviceStateDataType.REMITTANCE, employeeId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = Path.DEVICE_FC_PURCHASE_UPDATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcPurchase(
			@RequestParam(name = Params.DEVICE_TYPE) ClientType deviceType,
			@RequestParam(name = Params.TERMINAL_ID) Integer countryBranchSystemInventoryId,
			@Valid @RequestBody SignaturePadFCPurchaseSaleInfo signaturePadPurchseInfo,
			@RequestParam(name = Params.EMPLOYEE_ID) BigDecimal employeeId) {
		BoolRespModel otpResponse = deviceService.updateDeviceStateData(deviceType, countryBranchSystemInventoryId,
				signaturePadPurchseInfo, DeviceStateDataType.FC_PURCHASE, employeeId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = Path.DEVICE_FC_SALE_UPDATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateFcSale(
			@RequestParam(name = Params.DEVICE_TYPE) ClientType deviceType,
			@RequestParam(name = Params.TERMINAL_ID) Integer countryBranchSystemInventoryId,
			@Valid @RequestBody SignaturePadFCPurchaseSaleInfo signaturePadSaleInfo,
			@RequestParam(name = Params.EMPLOYEE_ID) BigDecimal employeeId) {
		BoolRespModel otpResponse = deviceService.updateDeviceStateData(deviceType, countryBranchSystemInventoryId,
				signaturePadSaleInfo, DeviceStateDataType.FC_SALE, employeeId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = Path.DEVICE_STATE_CUSTOMER_REG_UPDATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(
			@RequestParam(name = Params.DEVICE_TYPE) ClientType deviceType,
			@RequestParam(name = Params.TERMINAL_ID) Integer countryBranchSystemInventoryId,
			@Valid @RequestBody SignaturePadCustomerRegStateMetaInfo metaInfo,
			@RequestParam(name = Params.EMPLOYEE_ID) BigDecimal employeeId) {
		BoolRespModel otpResponse = deviceService.updateDeviceStateData(deviceType, countryBranchSystemInventoryId,
				metaInfo, DeviceStateDataType.CUSTOMER_REGISTRATION, employeeId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = Path.DEVICE_STATE_SIGNATURE_UPDATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer deviceRegId,
			@RequestParam String signatureImageClob) {
		BoolRespModel otpResponse = deviceService.updateSignatureStateData(deviceRegId, signatureImageClob);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = Path.DEVICE_STATE_CLEAR, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> clearDeviceState(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer deviceRegId,
			@RequestParam(name = Params.PAIRE_TOKEN) String paireToken,
			@RequestParam(name = Params.SESSION_TOKEN) String sessionToken) {
		BoolRespModel response = deviceService.clearDeviceState(deviceRegId, paireToken, sessionToken);
		return AmxApiResponse.build(response);
	}
}
