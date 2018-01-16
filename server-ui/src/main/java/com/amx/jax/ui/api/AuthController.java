
package com.amx.jax.ui.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.amx.jax.ui.Constants;
import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.session.UserSession;
import com.bootloaderjs.JsonUtil;

import io.swagger.annotations.Api;

@Controller
@Api(value = "Auth APIs")
public class AuthController {

	@Value("${jax.cdn.url}")
	private String cdnUrl;

	@Value("${application.title}")
	private String applicationTitle;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserSession userSession;

	public String getDeviceId() {
		if (userSession.getDeviceId() != null) {
			String deviceId = WebUtils.getCookie(request, Constants.DEVICE_ID_KEY).getValue();
			userSession.setDeviceId(deviceId);
		}
		return userSession.getDeviceId();
	}

	@RequestMapping(value = "/login/**", method = { RequestMethod.GET })
	public String loginJPage(Model model) {
		model.addAttribute("applicationTitle", applicationTitle);
		model.addAttribute("cdnUrl", cdnUrl);
		model.addAttribute(Constants.DEVICE_ID_KEY, getDeviceId());
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
		model.addAttribute(Constants.DEVICE_ID_KEY, getDeviceId());
		return "app";
	}

	String fingerprint = null;
	String fingerprintFound = null;

	@ResponseBody
	@RequestMapping(value = "/pub/track/**", method = { RequestMethod.GET })
	public String track(Model model) throws IOException {

		if (fingerprint == null) {
			StringWriter writer = new StringWriter();
			InputStream is = context.getResource("classpath:static/fingerprint.js").getInputStream();
			IOUtils.copy(is, writer);
			fingerprint = writer.toString();
		}
		return fingerprint;

		// return writer.toString().replace("{fingerprint}", fingerprint);
	}
}
