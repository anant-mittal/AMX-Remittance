
package com.amx.jax.ui.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.AppConstants;
import com.amx.jax.dict.Language;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rest.RestService;
import com.amx.jax.service.HttpService;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.WebAppConfig;
import com.amx.jax.ui.model.ServerStatus;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.session.UserDeviceBean;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;
import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;

/**
 * The Class HomeController.
 */
@Controller
@Api(value = "Auth APIs")
public class HomeController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(HomeController.class);

	/** The web app config. */
	@Autowired
	private WebAppConfig webAppConfig;

	/** The user device. */
	@Autowired
	private UserDeviceBean userDevice;

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/** The http service. */
	@Autowired
	HttpService httpService;

	/** The check time. */
	private long checkTime = 0L;

	/** The version new. */
	private String versionNew = "_";

	/** The post man service. */
	@Autowired
	private RestService restService;
	

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
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
				LOGGER.error("getVersion Exception", e);
			}
		}
		return versionNew;
	}

	/**
	 * Login ping.
	 *
	 * @param request
	 *            the request
	 * @return the string
	 */
	@ApiJaxStatus({ JaxError.ACCOUNT_LENGTH, JaxError.ACCOUNT_TYPE_UPDATE })
	@Timed
	@RequestMapping(value = "/pub/meta/**", method = { RequestMethod.GET })
	@ResponseBody
	public String loginPing(HttpServletRequest request) {
		ResponseWrapper<ServerStatus> wrapper = new ResponseWrapper<ServerStatus>(new ServerStatus());
		Integer hits = sessionService.getGuestSession().hitCounter();
		userDevice.getType();
		wrapper.getData().setHits(hits);
		wrapper.getData().setDomain(request.getRequestURL().toString());
		wrapper.getData().setRequestUri(request.getRequestURI());
		wrapper.getData().setRemoteAddr(httpService.getIPAddress());
		wrapper.getData().setDevice(userDevice.toMap());
		return JsonUtil.toJson(wrapper);
	}

	/**
	 * Login J page.
	 *
	 * @param model
	 *            the model
	 * @return the string
	 */
	@Timed
	@RequestMapping(value = "/login/**", method = { RequestMethod.GET })
	public String loginJPage(Model model) {
		LOGGER.debug("This is debug Statment");
		LOGGER.info("This is info Statment");
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webAppConfig.getAppTitle());
		model.addAttribute("cdnUrl", webAppConfig.getCleanCDNUrl());
		model.addAttribute(UIConstants.CDN_VERSION, getVersion());
		model.addAttribute(AppConstants.DEVICE_ID_KEY, userDevice.getFingerprint());
		model.addAttribute("fcmSenderId", webAppConfig.getFcmSenderId());
		return "app";
	}

	/**
	 * Login P json.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/login/**", method = { RequestMethod.GET, RequestMethod.POST }, headers = {
			"Accept=application/json", "Accept=application/v0+json" })
	@ResponseBody
	public String loginPJson() {
		LOGGER.debug("This is debug Statment");
		LOGGER.info("This is debug Statment");
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>(null);
		wrapper.setMessage(WebResponseStatus.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
		return JsonUtil.toJson(wrapper);
	}

	/**
	 * Default page.
	 *
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = { "/register/**", "/app/**", "/home/**", "/" }, method = { RequestMethod.GET })
	public String defaultPage(Model model) {
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", webAppConfig.getAppTitle());
		model.addAttribute("cdnUrl", webAppConfig.getCleanCDNUrl());
		model.addAttribute(UIConstants.CDN_VERSION, getVersion());
		model.addAttribute(AppConstants.DEVICE_ID_KEY, userDevice.getFingerprint());
		model.addAttribute("fcmSenderId", webAppConfig.getFcmSenderId());
		return "app";
	}

	/**
	 * Terms page.
	 *
	 * @param model
	 *            the model
	 * @param lang
	 *            the lang
	 * @return the string
	 */
	@RequestMapping(value = { "/app/terms", "/pub/terms" }, method = { RequestMethod.GET })
	public String termsPage(Model model, @RequestParam Language lang) {
		model.addAttribute("lang", httpService.getLanguage());
		sessionService.getGuestSession().setLanguage(lang);
		model.addAttribute("terms", jaxService.setDefaults().getMetaClient().getTermsAndCondition().getResults());
		return "terms";
	}

	@RequestMapping(value = { "/apple-app-site-association" }, method = {
			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
	public String applejson(Model model) {
		model.addAttribute("appid", webAppConfig.getIosAppId());
		return "json/apple-app-site-association";
	}
}
