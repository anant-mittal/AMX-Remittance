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

@TunnelEventMapping(byEvent = UserInboxEvent.class, scheme = TunnelEventXchange.TASK_WORKER)
public class InBoxListener implements ITunnelSubscriber<UserInboxEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InBoxListener.class);

	@Autowired
	WhatsAppClient whatsAppClient;

	@Autowired
	private SnapQueryService snapQueryService;

	@Override
	public void onMessage(String channel, UserInboxEvent event) {
		if (!ArgUtil.isEmpty(event.getWaChannel())) {
			Map<String, Object> query = new HashMap<String, Object>();
			query.put("searchKey", "customer.mobile");
			query.put("searchValue", event.getFrom().replace("+965", "")
					.replace("+91", "").replace(" ", ""));

			LOGGER.info("Recieved {} {} ", event.getFrom(), event.getMessage());

			try {
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
			} catch (IOException e) {
				LOGGER.error("BOT EXCEPTION", e);
			}

		}
	}

}
