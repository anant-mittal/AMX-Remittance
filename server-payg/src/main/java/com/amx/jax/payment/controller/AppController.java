package com.amx.jax.payment.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import com.amx.jax.payg.PayGParams;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGSession;
import com.amx.jax.payment.service.LocalClient;
import com.amx.jax.payment.service.LocalClient.LocalPipe;
import com.amx.utils.CryptoUtil;

/**
 * @author Viki Sangani 13-Dec-2017 Appcontroller.java
 */
@Controller
public class AppController implements ErrorController {

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

	@RequestMapping(value = LocalClient.LOCAL_PAYG, method = RequestMethod.GET)
	public String localpg(Model model, @RequestParam String piped,
			@RequestParam String paramsd) {

		if (!payGConfig.isTestEnabled()) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
		}

		LocalPipe pipe = new CryptoUtil.Encoder().message(piped).decodeBase64().decrypt().toObzect(LocalPipe.class);
		PayGParams params = new CryptoUtil.Encoder().message(paramsd).decodeBase64().decrypt()
				.toObzect(PayGParams.class);
		model.addAttribute("pipe", pipe);
		model.addAttribute("params", params);

		return "thymeleaf/localpg";
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

	@RequestMapping("/error")
	public String handleError() {
		return "thymeleaf/pg_error";
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
