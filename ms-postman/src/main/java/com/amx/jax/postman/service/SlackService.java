package com.amx.jax.postman.service;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.model.Message;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class SlackService {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${msg91.remote.url}")
	private String remoteUrl;
	@Value("${msg91.sender.id}")
	private String senderId;

	@Value("${slack.send.notify}")
	private String sendNotificationApi;

	@Value("${slack.send.exception}")
	private String sendException;

	public Message sendNotification(Message msg) throws UnirestException {

		HttpResponse<String> response = Unirest.post(sendNotificationApi).header("content-type", "application/json")
				.body(String.format("{\"text\": \"%s\"}", msg.getMessage())).asString();
		LOGGER.info("Slack Sent", response.getBody());
		return msg;
	}

	public Message sendNotification(String to, Exception e) {
		LOGGER.error("Exception to=" + to, e);
		try {
			HttpResponse<String> response = Unirest.post(sendException).header("content-type", "application/json")
					.body(String.format("{\"text\": \"%s = %s\"}", to, URLEncoder.encode(e.getMessage(), "UTF-8")))
					.asString();
		} catch (Exception e1) {
			LOGGER.error("NestedException ", e1);
		}
		return null;
	}
}
