package com.amx.jax.postman.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.model.SMS;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class SMService {

	private Logger logger = Logger.getLogger(SMService.class);

	@Value("${msg91.auth.key}")
	private String authKey;
	@Value("${msg91.remote.url}")
	private String remoteUrl;
	@Value("${msg91.sender.id}")
	private String senderId;
	@Value("${msg91.route}")
	private String route;

	public void sendSMS(SMS sms) throws UnirestException {

		HttpResponse<String> response = Unirest.post(remoteUrl).header("authkey", authKey)
				.header("content-type", "application/json")
				.body(String.format(
						"{ \"sender\": \"%s\", \"route\": \"%s\", \"country\": \"91\", \"sms\": [ { \"message\": \"%s\", \"to\": [ \"%s\" ] } ] }",
						senderId, route, sms.getText(), sms.getTo().get(0)))
				.asString();
		logger.info("SMS Sent   " + response.getBody());
	}
}
