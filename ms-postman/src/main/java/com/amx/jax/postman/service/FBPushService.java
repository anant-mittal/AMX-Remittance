package com.amx.jax.postman.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;
import com.amx.utils.FileUtil;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonUtil;
import com.amx.utils.MapBuilder;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

@Component
public class FBPushService {

	private static final Logger LOGGER = LoggerService.getLogger(FBPushService.class);

	@Value("${fcm.server.key}")
	String serverKey;

	@Autowired
	public FBPushService(@Value("${fcm.service.file}") String fcmServiceFile) {
		try {
			InputStream serviceAccount = FileUtil.getExternalResourceAsStream(fcmServiceFile, FBPushService.class);

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
			FirebaseApp.initializeApp(options);

		} catch (IOException e) {
			LOGGER.error("While Loading firebase key ", e);
		}
	}

	@Autowired
	RestService restService;

	private static final JsonPath MAIN_TOPIC = new JsonPath("/to");
	private static final JsonPath DATA_TITLE = new JsonPath("/data/data/title");
	private static final JsonPath DATA_IS_BG = new JsonPath("/data/data/is_background");
	private static final JsonPath DATA_MESSAGE = new JsonPath("/data/data/message");
	private static final JsonPath DATA_IMAGE = new JsonPath("/data/data/image");
	private static final JsonPath DATA_PAYLOAD = new JsonPath("/data/data/payload");
	private static final JsonPath DATA_TIMESTAMP = new JsonPath("/data/data/timestamp");

	private static final JsonPath NOTFY_TITLE = new JsonPath("/notification/title");
	private static final JsonPath NOTFY_MESSAGE = new JsonPath("/notification/body");

	public PushMessage sendDirect(PushMessage msg) throws InterruptedException, ExecutionException {
		if (msg.getTo() != null) {
			String topic = msg.getTo().get(0);

			if (!ArgUtil.isEmptyString(topic)) {
				this.sendAndroid(topic, msg);
				this.sendIOS(topic, msg);
			}

		}
		return msg;
	}

	@Async
	public void sendAndroid(String topic, PushMessage msg) throws InterruptedException, ExecutionException {
		Map<String, Object> fields = MapBuilder.map().put(MAIN_TOPIC, topic + "_and")

				.put(DATA_IS_BG, true).put(DATA_TITLE, msg.getSubject()).put(DATA_MESSAGE, msg.getMessage())
				.put(DATA_IMAGE, msg.getImage()).put(DATA_PAYLOAD, msg.getModel())
				.put(DATA_TIMESTAMP, System.currentTimeMillis()).toMap();

		LOGGER.info("Data JSON {}", JsonUtil.toJson(fields));
		LOGGER.info("Sneinfnnnnnn {}",
				restService.ajax("https://fcm.googleapis.com/fcm/send").header("Authorization", "key=" + serverKey)
						.header("Content-Type", "application/json").post(fields).asString());
	}

	@Async
	public void sendIOS(String topic, PushMessage msg) throws InterruptedException, ExecutionException {
		Map<String, Object> fields = MapBuilder.map().put(MAIN_TOPIC, topic)

				.put(DATA_IS_BG, true).put(DATA_TITLE, msg.getSubject()).put(DATA_MESSAGE, msg.getMessage())
				.put(DATA_IMAGE, msg.getImage()).put(DATA_PAYLOAD, msg.getModel())
				.put(DATA_TIMESTAMP, System.currentTimeMillis())

				.put(NOTFY_TITLE, msg.getSubject()).put(NOTFY_MESSAGE, msg.getMessage())

				.toMap();

		LOGGER.info("Notification JSON {}", JsonUtil.toJson(fields));
		LOGGER.info("Sneinfnnnnnn {}",
				restService.ajax("https://fcm.googleapis.com/fcm/send").header("Authorization", "key=" + serverKey)
						.header("Content-Type", "application/json").post(fields).asString());
	}

	public void subscribe(String token, String topic) {
		restService.ajax("https://iid.googleapis.com/iid/v1/" + token + "/rel/topics/" + topic)
				.header("Authorization", "key=" + serverKey).header("Content-Type", "application/json").post()
				.asString();
	}

}
