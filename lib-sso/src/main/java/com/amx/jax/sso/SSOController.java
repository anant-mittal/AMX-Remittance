
package com.amx.jax.sso;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import com.amx.utils.URLBuilder;

@Controller
public class SSOController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSOController.class);

	@Autowired
	HttpService httpService;

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

		if (auth == SSOAuth.DONE) {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(tranxId, sotp);
			token.setDetails(new WebAuthenticationDetails(request));
			SecurityContextHolder.getContext().setAuthentication(token);
			ssoUser.setAuthDone(true);
		}

		if (!ssoUser.isAuthDone()) {
			sSOTranx.setLandingUrl(request.getRequestURL().toString());
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath(SSOUtils.SSO_LOGIN_URL).addParameter(AppConstants.TRANX_ID_XKEY, tranxId);
			// return "redirect:" + builder.getURL();
		}
		return "home";
	}

	@RequestMapping(value = SSOUtils.LOGGEDIN_URL, method = { RequestMethod.GET })
	public String loggedinJPage(Model model, @RequestParam boolean done)
			throws MalformedURLException, URISyntaxException {
		if (!ssoUser.isAuthDone()) {
			sSOTranx.init();
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath(SSOUtils.SSO_LOGIN_URL).addParameter(AppConstants.TRANX_ID_XKEY,
					AppContextUtil.getTranxId());
			return "redirect:" + builder.getURL();
		}
		return "home";
	}

}
