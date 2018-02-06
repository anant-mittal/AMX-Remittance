
package com.amx.jax.ui.api;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.postman.PostManService;
import com.amx.jax.ui.Constants;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.session.UserDevice;
import com.bootloaderjs.ArgUtil;
import com.bootloaderjs.JsonUtil;

import io.swagger.annotations.Api;

@Controller
@Api(value = "Auth APIs")
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Value("${application.title}")
	private String applicationTitle;

	@Autowired
	private UserDevice userDevice;

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
					versionNew = ArgUtil.parseAsString(map.getLong("version"));
				}
				checkTime = checkTimeNew;
			} catch (Exception e) {
				LOGGER.error("getVersion Exception", e);
			}
		}
		return versionNew;
	}

	@RequestMapping(value = "/login/**", method = { RequestMethod.GET })
	public String loginJPage(Model model) {
		model.addAttribute("applicationTitle", applicationTitle);
		model.addAttribute("cdnUrl", cleanCDNUrl);
		model.addAttribute(Constants.CDN_VERSION, getVersion());
		model.addAttribute(Constants.DEVICE_ID_KEY, userDevice.getDeviceId());
		return "app";
	}

	@RequestMapping(value = "/login/**", method = { RequestMethod.GET, RequestMethod.POST }, headers = {
			"Accept=application/json", "Accept=application/v0+json" })
	@ResponseBody
	public String loginPJson() {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>(null);
		wrapper.setMessage(ResponseStatus.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
		return JsonUtil.toJson(wrapper);
	}

	@RequestMapping(value = { "/register/**", "/app/**", "/home/**", "/" }, method = { RequestMethod.GET })
	public String defaultPage(Model model) {
		model.addAttribute("applicationTitle", applicationTitle);
		model.addAttribute("cdnUrl", cleanCDNUrl);
		model.addAttribute(Constants.CDN_VERSION, getVersion());
		model.addAttribute(Constants.DEVICE_ID_KEY, userDevice.getDeviceId());
		return "app";
	}
}
