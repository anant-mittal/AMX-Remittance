package com.amx.jax.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.AmxConstants;
import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.branch.common.OffsiteStatus.ApiOffisteStatus;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerCodes;
import com.amx.jax.branch.common.OffsiteStatus.OffsiteServerError;
import com.amx.jax.client.MetaClient;
import com.amx.jax.def.ATxCacheBox.Tx;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.DevicePairingRequest;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.response.BranchSystemDetailDto;
import com.amx.jax.rbaac.IRbaacService;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.sso.SSOAuditEvent;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.SSOTranx.SSOModel;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.sso.server.ApiHeaderAnnotations.ApiDeviceHeaders;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil;

import io.swagger.annotations.Api;

@Controller
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

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private AuditService auditService;

	@Autowired(required = false)
	private SSOUser ssoUser;

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_TERMINALS }, method = { RequestMethod.GET })
	@ResponseBody
	public AmxApiResponse<BranchSystemDetailDto, Object> getTerminals() {
		return metaClient.listBranchSystemInventory();
	}

	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_PAIR }, method = { RequestMethod.POST })
	@ResponseBody
	public AmxApiResponse<DevicePairingCreds, Object> registerNewDevice(@Valid @RequestBody DevicePairingRequest req) {

		String deivceTerminalIp = req.getDeivceTerminalId();
		ClientType deivceClientType = req.getDeivceClientType();

		AppContextUtil.getUserClient().setClientType(deivceClientType);

		if ((ArgUtil.isEmpty(deivceTerminalIp) && ArgUtil.isEmpty(req.getIdentity()))
				|| ArgUtil.isEmpty(deivceClientType)) {
			throw new OffsiteServerError(OffsiteServerCodes.CLIENT_UNKNOWN, "hoho");
		}

		SSOAuditEvent auditEvent = new SSOAuditEvent(SSOAuditEvent.Type.DEVICE_PAIR, Result.FAIL)
				.terminalIp(deivceTerminalIp);

		try {
			// validate Device with jax
			DeviceRegistrationRequest deviceRegistrationRequest = new DeviceRegistrationRequest();
			deviceRegistrationRequest.setDeviceType(deivceClientType);
			deviceRegistrationRequest.setBranchSystemIp(deivceTerminalIp);
			deviceRegistrationRequest.setDeviceId(commonHttpRequest.getDeviceId());
			deviceRegistrationRequest.setIdentityInt(req.getIdentity());
			DeviceDto deviceDto = rbaacServiceClient.registerNewDevice(deviceRegistrationRequest).getResult();

			DevicePairingCreds creds = DeviceRestModels.get();
			creds.setDeviceRegToken(deviceDto.getPairToken());
			creds.setDeviceRegId(ArgUtil.parseAsString(deviceDto.getRegistrationId()));
			creds.setOtpTtl(AmxConstants.OFFLINE_OTP_TTL);
			creds.setRequestTtl(DeviceConstants.Config.REQUEST_TOKEN_VALIDITY);
			creds.setOtpChars(CryptoUtil.COMPLEX_CHARS);
			creds.setDeviceSecret(deviceDto.getDeviceSecret());
			// Audit
			auditEvent.terminalId(deviceDto.getTermialId())
					.deviceRegId(deviceDto.getRegistrationId()).setResult(Result.DONE);

			return AmxApiResponse.build(creds);
		} finally {
			// Audit
			auditService.log(auditEvent);
		}

	}

	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_PAIR }, method = { RequestMethod.GET })
	public String registerNewDevice(Model model) {
		model.addAttribute("lang", commonHttpRequest.getLanguage());
		model.addAttribute("cdnUrl", appConfig.getCdnURL());
		model.addAttribute("appContext", appConfig.getAppPrefix());
		model.addAttribute(AppConstants.DEVICE_ID_KEY, commonHttpRequest.getUserDevice().getFingerprint());
		return "sso_device";
	}

	@Deprecated
	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_ACTIVATE }, method = { RequestMethod.POST })
	@ResponseBody
	public AmxApiResponse<BoolRespModel, Object> activateDevice(
			@RequestParam(required = false) String secureKey,
			@RequestParam Integer deviceRegId,
			@RequestParam ClientType deviceType, @RequestParam(required = false) String mOtp) {
		deviceRequestValidator.updateStamp(deviceRegId);
		return rbaacServiceClient.activateDevice(deviceRegId, mOtp);
	}

	@Deprecated
	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_DEACTIVATE }, method = { RequestMethod.POST })
	@ResponseBody
	public AmxApiResponse<BoolRespModel, Object> deActivateDevice(
			@RequestParam(required = false) String secureKey,
			@RequestParam Integer deviceRegId, @RequestParam ClientType deviceType) {
		deviceRequestValidator.updateStamp(deviceRegId);
		return rbaacServiceClient.deactivateDevice(deviceRegId);
	}

	@ApiDeviceHeaders
	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_SESSION }, method = { RequestMethod.GET })
	@ResponseBody
	public AmxApiResponse<SessionPairingCreds, Object> sendOtpForPairing() {

		deviceRequestValidator.validateDevice();

		String deviceRegId = deviceRequestValidator.getDeviceRegId();
		String deviceRegToken = deviceRequestValidator.getDeviceRegToken();

		DevicePairOtpResponse resp = rbaacServiceClient
				.createDeviceSession(ArgUtil.parseAsInteger(deviceRegId), deviceRegToken)
				.getResult();
		SessionPairingCreds creds = deviceRequestValidator.createSession(resp.getSessionPairToken(), resp.getOtp(),
				resp.getTermialId(), resp.getEmpId(), resp.getDeviceType());
		creds.setOtpTtl(AmxConstants.OFFLINE_OTP_TTL);
		creds.setRequestTtl(DeviceConstants.Config.REQUEST_TOKEN_VALIDITY);
		creds.setOtpChars(CryptoUtil.COMPLEX_CHARS);
		String meta = ArgUtil.isEmpty(resp.getEmpId()) ? resp.getTermialId() : resp.getEmpId();

		AppContextUtil.getUserClient().setClientType(resp.getDeviceType());

		// Audit
		auditService.log(new SSOAuditEvent(SSOAuditEvent.Type.DEVICE_SESSION_CREATED)
				.terminalId(resp.getTermialId())
				.deviceRegId(deviceRegId));

		return AmxApiResponse.build(creds, meta);
	}

	@Deprecated
	@RequestMapping(value = DeviceConstants.Path.SESSION_PAIR, method = RequestMethod.POST)
	@ResponseBody
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> validateOtpForPairing(
			@RequestParam ClientType deviceType,
			@RequestParam Integer terminalId, @RequestParam(required = false) String mOtp) {
		AmxApiResponse<DevicePairOtpResponse, BoolRespModel> resp = rbaacServiceClient.pairDeviceSession(
				deviceType, terminalId,
				mOtp);
		deviceRequestValidator.updateStamp(resp.getResult().getDeviceRegId());
		// Audit
		AppContextUtil.getUserClient().setClientType(resp.getResult().getDeviceType());
		auditService.log(new SSOAuditEvent(SSOAuditEvent.Type.DEVICE_SESSION_PAIR)
				.terminalId(resp.getResult().getTermialId())
				.deviceRegId(resp.getResult().getDeviceRegId()));

		return resp;
	}

	@RequestMapping(value = DeviceConstants.Path.SESSION_PAIR_DEVICE, method = RequestMethod.POST)
	@ResponseBody
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> validateOtpForPairingDevice(
			@PathVariable(value = "deviceType") ClientType deviceType, @RequestParam(required = false) String mOtp) {

		AmxApiResponse<DevicePairOtpResponse, BoolRespModel> resp = rbaacServiceClient.pairDeviceSession(deviceType,
				ssoUser.getUserClient().getTerminalId().intValueExact(), mOtp);
		deviceRequestValidator.updateStamp(resp.getResult().getDeviceRegId());
		// Audit
		AppContextUtil.getUserClient().setClientType(resp.getResult().getDeviceType());
		auditService.log(new SSOAuditEvent(SSOAuditEvent.Type.DEVICE_SESSION_PAIR)
				.terminalId(resp.getResult().getTermialId()).deviceRegId(resp.getResult().getDeviceRegId()));

		return resp;
	}

	@RequestMapping(value = DeviceConstants.Path.DEVICE_TYPE, method = RequestMethod.GET)
	@ResponseBody
	public AmxApiResponse<ClientType, Object> deviceList() {

		List<ClientType> deviceTypeList = new ArrayList<ClientType>(Arrays.asList(ClientType.values()));
		return AmxApiResponse.buildList(deviceTypeList);

	}

	@RequestMapping(value = { DeviceConstants.Path.SESSION_TERMINAL }, method = { RequestMethod.GET })
	@ResponseBody
	public AmxApiResponse<Object, Object> webAppLogin() {

		DeviceData deviceData = deviceRequestValidator.validateRequest();

		String terminalId = deviceData.getTerminalId();
		Tx<SSOModel> tx = sSOTranx.getX();
		tx.get().setBranchAdapterId(deviceRequestValidator.getDeviceRegId());
		tx.get().setTerminalId(ArgUtil.parseAsBigDecimal(terminalId));
		// sSOTranx.get().getUserClient().setDeviceRegId(deviceRequestValidator.getDeviceRegId());
		// sSOTranx.get().getUserClient().setGlobalIpAddress(deviceData.getGlobalIp());
		// sSOTranx.get().getUserClient().setLocalIpAddress(deviceData.getLocalIp());
		sSOTranx.commitX(tx);

		// Audit
		AppContextUtil.getUserClient().setClientType(ClientType.BRANCH_ADAPTER);
		auditService.log(new SSOAuditEvent(SSOAuditEvent.Type.SESSION_TERMINAL_MAP)
				.terminalId(sSOTranx.get().getTerminalId()).deviceRegId(sSOTranx.get().getBranchAdapterId()));

		return AmxApiResponse.build(terminalId, deviceRequestValidator.getDeviceRegId());
	}

	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_DELETE }, method = { RequestMethod.POST })
	@ResponseBody
	public AmxApiResponse<BoolRespModel, Object> deleteDevice(@RequestParam Integer deviceRegId,
			@RequestParam ClientType deviceType) {
		deviceRequestValidator.updateStamp(deviceRegId);
		return rbaacServiceClient.deleteDevice(deviceRegId);
	}

	@ApiOffisteStatus({ OffsiteServerCodes.CLIENT_UNKNOWN })
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_ACTIVATE }, method = { RequestMethod.POST })
	@ResponseBody
	public AmxApiResponse<BoolRespModel, Object> statusDevice(
			@RequestParam(required = false) String secureKey,
			@RequestParam Integer deviceRegId,
			@RequestParam ClientType deviceType, @RequestParam(required = false) String mOtp) {
		deviceRequestValidator.updateStamp(deviceRegId);
		return rbaacServiceClient.activateDevice(deviceRegId, mOtp);
	}

}
