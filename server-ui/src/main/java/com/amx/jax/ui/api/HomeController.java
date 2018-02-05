
package com.amx.jax.ui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.ui.Constants;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.session.UserDevice;
import com.bootloaderjs.JsonUtil;

import io.swagger.annotations.Api;

@Controller
@Api(value = "Auth APIs")
public class HomeController {

	@Value("${jax.cdn.url}")
	private String cdnUrl;

	@Value("${application.title}")
	private String applicationTitle;

	@Autowired
	private UserDevice userDevice;

	@RequestMapping(value = "/login/**", method = { RequestMethod.GET })
	public String loginJPage(Model model) {
		model.addAttribute("applicationTitle", applicationTitle);
		model.addAttribute("cdnUrl", cdnUrl);
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
		model.addAttribute("cdnUrl", cdnUrl);
		model.addAttribute(Constants.DEVICE_ID_KEY, userDevice.getDeviceId());
		return "app";
	}
}
