package com.amx.jax.postman.api;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.service.FBPushServiceImpl;

@RestController
public class FBPushController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FBPushController.class);

	@Autowired
	FBPushServiceImpl fBPushService;

	@RequestMapping(value = PostManUrls.NOTIFY_PUSH, method = RequestMethod.POST)
	public PushMessage fbPush(@RequestBody PushMessage msg)
			throws PostManException, InterruptedException, ExecutionException {
		fBPushService.sendDirect(msg);
		return msg;
	}

	@RequestMapping(value = PostManUrls.NOTIFY_PUSH_SUBSCRIBE, method = RequestMethod.POST)
	public String fbPush(@RequestParam String token, @PathVariable String topic)
			throws PostManException, InterruptedException, ExecutionException {
		fBPushService.subscribe(token, topic);
		return topic;
	}
	
}
