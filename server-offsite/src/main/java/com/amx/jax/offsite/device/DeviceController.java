package com.amx.jax.offsite.device;

import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.offsite.OffsiteStatus.ApiOffisteStatus;
import com.amx.jax.offsite.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.offsite.device.DeviceConfigs.CardBox;
import com.amx.jax.offsite.device.DeviceConfigs.DeviceData;
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
	private DeviceRequest deviceRequestValidator;

	@Autowired
	private CardBox cardBox;

	@ApiOffisteStatus({ OffsiteServerCodes.DEVICE_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_PAIR }, method = { RequestMethod.POST })
	public AmxApiResponse<DevicePairingCreds, Object> registerNewDevice(
			@Valid @RequestBody DevicePairingRequest req) {

		String deivceTerminalId = req.getDeivceTerminalId();
		ClientType deivceClientType = req.getDeivceClientType();

		if (ArgUtil.isEmpty(deivceTerminalId) || ArgUtil.isEmpty(deivceClientType)) {
			// throw new OffsiteServerError(OffsiteServerCodes.DEVICE_UNKNOWN,"hoho");
		}

		// validate Device with jax
		DeviceRegistrationRequest deviceRegistrationRequest = new DeviceRegistrationRequest();
		deviceRegistrationRequest.setDeviceType(deivceClientType);
		deviceRegistrationRequest.setBranchSystemIp(deivceTerminalId);
		deviceRegistrationRequest.setDeviceId(commonHttpRequest.getDeviceId());
		DeviceDto deviceDto = deviceClient.registerNewDevice(deviceRegistrationRequest).getResult();

		DevicePairingCreds creds = DeviceRestModels.get();
		creds.setDeviceRegToken(deviceDto.getPairToken());
		creds.setDeviceRegKey(ArgUtil.parseAsString(deviceDto.getRegistrationId()));
		return AmxApiResponse.build(creds);
	}

	@ApiDeviceHeaders
	@ApiOffisteStatus({ OffsiteServerCodes.DEVICE_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.SESSION_PAIR }, method = { RequestMethod.GET })
	public AmxApiResponse<SessionPairingCreds, Object> sendOtpForPairing() {

		deviceRequestValidator.validateDevice();

		String deviceRegKey = deviceRequestValidator.getDeviceRegKey();
		String deviceRegToken = deviceRequestValidator.getDeviceRegToken();

		DevicePairOtpResponse resp = deviceClient
				.sendOtpForPairing(ArgUtil.parseAsInteger(deviceRegKey), deviceRegToken).getResult();
		SessionPairingCreds creds = deviceRequestValidator.createSession(resp.getSessionPairToken(), resp.getOtp(),
				resp.getTermialId());
		return AmxApiResponse.build(creds);
	}

	@ApiDeviceHeaders
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_ACTIVITY }, method = { RequestMethod.GET })
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus() {
		deviceRequestValidator.validateRequest();
		return deviceClient.getStatus(ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegKey()),
				deviceRequestValidator.getDeviceRegToken(), deviceRequestValidator.getDeviceSessionToken());
	}

	@ApiDeviceHeaders
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_CARD }, method = { RequestMethod.POST })
	public AmxApiResponse<CardData, Object> saveCardDetails(@RequestBody CardReader reader) {
		DeviceData deviceData = deviceRequestValidator.validateRequest();
		if (ArgUtil.isEmpty(reader.getData())) {
			cardBox.fastRemove(deviceData.getTerminalId());
		} else {
			cardBox.put(deviceData.getTerminalId(), reader.getData());
		}
		return AmxApiResponse.build(reader.getData());
	}

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_CARD }, method = { RequestMethod.GET })
	public AmxApiResponse<CardData, Object> getCardDetails(
			@RequestParam(value = DeviceConstants.Params.PARAM_SYSTEM_ID) String systemid,
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
