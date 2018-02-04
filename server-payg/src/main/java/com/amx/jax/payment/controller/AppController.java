/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amx.jax.payment.gateway.PayGSession;
import com.bootloaderjs.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Viki Sangani 13-Dec-2017 Appcontroller.java
 */
@Controller
@Api(value = "App APIs")
public class AppController {

	private Logger log = Logger.getLogger(AppController.class);

	@Autowired
	private PayGSession payGSession;

	@ApiOperation(value = "Index page")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		return "thymeleaf/index";
	}

	@RequestMapping(value = "callback/{page}", method = RequestMethod.GET)
	public String success(Model model, @PathVariable("page") String page) {

		model.addAttribute("page", page);
		model.addAttribute("callback", payGSession.getCallback());
		if (payGSession.getCallback() != null && !Constants.defaultString.equals(payGSession.getCallback())) {
			return "redirect:" + payGSession.getCallback();
		}

		return "thymeleaf/pg_response";
	}

}
