package com.amx.jax.worker.listner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.event.Event;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEvent(topic = AmxTunnelEvents.Names.PING_MULTIPLE)
public class PingMultiListner implements ITunnelSubscriber<Event> {

	@Autowired
	PostManClient postManClient;

	@Autowired
	PushNotifyClient pushNotifyClient;

	@Autowired
	WhatsAppClient whatsAppClient;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(String channel, Event event) {
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

		Email email = new Email();
		email.setModel(wrapper);
		email.addTo(emailId);
		email.setHtml(true);
		email.setTemplate(Templates.SERVER_PING);
		email.setSubject("Subject:Server Ping");
		postManClient.sendEmailAsync(email);

		PushMessage pushMessage = new PushMessage();
		pushMessage.setTemplate(Templates.SERVER_PING_JSON);
		pushMessage.addToUser(customerId);
		pushMessage.setModel(wrapper);
		pushNotifyClient.send(pushMessage);

		SMS sms = new SMS();
		sms.setTemplate(Templates.SERVER_PING);
		sms.addTo(smsNo);
		sms.setModel(wrapper);
		postManClient.sendSMSAsync(sms);

		WAMessage whatsapp = new WAMessage();
		whatsapp.setMessage(message);
		whatsapp.addTo(whatsappNo);
		whatsapp.setModel(wrapper);
		whatsAppClient.send(whatsapp);

	}
}