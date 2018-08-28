package com.amx.jax.worker.service;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.PlaceOrderNotificationDTO;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Templates;

@Service
public class NotificationService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PostManClient postManClient;

	public void sendBatchNotification(List<PlaceOrderNotificationDTO> placeorderNotDTO) {

		List<Email> emailList = new ArrayList<Email>();
		for (PlaceOrderNotificationDTO placeorderNot : placeorderNotDTO) {
			logger.info("Sending rate alert to " + placeorderNot.getEmail());
			Email email = new Email();
			email.setSubject("AMX Rate Alert");
			email.addTo(placeorderNot.getEmail());
			email.setTemplate(Templates.RATE_ALERT);
			email.setHtml(true);
			email.getModel().put(RESP_DATA_KEY, placeorderNot);
			emailList.add(email);
		}
		try {
			postManClient.sendEmailBulk(emailList);
		} catch (PostManException e) {
			logger.error("error in sendBatchNotification", e);
		}
	}
}
