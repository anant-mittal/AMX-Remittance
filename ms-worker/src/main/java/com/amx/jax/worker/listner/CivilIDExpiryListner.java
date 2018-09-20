package com.amx.jax.worker.listner;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.dict.Language;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.event.Event;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEvent(topic = AmxTunnelEvents.Names.CIVIL_ID_EXPIRY, scheme = TunnelEventXchange.TASK_WORKER)
public class CivilIDExpiryListner implements ITunnelSubscriber<Event> {

	@Autowired
	PostManClient postManClient;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static final String EMAIL = "EMAIL";
	public static final String MOBILE = "MOBILE";
	public static final String CUST_NAME = "CUST_NAME";
	public static final String EXP_DATE = "EXP_DATE";
	public static final String LANG_ID = "LANG_ID";
	public static final String TEMPLATE = "TEMPLATE";
	public static final String TENANT = "TENANT";
	public static final String EXPIRED = "EXPIRED";

	// {"event_code":"XRATE_BEST_RATE_CHANGE","priority":"H",
	// "description":"Exchange Rate change","data":
	// {"FROMAMOUNT":"1","TOAMOUNT":"300000","CNTRYID":"94","CURRID":"4","BANKID":"1256","DRVSELLRATE":".004524"}}

	@Override
	public void onMessage(String channel, Event event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		String emailId = ArgUtil.parseAsString(event.getData().get(EMAIL));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));
		String custNname = ArgUtil.parseAsString(event.getData().get(CUST_NAME));
		String expDate = ArgUtil.parseAsString(event.getData().get(EXP_DATE));
		String expired = ArgUtil.parseAsString(event.getData().get(EXPIRED));

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custNname);
		modeldata.put("date", expDate);
		wrapper.put("data", modeldata);
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
			email.setTemplate(Templates.CIVILID_EXPIRY);
			email.setSubject("Civil ID Expiry Reminder"); // Given by Umesh
		} else {
			email.setSubject("Civil ID has been expired"); // Given by Umesh
			email.setTemplate(Templates.CIVILID_EXPIRED);
		}
		postManClient.sendEmailAsync(email);
	}
}