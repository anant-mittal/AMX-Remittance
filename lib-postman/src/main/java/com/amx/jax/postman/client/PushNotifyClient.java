package com.amx.jax.postman.client;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.postman.IPushNotifyService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.rest.RestService;

@Component
public class PushNotifyClient implements IPushNotifyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PushNotifyClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<PushMessage, Object> sendDirect(PushMessage msg) throws PostManException {
		LOGGER.info("Sending Push Notifications");
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.NOTIFY_PUSH).post(msg)
					.as(new ParameterizedTypeReference<AmxApiResponse<PushMessage, Object>>() {
					});
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	/**
	 * This is convenient method for {@link PushNotifyClient#send(List)}
	 * 
	 * @param msg
	 * @return
	 * @throws PostManException
	 */
	public AmxApiResponse<PushMessage, Object> send(PushMessage msg) throws PostManException {
		return this.send(Arrays.asList(msg));
	}

	@Override
	public AmxApiResponse<PushMessage, Object> send(List<PushMessage> msgs) throws PostManException {
		LOGGER.info("Sending Push Notifications");
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.NOTIFY_PUSH_BULK).post(msgs)
					.asApiResponse(PushMessage.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	@Override
	public AmxApiResponse<String, Object> subscribe(String token, String topic) throws PostManException {
		LOGGER.info("Subscribing for Push Notifications on web");
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.NOTIFY_PUSH_SUBSCRIBE)
					.queryParam(PARAM_TOKEN, token).pathParam(PARAM_TOPIC, topic).post().asApiResponse(String.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}

	}

	@Override
	public String shortLink(String relativeUrl) throws PostManException {
		LOGGER.info("Subscribing for Push Notifications on web" + appConfig.getPostmapURL());
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.SHORT_LINK)
					.queryParam(PARAM_RELATIVE_URL, relativeUrl).post().asString();
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

}
