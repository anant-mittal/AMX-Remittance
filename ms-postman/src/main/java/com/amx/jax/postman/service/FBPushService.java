package com.amx.jax.postman.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.model.PushMessage;
import com.amx.utils.FileUtil;
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

	public PushMessage send(PushMessage msg) throws InterruptedException, ExecutionException {
		if (msg.getTo() != null) {
			String to = msg.getTo().get(0);
			Map<String, String> data = new HashMap<String, String>();

			data.put("message", msg.getMessage());

			Message message = Message.builder().putAllData(data).setTopic(to)
					.setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
							.setNotification(new WebpushNotification("Background Title (server)",
									"Background Body (server)", "mail2.png"))
							.build())
					.build();
			String response = FirebaseMessaging.getInstance().sendAsync(message).get();
			LOGGER.info("Sent message: " + response);
		}
		return msg;
	}
}
