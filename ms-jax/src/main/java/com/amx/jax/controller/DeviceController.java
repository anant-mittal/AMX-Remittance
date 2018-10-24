package com.amx.jax.controller;

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
import com.amx.jax.device.SignaturePadRemittanceInfo;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
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
	public AmxApiResponse<DeviceDto, Object> updateDeviceState(
			@Valid @RequestBody DeviceStateInfoChangeRequest request) {
		return deviceService.updateDeviceState(request);
	}

	@RequestMapping(value = DEVICE_ACTIVATE, method = RequestMethod.GET)
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
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(@RequestParam ClientType deviceType,
			@RequestParam Integer countryBranchSystemInventoryId,
			@Valid @RequestBody SignaturePadRemittanceInfo signaturePadRemittanceInfo) {
		BoolRespModel otpResponse = deviceService.updateDeviceState(deviceType, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, DeviceStateDataType.REMITTANCE);
		return AmxApiResponse.build(otpResponse);
	}
}
