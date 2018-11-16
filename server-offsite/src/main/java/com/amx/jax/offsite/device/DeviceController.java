package com.amx.jax.offsite.device;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.adapter.ICardService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.DeviceClient;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.IDeviceService.Path;
import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceRestModels;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.DevicePairingRequest;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.BranchSystemDetailDto;
import com.amx.jax.model.response.DeviceDto;
import com.amx.jax.model.response.DevicePairOtpResponse;
import com.amx.jax.offsite.OffsiteStatus.ApiOffisteStatus;
import com.amx.jax.offsite.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.offsite.device.DeviceConfigs.DeviceData;
import com.amx.jax.sso.SSOTranx;
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
	private MetaClient metaClient;

	@Autowired
	private CommonHttpRequest commonHttpRequest;

	@Autowired
	private DeviceRequest deviceRequestValidator;

	@Autowired
	private SSOTranx sSOTranx;

	@Autowired
	private ICardService iCardService;

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_TERMINALS }, method = { RequestMethod.GET })
	public AmxApiResponse<BranchSystemDetailDto, Object> getTerminals() {
		return metaClient.listBranchSystemInventory();
	}

	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_PAIR }, method = { RequestMethod.POST })
	public AmxApiResponse<DevicePairingCreds, Object> registerNewDevice(@Valid @RequestBody DevicePairingRequest req) {

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
		creds.setDeviceRegId(ArgUtil.parseAsString(deviceDto.getRegistrationId()));
		return AmxApiResponse.build(creds);
	}

	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_ACTIVATE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> activateDevice(
			@RequestParam Integer deviceRegId,
			@RequestParam ClientType deviceType, @RequestParam(required = false) String mOtp) {
		return deviceClient.activateDevice(deviceRegId, mOtp);
	}

	@ApiDeviceHeaders
	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.SESSION_CREATE }, method = { RequestMethod.GET })
	public AmxApiResponse<SessionPairingCreds, Object> sendOtpForPairing() {

		deviceRequestValidator.validateDevice();

		String deviceRegId = deviceRequestValidator.getDeviceRegId();
		String deviceRegToken = deviceRequestValidator.getDeviceRegToken();

		DevicePairOtpResponse resp = deviceClient.sendOtpForPairing(ArgUtil.parseAsInteger(deviceRegId), deviceRegToken)
				.getResult();
		SessionPairingCreds creds = deviceRequestValidator.createSession(resp.getSessionPairToken(), resp.getOtp(),
				resp.getTermialId());
		return AmxApiResponse.build(creds);
	}

	@RequestMapping(value = DeviceConstants.Path.SESSION_PAIR, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> validateOtpForPairing(
			@RequestParam ClientType deviceType,
			@RequestParam Integer terminalId, @RequestParam(required = false) String mOtp) {
		return deviceClient.validateOtpForPairing(
				deviceType, terminalId,
				mOtp);
	}

	@RequestMapping(value = { DeviceConstants.Path.TERMINAL_PAIRING }, method = { RequestMethod.GET })
	public AmxApiResponse<SessionPairingCreds, Object> webAppLogin() {
		DeviceData deviceData = deviceRequestValidator.validateRequest();
		String terminalId = deviceData.getTerminalId();
		sSOTranx.get().setTerminalId(terminalId);
		sSOTranx.save();
		return AmxApiResponse.build();
	}

	@ApiDeviceHeaders
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_CARD }, method = { RequestMethod.POST })
	public AmxApiResponse<CardData, Object> saveCardDetails(@RequestBody CardReader reader) {
		DeviceData deviceData = deviceRequestValidator.validateRequest();
		iCardService.saveCardDetailsByTerminal(deviceData.getTerminalId(), reader.getData());
		return AmxApiResponse.build(reader.getData());
	}

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_CARD }, method = { RequestMethod.GET })
	public AmxApiResponse<CardData, Object> getCardDetails(
			@RequestParam(value = DeviceConstants.Params.PARAM_SYSTEM_ID) String systemid,
			@RequestParam(required = false) Boolean wait, @RequestParam(required = false) Boolean flush)
			throws InterruptedException {
		wait = ArgUtil.parseAsBoolean(wait, Boolean.FALSE);
		flush = ArgUtil.parseAsBoolean(flush, Boolean.FALSE);

		CardData data = iCardService.getCardDetailsByTerminal(systemid, wait, flush);

		return AmxApiResponse.build(data);
	}

}
