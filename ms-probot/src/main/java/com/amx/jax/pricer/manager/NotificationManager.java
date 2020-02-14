package com.amx.jax.pricer.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.dbmodel.ExEmailNotification;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.probot.notifications.RoutingStatusChangeNotification;
import com.amx.jax.repository.IExEmailNotificationDao;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class NotificationManager {

	public static final String RESP_DATA_KEY = "data";

	@Autowired
	IExEmailNotificationDao emailNotificationDao;

	@Autowired
	private PostManService postManService;

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Async(ExecutorConfig.EXECUTER_PLATINUM)
	public void sendRoutingStatusUpdateNotification(RoutingStatusChangeNotification notificationData) {

		List<ExEmailNotification> emailNotifications = emailNotificationDao.getSalesAdminSupportEmailNotification();

		if (emailNotifications != null && !emailNotifications.isEmpty()) {

			for (ExEmailNotification eNotification : emailNotifications) {

				Email email = new Email();
				email.addTo(eNotification.getEmailId());
				email.setITemplate(TemplatesMX.ROUTING_PRODUCT_STATUS_CHANGE);
				email.setHtml(true);
				email.getModel().put(RESP_DATA_KEY, notificationData);
				sendEmail(email);

			}
		}

	}

	public void sendEmail(Email email) {
		try {
			postManService.sendEmailAsync(email);
		} catch (PostManException e) {
			LOGGER.error("error in sendRoutingStatusUpdateNotification", e);
		}
	}

}
