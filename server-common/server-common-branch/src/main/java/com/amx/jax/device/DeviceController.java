package com.amx.jax.device;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AmxConstants;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.branch.common.OffsiteStatus.ApiOffisteStatus;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerError;
import com.amx.jax.client.MetaClient;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.DevicePairingRequest;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.response.BranchSystemDetailDto;
import com.amx.jax.rbaac.IRbaacService;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.server.ApiHeaderAnnotations.ApiDeviceHeaders;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Device APIs")
@ApiStatusService(IRbaacService.class)
public class DeviceController {

	private static final Logger LOGGER = LoggerService.getLogger(DeviceController.class);

	@Autowired
	private RbaacServiceClient rbaacServiceClient;

	@Autowired
	private MetaClient metaClient;

	@Autowired
	private CommonHttpRequest commonHttpRequest;

	@Autowired
	private DeviceRequest deviceRequestValidator;

	@Autowired
	private SSOTranx sSOTranx;

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_TERMINALS }, method = { RequestMethod.GET })
	public AmxApiResponse<BranchSystemDetailDto, Object> getTerminals() {
		return metaClient.listBranchSystemInventory();
	}

	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_PAIR }, method = { RequestMethod.POST })
	public AmxApiResponse<DevicePairingCreds, Object> registerNewDevice(@Valid @RequestBody DevicePairingRequest req) {

		String deivceTerminalId = req.getDeivceTerminalId();
		ClientType deivceClientType = req.getDeivceClientType();

		if ((ArgUtil.isEmpty(deivceTerminalId) && ArgUtil.isEmpty(req.getIdentity()))
				|| ArgUtil.isEmpty(deivceClientType)) {
			throw new OffsiteServerError(OffsiteServerCodes.CLIENT_UNKNOWN, "hoho");
		}

		// validate Device with jax
		DeviceRegistrationRequest deviceRegistrationRequest = new DeviceRegistrationRequest();
		deviceRegistrationRequest.setDeviceType(deivceClientType);
		deviceRegistrationRequest.setBranchSystemIp(deivceTerminalId);
		deviceRegistrationRequest.setDeviceId(commonHttpRequest.getDeviceId());
		deviceRegistrationRequest.setIdentityInt(req.getIdentity());
		DeviceDto deviceDto = rbaacServiceClient.registerNewDevice(deviceRegistrationRequest).getResult();

		DevicePairingCreds creds = DeviceRestModels.get();
		creds.setDeviceRegToken(deviceDto.getPairToken());
		creds.setDeviceRegId(ArgUtil.parseAsString(deviceDto.getRegistrationId()));
		creds.setOtpTtl(AmxConstants.OFFLINE_OTP_TTL);
		creds.setRequestTtl(DeviceConstants.Config.REQUEST_TOKEN_VALIDITY);
		creds.setDeviceSecret(deviceDto.getDeviceSecret());
		return AmxApiResponse.build(creds);
	}

	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_ACTIVATE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> activateDevice(
			@RequestParam Integer deviceRegId,
			@RequestParam ClientType deviceType, @RequestParam(required = false) String mOtp) {
		deviceRequestValidator.updateStamp(deviceRegId);
		return rbaacServiceClient.activateDevice(deviceRegId, mOtp);
	}

	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_DEACTIVATE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> deActivateDevice(
			@RequestParam Integer deviceRegId, @RequestParam ClientType deviceType) {
		deviceRequestValidator.updateStamp(deviceRegId);
		return rbaacServiceClient.deactivateDevice(deviceRegId);
	}

	@ApiDeviceHeaders
	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.SESSION_CREATE }, method = { RequestMethod.GET })
	public AmxApiResponse<SessionPairingCreds, Object> sendOtpForPairing() {

		deviceRequestValidator.validateDevice();

		String deviceRegId = deviceRequestValidator.getDeviceRegId();
		String deviceRegToken = deviceRequestValidator.getDeviceRegToken();

		DevicePairOtpResponse resp = rbaacServiceClient
				.createDeviceSession(ArgUtil.parseAsInteger(deviceRegId), deviceRegToken)
				.getResult();
		SessionPairingCreds creds = deviceRequestValidator.createSession(resp.getSessionPairToken(), resp.getOtp(),
				resp.getTermialId(), resp.getEmpId());
		creds.setOtpTtl(AmxConstants.OTP_TTL);
		String meta = ArgUtil.isEmpty(resp.getEmpId()) ? resp.getTermialId() : resp.getEmpId();
		return AmxApiResponse.build(creds, meta);
	}

	@RequestMapping(value = DeviceConstants.Path.SESSION_PAIR, method = RequestMethod.POST)
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> validateOtpForPairing(
			@RequestParam ClientType deviceType,
			@RequestParam Integer terminalId, @RequestParam(required = false) String mOtp) {
		AmxApiResponse<DevicePairOtpResponse, BoolRespModel> resp = rbaacServiceClient.pairDeviceSession(
				deviceType, terminalId,
				mOtp);
		deviceRequestValidator.updateStamp(resp.getResult().getDeviceRegId());
		return resp;
	}

	@RequestMapping(value = { DeviceConstants.Path.SESSION_TERMINAL }, method = { RequestMethod.GET })
	public AmxApiResponse<Object, Object> webAppLogin() {
		DeviceData deviceData = deviceRequestValidator.validateRequest();
		String terminalId = deviceData.getTerminalId();
		sSOTranx.get().setBranchAdapterId(deviceRequestValidator.getDeviceRegId());
		sSOTranx.get().getUserClient().setTerminalId(ArgUtil.parseAsBigDecimal(terminalId));
		// sSOTranx.get().getUserClient().setDeviceRegId(deviceRequestValidator.getDeviceRegId());
		// sSOTranx.get().getUserClient().setGlobalIpAddress(deviceData.getGlobalIp());
		// sSOTranx.get().getUserClient().setLocalIpAddress(deviceData.getLocalIp());
		sSOTranx.save();
		return AmxApiResponse.build(terminalId, deviceRequestValidator.getDeviceRegId());
	}

}
