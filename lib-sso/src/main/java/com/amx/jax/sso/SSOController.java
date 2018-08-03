
package com.amx.jax.sso;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.amx.jax.service.HttpService;
import com.amx.utils.Random;
import com.amx.utils.URLBuilder;

@Controller
public class SSOController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSOController.class);
	private static final String REDIRECT = "redirect:";

	@Autowired
	HttpService httpService;

	@Autowired
	SSOAuthProvider authProvider;

	@Autowired
	SSOUser ssoUser;

	@Autowired
	AppConfig appConfig;

	@Autowired
	SSOTranx sSOTranx;

	public enum SSOAuth {
		DONE, INIT, NONE
	}

	@RequestMapping(value = SSOUtils.LOGIN_URL, method = { RequestMethod.GET })
	public String loginJPage(@RequestParam(required = false) SSOAuth auth, @RequestParam(required = false) String sotp,
			Model model, HttpServletRequest request, HttpServletResponse response)
			throws MalformedURLException, URISyntaxException {
		String tranxId = AppContextUtil.getTranxId();
		if (auth == null) {
			auth = SSOAuth.NONE;
		}

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
			builder.setPath(SSOUtils.SSO_LOGIN_URL).addParameter(AppConstants.TRANX_ID_XKEY, tranxId);
			return REDIRECT + builder.getURL();
		}
		return REDIRECT + sSOTranx.get().getReturnUrl();
	}

	@RequestMapping(value = SSOUtils.LOGGEDIN_URL, method = { RequestMethod.GET })
	public String loggedinJPage(Model model, @RequestParam boolean done)
			throws MalformedURLException, URISyntaxException {
		if (!ssoUser.isAuthDone()) {
			LOGGER.debug("ssoUser.isAuthDone() is false");
			sSOTranx.init();
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath(SSOUtils.SSO_LOGIN_URL).addParameter(AppConstants.TRANX_ID_XKEY,
					AppContextUtil.getTranxId());
			return REDIRECT + builder.getURL();
		}
		return "home";
	}

}
