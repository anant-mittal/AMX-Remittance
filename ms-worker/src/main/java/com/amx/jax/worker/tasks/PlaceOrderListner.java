package com.amx.jax.worker.tasks;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.amxlib.model.PlaceOrderNotificationDTO;
import com.amx.jax.client.JaxPushNotificationClient;
import com.amx.jax.client.PlaceOrderClient;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.XRATE_BEST_RATE_CHANGE, scheme = TunnelEventXchange.TASK_WORKER)
public class PlaceOrderListner implements ITunnelSubscriber<DBEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static final String PIPSMASTERID = "PIPS_MASTER_ID";

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		BigDecimal pipsMasterId = ArgUtil.parseAsBigDecimal(event.getData().get(PIPSMASTERID));
		this.rateAlertPlaceOrder(pipsMasterId);
	}

	@Autowired
	PlaceOrderClient placeOrderClient;

	public void rateAlertPlaceOrder(BigDecimal pipsMasterId) {

		try {
			List<PlaceOrderNotificationDTO> placeOrderList = placeOrderClient.getPlaceOrderOnTrigger(pipsMasterId)
					.getResults();
			if (placeOrderList != null && !placeOrderList.isEmpty()) {
				this.sendBatchNotification(placeOrderList);
			}
		} catch (Exception e) {
			LOGGER.error("Error while fetching Place Order List by Trigger Exchange Rate", e);
		}
	}

	@Autowired
	private PostManClient postManClient;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	JaxPushNotificationClient notificationClient;

	public void sendBatchNotification(List<PlaceOrderNotificationDTO> placeorderNotDTO) {

		List<Email> emailList = new ArrayList<Email>();
		List<PushMessage> notificationsList = new ArrayList<PushMessage>();
		for (PlaceOrderNotificationDTO placeorderNot : placeorderNotDTO) {
			LOGGER.info("Sending rate alert to " + placeorderNot.getEmail());

			Email email = new Email();
			email.setSubject("AMX Rate Alert");
			email.addTo(placeorderNot.getEmail());
			email.setITemplate(TemplatesMX.RATE_ALERT);
			email.setHtml(true);
			email.getModel().put(RESP_DATA_KEY, placeorderNot);
			emailList.add(email);

			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(TemplatesMX.RATE_ALERT);
			pushMessage.addToUser(placeorderNot.getCustomerId());
			pushMessage.getModel().put(RESP_DATA_KEY, placeorderNot);
			notificationsList.add(pushMessage);

		}
		try {
			postManClient.sendEmailBulk(emailList);
			pushNotifyClient.send(notificationsList);
		} catch (PostManException e) {
			LOGGER.error("error in sendBatchNotification", e);
		}
	}
}
