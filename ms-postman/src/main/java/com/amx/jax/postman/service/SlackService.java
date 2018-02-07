package com.amx.jax.postman.service;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

	public Exception sendException(String to, Exception e) {
		LOGGER.error("Exception to=" + to, e);
		try {
			StackTraceElement[] traces = e.getStackTrace();

			Map<String, Object> message = new HashMap<>();
			message.put("text", String.format("%s = %s", to, URLEncoder.encode(e.getMessage(), "UTF-8")));

			if (traces.length > 0 && traces[0].toString().length() > 0) {
				Map<String, String> attachment = new HashMap<>();

				StringBuilder tracetext = new StringBuilder();

				for (StackTraceElement trace : traces) {
					tracetext.append("\n" + trace.toString());
				}

				attachment.put("text", tracetext.toString());
				attachment.put("color", "danger");
				message.put("attachments", Collections.singletonList(attachment));
			}

			HttpResponse<String> response = Unirest.post(sendException).header("content-type", "application/json")
					.body(message).asString();

		} catch (Exception e1) {
			LOGGER.error("NestedException ", e1);
		}
		return e;
	}
}
