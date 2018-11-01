package com.amx.jax.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.device.CardReader;

@Controller
public class SWAdapterController {

	public static final String PUB_AMX_PREFIX = "/pub/amx";
	public static final String PARAM_URL = PUB_AMX_PREFIX + "/params";

	@Autowired
	ACardReaderService kwtCardReaderService;

	@Autowired
	AdapterServiceClient adapterServiceClient;

	@ResponseBody
	@RequestMapping(value = "/pub/card/kwt/read", method = RequestMethod.GET)
	public CardReader readCard() throws InterruptedException {
		return kwtCardReaderService.read();
	}

	@ResponseBody
	@RequestMapping(value = "/pub/script/validation.js", method = RequestMethod.GET)
	public String makesession(@RequestParam String tranx) throws Exception {
		adapterServiceClient.pairTerminal(kwtCardReaderService.getAddress(),
				kwtCardReaderService.getDevicePairingCreds(), kwtCardReaderService.getSessionPairingCreds(), tranx);
		return "var _ba_ = true";
	}

}
