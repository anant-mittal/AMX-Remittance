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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.sso.SSOConstants;
import com.amx.jax.sso.SSOConstants.SSOAuth;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.ArgUtil;
import com.amx.utils.Random;
import com.amx.utils.URLBuilder;

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

	@RequestMapping(value = SSOConstants.APP_LOGIN_URL, method = { RequestMethod.GET })
	public String loginJPage(@RequestParam(required = false) SSOAuth auth, @RequestParam(required = false) String sotp,
			Model model, HttpServletRequest request, HttpServletResponse response)
			throws MalformedURLException, URISyntaxException {

		String tranxId = ssoUser.ssoTranxId();

		auth = (SSOAuth) ArgUtil.parseAsEnum(auth, SSOAuth.NONE);

		if (auth == SSOAuth.DONE && sotp != null && sotp.equals(sSOTranx.get().getSotp())) {
			LOGGER.debug("auth == SSOAuth.DONE");
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(tranxId, sotp);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authProvider.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			ssoUser.setAuthDone(true);
		}

		if (!ssoUser.isAuthDone()) {
			LOGGER.debug("ssoUser.isAuthDone() is false");
			sSOTranx.setLandingUrl(request.getRequestURL().toString(), Random.randomAlphaNumeric(6));
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath(SSOConstants.SSO_LOGIN_URL_REDIRECT).addParameter(AppConstants.TRANX_ID_XKEY, tranxId);
			return SSOConstants.REDIRECT + builder.getURL();
		}

		return SSOConstants.REDIRECT + sSOTranx.get().getReturnUrl();
	}

	@RequestMapping(value = SSOConstants.APP_LOGGEDIN_URL, method = { RequestMethod.GET })
	public String loggedinJPage(Model model, @RequestParam boolean done)
			throws MalformedURLException, URISyntaxException {
		if (!ssoUser.isAuthDone()) {
			LOGGER.debug("ssoUser.isAuthDone() is false");
			sSOTranx.init();
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath(SSOConstants.SSO_LOGIN_URL_REDIRECT).addParameter(AppConstants.TRANX_ID_XKEY,
					AppContextUtil.getTranxId());
			return SSOConstants.REDIRECT + builder.getURL();
		}
		return "home";
	}
}
