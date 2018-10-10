package com.amx.jax.worker.tasks;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.dict.Language;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.event.Event;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEvent(topic = AmxTunnelEvents.Names.TRNX_BENE_CREDIT, scheme = TunnelEventXchange.TASK_WORKER)
public class TrnaxBeneCredit implements ITunnelSubscriber<Event> {

	@Autowired
	PostManClient postManClient;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	// EMAIL:MOBILE:CUST_NAME:TRNXAMT:LOYALTY:TRNREF:TRNDATE:LANG_ID:TNT
	private static final String EMAIL = "EMAIL";
	private static final String MOBILE = "MOBILE";
	private static final String CUST_NAME = "CUST_NAME";
	private static final String CUST_ID = "CUST_ID";
	private static final String TRNXAMT = "TRNXAMT";
	private static final String LOYALTY = "LOYALTY";
	private static final String TRNREF = "TRNREF";
	private static final String TRNDATE = "TRNDATE";
	private static final String LANG_ID = "LANG_ID";
	private static final String TENANT = "TENANT";
	private static final String CURNAME = "CURNAME";

	@Override
	public void onMessage(String channel, Event event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		String emailId = ArgUtil.parseAsString(event.getData().get(EMAIL));
		String smsNo = ArgUtil.parseAsString(event.getData().get(MOBILE));
		String custNname = ArgUtil.parseAsString(event.getData().get(CUST_NAME));
		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		BigDecimal trnxAmount = ArgUtil.parseAsBigDecimal(event.getData().get(TRNXAMT));
		BigDecimal loyality = ArgUtil.parseAsBigDecimal(event.getData().get(LOYALTY));
		String trnxRef = ArgUtil.parseAsString(event.getData().get(TRNREF));
		String trnxDate = ArgUtil.parseAsString(event.getData().get(TRNDATE));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));
		String curName = ArgUtil.parseAsString(event.getData().get(CURNAME));

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custNname);
		modeldata.put("amount", trnxAmount);
		modeldata.put("loyaltypoints", loyality);
		modeldata.put("refno", trnxRef);
		modeldata.put("date", trnxDate);
		modeldata.put("currency", curName);
		wrapper.put("data", modeldata);

		if (!ArgUtil.isEmpty(emailId)) {

			Email email = new Email();

			if ("2".equals(langId)) {
				email.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				email.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);
			email.setSubject("Feedback Email"); // Given by Umesh
			email.setTemplate(Templates.BRANCH_FEEDBACK);
			postManClient.sendEmailAsync(email);
		}

		if (!ArgUtil.isEmpty(smsNo)) {

		}

		if (!ArgUtil.isEmpty(custId)) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setTemplate(Templates.BRANCH_FEEDBACK);
			pushMessage.addToUser(custId);
			pushMessage.setModel(wrapper);
			pushNotifyClient.send(pushMessage);
		}

	}
}