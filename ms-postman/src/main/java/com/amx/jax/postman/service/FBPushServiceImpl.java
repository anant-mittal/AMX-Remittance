package com.amx.jax.postman.service;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.postman.FBPushService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.audit.PMGaugeEvent;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonPath;
import com.amx.utils.MapBuilder;

/**
 * The Class FBPushServiceImpl.
 */
@Service
public class FBPushServiceImpl implements FBPushService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(FBPushService.class);

	/** The server key. */
	@Value("${fcm.server.key}")
	String serverKey;

	// @Autowired
	// public FBPushServiceImpl(@Value("${fcm.service.file}") String fcmServiceFile)
	// {
	// try {
	// InputStream serviceAccount =
	// FileUtil.getExternalResourceAsStream(fcmServiceFile, FBPushService.class);
	//
	// FirebaseOptions options = new FirebaseOptions.Builder()
	// .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
	// FirebaseApp.initializeApp(options);
	//
	// } catch (Exception e) {
	// LOGGER.error("While Loading firebase key ", e);
	// }
	// }

	/** The rest service. */
	@Autowired
	RestService restService;

	@Autowired
	SlackService slackService;

	/** The audit service client. */
	@Autowired
	AuditServiceClient auditServiceClient;

	/** The Constant MAIN_TOPIC. */
	private static final JsonPath MAIN_TOPIC = new JsonPath("/to");

	/** The Constant DATA_TITLE. */
	private static final JsonPath DATA_TITLE = new JsonPath("/data/data/title");

	/** The Constant DATA_IS_BG. */
	private static final JsonPath DATA_IS_BG = new JsonPath("/data/data/is_background");

	/** The Constant DATA_MESSAGE. */
	private static final JsonPath DATA_MESSAGE = new JsonPath("/data/data/message");

	/** The Constant DATA_IMAGE. */
	private static final JsonPath DATA_IMAGE = new JsonPath("/data/data/image");

	/** The Constant DATA_PAYLOAD. */
	private static final JsonPath DATA_PAYLOAD = new JsonPath("/data/data/payload");

	/** The Constant DATA_TIMESTAMP. */
	private static final JsonPath DATA_TIMESTAMP = new JsonPath("/data/data/timestamp");

	/** The Constant NOTFY_TITLE. */
	private static final JsonPath NOTFY_TITLE = new JsonPath("/notification/title");

	/** The Constant NOTFY_SOUND. */
	private static final JsonPath NOTFY_SOUND = new JsonPath("/notification/sound");

	/** The Constant NOTFY_MESSAGE. */
	private static final JsonPath NOTFY_MESSAGE = new JsonPath("/notification/body");

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.postman.FBPushService#sendDirect(com.amx.jax.postman.model.
	 * PushMessage)
	 */
	@Async
	public PushMessage sendDirect(PushMessage msg) {
		LOGGER.info("Inside message");
		if (msg.getTo() != null) {
			String topic = msg.getTo().get(0);

			if (!ArgUtil.isEmptyString(topic)) {
				String topicLower = topic.toLowerCase();
				if (msg.getMessage() != null) {
					this.send(PMGaugeEvent.Type.NOTIFCATION_ANDROID, topicLower, msg, msg.getMessage());
					this.send(PMGaugeEvent.Type.NOTIFCATION_IOS, topicLower, msg, msg.getMessage());
					this.send(PMGaugeEvent.Type.NOTIFCATION_WEB, topicLower, msg, msg.getMessage());
				}
				if (msg.getLines() != null) {
					for (String message : msg.getLines()) {
						this.send(PMGaugeEvent.Type.NOTIFCATION_ANDROID, topicLower, msg, message);
						this.send(PMGaugeEvent.Type.NOTIFCATION_IOS, topicLower, msg, message);
						this.send(PMGaugeEvent.Type.NOTIFCATION_WEB, topicLower, msg, message);
					}
				}
			}
		}

		return msg;
	}

	/**
	 * Send.
	 *
	 * @param type
	 *            the type
	 * @param topic
	 *            the topic
	 * @param msg
	 *            the msg
	 * @param message
	 *            the message
	 */
	@Async
	private void send(PMGaugeEvent.Type type, String topic, PushMessage msg, String message) {
		PMGaugeEvent pMGaugeEvent = new PMGaugeEvent(type);
		try {
			String response = null;
			if (type == PMGaugeEvent.Type.NOTIFCATION_ANDROID) {
				response = this.sendAndroid(topic, msg, message);
			} else if (type == PMGaugeEvent.Type.NOTIFCATION_IOS) {
				response = this.sendIOS(topic, msg, message);
			} else if (type == PMGaugeEvent.Type.NOTIFCATION_WEB) {
				response = this.sendWeb(topic, msg, message);
			} else {
				throw new PostManException(PostManException.ErrorCode.NO_CHANNEL_DEFINED);
			}
			auditServiceClient.gauge(pMGaugeEvent.set(Result.DONE).set(msg, message, response));
		} catch (PostManException e) {
			auditServiceClient.gauge(pMGaugeEvent.set(Result.FAIL).set(msg, message, null));
		} catch (Exception e) {
			auditServiceClient.excep(pMGaugeEvent.set(msg, message, null), LOGGER, e);
		}

	}

	/**
	 * Send android.
	 *
	 * @param topic
	 *            the topic
	 * @param msg
	 *            the msg
	 * @param message
	 *            the message
	 * @return the string
	 */
	private String sendAndroid(String topic, PushMessage msg, String message) {
		Map<String, Object> fields = MapBuilder.map().put(MAIN_TOPIC, topic + "_and")

				.put(DATA_IS_BG, true).put(DATA_TITLE, msg.getSubject()).put(DATA_MESSAGE, message)
				.put(DATA_IMAGE, msg.getImage()).put(DATA_PAYLOAD, msg.getModel())
				.put(DATA_TIMESTAMP, System.currentTimeMillis()).toMap();

		return restService.ajax("https://fcm.googleapis.com/fcm/send").header("Authorization", "key=" + serverKey)
				.header("Content-Type", "application/json").post(fields).asString();
	}

	/**
	 * Send IOS.
	 *
	 * @param topic
	 *            the topic
	 * @param msg
	 *            the msg
	 * @param message
	 *            the message
	 * @return the string
	 */
	private String sendIOS(String topic, PushMessage msg, String message) {
		Map<String, Object> fields = MapBuilder.map().put(MAIN_TOPIC, topic + "_ios")

				.put(DATA_IS_BG, true).put(DATA_TITLE, msg.getSubject()).put(DATA_MESSAGE, message)
				.put(DATA_IMAGE, msg.getImage()).put(DATA_PAYLOAD, msg.getModel())
				.put(DATA_TIMESTAMP, System.currentTimeMillis())

				.put(NOTFY_TITLE, msg.getSubject()).put(NOTFY_MESSAGE, message).put(NOTFY_SOUND, "default")

				.toMap();

		return restService.ajax("https://fcm.googleapis.com/fcm/send").header("Authorization", "key=" + serverKey)
				.header("Content-Type", "application/json").post(fields).asString();

	}

	/**
	 * Send web.
	 *
	 * @param topic
	 *            the topic
	 * @param msg
	 *            the msg
	 * @param message
	 *            the message
	 * @return the string
	 */
	private String sendWeb(String topic, PushMessage msg, String message) {
		Map<String, Object> fields = MapBuilder.map().put(MAIN_TOPIC, topic + "_web")

				.put(DATA_IS_BG, true).put(DATA_TITLE, msg.getSubject()).put(DATA_MESSAGE, message)
				.put(DATA_IMAGE, msg.getImage()).put(DATA_PAYLOAD, msg.getModel())
				.put(DATA_TIMESTAMP, System.currentTimeMillis())

				.put(NOTFY_TITLE, msg.getSubject()).put(NOTFY_MESSAGE, message).put(NOTFY_SOUND, "default")

				.toMap();

		return restService.ajax("https://fcm.googleapis.com/fcm/send").header("Authorization", "key=" + serverKey)
				.header("Content-Type", "application/json").post(fields).asString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.postman.FBPushService#subscribe(java.lang.String,
	 * java.lang.String)
	 */
	public void subscribe(String token, String topic) {
		PMGaugeEvent pMGaugeEvent = new PMGaugeEvent();
		pMGaugeEvent.setType(PMGaugeEvent.Type.NOTIFCATION_SUBSCRIPTION);
		try {
			String response = restService.ajax("https://iid.googleapis.com/iid/v1/" + token + "/rel/topics/" + topic)
					.header("Authorization", "key=" + serverKey).header("Content-Type", "application/json").post()
					.asString();
			pMGaugeEvent.setResponseText(response);
			auditServiceClient.gauge(pMGaugeEvent);
		} catch (Exception e) {
			auditServiceClient.excep(pMGaugeEvent, LOGGER, e);
			slackService.sendException(topic, e);
		}
	}

}
