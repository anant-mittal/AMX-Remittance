package com.amx.jax.postman;

import java.util.List;

import com.amx.jax.postman.model.PushMessage;

public interface IPushNotifyService {
	
	public static final String PARAM_TOKEN = "token";
	
	public static final String PARAM_TOPIC = "topic";
	
	public PushMessage sendDirect(PushMessage msg) throws PostManException;
	
	public PostManResponse send(List<PushMessage> msgs) throws PostManException;

	public PostManResponse subscribe(String token, String topic) throws PostManException;
	
}
