package com.amx.jax.worker.tasks.ping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

public class PingCommonListner implements ITunnelSubscriber<DBEvent> {

	@Autowired
	PostManClient postManClient;

	@Autowired
	PushNotifyClient pushNotifyClient;

	@Autowired
	WhatsAppClient whatsAppClient;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		String message = ArgUtil.parseAsString(event.getData().get("message"));
		String smsNo = ArgUtil.parseAsString(event.getData().get("sms"));
		String whatsappNo = ArgUtil.parseAsString(event.getData().get("whatsapp"));
		String emailId = ArgUtil.parseAsString(event.getData().get("email"));
		BigDecimal customerId = ArgUtil.parseAsBigDecimal(event.getData().get("customerId"));

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("msg", message);
		wrapper.put("data", modeldata);

		if (emailId != null) {
			Email email = new Email();
			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);
			email.setITemplate(TemplatesMX.SERVER_PING);
			email.setSubject("Subject:Server Ping");
			postManClient.sendEmailAsync(email);
		}

		if (customerId != null) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(TemplatesMX.SERVER_PING);
			pushMessage.addToUser(customerId);
			pushMessage.setModel(wrapper);
			pushNotifyClient.send(pushMessage);
		}

		if (smsNo != null) {
			SMS sms = new SMS();
			sms.setITemplate(TemplatesMX.SERVER_PING);
			sms.addTo(smsNo);
			sms.setModel(wrapper);
			postManClient.sendSMSAsync(sms);
		}

		if (whatsappNo != null) {
			WAMessage whatsapp = new WAMessage();
			whatsapp.setMessage(message);
			whatsapp.addTo(whatsappNo);
			whatsapp.setModel(wrapper);
			whatsAppClient.send(whatsapp);
		}

		Notipy slack = new Notipy();
		slack.setMessage(message);
		slack.setChannel(Channel.DEFAULT);
		postManClient.notifySlack(slack);

	}
}