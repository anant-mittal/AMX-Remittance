package com.amx.jax.customer.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.INS_OPTOUT, scheme = TunnelEventXchange.TASK_WORKER)
public class GigOptOutPolicyListener implements ITunnelSubscriber<DBEvent> {
	@Autowired
	PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerFlagManager customerFlagManager;
	
	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	
	@Autowired
	WhatsAppClient whatsAppClient;
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final String CUST_ID = "CUST_ID";
	private static final String OPTOUT_BY = "OPTOUT_BY";
	private static final String LANG_ID = "LANG_ID";

	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.debug("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));

		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		String optoutBy = ArgUtil.parseAsString(event.getData().get(OPTOUT_BY));

		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));

		Customer c = customerRepository.getCustomerByCustomerIdAndIsActive(custId, "Y");

		String emailId = c.getEmail();

		String custName;
		if (StringUtils.isEmpty(c.getMiddleName())) {
			c.setMiddleName("");
			custName = c.getFirstName() + c.getMiddleName() + ' ' + c.getLastName();
		} else {
			custName = c.getFirstName() + ' ' + c.getMiddleName() + ' ' + c.getLastName();
		}

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custName);
		modeldata.put("optoutby", optoutBy);

		wrapper.put("data", modeldata);
		
		TemplatesMX templatesMX = null;
		if (optoutBy.equals("C")) {
			templatesMX=TemplatesMX.POLICY_OPTOUT_CUSTOMER;
		} else {
			templatesMX=TemplatesMX.POLICY_OPTOUT_SYSTEM;
		}
		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(CommunicationEvents.GIG_OPTOUT_POLICY, c);
		if (communicationPrefsResult.isEmail()) {

			Email email = new Email();
			if ("2".equals(langId)) {
				email.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				email.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}

			LOGGER.debug("Json value of wrapper is " + JsonUtil.toJson(wrapper));

			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);
			email.setITemplate(templatesMX);
			sendEmail(email);
		}
		if(communicationPrefsResult.isSms()) {
			SMS sms = new SMS();
			sms.setModel(wrapper);
			sms.addTo(c.getPrefixCodeMobile()+c.getMobile());
			sms.setITemplate(templatesMX);
			postManService.sendSMSAsync(sms);
		}
		
		if(communicationPrefsResult.isWhatsApp()) {
			WAMessage waMessage = new WAMessage();
			waMessage.setModel(wrapper);
			waMessage.addTo(c.getWhatsappPrefix()+c.getWhatsapp());
			waMessage.setITemplate(templatesMX);
			whatsAppClient.send(waMessage);
		}

		if (!ArgUtil.isEmpty(custId)&&communicationPrefsResult.isPushNotify()) {
			PushMessage pushMessage = new PushMessage();
			pushMessage.setITemplate(templatesMX);
			pushMessage.setModel(wrapper);
			pushMessage.addToUser(custId);
			pushMessage.setModel(wrapper);
			pushNotifyClient.send(pushMessage);
		}

	}

	@Async(ExecutorConfig.DEFAULT)
	public void sendEmail(Email email) {
		try {

			postManService.sendEmailAsync(email);
		} catch (PostManException e) {

			LOGGER.debug("error in optout policy", e);
		}
	}

}
