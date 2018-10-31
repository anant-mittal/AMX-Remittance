package com.amx.jax.adapter.kwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.device.CardReader;

@RestController
public class KWTCardController {

	public static final String PUB_AMX_PREFIX = "/pub/amx";
	public static final String PARAM_URL = PUB_AMX_PREFIX + "/params";

	@Autowired
	KWTCardReaderService kwtCardReaderService;

	@RequestMapping(value = "/pub/card/kwt/read", method = RequestMethod.GET)
	public CardReader readCard() throws InterruptedException {
		return kwtCardReaderService.read();
	}

	@RequestMapping(value = "/pub/script/validation.js", method = RequestMethod.GET)
	public String makesession(@RequestParam String tranx) throws InterruptedException {
		return "var _ba_ = true";
	}

}
