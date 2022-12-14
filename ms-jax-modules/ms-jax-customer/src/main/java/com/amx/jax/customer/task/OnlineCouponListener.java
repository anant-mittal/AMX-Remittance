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
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
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

@TunnelEventMapping(topic = AmxTunnelEvents.Names.IPSOS_DISCOUNT, scheme = TunnelEventXchange.TASK_WORKER)
public class OnlineCouponListener implements ITunnelSubscriber<DBEvent> {
	@Autowired
	PostManService postManService;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerFlagManager customerFlagManager;
	
	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final String CUST_ID = "CUST_ID";
	private static final String CIVIL_ID = "CIVIL_ID";
	private static final String CUMM_DISC = "CUMM_DISC";
	private static final String TRNX_DISC = "TRNX_DISC";
	private static final String LANG_ID = "LANG_ID";

	@Override
	public void onMessage(String channel, DBEvent event) {

		LOGGER.debug("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));

		BigDecimal custId = ArgUtil.parseAsBigDecimal(event.getData().get(CUST_ID));
		BigDecimal civilId = ArgUtil.parseAsBigDecimal(event.getData().get(CIVIL_ID));
		BigDecimal cummDiscount = ArgUtil.parseAsBigDecimal(event.getData().get(CUMM_DISC));
		BigDecimal trnxDiscount = ArgUtil.parseAsBigDecimal(event.getData().get(TRNX_DISC));
		String langId = ArgUtil.parseAsString(event.getData().get(LANG_ID));
		
		Customer c = customerRepository.getNationalityValue(custId);
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
		modeldata.put("cummDiscount", cummDiscount);
		modeldata.put("trnxDiscount", trnxDiscount);
		wrapper.put("data", modeldata);
		CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(CommunicationEvents.IPSOS_DISCOUNT, c);
		
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

			email.setITemplate(TemplatesMX.ONLINE_COUPON);

			sendEmail(email);
		}

	}

	@Async(ExecutorConfig.DEFAULT)
	public void sendEmail(Email email) {
		try {
			postManService.sendEmailAsync(email);
		} catch (PostManException e) {

			LOGGER.debug("error in send online coupon", e);
		}
	}

}
