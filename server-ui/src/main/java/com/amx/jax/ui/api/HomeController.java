
package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.AppConstants;
import com.amx.jax.dict.Language;
import com.amx.jax.postman.PostManService;
import com.amx.jax.service.HttpService;
import com.amx.jax.ui.UIConstants;
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

@Controller
@Api(value = "Auth APIs")
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Value("${application.title}")
	private String applicationTitle;

	@Autowired
	private UserDeviceBean userDevice;

	@Autowired
	private JaxService jaxService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	HttpService httpService;

	private long checkTime = 0L;
	private String versionNew = "_";

	@Autowired
	private PostManService postManService;

	@Value("${jax.cdn.url}")
	private String cleanCDNUrl;

	public String getVersion() {
		long checkTimeNew = System.currentTimeMillis() / (1000 * 60 * 5);
		if (checkTimeNew != checkTime) {
			try {
				JSONObject map = postManService.getMap(cleanCDNUrl + "/dist/build.json?_=" + checkTimeNew);
				if (map.has("version")) {
					versionNew = ArgUtil.parseAsString(map.get("version"));
				}
				checkTime = checkTimeNew;
			} catch (Exception e) {
				LOGGER.error("getVersion Exception", e);
			}
		}
		return versionNew;
	}

	@Timed
	@RequestMapping(value = "/pub/meta/**", method = { RequestMethod.GET })
	@ResponseBody
	public String loginPing(HttpServletRequest request) {
		ResponseWrapper<ServerStatus> wrapper = new ResponseWrapper<ServerStatus>(new ServerStatus());
		Integer hits = sessionService.getGuestSession().hitCounter();
		userDevice.getType();
		wrapper.getData().hits = hits;
		wrapper.getData().domain = request.getRequestURL().toString();
		wrapper.getData().requestUri = request.getRequestURI();
		wrapper.getData().remoteAddr = httpService.getIPAddress();
		wrapper.getData().device = userDevice.toMap();
		return JsonUtil.toJson(wrapper);
	}

	@Timed
	@RequestMapping(value = "/login/**", method = { RequestMethod.GET })
	public String loginJPage(Model model) {
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", applicationTitle);
		model.addAttribute("cdnUrl", cleanCDNUrl);
		model.addAttribute(UIConstants.CDN_VERSION, getVersion());
		model.addAttribute(AppConstants.DEVICE_ID_KEY, userDevice.getFingerprint());
		return "app";
	}

	@RequestMapping(value = "/login/**", method = { RequestMethod.GET, RequestMethod.POST }, headers = {
			"Accept=application/json", "Accept=application/v0+json" })
	@ResponseBody
	public String loginPJson() {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>(null);
		wrapper.setMessage(WebResponseStatus.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
		return JsonUtil.toJson(wrapper);
	}

	@RequestMapping(value = { "/register/**", "/app/**", "/home/**", "/" }, method = { RequestMethod.GET })
	public String defaultPage(Model model) {
		model.addAttribute("lang", httpService.getLanguage());
		model.addAttribute("applicationTitle", applicationTitle);
		model.addAttribute("cdnUrl", cleanCDNUrl);
		model.addAttribute(UIConstants.CDN_VERSION, getVersion());
		model.addAttribute(AppConstants.DEVICE_ID_KEY, userDevice.getFingerprint());
		return "app";
	}

	@RequestMapping(value = { "/app/terms", "/pub/terms" }, method = { RequestMethod.GET })
	public String termsPage(Model model, @RequestParam Language lang) {
		model.addAttribute("lang", httpService.getLanguage());
		sessionService.getGuestSession().setLang(lang);
		model.addAttribute("terms", jaxService.setDefaults().getMetaClient().getTermsAndCondition().getResults());
		return "terms";
	}
}
