package com.amx.jax.worker.tasks;

import java.math.BigDecimal;
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

@TunnelEventMapping(topic = AmxTunnelEvents.Names.CIVIL_ID_EXPIRY, scheme = TunnelEventXchange.TASK_WORKER)
public class CivilIDExpiryListner implements ITunnelSubscriber<DBEvent> {

	@Autowired
	PostManClient postManClient;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static final String EMAIL = "EMAIL";
	public static final String MOBILE = "MOBILE";
	public static final String CUST_NAME = "CUST_NAME";
	private static final String CUST_ID = "CUST_ID";
	public static final String EXP_DATE = "EXP_DATE";
	public static final String LANG_ID = "LANG_ID";
	public static final String TEMPLATE = "TEMPLATE";
	public static final String TENANT = "TENANT";
	public static final String EXPIRED = "EXPIRED";

	// {"event_code":"XRATE_BEST_RATE_CHANGE","priority":"H",
	// "description":"Exchange Rate change","data":
	// {"FROMAMOUNT":"1","TOAMOUNT":"300000","CNTRYID":"94","CURRID":"4","BANKID":"1256","DRVSELLRATE":".004524"}}

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		String emailId = ArgUtil.parseAsString(event.getData().get(EMAIL));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));
		String custNname = ArgUtil.parseAsString(event.getData().get(CUST_NAME));
		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		String expDate = ArgUtil.parseAsString(event.getData().get(EXP_DATE));
		String expired = ArgUtil.parseAsString(event.getData().get(EXPIRED));

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custNname);
		modeldata.put("date", expDate);
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

			if (ArgUtil.areEqual(expired, "0")) {
				email.setITemplate(TemplatesMX.CIVILID_EXPIRY);
				email.setSubject("Civil ID Expiry Reminder"); // Given by Umesh
			} else {
				email.setSubject("Civil ID has been expired"); // Given by Umesh
				email.setITemplate(TemplatesMX.CIVILID_EXPIRED);
			}
			postManClient.sendEmailAsync(email);
		}

		if (!ArgUtil.isEmpty(custId)) {
			PushMessage pushMessage = new PushMessage();
			if (ArgUtil.areEqual(expired, "0")) {
				pushMessage.setITemplate(TemplatesMX.CIVILID_EXPIRY);
			} else {
				pushMessage.setITemplate(TemplatesMX.CIVILID_EXPIRED);
			}
			pushMessage.addToUser(custId);
			pushMessage.setModel(wrapper);
			pushNotifyClient.send(pushMessage);
		}

	}
}
