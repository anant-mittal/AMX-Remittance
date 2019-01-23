package com.amx.jax.postman.hooks;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.JsonUtil;

import io.swagger.annotations.Api;

/**
 * The Class MetaController.
 */
@RestController
@Api(value = "WhatsAppHooks")
public class WhatsAppHooks {

	private static final Logger LOGGER = LoggerService.getLogger(WhatsAppHooks.class);

	@Autowired
	AppConfig appConfig;

	@Autowired
	private TunnelService tunnelService;

	@RequestMapping(value = "/postman/webhook/apiwha/{secret}/update", method = { RequestMethod.POST })
	public AmxApiResponse<Object, Object> onAPIWHAMessage(@RequestParam(required = false) String secret,
			@RequestParam String data) {
		try {
			Map<String, Object> dataMap = JsonUtil.getMapFromJsonString(data);
			String event = ArgUtil.parseAsString(dataMap.get("event"), Constants.BLANK);
			if ("INBOX".equals(event)) {
				UserInboxEvent userInboxEvent = new UserInboxEvent();
				userInboxEvent.setFrom(ArgUtil.parseAsString(dataMap.get("from"), Constants.BLANK));
				userInboxEvent.setTo(ArgUtil.parseAsString(dataMap.get("to"), Constants.BLANK));
				userInboxEvent.setMessage(ArgUtil.parseAsString(dataMap.get("text"), Constants.BLANK));
				tunnelService.task(userInboxEvent);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return AmxApiResponse.build();
	}

}
