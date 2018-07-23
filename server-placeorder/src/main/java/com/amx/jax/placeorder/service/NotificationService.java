package com.amx.jax.placeorder.service;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Templates;

@Service
public class NotificationService {


	Logger logger = LoggerFactory.getLogger(getClass());

/*	@Autowired
	private PostManService postManService;*/
	
	public void sendBatchNotification(List<Customer> subList) {
		for (Customer alert : subList) {
			logger.info("Sending rate alert to " + alert.getEmail());
			Email email = new Email();
			email.setSubject("AMX Rate Alert");
			email.addTo(alert.getEmail());
			email.setTemplate(Templates.RATE_ALERT);
			email.setHtml(true);
			email.getModel().put(RESP_DATA_KEY, alert);
/*			try {
				postManService.sendEmailAsync(email);
			} catch (PostManException e) {
				logger.error("error in sendBatchNotification", e);
			}*/
		}
	}

}
