package com.amx.jax.postman.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.model.Message;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class SlackService {

	private Logger logger = Logger.getLogger(SlackService.class);

	@Value("${msg91.remote.url}")
	private String remoteUrl;
	@Value("${msg91.sender.id}")
	private String senderId;

	public Message sendNotification(Message msg) throws UnirestException {

		HttpResponse<String> response = Unirest
				.post("https://hooks.slack.com/services/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")
				.header("content-type", "application/json").body(String.format("{\"text\": \"%s\"}", msg.getMessage()))
				.asString();
		logger.info("Slack Sent   " + response.getBody());
		return msg;
	}

	public Message sendNotification(Exception e) {
		logger.error("Exception ", e);
		try {
			HttpResponse<String> response = Unirest
					.post("https://hooks.slack.com/services/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")
					.header("content-type", "application/json")
					.body(String.format("{\"text\": \"%s\"}", URLEncoder.encode(e.getMessage(), "UTF-8"))).asString();
		} catch (Exception e1) {
			logger.error("NestedException ", e1);
		}
		return null;
	}
}
