package com.amx.jax.worker.tasks;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.jax.client.JaxPushNotificationClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.Nations;
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.AuditService;
import com.amx.jax.postman.client.PostManClient;
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
	PostManClient postManClient;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	JaxPushNotificationClient notificationClient;

	@Autowired
	protected JaxMetaInfo jaxMetaInfo;

	@Autowired
	AuditService auditService;

	@Override
	public void onMessage(String channel, PromoNotifyTask task) {

		Tenant tnt = TenantContextHolder.currentSite();
		jaxMetaInfo.setCountryId(tnt.getBDCode());
		jaxMetaInfo.setTenant(tnt);
		jaxMetaInfo.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxMetaInfo.DEFAULT_COUNTRY_BRANCH_ID));

		PushMessage msg = new PushMessage();
		msg.setMessage(task.getMessage());
		msg.setSubject(task.getTitle());

		CustomerNotificationDTO customerNotification = new CustomerNotificationDTO();
		customerNotification.setMessage(task.getMessage());
		customerNotification.setTitle(task.getTitle());

		if (task.getNationality() == Nations.ALL) {
			msg.addTopic(String.format(PushMessage.FORMAT_TO_ALL, tnt.toString().toLowerCase()));
			customerNotification.setCountryId(tnt.getBDCode());
		} else {
			msg.addTopic(String.format(PushMessage.FORMAT_TO_NATIONALITY, tnt.toString().toLowerCase(),
					task.getNationality().getCode()));
			customerNotification.setNationalityId(new BigDecimal(task.getNationality().getCode()));
		}
		notificationClient.save(customerNotification);
		pushNotifyClient.sendDirect(msg).getResult();

	}
}