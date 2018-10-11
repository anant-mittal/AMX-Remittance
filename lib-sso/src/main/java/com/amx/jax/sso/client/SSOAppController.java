package com.amx.jax.sso.client;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

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
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.sso.SSOConstants;
import com.amx.jax.sso.SSOConstants.SSOAuthStep;
import com.amx.jax.sso.SSOStatus.ApiSSOStatus;
import com.amx.jax.sso.SSOStatus.SSOServerCodes;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.Random;
import com.amx.utils.URLBuilder;

import io.swagger.annotations.ApiParam;

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
	@RequestMapping(value = SSOConstants.APP_LOGIN_URL_HTML, method = { RequestMethod.GET })
	public String loginJPage(
			@PathVariable(required = false, value = "htmlstep") @ApiParam(defaultValue = "CHECK") SSOAuthStep step,
			@RequestParam(required = false) String sotp, Model model, HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException, URISyntaxException {

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
			sSOTranx.setAppReturnDetails(request.getRequestURL().toString(), Random.randomAlphaNumeric(6));
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath(SSOConstants.SSO_LOGIN_URL_REQUIRED).addParameter(AppConstants.TRANX_ID_XKEY, tranxId);
			return SSOConstants.REDIRECT + builder.getURL();
		}
		return SSOConstants.REDIRECT + sSOTranx.get().getReturnUrl();
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@ResponseBody
	@RequestMapping(value = SSOConstants.APP_LOGIN_URL_JSON, method = { RequestMethod.GET }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public String loginJSON(
			@PathVariable(required = false, value = "jsonstep") @ApiParam(defaultValue = "CHECK") SSOAuthStep step,
			@RequestParam(required = false) String sotp, Model model, HttpServletRequest request,
			HttpServletResponse response) throws MalformedURLException, URISyntaxException {
		String redirectUrl = this.loginJPage(step, sotp, model, request, response);
		response.setHeader("Location", redirectUrl.replace(SSOConstants.REDIRECT, ""));
		response.setStatus(302);
		return JsonUtil.toJson(AmxApiResponse.build());
	}

	@RequestMapping(value = SSOConstants.APP_LOGGEDIN_URL_HTML, method = { RequestMethod.GET, RequestMethod.POST })
	public String loggedinJPage() throws MalformedURLException, URISyntaxException {
		return "sso_home";
	}

	@ApiSSOStatus({ SSOServerCodes.AUTH_REQUIRED, SSOServerCodes.AUTH_DONE })
	@ResponseBody
	@RequestMapping(value = SSOConstants.APP_LOGGEDIN_URL_JSON, method = { RequestMethod.GET,
			RequestMethod.POST }, produces = { CommonMediaType.APPLICATION_JSON_VALUE,
					CommonMediaType.APPLICATION_V0_JSON_VALUE })
	public String loggedinJson() throws MalformedURLException, URISyntaxException {
		return JsonUtil.toJson(AmxApiResponse.build(ssoUser.getUserDetails()));
	}
}
