package com.amx.jax.postman.service;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.postman.model.Notipy;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
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

	@Autowired
	AppConfig appConfig;

	public Notipy sendNotification(Notipy msg) throws UnirestException {

		Map<String, Object> message = new HashMap<>();
		message.put("text", msg.getMessage());
		message.put("channel", msg.getChannel().getCode());

		if (msg.getLines().size() > 0 && msg.getLines().get(0).toString().length() > 0) {
			Map<String, String> attachment = new HashMap<>();

			StringBuilder tracetext = new StringBuilder();

			for (String line : msg.getLines()) {
				tracetext.append("\n" + line);
			}

			attachment.put("text", tracetext.toString());
			attachment.put("color", msg.getColor().getCode());
			message.put("attachments", Collections.singletonList(attachment));
		}

		HttpResponse<String> response = Unirest.post("https://slack.com/api/chat.postMessage")
				.header("Authorization",
						"Bearer xoxp-253198866083-252757085313-290617557616-ba4ac4b1a235baae2fe2ac930213d171")
				.header("content-type", "application/json").body(message).asString();

		LOGGER.info("Slack Sent", response.getBody());

		return msg;
	}

	public Exception sendException(String to, Exception e) {

		if (appConfig.isDebug()) {
			LOGGER.error("Slack-Notify-Exception ", e);
			return e;
		}

		AppContext context = AppContextUtil.getContext();
		try {
			StackTraceElement[] traces = e.getStackTrace();

			Map<String, Object> message = new HashMap<>();
			message.put("text", to);
			List<Map<String, String>> attachments = new LinkedList<Map<String, String>>();

			Map<String, String> attachmentTrace = new HashMap<>();
			attachmentTrace.put("text", String.format("TraceId = %s-%s \n Tranx = %s", context.getTenant(),
					context.getTraceId(), context.getTranxId()));
			attachmentTrace.put("color", "danger");
			attachments.add(attachmentTrace);

			Map<String, String> attachmentTitle = new HashMap<>();
			attachmentTitle.put("text",
					URLEncoder.encode(ArgUtil.parseAsString(e.getMessage(), Constants.BLANK), "UTF-8"));
			attachmentTitle.put("color", "danger");
			attachments.add(attachmentTitle);

			if (traces.length > 0 && traces[0].toString().length() > 0) {
				Map<String, String> attachment = new HashMap<>();

				StringBuilder tracetext = new StringBuilder();

				for (StackTraceElement trace : traces) {
					tracetext.append("\n" + trace.toString());
				}

				attachment.put("text", tracetext.toString());
				attachment.put("color", "danger");
				// message.put("attachments", Collections.singletonList(attachment));
				attachments.add(attachment);
			}

			message.put("attachments", attachments);

			HttpResponse<String> response = Unirest.post(sendException).header("content-type", "application/json")
					.body(message).asString();

		} catch (Exception e1) {
			LOGGER.error("NestedException ", e1);
		}
		return e;
	}
}
