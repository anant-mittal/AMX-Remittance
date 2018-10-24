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
import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceRestModels;
import com.amx.jax.device.DeviceRestModels.DevicePairingRequest;
import com.amx.jax.device.DeviceRestModels.DevicePairingResponse;
import com.amx.jax.device.DeviceRestModels.SessionPairingRequest;
import com.amx.jax.device.DeviceRestModels.SessionPairingResponse;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.offsite.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.offsite.OffsiteStatus.OffsiteServerError;
import com.amx.jax.offsite.device.DeviceConfigs.CardBox;
import com.amx.utils.ArgUtil;
import com.amx.utils.Random;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Device APIs")
public class DeviceController {

	private static final Logger LOGGER = LoggerService.getLogger(DeviceController.class);

	@Autowired
	DeviceClient deviceClient;

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@RequestMapping(value = { DeviceConstants.DEVICE_PAIR }, method = { RequestMethod.GET, RequestMethod.POST })
	public AmxApiResponse<DevicePairingResponse, Object> pairDevice(
			@RequestParam(required = false) String deivceTerminalId,
			@RequestParam(required = false) ClientType deivceClientType) {
		if (ArgUtil.isEmpty(deivceTerminalId) || ArgUtil.isEmpty(deivceClientType)) {
			throw new OffsiteServerError(OffsiteServerCodes.DEVICE_NOT_PAIRED);
		}

		// validate Device with jax
		DeviceRegistrationRequest deviceRegistrationRequest = new DeviceRegistrationRequest();
		deviceRegistrationRequest.setDeviceType(deivceClientType);
		deviceRegistrationRequest.setBranchSystemIp(deivceTerminalId);
		deviceRegistrationRequest.setDeviceId(commonHttpRequest.getDeviceId());
		deviceClient.registerNewDevice(deviceRegistrationRequest);

		DevicePairingResponse creds = DeviceRestModels.get();
		creds.setDevicePairingToken(Random.randomAlphaNumeric(10).toLowerCase());
		creds.setDeviceRegId(Random.randomNumeric(2));
		return AmxApiResponse.build(creds);
	}

	@RequestMapping(value = { DeviceConstants.DEVICE_PAIR }, method = { RequestMethod.POST }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public AmxApiResponse<DevicePairingResponse, Object> pairDevice(@RequestBody DevicePairingRequest req) {
		DevicePairingResponse creds = DeviceRestModels.get();
		creds.setDevicePairingToken(Random.randomAlphaNumeric(10).toLowerCase());
		creds.setDeviceRegId(Random.randomNumeric(2));
		return AmxApiResponse.build(creds);
	}

	@RequestMapping(value = { DeviceConstants.SESSION_PAIR }, method = { RequestMethod.POST })
	public AmxApiResponse<SessionPairingResponse, Object> pairSession(@RequestBody SessionPairingRequest req) {
		return AmxApiResponse.build(DeviceRestModels.get());
	}

	@Autowired
	CardBox cardBox;

	@RequestMapping(value = { DeviceConstants.DEVICE_INFO_URL }, method = { RequestMethod.POST })
	public AmxApiResponse<CardData, Object> saveCardDetails(@RequestBody CardReader reader,
			@PathVariable(value = DeviceConstants.PARAM_SYSTEM_ID) String systemid) {
		if (ArgUtil.isEmpty(reader.getData())) {
			cardBox.fastRemove(systemid);
		} else {
			cardBox.put(systemid, reader.getData());
		}
		return AmxApiResponse.build(reader.getData());
	}

	@RequestMapping(value = { DeviceConstants.DEVICE_INFO_URL }, method = { RequestMethod.GET })
	public AmxApiResponse<CardData, Object> getCardDetails(
			@PathVariable(value = DeviceConstants.PARAM_SYSTEM_ID) String systemid,
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
