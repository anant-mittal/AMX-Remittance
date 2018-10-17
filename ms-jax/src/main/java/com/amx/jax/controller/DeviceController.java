package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.META_API_ENDPOINT;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.services.DeviceService;

@RestController
@RequestMapping(META_API_ENDPOINT + "/device")
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
	public AmxApiResponse<DeviceDto, Object> updateDeviceState(@Valid DeviceStateInfoChangeRequest request) {
		return deviceService.updateDeviceState(request);
	}

	@RequestMapping(value = DEVICE_ACTIVATE)
	public AmxApiResponse<BooleanResponse, Object> activateDevice(@RequestParam Integer countryBranchSystemInventoryId,
			@RequestParam String deviceType) {
		BooleanResponse response = deviceService.activateDevice(countryBranchSystemInventoryId, deviceType);
		return AmxApiResponse.build(response);
	}

	@RequestMapping(value = DEVICE_SEND_PAIR_OTP)
	public AmxApiResponse<DevicePairOtpResponse, Object> sendOtpForPairing(@RequestParam Integer deviceRegId) {
		DevicePairOtpResponse otpResponse = deviceService.sendOtpForPairing(deviceRegId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = DEVICE_VALIDATE_PAIR_OTP)
	public AmxApiResponse<BooleanResponse, Object> validateOtpForPairing(
			@RequestParam Integer countryBranchSystemInventoryId, @RequestParam @Size(min = 6, max = 6) Integer otp) {
		BooleanResponse otpResponse = deviceService.validateOtpForPairing(countryBranchSystemInventoryId,
				otp.toString());
		return AmxApiResponse.build(otpResponse);
	}
}
