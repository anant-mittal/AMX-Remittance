package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.META_API_ENDPOINT;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.constants.DeviceStateDataType;
import com.amx.jax.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.DeviceStatusInfoDto;
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

	@RequestMapping(value = DEVICE_ACTIVATE, method = RequestMethod.GET)
	public AmxApiResponse<BooleanResponse, Object> activateDevice(@RequestParam Integer countryBranchSystemInventoryId,
			@RequestParam String deviceType) {
		BooleanResponse response = deviceService.activateDevice(countryBranchSystemInventoryId, deviceType);
		return AmxApiResponse.build(response);
	}

	@RequestMapping(value = DEVICE_SEND_PAIR_OTP, method = RequestMethod.GET)
	public AmxApiResponse<DevicePairOtpResponse, Object> sendOtpForPairing(@RequestParam Integer deviceRegId) {
		DevicePairOtpResponse otpResponse = deviceService.sendOtpForPairing(deviceRegId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = DEVICE_VALIDATE_PAIR_OTP, method = RequestMethod.GET)
	public AmxApiResponse<BooleanResponse, Object> validateOtpForPairing(
			@RequestParam Integer countryBranchSystemInventoryId, String otp) {
		BooleanResponse otpResponse = deviceService.validateOtpForPairing(countryBranchSystemInventoryId,
				otp.toString());
		return AmxApiResponse.build(otpResponse);
	}

	@Override
	@RequestMapping(value = DEVICE_STATUS_GET, method = RequestMethod.GET)
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(@RequestHeader Integer registrationId) {
		DeviceStatusInfoDto otpResponse = deviceService.getStatus(registrationId);
		return AmxApiResponse.build(otpResponse);
	}

	@RequestMapping(value = DEVICE_STATE_REMITTANCE_UPDATE, method = RequestMethod.GET)
	public AmxApiResponse<BooleanResponse, Object> updateRemittanceState(
			@RequestParam Integer countryBranchSystemInventoryId,
			@RequestBody SignaturePadRemittanceInfo signaturePadRemittanceInfo) {
		BooleanResponse otpResponse = deviceService.updateDeviceState(countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, DeviceStateDataType.REMITTANCE);
		return AmxApiResponse.build(otpResponse);
	}
}
