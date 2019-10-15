package com.amx.jax.branch.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amx.jax.branch.WebAppConfig;
import com.amx.jax.rest.RestService;
import com.amx.jax.sso.SSOConstants;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(value = "App Pages")
public class BranchController {

	/** The web app config. */
	@Autowired
	private WebAppConfig webAppConfig;

	/** The version new. */
	private String versionNew = "_";

	/** The check time. */
	private long checkTime = 0L;

	/** The post man service. */
	@Autowired
	private RestService restService;

	@Autowired
	SSOUser sSOUser;

	private Logger logger = Logger.getLogger(BranchController.class);

	@ApiOperation(value = "Branch Pages")
	@RequestMapping(value = { "/register/**", "/app/**", "/login/**", "/push/**" }, method = { RequestMethod.GET })
	public String defaultPage(Model model, HttpServletRequest request) {
		model.addAttribute("cdnUrl", webAppConfig.getCleanCDNUrl());
		model.addAttribute("oldBranchUrl", webAppConfig.getOldBranchUrl());
		model.addAttribute("wuLoginUrl", webAppConfig.getWuLoginUrl());
		model.addAttribute("mgLoginUrl", webAppConfig.getMgLoginUrl());
		model.addAttribute("hsLoginUrl", webAppConfig.getHsLoginUrl());
		model.addAttribute("cdnVersion", getVersion());
		model.addAttribute("applicationTitle", webAppConfig.getAppTitle());

		if (sSOUser.isAuthDone() && !sSOUser.isAMXCookieValid()) {
			return SSOConstants.REDIRECT + SSOConstants.APP_LOGOUT_URL;
		}

		return "index";
	}

	@ApiOperation(value = "Base URL")
	@RequestMapping(value = { "/" }, method = { RequestMethod.GET })
	public String baseUrl(Model model) {
		return "redirect:/app";
	}

	public String getVersion() {
		long checkTimeNew = System.currentTimeMillis() / (1000 * 60 * 5);
		if (checkTimeNew != checkTime) {
			try {
				Map<String, Object> map = restService
						.ajax(webAppConfig.getCleanCDNUrl() + "/dist/build.json?_=" + checkTimeNew).get().asMap();
				if (map.containsKey("version")) {
					versionNew = ArgUtil.parseAsString(map.get("version"));
				}
				checkTime = checkTimeNew;
			} catch (Exception e) {
				logger.error("getVersion Exception", e);
			}
		}
		return versionNew;
	}

}
