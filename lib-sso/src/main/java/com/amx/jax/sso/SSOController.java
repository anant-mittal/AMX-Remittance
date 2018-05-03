
package com.amx.jax.sso;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping(value = "/sso/login/**", method = { RequestMethod.GET })
	public String loginJPage(Model model) throws MalformedURLException, URISyntaxException {
		if (!ssoUser.isAuthDone()) {
			sSOTranx.init();
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath("sso/auth/login").addParameter(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId());
			return "redirect:" + builder.getURL();
		}
		return "home";
	}

	@RequestMapping(value = "/sso/loggedin/**", method = { RequestMethod.GET })
	public String loggedinJPage(Model model) throws MalformedURLException, URISyntaxException {
		if (!ssoUser.isAuthDone()) {
			sSOTranx.init();
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath("sso/auth/login").addParameter(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId());
			return "redirect:" + builder.getURL();
		}
		return "home";
	}

}
