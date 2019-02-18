package com.amx.jax.bot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.radar.snap.SnapModels.MapModel;
import com.amx.jax.radar.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.radar.snap.SnapQueryService;
import com.amx.jax.radar.snap.SnapQueryTemplate;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

@TunnelEventMapping(byEvent = UserInboxEvent.class, scheme = TunnelEventXchange.TASK_WORKER)
public class InBoxListener implements ITunnelSubscriber<UserInboxEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InBoxListener.class);
	private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

	@Autowired
	WhatsAppClient whatsAppClient;

	@Autowired
	private SnapQueryService snapQueryService;

	@Override
	public void onMessage(String channel, UserInboxEvent event) {
		if (!ArgUtil.isEmpty(event.getWaChannel())) {
			try {
				PhoneNumber swissNumberProto = phoneUtil.parse("+" + event.getFrom(), "IN");

				Map<String, Object> query = new HashMap<String, Object>();
				query.put("gte", "now-20y");
				query.put("lte", "now");
				query.put("searchKey", "customer.mobile");
				query.put("searchValue", swissNumberProto.getNationalNumber());

				LOGGER.info("Recieved {} {} ", swissNumberProto.getNationalNumber(), event.getMessage());
				String message = "";
				SnapModelWrapper x = snapQueryService.execute(SnapQueryTemplate.CUSTOMERS_PROFILE, query);
				if (x.getHits().getTotal() > 0) {
					MapModel customer = x.getHits().getHits().get(0).getSource().getMap("customer");
					message = String.format("Hello %s, You account is linked with this whatsapp account, thanx.",
							customer.getString("name"));
				} else {
					message = "We cannot find any account linked with this number please visit nearest branch";
				}
				WAMessage reply = new WAMessage();
				reply.setQueue(event.getQueue());
				reply.setChannel(event.getWaChannel());
				reply.addTo(event.getFrom());
				reply.setMessage(message);
				whatsAppClient.send(reply);
			} catch (IOException | NumberParseException e) {
				LOGGER.error("BOT EXCEPTION", e);
			}

		}
	}

}
