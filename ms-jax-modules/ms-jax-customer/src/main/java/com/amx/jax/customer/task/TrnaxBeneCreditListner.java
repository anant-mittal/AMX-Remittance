package com.amx.jax.customer.task;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.client.JaxClientUtil;
import com.amx.jax.customer.manager.CustomerContactVerificationManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.Language;
import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEventMapping;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.jax.util.AmxDBConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@TunnelEventMapping(topic = AmxTunnelEvents.Names.TRNX_BENE_CREDIT, scheme = TunnelEventXchange.TASK_WORKER)
public class TrnaxBeneCreditListner implements ITunnelSubscriber<DBEvent> {

	@Autowired
	PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	private CustomerContactVerificationManager customerContactVerificationManager;

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	CustomerFlagManager customerFlagManager;
	

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	// EMAIL:MOBILE:CUST_NAME:TRNXAMT:LOYALTY:TRNREF:TRNDATE:LANG_ID:TNT
	private static final String EMAIL = "EMAIL";
	private static final String MOBILE = "MOBILE";
	private static final String CUST_NAME = "CUST_NAME";
	private static final String CUST_ID = "CUST_ID";
	private static final String TRANX_ID = "TRANX_ID";
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
		String type = ArgUtil.parseAsString(event.getData().get(TYPE));
		//BigDecimal tranxId = ArgUtil.parseAsBigDecimal(event.getData().get(TRANX_ID));
		//LOGGER.info("transaction id is  "+tranxId);
		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);
		String trnxAmountval = myFormat.format(trnxAmount);

		Customer c = customerRepository.getCustomerByCustomerIdAndIsActive(custId, "Y");
		//CustomerFlags customerFlags=null;
		//customerFlags = customerFlagManager.getCustomerFlags(custId);
		Boolean isOnlineCustomer=false;
		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", emailId);
		modeldata.put("customer", custNname);
		modeldata.put("amount", trnxAmountval);
		modeldata.put("loyaltypoints", loyality);
		modeldata.put("refno", trnxRef);
		modeldata.put("date", trnxDate);
		modeldata.put("currency", curName);
		//modeldata.put("tranxId", tranxId);
		//modeldata.put("verCode",
		//		JaxClientUtil.getTransactionVeryCode(tranxId).output());

		for (Map.Entry<String, Object> entry : modeldata.entrySet()) {
			LOGGER.info("KeyModel = " + entry.getKey() + ", ValueModel = " + entry.getValue());
		}

		wrapper.put("data", modeldata);

		if (!ArgUtil.isEmpty(emailId)) {

			if (c.getEmailVerified() != AmxDBConstants.Status.Y) {

				CustomerContactVerification x = customerContactVerificationManager.create(c, ContactType.EMAIL);
				modeldata.put("customer", c);
				modeldata.put("verifylink", x);
				LOGGER.debug("Model data is ", modeldata.get("verifylink"));
				LOGGER.debug("Customer value is ", modeldata.get("customer"));

			} else {
				modeldata.put("customer", null);
				modeldata.put("verifylink", null);
			}

			Email email = new Email();
			if ("2".equals(langId)) {
				email.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				email.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			LOGGER.info("Wrapper data is  ", wrapper.get("data"));
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
			sendEmail(email);
		}

		if (!ArgUtil.isEmpty(smsNo)&&isOnlineCustomer) {
			if (c.getMobileVerified() != AmxDBConstants.Status.Y) {
				CustomerContactVerification x = customerContactVerificationManager.create(c, ContactType.SMS);
				modeldata.put("customer", c);
				modeldata.put("verifylink", x);
			} else {
				modeldata.put("customer", null);
				modeldata.put("verifylink", null);
			}
			SMS sms = new SMS();
			if ("2".equals(langId)) {
				sms.setLang(Language.AR);
				modeldata.put("languageid", Language.AR);
			} else {
				sms.setLang(Language.EN);
				modeldata.put("languageid", Language.EN);
			}
			sms.addTo(c.getMobile());
			sms.setModel(wrapper);
			sms.setSubject("Transaction Credit Notification");
			switch (type) {
			case "CASH":
				sms.setITemplate(TemplatesMX.CASH);
				break;
			case "TT":
				sms.setITemplate(TemplatesMX.TT);
				break;
			case "EFT":
				sms.setITemplate(TemplatesMX.EFT);
				break;
			default:
				break;
			}
			postManService.sendSMSAsync(sms);

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
			LOGGER.info("email exception");
			LOGGER.error("error in link fingerprint", e);
		}
	}

}
