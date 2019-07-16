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
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.INS_OPTOUT, scheme = TunnelEventXchange.TASK_WORKER)
public class GigOptOutPolicyListener implements ITunnelSubscriber<DBEvent>{
	@Autowired
	PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	CustomerFlagManager customerFlagManager;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	
	private static final String CUST_ID = "CUST_ID";
	private static final String OPTOUT_BY = "OPTOUT_BY";
	private static final String LANG_ID = "LANG_ID";
	@Override
	public void onMessage(String channel, DBEvent event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		
		
		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		String optoutBy = ArgUtil.parseAsString(event.getData().get(OPTOUT_BY));
		
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));
		
		LOGGER.info("Customer id is "+custId);
		Customer c = customerRepository.getCustomerByCustomerIdAndIsActive(custId, "Y");
		LOGGER.info("Customer object is "+c.toString());
		String emailId = c.getEmail();
		
		
		String custName;
		if(StringUtils.isEmpty(c.getMiddleName())) {
			c.setMiddleName("");
			custName=c.getFirstName()+c.getMiddleName() + ' '+c.getLastName();
		}else {
			custName=c.getFirstName()+' '+c.getMiddleName() + ' '+c.getLastName();
		}
		
		 
		LOGGER.info("opt out is done by "+optoutBy);
		
		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custName);
		modeldata.put("optoutby", optoutBy);
		
		for (Map.Entry<String, Object> entry : modeldata.entrySet()) {
			LOGGER.info("KeyModel = " + entry.getKey() + ", ValueModel = " + entry.getValue());
		}

		wrapper.put("data", modeldata);
		LOGGER.info("email is  "+emailId);
		if (!ArgUtil.isEmpty(emailId)) {
			
			

			Email email = new Email();
			if ("2".equals(langId)) {
				email.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				email.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			for (Map.Entry<String, Object> entry : wrapper.entrySet()) {
				LOGGER.info("KeyModelWrap = " + entry.getKey() + ", ValueModelWrap = " + entry.getValue());
			}
			LOGGER.info("Json value of wrapper is "+JsonUtil.toJson(wrapper));
			LOGGER.info("Wrapper data is {}", wrapper.get("data"));
			email.setModel(wrapper);
			email.addTo(emailId);
			email.setHtml(true);
			
			if(optoutBy.equals("C")) {
				email.setITemplate(TemplatesMX.POLICY_OPTOUT_CUSTOMER);
			}
			else {
				email.setITemplate(TemplatesMX.POLICY_OPTOUT_SYSTEM);
			}
			
				
			sendEmail(email);
		}

		

		if (!ArgUtil.isEmpty(custId)) {
			PushMessage pushMessage = new PushMessage();

			if(optoutBy.equals("C")) {
				pushMessage.setITemplate(TemplatesMX.POLICY_OPTOUT_CUSTOMER);
			}
			else {
				pushMessage.setITemplate(TemplatesMX.POLICY_OPTOUT_SYSTEM);
			}
			
			pushMessage.setModel(wrapper);
			pushMessage.addToUser(custId);
			pushMessage.setModel(wrapper);
			pushNotifyClient.send(pushMessage);
		}
		
	}
	@Async(ExecutorConfig.DEFAULT)
	public void sendEmail(Email email) {
		try {
			LOGGER.info("email sent");
			postManService.sendEmailAsync(email);
		} catch (PostManException e) {
			
			LOGGER.info("error in optout policy", e);
		}
	}

}
