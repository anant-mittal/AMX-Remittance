package com.amx.jax.postman.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.postman.FBPushService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.rest.RestService;

@Component
public class FBPushClient implements FBPushService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FBPushClient.class);
	
	@Autowired
	RestService restService;
	
	@Autowired
	AppConfig appConfig;

	@Override
	public PushMessage sendDirect(PushMessage msg) throws PostManException{
		LOGGER.info("Sending Push Notifications");
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.NOTIFY_PUSH)
					.post(new HttpEntity<PushMessage>(msg))
					.as(PushMessage.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}
		
	}

	@Override
	public void subscribe(String token, String topic) throws PostManException {
		LOGGER.info("Subscribing for Push Notifications on web");
		try {
			restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.NOTIFY_PUSH_SUBSCRIBE)
					.queryParam(PARAM_TOKEN, token)
					.pathParam(PARAM_TOPIC, topic)
					.post().asString();
		} catch (Exception e) {
			throw new PostManException(e);
		}
		
		
	}

}
