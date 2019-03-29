package com.amx.jax.radar.tasks;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.TRNX_BENE_CREDIT_DELAY, scheme = TunnelEventXchange.TASK_WORKER)
public class TrnaxBeneCreditDelay implements ITunnelSubscriber<DBEvent> {

	@Autowired
	PostManClient postManClient;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	// EMAIL:MOBILE:CUST_NAME:TRNXAMT:LOYALTY:TRNREF:TRNDATE:LANG_ID:TNT
	private static final String EMAIL = "EMAIL";
	private static final String MOBILE = "MOBILE";
	private static final String CUST_ID = "CUST_ID";

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		String emailId = ArgUtil.parseAsString(event.getData().get(EMAIL));
		String smsNo = ArgUtil.parseAsString(event.getData().get(MOBILE));
		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));

		if (!ArgUtil.isEmpty(emailId)) {
			Email email = new Email();
			postManClient.sendEmailAsync(email);
		}

		if (!ArgUtil.isEmpty(smsNo)) {
			SMS sms = new SMS();
			sms.addTo(smsNo);
			sms.setMessage("There is little delay in transaction, kindly wait till monday");
			postManClient.sendSMSAsync(sms);
		}

		if (!ArgUtil.isEmpty(custId)) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setMessage("There is little delay in transaction, kindly wait till monday");
			pushMessage.addToUser(custId);
			pushNotifyClient.send(pushMessage);
		}

	}
}
