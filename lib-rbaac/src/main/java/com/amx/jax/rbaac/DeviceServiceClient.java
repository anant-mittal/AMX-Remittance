package com.amx.jax.rbaac;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rest.RestService;

public class DeviceServiceClient implements IDeviceService {

	private static final Logger LOGGER = LoggerService.getLogger(DeviceServiceClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request) {
		LOGGER.debug("in registerNewDevice");
		String url = appConfig.getAuthURL() + Path.DEVICE_REG;
		return restService.ajax(url).post(request)
				.as(new ParameterizedTypeReference<AmxApiResponse<DeviceDto, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> activateDevice(Integer deviceRegId, String mOtp) {
		LOGGER.debug("in activateDevice");
		String url = appConfig.getAuthURL() + Path.DEVICE_ACTIVATE;
		return restService.ajax(url).field(Params.MOTP, mOtp).field(Params.DEVICE_REG_ID, deviceRegId).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> deactivateDevice(Integer deviceRegId) {
		LOGGER.debug("in deactivateDevice");
		String url = appConfig.getAuthURL() + Path.DEVICE_DEACTIVATE;
		return restService.ajax(url).field(Params.DEVICE_REG_ID, deviceRegId).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> createDeviceSession(Integer deviceRegId, String paireToken) {
		LOGGER.debug("in deactivateDevice");
		String url = appConfig.getAuthURL() + Path.DEVICE_CREATE_SESSION;
		return restService.ajax(url).field(Params.DEVICE_REG_ID, deviceRegId).field(Params.PAIRE_TOKEN, paireToken)
				.postForm().as(new ParameterizedTypeReference<AmxApiResponse<DevicePairOtpResponse, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> pairDeviceSession(ClientType deviceType,
			Integer countryBranchSystemInventoryId, String otp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> validateDeviceToken(BigDecimal deviceRegId, String devicePairToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> validateDeviceSessionToken(BigDecimal deviceRegId,
			String deviceSessionToken) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<BigDecimal, Object> getDeviceRegIdByBranchInventoryId(ClientType deviceClientType,
			BigDecimal countryBranchSystemInventoryId) {
		// TODO Auto-generated method stub
		return null;
	}

}
