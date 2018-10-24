package com.amx.jax.offsite.device;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.DeviceClient;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceRestModels;
import com.amx.jax.device.DeviceRestModels.DevicePairingRequest;
import com.amx.jax.device.DeviceRestModels.DevicePairingResponse;
import com.amx.jax.device.DeviceRestModels.SessionPairingResponse;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.offsite.OffsiteStatus.ApiOffisteStatus;
import com.amx.jax.offsite.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.offsite.OffsiteStatus.OffsiteServerError;
import com.amx.jax.offsite.device.DeviceConfigs.CardBox;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Device APIs")
@ApiStatusService(IDeviceService.class)
public class DeviceController {

	private static final Logger LOGGER = LoggerService.getLogger(DeviceController.class);

	@Autowired
	private DeviceClient deviceClient;

	@Autowired
	private CommonHttpRequest commonHttpRequest;

	@Autowired
	private DeviceRequestValidator deviceRequestValidator;

	@Autowired
	private CardBox cardBox;

	@ApiOffisteStatus({ OffsiteServerCodes.DEVICE_NOT_REGISTERED })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_PAIR }, method = { RequestMethod.GET, RequestMethod.POST })
	public AmxApiResponse<DevicePairingResponse, Object> pairDevice(
			@RequestParam(required = false) String deivceTerminalId,
			@RequestParam(required = false) ClientType deivceClientType) {
		if (ArgUtil.isEmpty(deivceTerminalId) || ArgUtil.isEmpty(deivceClientType)) {
			throw new OffsiteServerError(OffsiteServerCodes.DEVICE_NOT_REGISTERED);
		}

		// validate Device with jax
		DeviceRegistrationRequest deviceRegistrationRequest = new DeviceRegistrationRequest();
		deviceRegistrationRequest.setDeviceType(deivceClientType);
		deviceRegistrationRequest.setBranchSystemIp(deivceTerminalId);
		deviceRegistrationRequest.setDeviceId(commonHttpRequest.getDeviceId());
		DeviceDto deviceDto = deviceClient.registerNewDevice(deviceRegistrationRequest).getResult();

		DevicePairingResponse creds = DeviceRestModels.get();
		creds.setDeviceRegToken(deviceDto.getPairToken());
		creds.setDeviceRegKey(ArgUtil.parseAsString(deviceDto.getRegistrationId()));
		return AmxApiResponse.build(creds);
	}

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_PAIR }, method = { RequestMethod.POST }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public AmxApiResponse<DevicePairingResponse, Object> pairDevice(@RequestBody DevicePairingRequest req) {
		return pairDevice(req.getDeivceTerminalId(), req.getDeivceClientType());
	}

	@RequestMapping(value = { DeviceConstants.Path.SESSION_PAIR }, method = { RequestMethod.GET })
	public AmxApiResponse<SessionPairingResponse, Object> pairSession() {
		String deviceRegKey = commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REG_KEY_XKEY);
		String deviceRegToken = commonHttpRequest.get(DeviceConstants.Keys.DEVICE_REG_TOKEN_XKEY);

		if (ArgUtil.isEmpty(deviceRegKey) || ArgUtil.isEmpty(deviceRegToken)) {
			throw new OffsiteServerError(OffsiteServerCodes.DEVICE_CREDS_MISSING);
		}

		DevicePairOtpResponse resp = deviceClient.sendOtpForPairing(ArgUtil.parseAsInteger(deviceRegKey), deviceRegKey)
				.getResult();
		SessionPairingResponse creds = deviceRequestValidator.validate(resp.getSessionPairToken(), resp.getOtp());
		return AmxApiResponse.build(creds);
	}

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_INFO_URL }, method = { RequestMethod.POST })
	public AmxApiResponse<CardData, Object> saveCardDetails(@RequestBody CardReader reader,
			@PathVariable(value = DeviceConstants.Params.PARAM_SYSTEM_ID) String systemid) {
		if (ArgUtil.isEmpty(reader.getData())) {
			cardBox.fastRemove(systemid);
		} else {
			cardBox.put(systemid, reader.getData());
		}
		return AmxApiResponse.build(reader.getData());
	}

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_INFO_URL }, method = { RequestMethod.GET })
	public AmxApiResponse<CardData, Object> getCardDetails(
			@PathVariable(value = DeviceConstants.Params.PARAM_SYSTEM_ID) String systemid,
			@RequestParam(required = false) Boolean wait, @RequestParam(required = false) Boolean flush)
			throws InterruptedException {
		wait = ArgUtil.parseAsBoolean(wait, Boolean.FALSE);
		flush = ArgUtil.parseAsBoolean(flush, Boolean.FALSE);
		CardData data = null;
		if (wait) {
			data = cardBox.take(systemid, 15, TimeUnit.SECONDS);
		} else {
			data = cardBox.get(systemid);
		}

		if (flush) {
			cardBox.fastRemove(systemid);
		}
		return AmxApiResponse.build(data);
	}

}
