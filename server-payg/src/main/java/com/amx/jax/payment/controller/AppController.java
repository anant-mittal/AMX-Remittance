package com.amx.jax.payment.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;

import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGSession;

/**
 * @author Viki Sangani 13-Dec-2017 Appcontroller.java
 */
@Controller
public class AppController {

	private static final Logger LOGGER = Logger.getLogger(AppController.class);

	@Autowired
	private PayGSession payGSession;

	@Autowired
	PayGConfig payGConfig;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {

		if (!payGConfig.isTestEnabled()) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}
		return "thymeleaf/index";
	}

	@RequestMapping(value = "callback/{page}", method = RequestMethod.GET)
	public String success(Model model, @PathVariable("page") String page) {

		model.addAttribute("page", page);
		model.addAttribute("callback", payGSession.getCallback());
		/*
		 * if (payGSession.getCallback() != null &&
		 * !Constants.defaultString.equals(payGSession.getCallback())) { return
		 * "redirect:" + payGSession.getCallback(); }
		 */
		LOGGER.info("payGSession getCallback ------->" + payGSession.getCallback());
		if (payGSession.getCallback() != null) {
			return "redirect:" + payGSession.getCallback();
		}
		return "thymeleaf/pg_response";
	}

}
