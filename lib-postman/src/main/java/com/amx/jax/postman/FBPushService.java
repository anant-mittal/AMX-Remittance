package com.amx.jax.postman;

import com.amx.jax.postman.model.PushMessage;

public interface FBPushService {
	
	public static final String PARAM_TOKEN = "token";
	
	public static final String PARAM_TOPIC = "topic";
	
	public PushMessage sendDirect(PushMessage msg) throws PostManException;
	
	public void subscribe(String token, String topic) throws PostManException;
	
}
