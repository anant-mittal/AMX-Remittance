package com.amx.jax.sso.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.adapter.DeviceConnectorClient;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.device.CardData;
import com.amx.jax.device.DeviceBox;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceData;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.http.RequestType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.UserDevice;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.constants.RbaacServiceConstants.LOGIN_TYPE;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
import com.amx.jax.sso.SSOConfig;
import com.amx.jax.sso.SSOConstants;
import com.amx.jax.sso.SSOConstants.SSOAuthStep;
import com.amx.jax.sso.SSOStatus.ApiSSOStatus;
import com.amx.jax.sso.SSOStatus.SSOServerCodes;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.SSOTranx.SSOModel;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.sso.server.ApiHeaderAnnotations.ApiDeviceHeaders;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.Urly;

import io.swagger.annotations.ApiParam;

@Controller
public class SSOServerController {

	private Logger LOGGER = LoggerService.getLogger(SSOServerController.class);

	@Autowired
	private SSOTranx sSOTranx;

	@Autowired
	private SSOConfig sSOConfig;

	@Autowired
	private SSOUser ssoUser;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private DeviceBox deviceBox;

	@Autowired
	private CommonHttpRequest commonHttpRequest;

	@Autowired
	RbaacServiceClient rbaacServiceClient;

	@Autowired
	DeviceConnectorClient adapterServiceClient;

	private Map<String, Object> getModelMap() {
		ssoUser.ssoTranxId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		map.put(SSOConstants.PARAM_SSO_LOGIN_URL, appConfig.getAppPrefix() + SSOConstants.SSO_LOGIN_URL_DO);
		map.put(SSOConstants.PARAM_SSO_LOGIN_PREFIX, appConfig.getAppPrefix());
		map.put(SSOConstants.SECURITY_CODE_KEY, ssoUser.getSelfSAC());
		map.put(SSOConstants.PARTNER_SECURITY_CODE_KEY, ssoUser.getPartnerSAC());
		return map;
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_HTML, method = RequestMethod.GET)
	public String authLoginView(Model model,
			@PathVariable(required = false, value = "htmlstep") @ApiParam(defaultValue = "REQUIRED") SSOAuthStep html) {
		ssoUser.generateSAC();
		model.addAllAttributes(getModelMap());
		return SSOConstants.SSO_INDEX_PAGE;
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_JSON, method = RequestMethod.GET, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	@ResponseBody
	public String authLoginJson(Model model,
			@PathVariable(required = false, value = "jsonstep") @ApiParam(defaultValue = "REQUIRED") SSOAuthStep json) {
		ssoUser.generateSAC();
		AmxApiResponse<Object, Map<String, Object>> result = AmxApiResponse.buildMeta(getModelMap());
		result.setStatusEnum(SSOServerCodes.AUTH_REQUIRED);
		return JsonUtil.toJson(result);
	}

	@ApiDeviceHeaders
	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE, SSOServerCodes.OTP_REQUIRED })
	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_JSON, method = { RequestMethod.POST }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	@ResponseBody
	public String loginJson(@RequestBody SSOLoginFormData formdata,
			@PathVariable(required = false, value = "jsonstep") @ApiParam(defaultValue = "CREDS") SSOAuthStep json,
			HttpServletResponse resp,
			@RequestParam(required = false) DeviceType deviceType,
			@RequestParam(required = false) ClientType clientType,
			@RequestParam(required = false, defaultValue = "SELF") LOGIN_TYPE loginType,
			@RequestParam(required = false, value = SSOConstants.IS_RETURN) Boolean isReturn,
			@RequestParam(required = false) Boolean redirect) throws URISyntaxException, IOException {

		redirect = ArgUtil.parseAsBoolean(redirect, true);
		isReturn = ArgUtil.parseAsBoolean(isReturn, false);

		if (json == SSOAuthStep.DO) {
			json = formdata.getStep();
		} else {
			formdata.setStep(json);
		}

		Map<String, Object> model = getModelMap();
		if (sSOTranx.get() == null) {
			sSOTranx.init();
		}
		AmxApiResponse<Object, Map<String, Object>> result = AmxApiResponse.buildMeta(model);
		result.setStatusEnum(SSOServerCodes.AUTH_REQUIRED);

		if (sSOTranx.get() != null) {
			UserDevice userDevice = commonHttpRequest.getUserDevice();

			if (SSOAuthStep.CREDS == json) {

				ssoUser.generateSAC();

				SSOModel ssomodel = sSOTranx.get();
				ssomodel.getUserClient().setDeviceType(userDevice.getType());
				ssomodel.getUserClient().setClientType(clientType);
				ssomodel.getUserClient().setGlobalIpAddress(userDevice.getIp());

				if (appConfig.isSwaggerEnabled() && !ArgUtil.isEmpty(deviceType)) {
					ssomodel.getUserClient().setDeviceType(deviceType);
				}

				if (!ArgUtil.isEmpty(sSOTranx.get().getBranchAdapterId())) {
					// Terminal Login
					DeviceData branchDeviceData = deviceBox.get(sSOTranx.get().getBranchAdapterId());
					ssomodel.getUserClient().setLocalIpAddress(branchDeviceData.getLocalIp());
					ssomodel.getUserClient().setTerminalId(ArgUtil.parseAsBigDecimal(branchDeviceData.getTerminalId()));
					LOGGER.info("Gloabal IPs THIS: {} ADAPTER: {}", userDevice.getIp(), branchDeviceData.getGlobalIp());
				} else {
					// Device LOGIN
					ssomodel.getUserClient().setLocalIpAddress(userDevice.getIp());
					ssomodel.getUserClient().setDeviceId(userDevice.getFingerprint());
					ssomodel.getUserClient()
							.setDeviceRegId(ArgUtil.parseAsBigDecimal(
									commonHttpRequest.get(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY)));
					ssomodel.getUserClient()
							.setDeviceRegToken(
									commonHttpRequest.get(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY));
					ssomodel.getUserClient()
							.setDeviceSessionToken(
									commonHttpRequest.get(DeviceConstants.Keys.CLIENT_SESSION_TOKEN_XKEY));
				}

				UserAuthInitReqDTO init = new UserAuthInitReqDTO();
				init.setEmployeeNo(formdata.getEcnumber());
				init.setIdentity(formdata.getIdentity());

				init.setUserClientDto(ssomodel.getUserClient());

				init.setLoginType(loginType);
				init.setSelfSAC(ssoUser.getSelfSAC());

				if (loginType == LOGIN_TYPE.ASSISTED) {
					init.setPartnerIdentity(formdata.getPartnerIdentity());
					init.setPartnerSAC(ssoUser.getPartnerSAC());
				}

				UserAuthInitResponseDTO initResp = rbaacServiceClient.initAuthForUser(init).getResult();

				model.put("mOtpPrefix", ssoUser.getSelfSAC());
				adapterServiceClient.sendSACtoEmployee(ArgUtil.parseAsString(initResp.getEmployeeId()),
						ssoUser.getSelfSAC());

				if (loginType == LOGIN_TYPE.ASSISTED) {
					model.put("partnerMOtpPrefix", ssoUser.getSelfSAC());
					adapterServiceClient.sendSACtoEmployee(ArgUtil.parseAsString(initResp.getPartnerEmployeeId()),
							ssoUser.getPartnerSAC());
				}

				result.setStatusEnum(SSOServerCodes.OTP_REQUIRED);

			} else if ((SSOAuthStep.OTP == json) && formdata.getMotp() != null) {

				String terminalId = ArgUtil.parseAsString(sSOTranx.get().getUserClient().getTerminalId());

				UserAuthorisationReqDTO auth = new UserAuthorisationReqDTO();
				auth.setEmployeeNo(formdata.getEcnumber());

				if (ArgUtil.isEmpty(terminalId)) {
					auth.setIpAddress(userDevice.getIp());
				} else {
					auth.setIpAddress(terminalId);
				}

				auth.setDeviceId(userDevice.getFingerprint());
				auth.setmOtp(formdata.getMotp());
				if (loginType == LOGIN_TYPE.ASSISTED) {
					auth.setPartnerMOtp(formdata.getPartnerMOtp());
				}
				EmployeeDetailsDTO empDto = rbaacServiceClient.authoriseUser(auth).getResult();
				sSOTranx.setUserDetails(empDto);

				String redirectUrl = Urly.parse(
						ArgUtil.ifNotEmpty(sSOTranx.get().getAppUrl(),
								appConfig.getAppPrefix() + SSOConstants.APP_LOGIN_URL_DONE))
						.queryParam(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId())
						.queryParam(SSOConstants.PARAM_STEP, SSOAuthStep.DONE)
						.queryParam(SSOConstants.PARAM_SOTP, sSOTranx.get().getAppToken())
						.queryParam(SSOConstants.IS_RETURN, isReturn)
						.getURL();
				model.put(SSOConstants.PARAM_REDIRECT, redirectUrl);
				result.setRedirectUrl(redirectUrl);
				result.setStatusEnum(SSOServerCodes.AUTH_DONE);
				if (redirect) {
					resp.setHeader("Location", redirectUrl);
					resp.setStatus(302);
				}
			}
		}
		return JsonUtil.toJson(result);
	}

	@ApiRequest(type = RequestType.POLL)
	@ApiSSOStatus({ SSOServerCodes.NO_TERMINAL_SESSION, SSOServerCodes.AUTH_DONE })
	@RequestMapping(value = SSOConstants.SSO_CARD_DETAILS, method = RequestMethod.GET, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	@ResponseBody
	public String getCardDetails() throws InterruptedException {
		AmxApiResponse<CardData, Object> resp = AmxApiResponse.build(new CardData());
		ssoUser.ssoTranxId();
		String terminlId = ArgUtil.parseAsString(sSOTranx.get().getUserClient().getTerminalId());
		if (terminlId != null) {
			CardData card = adapterServiceClient.pollCardDetailsByTerminal(terminlId).getResult();
			if (card != null) {
				resp.setResults(Collections.singletonList(card));
			} else {
				resp.setStatusEnum(SSOServerCodes.NO_TERMINAL_CARD);
			}
		} else {
			resp.setStatusEnum(SSOServerCodes.NO_TERMINAL_SESSION);
		}
		return JsonUtil.toJson(resp);
	}

}
