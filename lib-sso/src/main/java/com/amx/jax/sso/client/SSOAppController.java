package com.amx.jax.sso.client;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AResponse.Target;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.sso.SSOConstants;
import com.amx.jax.sso.SSOConstants.SSOAuthStep;
import com.amx.jax.sso.SSOStatus.ApiSSOStatus;
import com.amx.jax.sso.SSOStatus.SSOServerCodes;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.SSOTranx.SSOModel;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.ArgUtil;
import com.amx.utils.HttpUtils;
import com.amx.utils.JsonUtil;
import com.amx.utils.Random;
import com.amx.utils.URLBuilder;

import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class SSOAppController {

	private Logger LOGGER = Logger.getLogger(SSOAppController.class);

	@Autowired
	private SSOTranx sSOTranx;

	@Autowired
	private SSOAuthProvider authProvider;

	@Autowired
	private SSOUser ssoUser;

	@Autowired
	private AppConfig appConfig;

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@ResponseBody
	@RequestMapping(value = SSOConstants.APP_LOGIN_URL_START, method = { RequestMethod.GET }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public String loginJSONStart(
			@RequestParam String sotp, @RequestParam(required = false) String returnUrld, HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException, URISyntaxException {
		AmxApiResponse<Object, Map<String, Object>> result = AmxApiResponse.buildMeta(new HashMap<String, Object>());
		String tranxId = ssoUser.ssoTranxId();
		String returnUrl = null;
		if (returnUrld != null) {
			byte[] decodedBytes = Base64.getDecoder().decode(returnUrld);
			returnUrl = new String(decodedBytes);
		}
		SSOModel sSOModel = sSOTranx.get();
		sSOModel.setAppUrl(returnUrl);
		sSOModel.setAppToken(sotp);
		sSOModel.getUserClient().setClientType(ClientType.BRANCH_WEB_OLD);
		sSOTranx.save(sSOModel);

		URLBuilder builder = new URLBuilder(HttpUtils.getServerName(request));
		builder.path(appConfig.getAppPrefix() + SSOConstants.SSO_LOGIN_URL_REQUIRED)
				.queryParam(AppConstants.TRANX_ID_XKEY, tranxId);
		result.setRedirectUrl(builder.getURL());
		result.getMeta().put(AppConstants.TRANX_ID_XKEY, tranxId);
		return JsonUtil.toJson(result);
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@ResponseBody
	@RequestMapping(value = SSOConstants.APP_LOGIN_URL_SESSION, method = { RequestMethod.POST }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public String loginJSONPreAuth(@RequestParam String targetUrl, HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException, URISyntaxException {
		AmxApiResponse<Object, Map<String, Object>> result = AmxApiResponse.buildMeta(new HashMap<String, Object>());
		String tranxId = ssoUser.ssoTranxId();
		URLBuilder builder = new URLBuilder(targetUrl).queryParam(AppConstants.TRANX_ID_XKEY, tranxId)
				.queryParam(SSOConstants.PARAM_SOTP, sSOTranx.get().getAppToken())
				.queryParam(SSOConstants.PARAM_STEP, SSOAuthStep.DONE)
				.queryParam(SSOConstants.PARAM_SESSION_TOKEN, AppContextUtil.getTraceId());
		result.setTargetUrl(builder.getURL(), Target._BLANK);
		return JsonUtil.toJson(result);
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@ResponseBody
	@RequestMapping(value = SSOConstants.APP_LOGIN_URL_VERYFY, method = { RequestMethod.GET }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public String loginJSONVerify(
			@RequestParam String sotp, HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException, URISyntaxException {
		String tranxId = ssoUser.ssoTranxId();
		if (!ArgUtil.isEmpty(sSOTranx.get().getUserDetails()) && sotp.equalsIgnoreCase(sSOTranx.get().getAppToken())) {
			return JsonUtil.toJson(AmxApiResponse.buildData(sSOTranx.get().getUserDetails()));
		}
		return JsonUtil.toJson(AmxApiResponse.build());
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@RequestMapping(value = SSOConstants.APP_LOGIN_URL_HTML, method = { RequestMethod.GET })
	public String loginJPage(
			@PathVariable(required = false, value = "htmlstep") @ApiParam(defaultValue = "CHECK") SSOAuthStep step,
			@RequestParam(required = false) String sotp,
			@RequestParam(required = false, value = SSOConstants.IS_RETURN) Boolean isReturn,
			Model model, HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException, URISyntaxException {
		/**
		 * By default for JSON apis, redirections should be true
		 */
		isReturn = ArgUtil.parseAsBoolean(isReturn, true);

		String tranxId = ssoUser.ssoTranxId();
		step = (SSOAuthStep) ArgUtil.parseAsEnum(step, SSOAuthStep.CHECK);

		if (sotp != null && sotp.equals(sSOTranx.get().getAppToken()) && sSOTranx.get().getUserDetails() != null) {
			LOGGER.debug("auth == SSOAuth.DONE");
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(tranxId, sotp);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authProvider.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			ssoUser.setAuthDone(true);
			ssoUser.setUserDetails(sSOTranx.get().getUserDetails());
		}

		if (!ssoUser.isAuthDone()) {
			LOGGER.debug("ssoUser.isAuthDone() is false");
			SSOModel sSOModel = sSOTranx.get();
			sSOModel.setAppUrl(request.getRequestURL().toString());
			sSOModel.setAppToken(Random.randomAlphaNumeric(6));
			sSOModel.getUserClient().setClientType(ClientType.BRANCH_WEB);
			sSOTranx.save(sSOModel);

			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.path(SSOConstants.SSO_LOGIN_URL_REQUIRED)
					.queryParam(AppConstants.TRANX_ID_XKEY, tranxId);
			return SSOConstants.REDIRECT + builder.getURL();
		}
		return SSOConstants.REDIRECT + (isReturn ? sSOTranx.get().getReturnUrl() : SSOConstants.APP_LOGGEDIN_URL);
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@ResponseBody
	@RequestMapping(value = SSOConstants.APP_LOGIN_URL_JSON, method = { RequestMethod.GET }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public String loginJSON(
			@PathVariable(required = false, value = "jsonstep") @ApiParam(defaultValue = "CHECK") SSOAuthStep step,
			@RequestParam(required = false) String sotp,
			@RequestParam(required = false, value = SSOConstants.IS_RETURN) Boolean isReturn,
			Model model, HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException, URISyntaxException {
		/**
		 * By default for JSON apis, redirections should be false
		 */
		isReturn = ArgUtil.parseAsBoolean(isReturn, false);

		String redirectUrl = this.loginJPage(step, sotp, isReturn, model, request, response);

		AmxApiResponse<Object, Object> resp = AmxApiResponse.build();
		resp.setRedirectUrl((appConfig.getAppPrefix() + redirectUrl.replace(SSOConstants.REDIRECT, "")));
		//if (isReturn || !ssoUser.isAuthDone()) {
			// Redirect only if user is not logged, otherwise redirection should be based on
			// argument passed by user
			response.setHeader("Location", resp.getRedirectUrl());
			response.setStatus(302);
		//}
		return JsonUtil.toJson(resp);
	}

	@ApiIgnore
	@RequestMapping(value = SSOConstants.APP_LOGGEDIN_URL_HTML, method = { RequestMethod.GET, RequestMethod.POST })
	public String loggedinJPage() throws MalformedURLException, URISyntaxException {
		return "sso_home";
	}

	@ApiIgnore
	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@ResponseBody
	@RequestMapping(value = SSOConstants.APP_LOGGEDIN_URL_JSON, method = { RequestMethod.GET,
			RequestMethod.POST },
			produces = { CommonMediaType.APPLICATION_JSON_VALUE,
					CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public String loggedinJson() throws MalformedURLException, URISyntaxException {
		return JsonUtil.toJson(AmxApiResponse.build(ssoUser.getUserDetails()));
	}

	@ResponseBody
	@RequestMapping(value = SSOConstants.APP_LOGOUT_URL, method = { RequestMethod.GET },
			produces = { CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public String logout(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) Boolean redirect) throws MalformedURLException, URISyntaxException {
		redirect = ArgUtil.parseAsBoolean(redirect, true);
		sSOTranx.clear(ssoUser.ssoTranxId());
		SecurityContextHolder.getContext().setAuthentication(null);
		ssoUser.setAuthDone(false);
		AmxApiResponse<Object, Model> result = AmxApiResponse.buildMeta(model);

//		URLBuilder builder = new URLBuilder(HttpUtils.getServerName(request));
//		builder.path(appConfig.getAppPrefix() + '/');
		String redirectUrl = HttpUtils.getServerName(request) + appConfig.getAppPrefix() + '/';
		result.setRedirectUrl(redirectUrl);
		if (redirect) {
			response.setHeader("Location", redirectUrl);
			response.setStatus(302);
		}
		return JsonUtil.toJson(result);
	}
}
