package com.amx.jax.postman.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.postman.IPushNotifyService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.audit.PMGaugeEvent;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.UserMessageEvent;
import com.amx.jax.rest.RestService;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.CryptoUtil;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonUtil;
import com.amx.utils.MapBuilder;
import com.amx.utils.MapBuilder.BuilderMap;

/**
 * The Class FBPushServiceImpl.
 */
@Service
public class FBPushServiceImpl implements IPushNotifyService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(FBPushServiceImpl.class);

	/** The server key. */
	@Value("${fcm.server.key}")
	String serverKey;

	/** The rest service. */
	@Autowired
	RestService restService;

	@Autowired
	SlackService slackService;

	/** The audit service client. */
	@Autowired
	AuditServiceClient auditServiceClient;

	@Autowired
	private FileService fileService;

	@Autowired
	private TunnelService tunnelService;

	/** The Constant MAIN_TOPIC. */
	private static final JsonPath MAIN_TOPIC = new JsonPath("/to");
	private static final JsonPath MAIN_CONDITION = new JsonPath("/condition");

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
	@Override
	public AmxApiResponse<PushMessage, Object> sendDirect(PushMessage msg) {
		try {

			if (msg.getTo() == null) {
				throw new PostManException(PostManException.ErrorCode.NO_RECIPIENT_DEFINED);
			}

			if (msg.getTemplate() != null) {
				File file = new File();
				file.setTemplate(msg.getTemplate());
				file.setModel(msg.getModel());
				file.setLang(msg.getLang());
				file.setType(File.Type.JSON);

				@SuppressWarnings("unchecked")
				Map<String, Object> map = JsonUtil.fromJson(fileService.create(file).getContent(), Map.class);
				msg.setModel(map);

				String message = ArgUtil.parseAsString(map.get("_message"));
				if (!ArgUtil.isEmptyString(message)) {
					msg.setMessage(message);
					map.remove("_message");
				}

				String subject = ArgUtil.parseAsString(map.get("_subject"));
				if (!ArgUtil.isEmptyString(subject)) {
					msg.setSubject(subject);
					map.remove("_subject");
				}

				String image = ArgUtil.parseAsString(map.get("_image"));
				if (!ArgUtil.isEmptyString(image)) {
					msg.setImage(image);
					map.remove("_image");
				}

				String link = ArgUtil.parseAsString(map.get("url"));
				if (!ArgUtil.isEmptyString(link)) {
					msg.setLink(link);
					// map.remove("url");
				}

			}

			UserMessageEvent userMessageEvent = new UserMessageEvent();
			userMessageEvent.setTo(msg.getTo());
			userMessageEvent.setSubject(msg.getSubject());
			userMessageEvent.setMessage(msg.getMessage());
			userMessageEvent.setImage(msg.getImage());
			userMessageEvent.setLink(msg.getLink());
			userMessageEvent.setTemplate(msg.getTemplate());

			String topic = msg.getTo().get(0);
			StringBuilder androidTopic = new StringBuilder();
			StringBuilder iosTopic = new StringBuilder();
			StringBuilder webTopic = new StringBuilder();

			if (msg.getTo().size() > 1) {
				String seperator = Constants.BLANK;
				for (String singleTopicPath : msg.getTo()) {
					String singleTopic = singleTopicPath.toLowerCase().replace(PushMessage.TOPICS_PREFIX,
							Constants.BLANK);
					androidTopic.append(seperator + "'" + singleTopic + "_and'" + " in topics ");
					iosTopic.append(seperator + "'" + singleTopic + "_ios'" + " in topics ");
					webTopic.append(seperator + "'" + singleTopic + "_web'" + " in topics ");
					seperator = PushMessage.CONDITION_SEPRATOR;
				}
				msg.setCondition(true);
			} else {
				String topicLower = topic.toLowerCase();
				androidTopic.append(topicLower + "_and");
				iosTopic.append(topicLower + "_ios");
				webTopic.append(topicLower + "_web");
			}

			if (!ArgUtil.isEmptyString(topic)) {
				if (msg.getMessage() != null) {
					this.send(PMGaugeEvent.Type.NOTIFCATION_ANDROID, androidTopic.toString(), msg, msg.getMessage());
					this.send(PMGaugeEvent.Type.NOTIFCATION_IOS, iosTopic.toString(), msg, msg.getMessage());
					this.send(PMGaugeEvent.Type.NOTIFCATION_WEB, webTopic.toString(), msg, msg.getMessage());
				}
				if (msg.getLines() != null) {
					for (String message : msg.getLines()) {
						this.send(PMGaugeEvent.Type.NOTIFCATION_ANDROID, androidTopic.toString(), msg, message);
						this.send(PMGaugeEvent.Type.NOTIFCATION_IOS, iosTopic.toString(), msg, message);
						this.send(PMGaugeEvent.Type.NOTIFCATION_WEB, webTopic.toString(), msg, message);
					}
				}
			}
			tunnelService.task(userMessageEvent);
			if (!ArgUtil.isEmpty(msg.getITemplate())
					&& !ArgUtil.isEmpty(msg.getITemplate().getChannel())) {
				Notipy noti = new Notipy();
				noti.setSubject(msg.getSubject());
				noti.setAuthor(String.format("Topic = %s", msg.getTo().get(0)));
				noti.setMessage(msg.getMessage());
				noti.setChannel(msg.getITemplate().getChannel());
				noti.addField("TEMPLATE", msg.getITemplate().toString());
				noti.setColor("#" + CryptoUtil.toHex(6, msg.getITemplate().toString()));
				slackService.sendNotification(noti);
			}

		} catch (PostManException e) {
			auditServiceClient.log(
					new PMGaugeEvent(PMGaugeEvent.Type.NOTIFCATION).set(Result.FAIL).set(msg, msg.getMessage(), null));
		} catch (Exception e) {
			auditServiceClient.excep(new PMGaugeEvent(PMGaugeEvent.Type.NOTIFCATION).set(msg, msg.getMessage(), null),
					LOGGER, e);
		}

		return AmxApiResponse.build(msg);
	}

	/**
	 * Send.
	 *
	 * @param type    the type
	 * @param topic   the topic
	 * @param msg     the msg
	 * @param message the message
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
			auditServiceClient.log(pMGaugeEvent.set(Result.DONE).set(msg, message, response));
		} catch (PostManException e) {
			auditServiceClient.log(pMGaugeEvent.set(Result.FAIL).set(msg, message, null));
		} catch (Exception e) {
			auditServiceClient.excep(pMGaugeEvent.set(msg, message, null), LOGGER, e);
		}

	}

	/**
	 * Send android.
	 *
	 * @param topic   the topic
	 * @param msg     the msg
	 * @param message the message
	 * @return the string
	 */
	private String sendAndroid(String topic, PushMessage msg, String message) {
		BuilderMap fields = MapBuilder.map()

				.put(DATA_IS_BG, true).put(DATA_TITLE, msg.getSubject()).put(DATA_MESSAGE, message)
				.put(DATA_IMAGE, msg.getImage()).put(DATA_PAYLOAD, msg.getModel())
				.put(DATA_TIMESTAMP, System.currentTimeMillis());

		fields.put(msg.isCondition() ? MAIN_CONDITION : MAIN_TOPIC, topic);

		return restService.ajax("https://fcm.googleapis.com/fcm/send").header("Authorization", "key=" + serverKey)
				.header("Content-Type", "application/json").post(fields.toMap()).asString();
	}

	/**
	 * Send IOS.
	 *
	 * @param topic   the topic
	 * @param msg     the msg
	 * @param message the message
	 * @return the string
	 */
	private String sendIOS(String topic, PushMessage msg, String message) {
		BuilderMap fields = MapBuilder.map()

				.put(DATA_IS_BG, true).put(DATA_TITLE, msg.getSubject()).put(DATA_MESSAGE, message)
				.put(DATA_IMAGE, msg.getImage()).put(DATA_PAYLOAD, msg.getModel())
				.put(DATA_TIMESTAMP, System.currentTimeMillis())

				.put(NOTFY_TITLE, msg.getSubject()).put(NOTFY_MESSAGE, message).put(NOTFY_SOUND, "default");

		fields.put(msg.isCondition() ? MAIN_CONDITION : MAIN_TOPIC, topic);

		return restService.ajax("https://fcm.googleapis.com/fcm/send").header("Authorization", "key=" + serverKey)
				.header("Content-Type", "application/json").post(fields.toMap()).asString();

	}

	/**
	 * Send web.
	 *
	 * @param topic   the topic
	 * @param msg     the msg
	 * @param message the message
	 * @return the string
	 */
	private String sendWeb(String topic, PushMessage msg, String message) {
		BuilderMap fields = MapBuilder.map()

				.put(DATA_IS_BG, true).put(DATA_TITLE, msg.getSubject()).put(DATA_MESSAGE, message)
				.put(DATA_IMAGE, msg.getImage()).put(DATA_PAYLOAD, msg.getModel())
				.put(DATA_TIMESTAMP, System.currentTimeMillis())

				.put(NOTFY_TITLE, msg.getSubject()).put(NOTFY_MESSAGE, message).put(NOTFY_SOUND, "default");

		fields.put(msg.isCondition() ? MAIN_CONDITION : MAIN_TOPIC, topic);

		return restService.ajax("https://fcm.googleapis.com/fcm/send").header("Authorization", "key=" + serverKey)
				.header("Content-Type", "application/json").post(fields.toMap()).asString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.postman.FBPushService#subscribe(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AmxApiResponse<String, Object> subscribe(String token, String topic) {
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
		return AmxApiResponse.build(token);
	}

	@Override
	public AmxApiResponse<PushMessage, Object> send(List<PushMessage> msgs) throws PostManException {
		return AmxApiResponse.buildList(msgs);
	}

}
