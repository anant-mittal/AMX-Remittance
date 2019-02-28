package com.amx.jax.bot;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;

@TunnelEventMapping(byEvent = UserInboxEvent.class, scheme = TunnelEventXchange.TASK_WORKER)
public class InBoxListener implements ITunnelSubscriber<UserInboxEvent> {

	@Autowired
	WhatsAppClient whatsAppClient;

	@Override
	public void onMessage(String channel, UserInboxEvent event) {
		if (ArgUtil.isEmpty(event.getWaChannel())) {
			WAMessage reply = new WAMessage();
			reply.setQueue(event.getQueue());
			reply.setChannel(event.getWaChannel());
			reply.addTo(event.getFrom());
			reply.setMessage("Hi this is Alex from AMX, how can I help you?");
			whatsAppClient.send(reply);
		}
	}

}
