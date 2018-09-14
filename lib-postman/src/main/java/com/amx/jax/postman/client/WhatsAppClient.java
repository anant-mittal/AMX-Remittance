package com.amx.jax.postman.client;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManResponse;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.rest.RestService;

@Component
public class WhatsAppClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(WhatsAppClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	/**
	 * This is convenient method for {@link WhatsAppClient#send(List)}
	 * 
	 * @param msg
	 * @return
	 * @throws PostManException
	 */
	public PostManResponse send(WAMessage msg) throws PostManException {
		return this.send(Arrays.asList(msg));
	}

	public PostManResponse send(List<WAMessage> msgs) throws PostManException {
		LOGGER.info("Sending WAMessage Notifications");
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.WHATS_APP_SEND).post(msgs)
					.as(PostManResponse.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

}
