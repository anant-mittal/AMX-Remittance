package com.amx.jax.sso.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
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
import com.amx.jax.api.AmxFieldError;
import com.amx.jax.device.CardData;
import com.amx.jax.device.DeviceBox;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceData;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.http.RequestType;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.logger.events.AuditActorInfo;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.constants.RbaacServiceConstants.LOGIN_TYPE;
import com.amx.jax.rbaac.dto.UserClientDto;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
import com.amx.jax.session.SessionContextService;
import com.amx.jax.sso.SSOAuditEvent;
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
import com.amx.utils.TimeUtils;
import com.amx.utils.URLBuilder;
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

	@Autowired
	private AuditService auditService;

	@Autowired
	private SessionContextService sessionContextService;

	private Map<String, Object> getModelMap() {
		ssoUser.ssoTranxId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		map.put(SSOConstants.PARAM_SSO_LOGIN_URL, appConfig.getAppPrefix() + SSOConstants.SSO_LOGIN_URL_DO);
		map.put(SSOConstants.PARAM_SSO_LOGIN_PREFIX, appConfig.getAppPrefix());
		map.put(SSOConstants.SECURITY_CODE_KEY, ssoUser.getSelfSAC());
		map.put(SSOConstants.PARTNER_SECURITY_CODE_KEY, ssoUser.getPartnerSAC());
		map.put(SSOConstants.SSO_TENANT_KEY, AppContextUtil.getTenant());

		String adapterUrl = sSOConfig.getAdapterUrl();
		Cookie kooky = commonHttpRequest.getCookie("adapter.url");
		if (kooky != null) {
			adapterUrl = ArgUtil.parseAsString(kooky.getValue(), adapterUrl);
		}
		map.put(SSOConstants.ADAPTER_URL, adapterUrl);
		map.put("tnt", AppContextUtil.getTenant().toString());
		map.put("loginWithRop", sSOConfig.isRopEnabled());
		map.put("loginWithoutCard", sSOConfig.isLoginWithoutCard());
		map.put("loginWithPartner", sSOConfig.isLoginWithPartner());
		map.put("loginWithDevice", sSOConfig.isLoginWithDevice());

		return map;
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_HTML, method = RequestMethod.GET)
	public String authLoginView(Model model,
			@PathVariable(required = false, value = "htmlstep") @ApiParam(defaultValue = "REQUIRED") SSOAuthStep html,
			@RequestParam(required = false) Long refresh, HttpServletResponse resp,
			@RequestParam(required = false, value = AppConstants.TRANX_ID_XKEY) String trnxId)
			throws MalformedURLException, URISyntaxException {
		if (sSOTranx.get() != null) {
			SSOModel x = sSOTranx.get();
			refresh = x.getCreatedStamp();
			// System.out.println("=="+refresh+"===="+System.currentTimeMillis()+"trnx
			// "+AppContextUtil.getTranxId());
			if ((ArgUtil.isEmpty(refresh) || TimeUtils.isExpired(refresh, 10 * 1000))) {
				String newTranxId = AppContextUtil.getTraceId(true, true);
				ssoUser.setTranxId(newTranxId);

				URLBuilder builder = Urly.parse(
						ArgUtil.ifNotEmpty(sSOTranx.get().getAppUrl(),
								appConfig.getAppPrefix() + SSOConstants.APP_LOGIN_URL_DONE))
						.queryParam(AppConstants.TRANX_ID_XKEY, newTranxId)
						.queryParam(SSOConstants.PARAM_STEP, SSOAuthStep.DONE)
						.queryParam(SSOConstants.PARAM_SOTP, sSOTranx.get().getAppToken());

				/// builder.path(appConfig.getAppPrefix() + SSOConstants.SSO_LOGIN_URL)
				// .queryParam("refresh", System.currentTimeMillis())
				;
				resp.setHeader("Location", builder.getURL());
				resp.setStatus(302);
			} else {
				sSOTranx.fastPut(x);
			}
		}
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
		clientType = (ClientType) ArgUtil.parseAsEnum(clientType, sSOConfig.getLoginWithClientType());

		if (json == SSOAuthStep.DO) {
			json = formdata.getStep();
		} else {
			formdata.setStep(json);
		}

		Map<String, Object> model = getModelMap();
		if (sSOTranx.get() == null) {
			sSOTranx.put(new SSOModel());
		}
		AmxApiResponse<Object, Map<String, Object>> result = AmxApiResponse.buildMeta(model);
		result.setStatusEnum(SSOServerCodes.AUTH_REQUIRED);

		AppContextUtil.getUserClient().setClientType(clientType);

		if (sSOTranx.get() != null) {

			UserDeviceClient userDeviceClient = commonHttpRequest.getUserDevice().toUserDeviceClient();

			if (SSOAuthStep.CREDS == json) {

				// Audit
				SSOAuditEvent auditEvent = new SSOAuditEvent(SSOAuditEvent.Type.LOGIN_INIT, Result.FAIL)
						.identity(formdata.getIdentity()).empno(formdata.getEcnumber());

				ssoUser.generateSAC();

				SSOModel ssomodel = sSOTranx.get();
				UserClientDto userClientDto = new UserClientDto();
				userClientDto.setDeviceType(userDeviceClient.getDeviceType());
				userClientDto.setClientType(clientType);
				userClientDto.setGlobalIpAddress(userDeviceClient.getIp());
				userClientDto.setTerminalId(ssomodel.getTerminalId());

				if (appConfig.isSwaggerEnabled() && !ArgUtil.isEmpty(deviceType)) {
					userClientDto.setDeviceType(deviceType);
				}
				AmxFieldError x = new AmxFieldError();
				x.setDescription("Terminal Id is " + sSOTranx.get().getBranchAdapterId());

				AppContextUtil.addWarning(x);
				if (!ArgUtil.isEmpty(sSOTranx.get().getBranchAdapterId())) {
					// Terminal Login
					DeviceData branchDeviceData = deviceBox.get(sSOTranx.get().getBranchAdapterId());
					userClientDto.setLocalIpAddress(branchDeviceData.getLocalIp());
					userClientDto.setTerminalId(ArgUtil.parseAsBigDecimal(branchDeviceData.getTerminalId()));
					LOGGER.info("Gloabal IPs THIS: {} ADAPTER: {}", userDeviceClient.getIp(),
							branchDeviceData.getGlobalIp());

					// Audit
					auditEvent.terminalId(userClientDto.getTerminalId())
							// .clientType(ClientType.BRANCH_ADAPTER)
							.deviceRegId(sSOTranx.get().getBranchAdapterId());
				} else {
					// Device LOGIN
					String deviceRegId = commonHttpRequest.get(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY);
					userClientDto.setLocalIpAddress(userDeviceClient.getIp());
					userClientDto.setDeviceId(userDeviceClient.getFingerprint());
					userClientDto.setDeviceRegId(ArgUtil.parseAsBigDecimal(deviceRegId));
					userClientDto.setDeviceRegToken(
							commonHttpRequest.get(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY));
					userClientDto.setDeviceSessionToken(
							commonHttpRequest.get(DeviceConstants.Keys.CLIENT_SESSION_TOKEN_XKEY));
					// Audit
					auditEvent.deviceRegId(deviceRegId);
				}

				ssoUser.setUserClient(userClientDto);
				UserAuthInitReqDTO init = new UserAuthInitReqDTO();
				init.setEmployeeNo(formdata.getEcnumber());
				init.setIdentity(formdata.getIdentity());

				init.setUserClientDto(userClientDto);

				init.setLoginType(loginType);
				init.setSelfSAC(ssoUser.getSelfSAC());

				if (loginType == LOGIN_TYPE.ASSISTED) {
					init.setPartnerIdentity(formdata.getPartnerIdentity());
					init.setPartnerSAC(ssoUser.getPartnerSAC());
				}
				try {
					// init.getUserClientDto().setTerminalId(new BigDecimal(1068));
					UserAuthInitResponseDTO initResp = rbaacServiceClient.initAuthForUser(init).getResult();

					model.put("mOtpPrefix", ssoUser.getSelfSAC());
					adapterServiceClient.sendSACtoEmployee(ArgUtil.parseAsString(initResp.getEmployeeId()),
							ssoUser.getSelfSAC());

					if (loginType == LOGIN_TYPE.ASSISTED) {
						model.put("partnerMOtpPrefix", ssoUser.getPartnerSAC());
						adapterServiceClient.sendSACtoEmployee(ArgUtil.parseAsString(initResp.getPartnerEmployeeId()),
								ssoUser.getPartnerSAC());
					}
					result.setStatusEnum(SSOServerCodes.OTP_REQUIRED);
					// Audit
					auditEvent.result(Result.DONE);
				} finally {
					auditService.log(auditEvent);
				}

			} else if ((SSOAuthStep.OTP == json) && formdata.getMotp() != null) {

				SSOAuditEvent auditEvent = new SSOAuditEvent(SSOAuditEvent.Type.LOGIN_OTP, Result.FAIL)
						.clientType(clientType);
				try {
					String terminalId = ArgUtil.parseAsString(sSOTranx.get().getTerminalId());

					UserAuthorisationReqDTO auth = new UserAuthorisationReqDTO();
					auth.setEmployeeNo(formdata.getEcnumber());

					if (ArgUtil.isEmpty(terminalId)) {
						auth.setIpAddress(userDeviceClient.getIp());
					} else {
						ssoUser.setTerminalId(terminalId);
						// TODO:-- TO validate
						auth.setIpAddress(terminalId);
					}

					auth.setDeviceId(userDeviceClient.getFingerprint());
					auth.setmOtp(formdata.getMotp());
					if (loginType == LOGIN_TYPE.ASSISTED) {
						auth.setPartnerMOtp(formdata.getPartnerMOtp());
					}

					// Audit
					auditEvent.terminalId(terminalId).terminalIp(userDeviceClient.getIp());

					EmployeeDetailsDTO empDto = rbaacServiceClient.authoriseUser(auth).getResult();
					sSOTranx.setUserDetails(empDto);

					/** <Save Session Info on Shared Context across microservices */
					AuditActorInfo actor = new AuditActorInfo(AuditActor.ActorType.EMP,
							empDto.getEmployeeId());
					actor.setBranchId(empDto.getCountryBranchId());
					actor.setBranchName(empDto.getBranchName());
					actor.setAreaName(empDto.getArea());
					actor.setAreaId(empDto.getAreaCode());
					actor.setUsername(empDto.getUserName());
					actor.setEmpno(empDto.getEmployeeNumber());
					sessionContextService.setContext(actor);
					/*** ends> */

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

					// Audit
					auditEvent.result(Result.DONE);
				} finally {
					auditService.log(auditEvent);
				}
			}
		}
		return JsonUtil.toJson(result);
	}

	@Autowired
	OutlookService outlookService;

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_OUTLOOK, method = RequestMethod.GET)
	public String authLoginViewOutLook(Model model,
			@PathVariable(required = false, value = "htmlstep") @ApiParam(defaultValue = "REQUIRED") SSOAuthStep html,
			@RequestParam(required = false) Long refresh, HttpServletResponse resp,
			@RequestParam(required = false, value = AppConstants.TRANX_ID_XKEY) String trnxId)
			throws MalformedURLException, URISyntaxException {

		UUID state = UUID.randomUUID();
		UUID nonce = UUID.randomUUID();
		ssoUser.setOutlookNonce(state);
		ssoUser.setOutlookNonce(nonce);

		resp.setHeader("Location", outlookService.getLoginUrl(state, nonce));
		resp.setStatus(302);
		model.addAllAttributes(getModelMap());
		return SSOConstants.SSO_INDEX_PAGE;
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_OUTLOOK, method = RequestMethod.POST)
	public String authLoginViewOutLookCallback(Model model,
			@RequestParam("code") String code,
			@RequestParam("id_token") String idToken,
			@RequestParam("state") UUID state)
			throws MalformedURLException, URISyntaxException {
		if (state.equals(ssoUser.getOutlookState())) {
			ssoUser.setOutlookAuthCode(code);
			ssoUser.setOutlookIdToken(idToken);
		} else {
			System.out.println("Unexpected state returned from authority.");
		}
		model.addAllAttributes(getModelMap());
		return SSOConstants.SSO_INDEX_PAGE;
	}

	@ApiRequest(type = RequestType.POLL)
	@ApiSSOStatus({ SSOServerCodes.NO_TERMINAL_SESSION, SSOServerCodes.AUTH_DONE })
	@RequestMapping(value = SSOConstants.SSO_CARD_DETAILS, method = RequestMethod.GET, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	@ResponseBody
	public String getCardDetails() throws InterruptedException {
		AmxApiResponse<CardData, Map<String, Object>> resp = AmxApiResponse.build(new CardData(),
				new HashMap<String, Object>());
		ssoUser.ssoTranxId();
		String terminlId = ArgUtil.parseAsString(sSOTranx.get().getTerminalId());
		resp.getMeta().put("tid", terminlId);
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
