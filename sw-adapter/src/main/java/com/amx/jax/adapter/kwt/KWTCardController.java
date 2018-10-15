package com.amx.jax.adapter.kwt;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KWTCardController {

	public static final String PUB_AMX_PREFIX = "/pub/amx";
	public static final String PARAM_URL = PUB_AMX_PREFIX + "/params";

	@RequestMapping(value = "/pub/card/kwt/read", method = RequestMethod.GET)
	public KWTCardDetails readCard() throws InterruptedException {
		KWTCardReader.start();
		return KWTCardReader.poll();
	}

}
