package com.amx.jax.customer.task;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.dict.Language;
import com.amx.jax.event.AmxTunnelEvents;
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

@TunnelEventMapping(topic = AmxTunnelEvents.Names.TRNX_BENE_CREDIT, scheme = TunnelEventXchange.TASK_WORKER)
public class TrnaxBeneCreditListner implements ITunnelSubscriber<DBEvent> {

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
	private static final String TYPE = "TYPE";

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.debug("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
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
		String type = ArgUtil.parseAsString(event.getData().get(TYPE));

		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);
		String trnxAmountval = myFormat.format(trnxAmount);

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custNname);
		modeldata.put("amount", trnxAmountval);
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
			email.setSubject("Transaction Credit Notification"); // changed as per BA
			switch (type) {
			case "CASH":
				email.setITemplate(TemplatesMX.CASH);
				break;
			case "TT":
				email.setITemplate(TemplatesMX.TT);
				break;
			case "EFT":
				email.setITemplate(TemplatesMX.EFT);
				break;
			default:
				break;
			}
			postManClient.sendEmailAsync(email);
		}

		if (!ArgUtil.isEmpty(smsNo)) {

		}

		if (!ArgUtil.isEmpty(custId)) {
			PushMessage pushMessage = new PushMessage();

			switch (type) {
			case "CASH":
				pushMessage.setITemplate(TemplatesMX.CASH);
				break;
			case "TT":
				pushMessage.setITemplate(TemplatesMX.TT);
				break;
			case "EFT":
				pushMessage.setITemplate(TemplatesMX.EFT);
				break;
			default:
				break;
			}

			pushMessage.addToUser(custId);
			pushMessage.setModel(wrapper);
			pushNotifyClient.send(pushMessage);
		}

	}
}
