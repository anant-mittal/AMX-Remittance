package com.amx.jax.worker.tasks;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.jax.client.JaxPushNotificationClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.events.UserMessageEvent;
import com.amx.jax.postman.model.Contact;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;

@TunnelEventMapping(byEvent = UserMessageEvent.class, scheme = TunnelEventXchange.TASK_WORKER)
public class UserNotificationSent implements ITunnelSubscriber<UserMessageEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private JaxPushNotificationClient notificationClient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Override
	public void onMessage(String channel, UserMessageEvent task) {

		Tenant tnt = TenantContextHolder.currentSite();
		jaxMetaInfo.setCountryId(tnt.getBDCode());
		jaxMetaInfo.setTenant(tnt);
		jaxMetaInfo.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxMetaInfo.DEFAULT_COUNTRY_BRANCH_ID));

		List<CustomerNotificationDTO> customerNotificationList = new LinkedList<CustomerNotificationDTO>();

		List<String> tos = task.getTo();

		for (String to : tos) {
			CustomerNotificationDTO customerNotification = new CustomerNotificationDTO();

			Contact c = PushMessage.toContact(to);
			boolean foundTo = false;
			if (!ArgUtil.isEmpty(c.getUserid())) {
				customerNotification.setCustomerId(ArgUtil.parseAsBigDecimal(c.getUserid()));
				foundTo = true;
			} else {
				if (!ArgUtil.isEmpty(c.getCountry())) {
					customerNotification.setNationalityId(ArgUtil.parseAsBigDecimal(c.getCountry()));
					foundTo = true;
				} else {
					if (!ArgUtil.isEmpty(c.getTenant())) {
						customerNotification.setCountryId(c.getTenant().getBDCode());
						foundTo = true;
					}
				}
			}

			if (foundTo) {
				customerNotification.setTitle(task.getSubject());
				customerNotification.setMessage(task.getMessage());
				customerNotificationList.add(customerNotification);
			}
		}

		notificationClient.save(customerNotificationList);

	}

}