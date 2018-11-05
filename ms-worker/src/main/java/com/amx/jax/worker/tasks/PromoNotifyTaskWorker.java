package com.amx.jax.worker.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.dict.Nations;
import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.task.events.PromoNotifyTask;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;

@TunnelEventMapping(byEvent = PromoNotifyTask.class, scheme = TunnelEventXchange.TASK_WORKER)
public class PromoNotifyTaskWorker implements ITunnelSubscriber<PromoNotifyTask> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Override
	public void onMessage(String channel, PromoNotifyTask task) {

		Tenant tnt = TenantContextHolder.currentSite();

		PushMessage msg = new PushMessage();
		msg.setMessage(task.getMessage());
		msg.setSubject(task.getTitle());

		if (task.getNationality() == Nations.ALL) {
			msg.addTopic(String.format(PushMessage.FORMAT_TO_ALL, tnt.toString().toLowerCase()));
		} else {
			msg.addTopic(String.format(PushMessage.FORMAT_TO_NATIONALITY, tnt.toString().toLowerCase(),
					task.getNationality().getCode()));
		}
		pushNotifyClient.sendDirect(msg).getResult();

	}
}