package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.exception.AbstractJaxException;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.request.DeviceStateInfoChangeRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.rest.RestService;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;

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
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in registerNewDevice : ", e);
			throw new JaxSystemError();
		}
	}

	@Override
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus(Integer registrationId, String paireToken,
			String sessionToken) {
		try {
			LOGGER.info("in getStatus");

			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_STATUS_GET;
			return restService.ajax(url).get().header("registrationId", registrationId.toString())
					.header("paireToken", paireToken).header("sessionToken", sessionToken)
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
			return restService.ajax(url).get().header("deviceRegId", deviceRegId.toString())
					.header("paireToken", paireToken)
					.as(new ParameterizedTypeReference<AmxApiResponse<DevicePairOtpResponse, Object>>() {
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

			String url = appConfig.getJaxURL() + END_POINT_JAX_DEVICE + DEVICE_STATE;
			return restService.ajax(url).post(request).header("deviceRegId", registrationId.toString())
					.header("paireToken", paireToken)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (AbstractJaxException ae) {
			throw ae;
		} catch (Exception e) {
			LOGGER.error("exception in updateDeviceState : ", e);
			throw new JaxSystemError();
		}
	}

}
