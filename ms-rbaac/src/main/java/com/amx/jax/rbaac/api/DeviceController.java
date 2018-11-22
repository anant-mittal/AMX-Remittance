package com.amx.jax.rbaac.api;

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
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.rbaac.IDeviceAuthService;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.service.AbstractService.BooleanResponse;
import com.amx.jax.rbaac.service.DeviceService;

@RestController
public class DeviceController implements IDeviceAuthService {

	@Autowired
	DeviceService deviceService;

	Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = Path.DEVICE_REG, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(@Valid @RequestBody DeviceRegistrationRequest request) {

		DeviceDto newDevice = deviceService.registerNewDevice(request);
		return AmxApiResponse.build(newDevice);
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

	@RequestMapping(value = Path.DEVICE_CREATE_SESSION, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> createDeviceSession(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer deviceRegId,
			@RequestParam(name = Params.PAIRE_TOKEN) String paireToken) {
		DevicePairOtpResponse otpResponse = deviceService.sendOtpForPairing(deviceRegId, paireToken);
		return AmxApiResponse.build(otpResponse);
	}

	@Override
	@RequestMapping(value = Path.DEVICE_PAIR_SESSION, method = RequestMethod.POST)
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> pairDeviceSession(
			@RequestParam(name = Params.DEVICE_TYPE) ClientType deviceType,
			@RequestParam(name = Params.TERMINAL_ID) Integer countryBranchSystemInventoryId,
			@RequestParam(name = Params.OTP) String otp) {
		DevicePairOtpResponse otpResponse = deviceService.validateOtpForPairing(deviceType,
				countryBranchSystemInventoryId, otp.toString());
		return AmxApiResponse.build(otpResponse, new BoolRespModel(Boolean.TRUE));
	}

	/*@Override
	@RequestMapping(value = Path.DEVICE_VALIDATE_DEVICE_TOKEN, method = RequestMethod.POST)
	public AmxApiResponse<DevicePairOtpResponse, Object> validateDeviceToken(
			@RequestParam(name = Params.DEVICE_REG_ID) BigDecimal deviceRegId,
			@RequestParam(name = Params.PAIRE_TOKEN) String devicePairToken) {
		DevicePairOtpResponse otpResponse = deviceService.validateDevicePairToken(deviceRegId, devicePairToken);
		return AmxApiResponse.build(otpResponse);
	}*/

	@Override
	@RequestMapping(value = Path.DEVICE_VALIDATE_SESSION_TOKEN, method = RequestMethod.POST)
	public AmxApiResponse<DevicePairOtpResponse, Object> validateDeviceSessionToken(
			@RequestParam(name = Params.DEVICE_REG_ID) BigDecimal deviceRegId,
			@RequestParam(name = Params.SESSION_TOKEN) String deviceSessionToken) {
		DevicePairOtpResponse repsonse = deviceService.validateDeviceSessionPairToken(deviceRegId, deviceSessionToken);
		return AmxApiResponse.build(repsonse);
	}

	@Override
	@RequestMapping(value = Path.DEVICE_GET_DEVICE_REG_ID, method = RequestMethod.POST)
	public AmxApiResponse<BigDecimal, Object> getDeviceRegIdByBranchInventoryId(
			@RequestParam(name = Params.DEVICE_CLIENT_TYPE) ClientType deviceClientType,
			@RequestParam(name = Params.DEVICE_SYS_INV_ID) BigDecimal countryBranchSystemInventoryId) {
		BigDecimal response = deviceService.getDeviceRegIdByBranchInventoryId(deviceClientType,
				countryBranchSystemInventoryId);
		return AmxApiResponse.build(response);
	}
}
