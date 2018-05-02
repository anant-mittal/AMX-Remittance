
package com.amx.jax.sso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.service.HttpService;

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
	public String loginJPage(Model model) {
		if (!ssoUser.isAuthDone()) {
			sSOTranx.init();
			return String.format("redirect:%s/sso/auth/%s", appConfig.getSsoURL(), AppContextUtil.getTranxId());
		}
		return "home";
	}

}
