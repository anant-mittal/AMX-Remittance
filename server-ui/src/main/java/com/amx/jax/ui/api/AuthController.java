
package com.amx.jax.ui.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.ui.response.ResponseMessage;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.bootloaderjs.JsonUtil;

import io.swagger.annotations.Api;

@Controller
@Api(value = "Auth APIs")
public class AuthController {

	@RequestMapping(value = "/login/**", method = { RequestMethod.GET })
	public String loginJPage(Model model) {

		return "login";
	}

	@RequestMapping(value = "/login/**", method = { RequestMethod.GET, RequestMethod.POST }, headers = {
			"Accept=application/json", "Accept=application/v0+json" })
	@ResponseBody
	public String loginPJson() {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>(null);

		wrapper.setMessage(ResponseStatus.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

		return JsonUtil.toJson(wrapper);
	}

}
