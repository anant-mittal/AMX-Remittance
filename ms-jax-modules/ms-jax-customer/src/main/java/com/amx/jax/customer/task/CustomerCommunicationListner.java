package com.amx.jax.customer.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.Language;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.CUSTOMER_COMM, scheme = TunnelEventXchange.TASK_WORKER)
public class CustomerCommunicationListner implements ITunnelSubscriber<DBEvent> {

	@Autowired
	PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	private WhatsAppClient whatsAppClient;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CommunicationPrefsUtil communicationPrefsUtil;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	// EMAIL:MOBILE:CUST_NAME:TRNXAMT:LOYALTY:TRNREF:TRNDATE:LANG_ID:TNT
	// private static final String EMAIL = "EMAIL";
	// private static final String MOBILE = "MOBILE";
	// private static final String CUST_NAME = "CUST_NAME";
	private static final String CUST_ID = "CUST_ID";
	private static final String TRANX_ID = "TRANX_ID";
	private static final String LANG_ID = "LANG_ID";
	private static final String TEMPLATE = "TEMPLATE";
	private static final String COMFLOW = "COMFLOW";

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.debug("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		// String emailId = ArgUtil.parseAsString(event.getData().get(EMAIL));
		// String smsNo = ArgUtil.parseAsString(event.getData().get(MOBILE));
		// String custNname = ArgUtil.parseAsString(event.getData().get(CUST_NAME));
		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		BigDecimal langId = ArgUtil.parseAsBigDecimal(event.getData().get(LANG_ID));
		Language lang = Language.fromId(langId);
		BigDecimal tranxId = ArgUtil.parseAsBigDecimal(event.getData().get(TRANX_ID));
		String thisTemplate = ArgUtil.parseAsString(event.getData().get(TEMPLATE));
		String communicationFlow = ArgUtil.parseAsString(event.getData().get(COMFLOW));
		Map<String, Object> wrapper = new HashMap<String, Object>();
		wrapper.put("details", event.getMsg());

		Customer c = null;
		CommunicationPrefsResult communicationFlowPrefs = null;
		if (ArgUtil.is(custId)) {
			c = customerRepository.getCustomerByCustomerIdAndIsActive(custId, "Y");
			wrapper.put("customer", c);
			String custName;
			if (StringUtils.isEmpty(c.getMiddleName())) {
				custName = c.getFirstName() + ' ' + c.getLastName();
			} else {
				custName = c.getFirstName() + ' ' + c.getMiddleName() + ' ' + c.getLastName();
			}
			wrapper.put("customerName", custName);
			communicationFlowPrefs = communicationPrefsUtil
					.forCustomer(CommunicationEvents.fromString(communicationFlow), c);
		}

		if (ArgUtil.is(tranxId)) {
			LOGGER.info("transaction id is  " + tranxId);
			wrapper.put("customer", c);
			// c = customerRepository.getCustomerByCustomerIdAndIsActive(custId, "Y");
		}

		if (ArgUtil.is(communicationFlowPrefs) && ArgUtil.is(c)) {
			if (communicationFlowPrefs.isEmail()) {
				Email email = new Email();
				email.setLang(lang);
				email.setModel(wrapper);
				email.addTo(c.getEmail());
				email.setHtml(true);
				email.setTemplate(thisTemplate);
				postManService.sendEmailAsync(email);
			}

			if (communicationFlowPrefs.isWhatsApp()) {
				WAMessage waMessage = new WAMessage();
				waMessage.setTemplate(thisTemplate);
				waMessage.setModel(wrapper);
				waMessage.addTo(c.getWhatsappPrefix() + c.getWhatsapp());
				whatsAppClient.send(waMessage);
			}

			if (communicationFlowPrefs.isSms()) {
				SMS smsMessage = new SMS();
				smsMessage.setTemplate(thisTemplate);
				smsMessage.setModel(wrapper);
				smsMessage.addTo(c.getMobile());
				postManService.sendSMSAsync(smsMessage);
			}

			if (communicationFlowPrefs.isPushNotify()) {
				PushMessage pushMessage = new PushMessage();
				pushMessage.setTemplate(thisTemplate);
				pushMessage.setModel(wrapper);
				pushMessage.addToUser(custId);
				pushNotifyClient.send(pushMessage);
			}
		}

	}

}
